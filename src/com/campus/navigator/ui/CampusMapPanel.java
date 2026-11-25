package com.campus.navigator.ui;

import com.campus.navigator.model.CampusGraph;
import com.campus.navigator.model.CampusNode;
import com.campus.navigator.model.Edge;
import com.campus.navigator.model.NavigationResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;

/**
 * Visual Campus Map Panel.
 * Dynamically renders campus locations as nodes and roads as weighted connecting lines.
 * Generated dynamically from the underlying CampusGraph instance.
 * Supports highlighting routes, drag-to-reposition nodes, and interactive hover tooltips.
 */
public class CampusMapPanel extends JPanel {

    private final CampusGraph graph;
    private NavigationResult activeResult;

    // Set of edges along the active path represented as "u-v" or "v-u" keys
    private final Set<String> activePathEdges;
    private final Set<Integer> activePathNodeIds;

    // Node dragging interaction
    private CampusNode draggedNode = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    // Node visual radius
    private static final int NODE_RADIUS = 22;

    public CampusMapPanel(CampusGraph graph) {
        this.graph = graph;
        this.activePathEdges = new HashSet<>();
        this.activePathNodeIds = new HashSet<>();

        setBackground(UITheme.BG_MAP);
        setOpaque(true);

        // Repaint whenever graph changes (e.g. dynamic add location or road)
        graph.addChangeListener(this::repaint);

        setupMouseInteractions();
    }

    /**
     * Updates the map to highlight the path in the given navigation result.
     */
    public void setActiveRoute(NavigationResult result) {
        this.activeResult = result;
        activePathEdges.clear();
        activePathNodeIds.clear();

        if (result != null && result.isSuccess() && result.getPathVertexIds().size() > 1) {
            List<Integer> ids = result.getPathVertexIds();
            activePathNodeIds.addAll(ids);

            for (int i = 0; i < ids.size() - 1; i++) {
                int u = ids.get(i);
                int v = ids.get(i + 1);
                activePathEdges.add(makeEdgeKey(u, v));
            }
        } else if (result != null && result.isSuccess() && result.getPathVertexIds().size() == 1) {
            activePathNodeIds.addAll(result.getPathVertexIds());
        }

        repaint();
    }

    /**
     * Clears route highlighting.
     */
    public void clearRoute() {
        this.activeResult = null;
        activePathEdges.clear();
        activePathNodeIds.clear();
        repaint();
    }

    private String makeEdgeKey(int u, int v) {
        return Math.min(u, v) + "-" + Math.max(u, v);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // 1. Draw subtle campus grid background
        drawCampusGrid(g2);

        // 2. Draw roads / edges (unhighlighted first, then active highlighted on top)
        drawRoads(g2);

        // 3. Draw campus location nodes
        drawNodes(g2);

        // 4. Draw legend card
        drawLegend(g2);

        g2.dispose();
    }

    private void drawCampusGrid(Graphics2D g2) {
        g2.setColor(new Color(241, 245, 249));
        int step = 40;
        for (int x = 0; x < getWidth(); x += step) {
            g2.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += step) {
            g2.drawLine(0, y, getWidth(), y);
        }
    }

    private void drawRoads(Graphics2D g2) {
        Set<String> drawnEdges = new HashSet<>();
        List<EdgeDrawInfo> edgesToDraw = new ArrayList<>();

        int vertexCount = graph.getVertexCount();
        for (int u = 0; u < vertexCount; u++) {
            CampusNode nodeU = graph.getNode(u);
            if (nodeU == null) continue;

            for (Edge edge : graph.getNeighbors(u)) {
                int v = edge.getTo();
                CampusNode nodeV = graph.getNode(v);
                if (nodeV == null) continue;

                String key = makeEdgeKey(u, v);
                if (!drawnEdges.contains(key)) {
                    drawnEdges.add(key);
                    boolean isHighlighted = activePathEdges.contains(key);
                    edgesToDraw.add(new EdgeDrawInfo(nodeU, nodeV, edge.getDistance(), isHighlighted));
                }
            }
        }

        // Draw regular edges first
        for (EdgeDrawInfo info : edgesToDraw) {
            if (!info.isHighlighted) {
                drawSingleRoad(g2, info);
            }
        }

        // Draw active highlighted edges on top with glowing emphasis
        for (EdgeDrawInfo info : edgesToDraw) {
            if (info.isHighlighted) {
                drawSingleRoad(g2, info);
            }
        }
    }

