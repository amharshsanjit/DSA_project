package com.campus.navigator.test;

import com.campus.navigator.algorithm.GraphAlgorithms;
import com.campus.navigator.model.CampusGraph;
import com.campus.navigator.model.NavigationResult;
import com.campus.navigator.ui.CampusMapPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GenerateMapImage {
    public static void main(String[] args) {
        try {
            System.setProperty("java.awt.headless", "true");
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

            CampusGraph graph = new CampusGraph();
            NavigationResult res = GraphAlgorithms.findShortestDistance(graph, "Gate", "Canteen");

            int width = 960;
            int height = 540;

            CampusMapPanel mapPanel = new CampusMapPanel(graph);
            mapPanel.setSize(width, height);
            mapPanel.setActiveRoute(res);
            mapPanel.doLayout();

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            mapPanel.paint(g2);

            // Draw a top banner with route info
            g2.setColor(new Color(255, 255, 255, 240));
            g2.fillRoundRect(16, 16, 520, 70, 12, 12);
            g2.setColor(new Color(226, 232, 240));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(16, 16, 520, 70, 12, 12);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.setColor(new Color(15, 23, 42));
            g2.drawString("🧭 Campus Navigator - Shortest Route (Dijkstra)", 30, 40);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.setColor(new Color(79, 70, 229));
            g2.drawString("Path: " + res.getFormattedPath(), 30, 60);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.setColor(new Color(100, 116, 139));
            g2.drawString("Total Distance: " + res.getTotalDistance() + " m  |  Stops: " + res.getTotalStops(), 30, 76);

            g2.dispose();

            // Save in both artifact dir and workspace dir
            File artifactDir = new File("C:/Users/Asus/.gemini/antigravity/brain/f278c683-52e8-416e-82a6-8447b6ee2bc0");
            if (!artifactDir.exists()) artifactDir.mkdirs();
            File outArtifact = new File(artifactDir, "campus_preview.png");
            ImageIO.write(image, "png", outArtifact);

            File outLocal = new File("campus_preview.png");
            ImageIO.write(image, "png", outLocal);

            System.out.println("IMAGE_GENERATED: " + outArtifact.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
