package com.campus.navigator.ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern design system for Campus Navigator.
 * Inspired by modern UI libraries with clean typography, rounded cards,
 * soft shadows, and vibrant campus navigation accents.
 */
public class UITheme {

    // --- Color Palette ---
    public static final Color PRIMARY = new Color(79, 70, 229);         // Indigo #4F46E5
    public static final Color PRIMARY_HOVER = new Color(67, 56, 202);   // Indigo-700
    public static final Color PRIMARY_LIGHT = new Color(238, 242, 255); // Indigo-50
    
    public static final Color SECONDARY = new Color(14, 165, 233);       // Sky blue #0EA5E9
    public static final Color SECONDARY_HOVER = new Color(2, 132, 199);
    
    public static final Color SUCCESS = new Color(16, 185, 129);         // Emerald #10B981
    public static final Color SUCCESS_LIGHT = new Color(209, 250, 229);
    
    public static final Color WARNING = new Color(245, 158, 11);         // Amber #F59E0B
    public static final Color WARNING_LIGHT = new Color(254, 243, 199);
    
    public static final Color DANGER = new Color(239, 68, 68);           // Crimson #EF4444
    public static final Color DANGER_LIGHT = new Color(254, 226, 226);
    
    public static final Color BG_APP = new Color(248, 250, 252);         // Slate-50
    public static final Color BG_CARD = Color.WHITE;
    public static final Color BG_SIDEBAR = new Color(241, 245, 249);     // Slate-100
    public static final Color BG_MAP = new Color(248, 250, 252);
    
    public static final Color TEXT_MAIN = new Color(15, 23, 42);         // Slate-900
    public static final Color TEXT_MUTED = new Color(100, 116, 139);     // Slate-500
    public static final Color TEXT_LIGHT = new Color(148, 163, 184);     // Slate-400
    
    public static final Color BORDER = new Color(226, 232, 240);         // Slate-200
    public static final Color BORDER_STRONG = new Color(203, 213, 225);  // Slate-300

    // Map Specific Colors
    public static final Color NODE_DEFAULT = new Color(51, 65, 85);      // Slate-700
    public static final Color NODE_START = new Color(16, 185, 129);      // Emerald
    public static final Color NODE_DEST = new Color(239, 68, 68);        // Rose/Crimson
    public static final Color NODE_PATH = new Color(245, 158, 11);       // Amber
    public static final Color EDGE_DEFAULT = new Color(203, 213, 225);   // Slate-300
    public static final Color EDGE_HIGHLIGHT = new Color(99, 102, 241);  // Indigo-500

    // --- Typography ---
    public static final Font FONT_HEADER_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER_SUB = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_METRIC_VAL = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_METRIC_LABEL = new Font("Segoe UI", Font.PLAIN, 11);

    /**
     * Creates a styled modern card panel with rounded borders, white background and subtle padding.
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(14, 16, 14, 16));
        return panel;
    }

    /**
     * Creates a modern rounded action button with hover transitions.
     */
    public static JButton createButton(String text, Color bg, Color hoverBg, Color fg) {
        JButton btn = new JButton(text) {
            private boolean isHovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isHovered ? hoverBg : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                FontMetrics fm = g2.getFontMetrics(getFont());
                Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
                int textX = (getWidth() - stringBounds.width) / 2;
                int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();

                g2.setColor(fg);
                g2.setFont(getFont());
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };

        btn.setFont(FONT_BOLD);
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, 38));
        return btn;
    }

    /**
     * Primary action button (Indigo).
     */
    public static JButton createPrimaryButton(String text) {
        return createButton(text, PRIMARY, PRIMARY_HOVER, Color.WHITE);
    }

    /**
     * Secondary action button (Sky Blue).
     */
    public static JButton createSecondaryButton(String text) {
        return createButton(text, SECONDARY, SECONDARY_HOVER, Color.WHITE);
    }

    /**
     * Outline / Ghost button.
     */
    public static JButton createOutlineButton(String text) {
        JButton btn = new JButton(text) {
            private boolean isHovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isHovered ? BG_SIDEBAR : BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(BORDER_STRONG);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                FontMetrics fm = g2.getFontMetrics(getFont());
                Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
                int textX = (getWidth() - stringBounds.width) / 2;
                int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();

                g2.setColor(isHovered ? PRIMARY : TEXT_MAIN);
                g2.setFont(getFont());
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };

        btn.setFont(FONT_BOLD);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, 34));
        return btn;
    }

    /**
     * Creates a styled modern combo box.
     */
    public static JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(FONT_REGULAR);
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXT_MAIN);
        combo.setPreferredSize(new Dimension(combo.getPreferredSize().width, 36));
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return combo;
    }

    /**
     * Metric pill box for displaying stats like Distance and Stops.
     */
    public static JPanel createMetricBox(String label, String value, Color accentColor) {
        JPanel box = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 18));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
            }
        };
        box.setOpaque(false);
        box.setLayout(new BorderLayout(4, 2));
        box.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(FONT_METRIC_VAL);
        valLbl.setForeground(accentColor);

        JLabel descLbl = new JLabel(label.toUpperCase());
        descLbl.setFont(FONT_METRIC_LABEL);
        descLbl.setForeground(TEXT_MUTED);

        box.add(valLbl, BorderLayout.CENTER);
        box.add(descLbl, BorderLayout.SOUTH);
        return box;
    }
}