    private void drawSingleRoad(Graphics2D g2, EdgeDrawInfo info) {
        int x1 = info.u.getX();
        int y1 = info.u.getY();
        int x2 = info.v.getX();
        int y2 = info.v.getY();

        if (info.isHighlighted) {
            // Glow effect
            g2.setColor(new Color(99, 102, 241, 60));
            g2.setStroke(new BasicStroke(10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x1, y1, x2, y2);

            // Bold active road line
            g2.setColor(UITheme.EDGE_HIGHLIGHT);
            g2.setStroke(new BasicStroke(4.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x1, y1, x2, y2);
        } else {
            // Normal road line
            g2.setColor(UITheme.EDGE_DEFAULT);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x1, y1, x2, y2);
        }

        // Distance pill badge at the center of the road
        int midX = (x1 + x2) / 2;
        int midY = (y1 + x2 == y1 ? (y1 + y2) / 2 : (y1 + y2) / 2);

        String distText = info.distance + " m";
        drawDistancePill(g2, distText, midX, midY, info.isHighlighted);
    }

    private void drawDistancePill(Graphics2D g2, String text, int cx, int cy, boolean isHighlighted) {
        Font font = new Font("Segoe UI", Font.BOLD, 11);
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(text);
        int textH = fm.getAscent();

        int pillW = textW + 14;
        int pillH = 18;
        int px = cx - pillW / 2;
        int py = cy - pillH / 2;

        if (isHighlighted) {
            g2.setColor(UITheme.PRIMARY);
            g2.fillRoundRect(px, py, pillW, pillH, 12, 12);
            g2.setColor(Color.WHITE);
            g2.drawString(text, px + 7, py + (pillH + textH) / 2 - 2);
        } else {
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(px, py, pillW, pillH, 12, 12);
            g2.setColor(UITheme.BORDER_STRONG);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(px, py, pillW, pillH, 12, 12);
            g2.setColor(UITheme.TEXT_MUTED);
            g2.drawString(text, px + 7, py + (pillH + textH) / 2 - 2);
        }
    }

    private void drawNodes(Graphics2D g2) {
        int startId = (activeResult != null && activeResult.isSuccess() && !activeResult.getPathVertexIds().isEmpty())
                ? activeResult.getPathVertexIds().get(0) : -1;
        int destId = (activeResult != null && activeResult.isSuccess() && !activeResult.getPathVertexIds().isEmpty())
                ? activeResult.getPathVertexIds().get(activeResult.getPathVertexIds().size() - 1) : -1;

        for (CampusNode node : graph.getAllNodes()) {
            int id = node.getId();
            int x = node.getX();
            int y = node.getY();

            Color ringColor = UITheme.NODE_DEFAULT;
            Color fillColor = Color.WHITE;
            String roleTag = null;

            if (id == startId && id == destId) {
                ringColor = UITheme.SUCCESS;
                roleTag = "START & DEST";
            } else if (id == startId) {
                ringColor = UITheme.SUCCESS;
                roleTag = "START";
            } else if (id == destId) {
                ringColor = UITheme.DANGER;
                roleTag = "DESTINATION";
            } else if (activePathNodeIds.contains(id)) {
                ringColor = UITheme.WARNING;
                roleTag = "ON ROUTE";
            }

            // Outer glow / shadow
            g2.setColor(new Color(ringColor.getRed(), ringColor.getGreen(), ringColor.getBlue(), 35));
            g2.fillOval(x - NODE_RADIUS - 5, y - NODE_RADIUS - 5, (NODE_RADIUS + 5) * 2, (NODE_RADIUS + 5) * 2);

            // Node Circle Fill
            g2.setColor(fillColor);
            g2.fillOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

            // Node Border
            g2.setColor(ringColor);
            g2.setStroke(new BasicStroke(activePathNodeIds.contains(id) ? 3.5f : 2.2f));
            g2.drawOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

            // Inner circle dot or icon
            g2.setColor(ringColor);
            g2.fillOval(x - 6, y - 6, 12, 12);

            // Location Name Label
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            int nameW = fm.stringWidth(node.getName());
            int labelY = y + NODE_RADIUS + 16;

            // Name label background pill
            int labelPillW = nameW + 12;
            int labelPillH = 18;
            g2.setColor(new Color(255, 255, 255, 220));
            g2.fillRoundRect(x - labelPillW / 2, labelY - 13, labelPillW, labelPillH, 8, 8);
            g2.setColor(UITheme.BORDER);
            g2.drawRoundRect(x - labelPillW / 2, labelY - 13, labelPillW, labelPillH, 8, 8);

            g2.setColor(UITheme.TEXT_MAIN);
            g2.drawString(node.getName(), x - nameW / 2, labelY);

            // Role Tag badge if Start/Dest/Route
            if (roleTag != null) {
                g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
                FontMetrics rfm = g2.getFontMetrics();
                int tagW = rfm.stringWidth(roleTag) + 10;
                int tagH = 15;
                int tagY = y - NODE_RADIUS - 18;

                g2.setColor(ringColor);
                g2.fillRoundRect(x - tagW / 2, tagY, tagW, tagH, 6, 6);
                g2.setColor(Color.WHITE);
                g2.drawString(roleTag, x - (rfm.stringWidth(roleTag)) / 2, tagY + 11);
            }
        }
    }

    private void drawLegend(Graphics2D g2) {
        int cardW = 190;
        int cardH = 130;
        int x = getWidth() - cardW - 16;
        int y = getHeight() - cardH - 16;

        if (x < 10 || y < 10) return; // not enough space

        // Legend Background Card
        g2.setColor(new Color(255, 255, 255, 230));
        g2.fillRoundRect(x, y, cardW, cardH, 12, 12);
        g2.setColor(UITheme.BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, cardW, cardH, 12, 12);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.setColor(UITheme.TEXT_MAIN);
        g2.drawString("CAMPUS MAP LEGEND", x + 12, y + 18);

        int itemY = y + 36;
        drawLegendItem(g2, UITheme.NODE_START, "Start Location", x + 12, itemY);
        drawLegendItem(g2, UITheme.NODE_DEST, "Destination", x + 12, itemY + 20);
        drawLegendItem(g2, UITheme.NODE_PATH, "Intermediate Stop", x + 12, itemY + 40);
        drawLegendItem(g2, UITheme.EDGE_HIGHLIGHT, "Active Route Road", x + 12, itemY + 60);
    }

    private void drawLegendItem(Graphics2D g2, Color color, String label, int lx, int ly) {
        g2.setColor(color);
        g2.fillOval(lx, ly - 7, 10, 10);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        g2.setColor(UITheme.TEXT_MUTED);
        g2.drawString(label, lx + 16, ly + 2);
    }

    private void setupMouseInteractions() {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Find if a node was clicked for dragging
                for (CampusNode node : graph.getAllNodes()) {
                    int dx = e.getX() - node.getX();
                    int dy = e.getY() - node.getY();
                    if (dx * dx + dy * dy <= NODE_RADIUS * NODE_RADIUS) {
                        draggedNode = node;
                        dragOffsetX = dx;
                        dragOffsetY = dy;
                        setCursor(new Cursor(Cursor.MOVE_CURSOR));
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggedNode != null) {
                    draggedNode = null;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedNode != null) {
                    int newX = Math.max(NODE_RADIUS + 10, Math.min(getWidth() - NODE_RADIUS - 10, e.getX() - dragOffsetX));
                    int newY = Math.max(NODE_RADIUS + 10, Math.min(getHeight() - NODE_RADIUS - 10, e.getY() - dragOffsetY));
                    draggedNode.setX(newX);
                    draggedNode.setY(newY);
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // Tooltip on node hover
                boolean hoveringNode = false;
                for (CampusNode node : graph.getAllNodes()) {
                    int dx = e.getX() - node.getX();
                    int dy = e.getY() - node.getY();
                    if (dx * dx + dy * dy <= NODE_RADIUS * NODE_RADIUS) {
                        hoveringNode = true;
                        setCursor(new Cursor(Cursor.HAND_CURSOR));

                        // Build helpful tooltip
                        StringBuilder sb = new StringBuilder("<html><b>");
                        sb.append(node.getName()).append("</b> (").append(node.getCategory()).append(")<br>");
                        sb.append("<i>Connected to:</i><br>");
                        List<Edge> neighbors = graph.getNeighbors(node.getId());
                        if (neighbors.isEmpty()) {
                            sb.append("&nbsp;&nbsp;(No connections yet)");
                        } else {
                            for (Edge edge : neighbors) {
                                sb.append("&nbsp;&nbsp;• ")
                                  .append(graph.getLocationName(edge.getTo()))
                                  .append(" (").append(edge.getDistance()).append(" m)<br>");
                            }
                        }
                        sb.append("</html>");
                        setToolTipText(sb.toString());
                        break;
                    }
                }
                if (!hoveringNode) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    setToolTipText(null);
                }
            }
        };

        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    private static class EdgeDrawInfo {
        final CampusNode u;
        final CampusNode v;
        final int distance;
        final boolean isHighlighted;

        EdgeDrawInfo(CampusNode u, CampusNode v, int distance, boolean isHighlighted) {
            this.u = u;
            this.v = v;
            this.distance = distance;
            this.isHighlighted = isHighlighted;
        }
    }
}
