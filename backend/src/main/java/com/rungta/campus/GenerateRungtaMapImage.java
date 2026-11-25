package com.rungta.campus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GenerateRungtaMapImage {
    public static void main(String[] args) {
        try {
            System.setProperty("java.awt.headless", "true");
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

            int width = 1100;
            int height = 660;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // 1. Dark Navy Background (Google Maps Dark / Navy Theme)
            g2.setColor(new Color(6, 11, 24)); // #060B18
            g2.fillRect(0, 0, width, height);

            // Subtle Campus Grid
            g2.setColor(new Color(16, 27, 56));
            int step = 45;
            for (int x = 0; x < width; x += step) g2.drawLine(x, 0, x, height);
            for (int y = 0; y < height; y += step) g2.drawLine(0, y, width, y);

            // 2. Campus Boundary Area (Rungta R1 Campus, 80+ Acres)
            g2.setColor(new Color(11, 19, 41, 230));
            g2.fillRoundRect(30, 90, width - 60, height - 120, 24, 24);
            g2.setColor(new Color(35, 52, 98));
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f, new float[]{8f, 6f}, 0f));
            g2.drawRoundRect(30, 90, width - 60, height - 120, 24, 24);

            // 3. Road Network Nodes (Realistic Campus Layout)
            // Coordinates scaled to canvas
            int gateX = 120, gateY = 560;
            int parkX = 80, parkY = 460;
            int atmX = 220, atmY = 530;
            int adminX = 280, adminY = 430;
            int enggX = 420, enggY = 320; // Block B + Auditorium
            int csX = 640, csY = 240;   // Block C + Labs
            int libX = 680, libY = 410;  // Central Library
            int canteenX = 450, canteenY = 450;
            int dentalX = 360, dentalY = 200; // Block D/E
            int clinicX = 240, clinicY = 210;
            int hostelBX = 860, hostelBY = 220;
            int hostelGX = 220, hostelGY = 140;
            int sportsX = 920, sportsY = 430;

            // Draw Regular Walking Roads
            int[][] roads = {
                {gateX, gateY, parkX, parkY},
                {gateX, gateY, atmX, atmY},
                {gateX, gateY, adminX, adminY},
                {atmX, atmY, adminX, adminY},
                {adminX, adminY, canteenX, canteenY},
                {enggX, enggY, csX, csY},
                {enggX, enggY, canteenX, canteenY},
                {enggX, enggY, dentalX, dentalY},
                {csX, csY, libX, libY},
                {csX, csY, hostelBX, hostelBY},
                {canteenX, canteenY, libX, libY},
                {dentalX, dentalY, clinicX, clinicY},
                {dentalX, dentalY, hostelGX, hostelGY},
                {clinicX, clinicY, hostelGX, hostelGY},
                {libX, libY, sportsX, sportsY},
                {hostelBX, hostelBY, sportsX, sportsY}
            };

            g2.setColor(new Color(51, 65, 85));
            g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int[] r : roads) {
                g2.drawLine(r[0], r[1], r[2], r[3]);
            }

            // Draw Road Distance Badges
            drawDistancePill(g2, "40m", (gateX + parkX)/2, (gateY + parkY)/2);
            drawDistancePill(g2, "60m", (gateX + atmX)/2, (gateY + atmY)/2);
            drawDistancePill(g2, "65m", (enggX + csX)/2, (enggY + csY)/2);
            drawDistancePill(g2, "50m", (enggX + canteenX)/2, (enggY + canteenY)/2);
            drawDistancePill(g2, "80m", (enggX + dentalX)/2, (enggY + dentalY)/2);
            drawDistancePill(g2, "70m", (csX + libX)/2, (csY + libY)/2);
            drawDistancePill(g2, "60m", (canteenX + libX)/2, (canteenY + libY)/2);
            drawDistancePill(g2, "120m", (csX + hostelBX)/2, (csY + hostelBY)/2);
            drawDistancePill(g2, "140m", (libX + sportsX)/2, (libY + sportsY)/2);

            // 4. Draw Active Route: Main Entrance -> Block A -> Central Library (Dijkstra Shortest: 205m)
            int[][] activePath = {
                {gateX, gateY, adminX, adminY, 95},
                {adminX, adminY, libX, libY, 110}
            };

            // Glow line
            g2.setColor(new Color(0, 240, 255, 65));
            g2.setStroke(new BasicStroke(16f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int[] ap : activePath) g2.drawLine(ap[0], ap[1], ap[2], ap[3]);

            // Main Active Electric Blue Line
            g2.setColor(new Color(37, 99, 235));
            g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int[] ap : activePath) g2.drawLine(ap[0], ap[1], ap[2], ap[3]);

            drawHighlightedPill(g2, "95m", (gateX + adminX)/2, (gateY + adminY)/2 - 12);
            drawHighlightedPill(g2, "110m", (adminX + libX)/2, (adminY + libY)/2 - 12);

            // 5. Draw Buildings as Architectural Campus Cards
            drawBuildingCard(g2, gateX, gateY, "MAIN ENTRANCE", "Kohka-Kurud Rd Checkpost", new Color(16, 185, 129), true, "START");
            drawBuildingCard(g2, adminX, adminY, "BLOCK A – ADMINISTRATION", "Chancellor / VC Directorate & Admissions", new Color(99, 102, 241), false, "WAYPOINT");
            drawBuildingCard(g2, enggX, enggY, "BLOCK B – ENGINEERING", "SANTOSH RUNGTA AUDITORIUM (800+ Seats)", new Color(59, 130, 246), false, null);
            drawBuildingCard(g2, csX, csY, "BLOCK C – ADVANCED IT", "Apple Mac Lab, Microsoft & Google Labs", new Color(14, 165, 233), false, null);
            drawBuildingCard(g2, libX, libY, "CENTRAL LIBRARY", "Dr. A.P.J. Abdul Kalam Knowledge Center", new Color(236, 72, 153), true, "DESTINATION");
            drawBuildingCard(g2, canteenX, canteenY, "CENTRAL CANTEEN", "Food Court & Nescafe Lounge", new Color(245, 158, 11), false, null);
            drawBuildingCard(g2, dentalX, dentalY, "BLOCK D & E – DENTAL & PHARMACY", "RCDSR Dental Hospital (100+ Chairs) & RIPS", new Color(239, 68, 68), false, null);
            drawBuildingCard(g2, hostelBX, hostelBY, "BOYS HOSTEL COMPLEX", "Hostels H1 & H2, Dining & Gym", new Color(139, 92, 246), false, null);
            drawBuildingCard(g2, hostelGX, hostelGY, "GIRLS HOSTEL COMPLEX", "Secure Residence & Dining Hall", new Color(168, 85, 247), false, null);
            drawBuildingCard(g2, sportsX, sportsY, "SPORTS & ATHLETICS ARENA", "Cricket Ground & Open Air Theatre (OAT)", new Color(20, 184, 166), false, null);
            drawBuildingCard(g2, parkX, parkY, "CAMPUS PARKING", "Zone 1 & 2 Covered Bays", new Color(100, 116, 139), false, null);
            drawBuildingCard(g2, atmX, atmY, "SBI & PNB ATM", "24/7 Cash & Fee Kiosk", new Color(5, 150, 105), false, null);
            drawBuildingCard(g2, clinicX, clinicY, "HEALTH CARE CLINIC", "Emergency First Aid & Ambulance", new Color(225, 29, 72), false, null);

            // 6. Header Banner (Dark Glassmorphic UI)
            g2.setColor(new Color(11, 19, 41, 240));
            g2.fillRoundRect(20, 16, width - 40, 60, 16, 16);
            g2.setColor(new Color(35, 52, 98));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(20, 16, width - 40, 60, 16, 16);

            // Brand Logo
            g2.setColor(new Color(37, 99, 235));
            g2.fillRoundRect(36, 26, 40, 40, 10, 10);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2.drawString("R1", 44, 52);

            // Title & Subtitle
            g2.setFont(new Font("Segoe UI", Font.BOLD, 17));
            g2.drawString("RUNGTA R1 SMART CAMPUS NAVIGATION SYSTEM", 90, 42);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.setColor(new Color(148, 163, 184));
            g2.drawString("Kohka–Kurud Road, Bhilai, Chhattisgarh • Coordinates: 21.2350° N, 81.3462° E • Leaflet OpenStreetMap", 90, 62);

            // Status Badge
            g2.setColor(new Color(16, 185, 129, 40));
            g2.fillRoundRect(width - 240, 28, 205, 36, 18, 18);
            g2.setColor(new Color(16, 185, 129));
            g2.drawRoundRect(width - 240, 28, 205, 36, 18, 18);
            g2.fillOval(width - 225, 42, 8, 8);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.drawString("VERIFIED CAMPUS MAP", width - 210, 51);

            // 7. Navigation Result HUD Overlay (Bottom-Left)
            int hudX = 40, hudY = 460, hudW = 420, hudH = 155;
            g2.setColor(new Color(11, 19, 41, 245));
            g2.fillRoundRect(hudX, hudY, hudW, hudH, 18, 18);
            g2.setColor(new Color(37, 99, 235, 180));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(hudX, hudY, hudW, hudH, 18, 18);

            // HUD Algorithm Badge
            g2.setColor(new Color(37, 99, 235, 40));
            g2.fillRoundRect(hudX + 16, hudY + 14, 210, 22, 6, 6);
            g2.setColor(new Color(56, 189, 248));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.drawString("⚡ DIJKSTRA'S SHORTEST PATH", hudX + 24, hudY + 29);

            // Route breadcrumbs
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.drawString("Main Entrance  ➔  Block A  ➔  Central Library", hudX + 16, hudY + 54);

            // Metrics row
            drawMetricBox(g2, hudX + 16, hudY + 68, "DISTANCE", "205 m", new Color(0, 240, 255));
            drawMetricBox(g2, hudX + 140, hudY + 68, "WALK TIME", "2.6 mins", new Color(16, 185, 129));
            drawMetricBox(g2, hudX + 264, hudY + 68, "STOPS (LEGS)", "2 edges", new Color(245, 158, 11));

            // Turn hint
            g2.setColor(new Color(148, 163, 184));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.drawString("✓ Turn-by-Turn: Walk straight 95m to Block A, then 110m along walkway to Library.", hudX + 16, hudY + 138);

            // 8. Legend Card (Bottom-Right)
            int legX = width - 260, legY = height - 170, legW = 220, legH = 135;
            g2.setColor(new Color(11, 19, 41, 230));
            g2.fillRoundRect(legX, legY, legW, legH, 14, 14);
            g2.setColor(new Color(35, 52, 98));
            g2.drawRoundRect(legX, legY, legW, legH, 14, 14);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.drawString("CAMPUS MAP LEGEND", legX + 14, legY + 20);

            drawLegendItem(g2, legX + 14, legY + 40, new Color(16, 185, 129), "Start Node (Main Entrance)");
            drawLegendItem(g2, legX + 14, legY + 62, new Color(236, 72, 153), "Target Destination (Library)");
            drawLegendItem(g2, legX + 14, legY + 84, new Color(37, 99, 235), "Academic & Auditorium Blocks");
            drawLegendItem(g2, legX + 14, legY + 106, new Color(0, 240, 255), "Active Walk Route (Dijkstra)");

            g2.dispose();

            File outArtifact = new File("C:/Users/Asus/.gemini/antigravity/brain/f278c683-52e8-416e-82a6-8447b6ee2bc0/rungta_campus_preview.png");
            ImageIO.write(image, "png", outArtifact);

            File outLocal = new File("rungta_campus_preview.png");
            ImageIO.write(image, "png", outLocal);

            System.out.println("PREVIEW_IMAGE_SAVED: " + outArtifact.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void drawBuildingCard(Graphics2D g2, int cx, int cy, String name, String subtitle, Color color, boolean isEndpoint, String tag) {
        int cardW = 160;
        int cardH = 46;
        int rx = cx - cardW / 2;
        int ry = cy - cardH / 2;

        // Outer glow
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
        g2.fillRoundRect(rx - 4, ry - 4, cardW + 8, cardH + 8, 12, 12);

        // Card Body
        g2.setColor(new Color(16, 27, 56));
        g2.fillRoundRect(rx, ry, cardW, cardH, 10, 10);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(isEndpoint ? 2.5f : 1.2f));
        g2.drawRoundRect(rx, ry, cardW, cardH, 10, 10);

        // Pin icon circle
        g2.setColor(color);
        g2.fillOval(rx + 8, ry + 13, 20, 20);
        g2.setColor(Color.WHITE);
        g2.fillOval(rx + 15, ry + 20, 6, 6);

        // Text
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(Color.WHITE);
        g2.drawString(name, rx + 34, ry + 20);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        g2.setColor(new Color(148, 163, 184));
        String sub = subtitle.length() > 24 ? subtitle.substring(0, 22) + ".." : subtitle;
        g2.drawString(sub, rx + 34, ry + 34);

        if (tag != null) {
            int tagW = 70;
            int tagH = 15;
            g2.setColor(color);
            g2.fillRoundRect(cx - tagW/2, ry - 18, tagW, tagH, 5, 5);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 8));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(tag, cx - fm.stringWidth(tag)/2, ry - 7);
        }
    }

    private static void drawDistancePill(Graphics2D g2, String text, int cx, int cy) {
        g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(text) + 12;
        int h = 15;
        g2.setColor(new Color(11, 19, 41));
        g2.fillRoundRect(cx - w/2, cy - h/2, w, h, 8, 8);
        g2.setColor(new Color(71, 85, 105));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(cx - w/2, cy - h/2, w, h, 8, 8);
        g2.setColor(new Color(148, 163, 184));
        g2.drawString(text, cx - fm.stringWidth(text)/2, cy + 3);
    }

    private static void drawHighlightedPill(Graphics2D g2, String text, int cx, int cy) {
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(text) + 16;
        int h = 18;
        g2.setColor(new Color(37, 99, 235));
        g2.fillRoundRect(cx - w/2, cy - h/2, w, h, 9, 9);
        g2.setColor(new Color(0, 240, 255));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(cx - w/2, cy - h/2, w, h, 9, 9);
        g2.setColor(Color.WHITE);
        g2.drawString(text, cx - fm.stringWidth(text)/2, cy + 4);
    }

    private static void drawMetricBox(Graphics2D g2, int x, int y, String label, String val, Color c) {
        g2.setColor(new Color(16, 27, 56));
        g2.fillRoundRect(x, y, 110, 48, 10, 10);
        g2.setColor(new Color(35, 52, 98));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, 110, 48, 10, 10);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 8));
        g2.setColor(new Color(148, 163, 184));
        g2.drawString(label, x + 10, y + 16);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        g2.setColor(c);
        g2.drawString(val, x + 10, y + 38);
    }

    private static void drawLegendItem(Graphics2D g2, int x, int y, Color c, String text) {
        g2.setColor(c);
        g2.fillOval(x, y - 8, 10, 10);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.setColor(new Color(203, 213, 225));
        g2.drawString(text, x + 18, y);
    }
}
