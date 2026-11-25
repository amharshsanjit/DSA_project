package com.rungta.campus;

import com.rungta.campus.algorithm.BfsAlgorithm;
import com.rungta.campus.algorithm.DijkstraAlgorithm;
import com.rungta.campus.model.*;
import com.rungta.campus.repository.CampusDataStore;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * High-performance Java REST API Server for Rungta Campus Navigation.
 * Provides complete Spring Boot REST API equivalent endpoints with CORS,
 * JSON serialization, Dijkstra and BFS route calculations, and admin capabilities.
 */
public class RungtaServer {

    private static final int PORT = 8080;
    private static final CampusDataStore dataStore = new CampusDataStore();
    private static final String ADMIN_TOKEN = "rungta_admin_token_2024";

    public static void main(String[] args) throws IOException {
        int port = PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newFixedThreadPool(10));

        // Setup REST Endpoints
        server.createContext("/api/health", new HealthHandler());
        server.createContext("/api/institutions", new InstitutionsHandler());
        server.createContext("/api/buildings", new BuildingsHandler());
        server.createContext("/api/blocks", new BlocksHandler());
        server.createContext("/api/floors", new FloorsHandler());
        server.createContext("/api/rooms", new RoomsHandler());
        server.createContext("/api/facilities", new FacilitiesHandler());
        server.createContext("/api/search", new SearchHandler());
        server.createContext("/api/route", new RouteHandler());
        server.createContext("/api/graph", new GraphHandler());
        server.createContext("/api/admin", new AdminHandler());

