package com.campus.navigator.test;

import com.campus.navigator.model.CampusGraph;
import com.campus.navigator.ui.NavigationDashboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class RenderSwingScreenshot {
    public static void main(String[] args) {
        try {
            System.setProperty("java.awt.headless", "true");
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

            CampusGraph graph = new CampusGraph();
            NavigationDashboard dashboard = new NavigationDashboard(graph);
            dashboard.setSize(1200, 780);
            dashboard.doLayout();
            
            // Revalidate and layout children
            layoutComponent(dashboard);

            BufferedImage image = new BufferedImage(1200, 780, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            dashboard.paint(g2);
            g2.dispose();

            File outDir = new File("C:/Users/Asus/.gemini/antigravity/brain/f278c683-52e8-416e-82a6-8447b6ee2bc0");
            if (!outDir.exists()) outDir.mkdirs();
            File outFile = new File(outDir, "campus_swing_preview.png");
            ImageIO.write(image, "png", outFile);
            System.out.println("SCREENSHOT_SAVED: " + outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void layoutComponent(Component c) {
        c.doLayout();
        if (c instanceof Container) {
            for (Component child : ((Container) c).getComponents()) {
                layoutComponent(child);
            }
        }
    }
}