        server.start();
        System.out.println("==========================================================");
        System.out.println("  RUNGTA R1 CAMPUS NAVIGATION - JAVA REST API SERVER      ");
        System.out.println("==========================================================");
        System.out.println("  Status: RUNNING");
        System.out.println("  Port:   http://localhost:" + port);
        System.out.println("  Campus: Rungta Educational Campus R1, Bhilai, C.G.");
        System.out.println("  Coordinates: 21.2350° N, 81.3462° E");
        System.out.println("==========================================================");
    }

    // --- Handlers ---

    private static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (handleCors(ex)) return;
            sendJsonResponse(ex, 200, "{\"status\":\"UP\",\"campus\":\"Rungta Educational Campus R1, Bhilai\",\"version\":\"2.0.0\"}");
        }
    }

    private static class InstitutionsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (handleCors(ex)) return;
            List<Institution> list = dataStore.getAllInstitutions();
            sendJsonResponse(ex, 200, toJson(list));
        }
    }

    private static class BuildingsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (handleCors(ex)) return;
            String path = ex.getRequestURI().getPath();
            // Check for /api/buildings/{id}
            String[] parts = path.split("/");
            if (parts.length > 3) {
                String id = parts[3];
                Building b = dataStore.getBuilding(id);
                if (b != null) {
                    sendJsonResponse(ex, 200, toJson(b));
                } else {
                    sendJsonResponse(ex, 404, "{\"error\":\"Building not found\"}");
                }
                return;
            }

            List<Building> list = dataStore.getAllBuildings();
            sendJsonResponse(ex, 200, toJson(list));
        }
    }

    private static class BlocksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (handleCors(ex)) return;
            Map<String, String> params = parseQueryParams(ex.getRequestURI().getQuery());
            String buildingId = params.get("buildingId");
            List<Block> list = (buildingId != null && !buildingId.isEmpty())
                    ? dataStore.getBlocksByBuilding(buildingId)
                    : dataStore.getAllBlocks();
            sendJsonResponse(ex, 200, toJson(list));
        }
    }

    private static class FloorsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (handleCors(ex)) return;
            Map<String, String> params = parseQueryParams(ex.getRequestURI().getQuery());
            String blockId = params.get("blockId");
            List<Floor> list = (blockId != null && !blockId.isEmpty())
                    ? dataStore.getFloorsByBlock(blockId)
                    : dataStore.getAllFloors();
            sendJsonResponse(ex, 200, toJson(list));
        }
    }

    private static class RoomsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (handleCors(ex)) return;
            Map<String, String> params = parseQueryParams(ex.getRequestURI().getQuery());
            String floorId = params.get("floorId");
            List<Room> list = (floorId != null && !floorId.isEmpty())
                    ? dataStore.getRoomsByFloor(floorId)
                    : dataStore.getAllRooms();
            sendJsonResponse(ex, 200, toJson(list));
        }
    }

    private static class FacilitiesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (handleCors(ex)) return;
            List<Facility> list = dataStore.getAllFacilities();
            sendJsonResponse(ex, 200, toJson(list));
        }
    }

    private static class SearchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (handleCors(ex)) return;
            Map<String, String> params = parseQueryParams(ex.getRequestURI().getQuery());
            String q = params.get("q");
            String cat = params.get("category");
            List<SearchResult> results = dataStore.search(q, cat);
            sendJsonResponse(ex, 200, toJson(results));
        }
    }

    private static class RouteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (handleCors(ex)) return;
            String path = ex.getRequestURI().getPath();
            Map<String, String> params = parseQueryParams(ex.getRequestURI().getQuery());
            String from = params.get("from");
            String to = params.get("to");

            if (from == null || to == null) {
                sendJsonResponse(ex, 400, "{\"error\":\"Parameters 'from' and 'to' are required\"}");
                return;
            }

            NavigationResult res;
            if (path.contains("fewest-stops")) {
                res = BfsAlgorithm.findFewestStopsPath(dataStore.getGraph(), from, to);
            } else {
                // Default: Dijkstra Shortest Distance
                res = DijkstraAlgorithm.findShortestPath(dataStore.getGraph(), from, to);
            }

            sendJsonResponse(ex, 200, toJson(res));
        }
    }

    private static class GraphHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (handleCors(ex)) return;
            String path = ex.getRequestURI().getPath();
            if (path.contains("nodes")) {
                sendJsonResponse(ex, 200, toJson(dataStore.getGraph().getAllNodes()));
            } else {
                sendJsonResponse(ex, 200, toJson(dataStore.getGraph().getAllUniqueEdges()));
            }
        }
    }

    private static class AdminHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (handleCors(ex)) return;
            String method = ex.getRequestMethod().toUpperCase();
            String path = ex.getRequestURI().getPath();

            // Admin Login endpoint
            if (path.endsWith("/login") && method.equals("POST")) {
                String body = readBody(ex);
                if (body.contains("admin123") || body.contains("rungta@2024")) {
                    sendJsonResponse(ex, 200, "{\"success\":true,\"token\":\"" + ADMIN_TOKEN + "\",\"role\":\"CAMPUS_ADMIN\"}");
                } else {
                    sendJsonResponse(ex, 401, "{\"success\":false,\"error\":\"Invalid admin credentials\"}");
                }
                return;
            }

            // Check authorization token for other admin actions
            String authHeader = ex.getRequestHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.contains(ADMIN_TOKEN)) {
                sendJsonResponse(ex, 403, "{\"error\":\"Unauthorized: Admin authentication token required\"}");
                return;
            }

            // Admin CRUD endpoints
            if (path.contains("/buildings")) {
                if (method.equals("POST")) {
                    String body = readBody(ex);
                    Building newB = parseBuildingFromJson(body);
                    dataStore.addBuilding(newB);
                    // Also create a graph node for routing
                    dataStore.getGraph().addNode(new GraphNode("node_" + newB.getId().replace("b_", ""), newB.getName(), newB.getCategory(), newB.getLatitude(), newB.getLongitude(), newB.getId()));
                    sendJsonResponse(ex, 201, toJson(newB));
                } else if (method.equals("DELETE")) {
                    String[] parts = path.split("/");
                    if (parts.length > 4) {
                        String id = parts[4];
                        dataStore.deleteBuilding(id);
                        sendJsonResponse(ex, 200, "{\"success\":true,\"message\":\"Building deleted\"}");
                    } else {
                        sendJsonResponse(ex, 400, "{\"error\":\"Missing building ID\"}");
                    }
                }
            } else if (path.contains("/paths") && method.equals("POST")) {
                String body = readBody(ex);
                GraphEdge newEdge = parseEdgeFromJson(body);
                dataStore.getGraph().addBidirectionalEdge(newEdge);
                sendJsonResponse(ex, 201, toJson(newEdge));
            } else {
                sendJsonResponse(ex, 200, "{\"success\":true,\"message\":\"Admin action recorded\"}");
            }
        }
    }

    // --- Utilities & JSON Serializer ---

    private static boolean handleCors(HttpExchange ex) throws IOException {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        if (ex.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            ex.sendResponseHeaders(204, -1);
            return true;
        }
        return false;
    }

    private static void sendJsonResponse(HttpExchange ex, int statusCode, String json) throws IOException {
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static Map<String, String> parseQueryParams(String query) {
        if (query == null || query.isEmpty()) return Collections.emptyMap();
        Map<String, String> map = new HashMap<>();
        for (String pair : query.split("&")) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                try {
                    String k = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                    String v = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                    map.put(k, v);
                } catch (Exception ignored) {}
            }
        }
        return map;
    }

    private static String readBody(HttpExchange ex) throws IOException {
        try (InputStream is = ex.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    // Lightweight clean JSON serialiser
    public static String toJson(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String) return "\"" + escapeJson((String) obj) + "\"";
        if (obj instanceof Number || obj instanceof Boolean) return obj.toString();
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                sb.append(toJson(list.get(i)));
                if (i < list.size() - 1) sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        }

        // Generic object serialization via getters
        if (obj instanceof Building) {
            Building b = (Building) obj;
            return String.format(Locale.US,
                    "{\"id\":\"%s\",\"name\":\"%s\",\"code\":\"%s\",\"institutionId\":\"%s\",\"category\":\"%s\"," +
                    "\"latitude\":%.6f,\"longitude\":%.6f,\"floorsCount\":%d,\"isVerified\":%b,\"sourceUrl\":\"%s\"," +
                    "\"verificationDate\":\"%s\",\"description\":\"%s\",\"departments\":%s,\"facilities\":%s,\"nearbyBuildingIds\":%s}",
                    escapeJson(b.getId()), escapeJson(b.getName()), escapeJson(b.getCode()), escapeJson(b.getInstitutionId()),
                    escapeJson(b.getCategory()), b.getLatitude(), b.getLongitude(), b.getFloorsCount(), b.isVerified(),
                    escapeJson(b.getSourceUrl()), escapeJson(b.getVerificationDate()), escapeJson(b.getDescription()),
                    toJson(b.getDepartments()), toJson(b.getFacilities()), toJson(b.getNearbyBuildingIds()));
        }

        if (obj instanceof Institution) {
            Institution inst = (Institution) obj;
            return String.format(Locale.US,
                    "{\"id\":\"%s\",\"name\":\"%s\",\"shortName\":\"%s\",\"type\":\"%s\",\"description\":\"%s\"," +
                    "\"website\":\"%s\",\"establishedYear\":%d,\"isVerified\":%b,\"sourceUrl\":\"%s\"}",
                    escapeJson(inst.getId()), escapeJson(inst.getName()), escapeJson(inst.getShortName()),
                    escapeJson(inst.getType()), escapeJson(inst.getDescription()), escapeJson(inst.getWebsite()),
                    inst.getEstablishedYear(), inst.isVerified(), escapeJson(inst.getSourceUrl()));
        }

        if (obj instanceof Block) {
            Block blk = (Block) obj;
            return String.format(Locale.US,
                    "{\"id\":\"%s\",\"buildingId\":\"%s\",\"name\":\"%s\",\"identifier\":\"%s\",\"description\":\"%s\",\"isVerified\":%b}",
                    escapeJson(blk.getId()), escapeJson(blk.getBuildingId()), escapeJson(blk.getName()),
                    escapeJson(blk.getIdentifier()), escapeJson(blk.getDescription()), blk.isVerified());
        }

        if (obj instanceof Floor) {
            Floor fl = (Floor) obj;
            return String.format(Locale.US,
                    "{\"id\":\"%s\",\"blockId\":\"%s\",\"floorNumber\":%d,\"floorName\":\"%s\",\"floorPlanSvg\":\"%s\",\"isVerified\":%b}",
                    escapeJson(fl.getId()), escapeJson(fl.getBlockId()), fl.getFloorNumber(),
                    escapeJson(fl.getFloorName()), escapeJson(fl.getFloorPlanSvg()), fl.isVerified());
        }

        if (obj instanceof Room) {
            Room rm = (Room) obj;
            return String.format(Locale.US,
                    "{\"id\":\"%s\",\"floorId\":\"%s\",\"roomNumber\":\"%s\",\"name\":\"%s\",\"type\":\"%s\",\"department\":\"%s\",\"capacity\":%d,\"isVerified\":%b,\"coordinatesSvg\":\"%s\"}",
                    escapeJson(rm.getId()), escapeJson(rm.getFloorId()), escapeJson(rm.getRoomNumber()),
                    escapeJson(rm.getName()), escapeJson(rm.getType()), escapeJson(rm.getDepartment()),
                    rm.getCapacity(), rm.isVerified(), escapeJson(rm.getCoordinatesSvg()));
        }

        if (obj instanceof Facility) {
            Facility f = (Facility) obj;
            return String.format(Locale.US,
                    "{\"id\":\"%s\",\"name\":\"%s\",\"category\":\"%s\",\"buildingId\":\"%s\",\"latitude\":%.6f,\"longitude\":%.6f,\"description\":\"%s\",\"openingHours\":\"%s\",\"isVerified\":%b}",
                    escapeJson(f.getId()), escapeJson(f.getName()), escapeJson(f.getCategory()), escapeJson(f.getBuildingId()),
                    f.getLatitude(), f.getLongitude(), escapeJson(f.getDescription()), escapeJson(f.getOpeningHours()), f.isVerified());
        }

        if (obj instanceof GraphNode) {
            GraphNode gn = (GraphNode) obj;
            return String.format(Locale.US,
                    "{\"id\":\"%s\",\"name\":\"%s\",\"category\":\"%s\",\"latitude\":%.6f,\"longitude\":%.6f,\"buildingId\":\"%s\"}",
                    escapeJson(gn.getId()), escapeJson(gn.getName()), escapeJson(gn.getCategory()), gn.getLatitude(), gn.getLongitude(), escapeJson(gn.getBuildingId()));
        }

        if (obj instanceof GraphEdge) {
            GraphEdge ge = (GraphEdge) obj;
            return String.format(Locale.US,
                    "{\"id\":\"%s\",\"fromNodeId\":\"%s\",\"toNodeId\":\"%s\",\"distanceMeters\":%d,\"estimatedTimeSeconds\":%d,\"isAccessible\":%b,\"roadType\":\"%s\",\"isVerified\":%b}",
                    escapeJson(ge.getId()), escapeJson(ge.getFromNodeId()), escapeJson(ge.getToNodeId()),
                    ge.getDistanceMeters(), ge.getEstimatedTimeSeconds(), ge.isAccessible(), escapeJson(ge.getRoadType()), ge.isVerified());
        }

        if (obj instanceof SearchResult) {
            SearchResult sr = (SearchResult) obj;
            return String.format(Locale.US,
                    "{\"id\":\"%s\",\"title\":\"%s\",\"subtitle\":\"%s\",\"category\":\"%s\",\"type\":\"%s\",\"buildingId\":\"%s\",\"blockId\":\"%s\",\"floorId\":\"%s\",\"latitude\":%.6f,\"longitude\":%.6f,\"isVerified\":%b,\"sourceUrl\":\"%s\"}",
                    escapeJson(sr.getId()), escapeJson(sr.getTitle()), escapeJson(sr.getSubtitle()), escapeJson(sr.getCategory()),
                    escapeJson(sr.getType()), escapeJson(sr.getBuildingId()), escapeJson(sr.getBlockId()), escapeJson(sr.getFloorId()),
                    sr.getLatitude(), sr.getLongitude(), sr.isVerified(), escapeJson(sr.getSourceUrl()));
        }

        if (obj instanceof NavigationResult) {
            NavigationResult nr = (NavigationResult) obj;
            return String.format(Locale.US,
                    "{\"success\":%b,\"algorithm\":\"%s\",\"sourceNodeId\":\"%s\",\"sourceName\":\"%s\",\"destinationNodeId\":\"%s\",\"destinationName\":\"%s\",\"pathNodeIds\":%s,\"pathNodes\":%s,\"totalDistanceMeters\":%d,\"estimatedWalkingTimeMinutes\":%.1f,\"totalStops\":%d,\"intermediateStops\":%d,\"turnByTurnInstructions\":%s,\"isAccessible\":%b,\"errorMessage\":%s}",
                    nr.isSuccess(), escapeJson(nr.getAlgorithm()), escapeJson(nr.getSourceNodeId()), escapeJson(nr.getSourceName()),
                    escapeJson(nr.getDestinationNodeId()), escapeJson(nr.getDestinationName()), toJson(nr.getPathNodeIds()),
                    toJson(nr.getPathNodes()), nr.getTotalDistanceMeters(), nr.getEstimatedWalkingTimeMinutes(),
                    nr.getTotalStops(), nr.getIntermediateStops(), toJson(nr.getTurnByTurnInstructions()),
                    nr.isAccessible(), nr.getErrorMessage() != null ? "\"" + escapeJson(nr.getErrorMessage()) + "\"" : "null");
        }

        return "{}";
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static Building parseBuildingFromJson(String json) {
        String id = extractJsonField(json, "id");
        if (id == null || id.isEmpty()) id = "b_" + UUID.randomUUID().toString().substring(0, 8);
        String name = extractJsonField(json, "name");
        String code = extractJsonField(json, "code");
        String instId = extractJsonField(json, "institutionId");
        String category = extractJsonField(json, "category");
        String desc = extractJsonField(json, "description");
        double lat = extractJsonDouble(json, "latitude", 21.2350);
        double lng = extractJsonDouble(json, "longitude", 81.3462);
        int floors = (int) extractJsonDouble(json, "floorsCount", 2);
        boolean verified = json.contains("\"isVerified\":true");
        return new Building(id, name, code, instId, category, lat, lng, floors, verified, CampusDataStore.SRC_RUNGTA_MAIN, "2024-03-01", desc);
    }

    private static GraphEdge parseEdgeFromJson(String json) {
        String id = extractJsonField(json, "id");
        if (id == null || id.isEmpty()) id = "e_" + UUID.randomUUID().toString().substring(0, 8);
        String from = extractJsonField(json, "fromNodeId");
        String to = extractJsonField(json, "toNodeId");
        int dist = (int) extractJsonDouble(json, "distanceMeters", 50);
        int time = (int) (dist / 1.3);
        boolean accessible = json.contains("\"isAccessible\":true");
        String roadType = extractJsonField(json, "roadType");
        if (roadType == null) roadType = "PAVED_WALKWAY";
        return new GraphEdge(id, from, to, dist, time, accessible, roadType, true);
    }

    private static String extractJsonField(String json, String key) {
        int idx = json.indexOf("\"" + key + "\"");
        if (idx == -1) return null;
        int colon = json.indexOf(':', idx);
        int quoteStart = json.indexOf('"', colon);
        if (quoteStart == -1) return null;
        int quoteEnd = json.indexOf('"', quoteStart + 1);
        if (quoteEnd == -1) return null;
        return json.substring(quoteStart + 1, quoteEnd);
    }

    private static double extractJsonDouble(String json, String key, double defaultVal) {
        int idx = json.indexOf("\"" + key + "\"");
        if (idx == -1) return defaultVal;
        int colon = json.indexOf(':', idx);
        int end = colon + 1;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.' || json.charAt(end) == '-')) {
            end++;
        }
        try {
            return Double.parseDouble(json.substring(colon + 1, end).trim());
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
