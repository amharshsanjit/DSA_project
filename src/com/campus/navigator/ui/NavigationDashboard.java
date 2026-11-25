package com.campus.navigator.ui;

import com.campus.navigator.algorithm.GraphAlgorithms;
import com.campus.navigator.model.CampusGraph;
import com.campus.navigator.model.NavigationResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Modern Campus Navigation Dashboard.
 * Houses the header, sidebar controls, campus map visualization,
 * and comprehensive route result reporting.
 */
public class NavigationDashboard extends JFrame {

    private final CampusGraph graph;
    private final CampusMapPanel mapPanel;

    // Controls
    private JComboBox<String> startCombo;
    private JComboBox<String> destCombo;
    private DefaultComboBoxModel<String> startModel;
    private DefaultComboBoxModel<String> destModel;

    // Result Card Components
    private JPanel resultCard;
    private JLabel algoBadge;
    private JLabel pathLabel;
    private JLabel distanceValLabel;
    private JLabel stopsValLabel;
    private JLabel intermediateValLabel;
    private JPanel stepsContainer;
    private JPanel errorBanner;
    private JLabel errorBannerLabel;

    public NavigationDashboard(CampusGraph graph) {
        super("Campus Navigator - College Campus Navigation System");
        this.graph = graph;
        this.mapPanel = new CampusMapPanel(graph);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 840);
        setMinimumSize(new Dimension(1020, 720));
        setLocationRelativeTo(null);

        // Main Background
        getContentPane().setBackground(UITheme.BG_APP);
        setLayout(new BorderLayout(0, 0));

        // 1. Header
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Central Split: Left Controls & Bottom Results, Center/Right Map
        JPanel mainContent = new JPanel(new BorderLayout(14, 14));
        mainContent.setBackground(UITheme.BG_APP);
        mainContent.setBorder(new EmptyBorder(12, 16, 16, 16));

        // Left Panel (Control Sidebar)
        JPanel leftSidebar = createSidebarPanel();
        mainContent.add(leftSidebar, BorderLayout.WEST);

        // Center Area (Map on Top, Result Card at Bottom)
        JPanel centerArea = new JPanel(new BorderLayout(12, 12));
        centerArea.setOpaque(false);

        // Map Wrapper Card
        JPanel mapCard = UITheme.createCardPanel();
        mapCard.setLayout(new BorderLayout());
        mapCard.add(mapPanel, BorderLayout.CENTER);
        centerArea.add(mapCard, BorderLayout.CENTER);

        // Result Card at Bottom
        resultCard = createResultCard();
        centerArea.add(resultCard, BorderLayout.SOUTH);

        mainContent.add(centerArea, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);

        // Subscribe to graph changes to update dropdown lists
        graph.addChangeListener(this::refreshLocationDropdowns);
        refreshLocationDropdowns();

        // Initial default route calculation for visual demonstration
        SwingUtilities.invokeLater(() -> executeShortestDistance("Gate", "Canteen"));
    }

    /**
     * Top Header section: Application name, subtitle, icon, welcome message.
     */
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                new EmptyBorder(14, 20, 14, 20)
        ));

        // Left Brand
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        brandPanel.setOpaque(false);

        // Campus Logo Badge Icon
        JLabel logoIcon = new JLabel("🧭");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JPanel titleBlock = new JPanel(new GridLayout(2, 1, 0, 2));
        titleBlock.setOpaque(false);

        JLabel title = new JLabel("Campus Navigator");
        title.setFont(UITheme.FONT_HEADER_TITLE);
        title.setForeground(UITheme.TEXT_MAIN);

        JLabel subtitle = new JLabel("Find your way around campus • Graph, BFS & Dijkstra Routing");
        subtitle.setFont(UITheme.FONT_HEADER_SUB);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        titleBlock.add(title);
        titleBlock.add(subtitle);

        brandPanel.add(logoIcon);
        brandPanel.add(titleBlock);
        header.add(brandPanel, BorderLayout.WEST);

        // Right Welcome Badge
        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        rightBadge.setOpaque(false);

        JLabel welcomeMsg = new JLabel("Welcome, Explorer! 👋  College Campus Map Online");
        welcomeMsg.setFont(new Font("Segoe UI", Font.BOLD, 12));
        welcomeMsg.setForeground(UITheme.PRIMARY);

        JPanel pill = new JPanel();
        pill.setBackground(UITheme.PRIMARY_LIGHT);
        pill.setBorder(new EmptyBorder(6, 12, 6, 12));
        pill.add(welcomeMsg);

        rightBadge.add(pill);
        header.add(rightBadge, BorderLayout.EAST);

        return header;
    }

    /**
     * Left Sidebar Panel containing Navigation Inputs, Graph Management, and Quick Test Cases.
     */
    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(320, 0));

        // --- Card 1: Route Planning Controls ---
        JPanel routeCard = UITheme.createCardPanel();
        routeCard.setLayout(new BoxLayout(routeCard, BoxLayout.Y_AXIS));

        JLabel routeCardTitle = new JLabel("Route Finder");
        routeCardTitle.setFont(UITheme.FONT_SECTION);
        routeCardTitle.setForeground(UITheme.TEXT_MAIN);
        routeCardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        routeCard.add(routeCardTitle);
        routeCard.add(Box.createVerticalStrut(12));

        // Starting Location
        JLabel fromLabel = new JLabel("Starting Location:");
        fromLabel.setFont(UITheme.FONT_BOLD);
        fromLabel.setForeground(UITheme.TEXT_MUTED);
        fromLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        routeCard.add(fromLabel);
        routeCard.add(Box.createVerticalStrut(4));

        startModel = new DefaultComboBoxModel<>();
        startCombo = new JComboBox<>(startModel);
        startCombo.setFont(UITheme.FONT_REGULAR);
        startCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        startCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        routeCard.add(startCombo);
        routeCard.add(Box.createVerticalStrut(8));

        // Swap button
        JButton swapBtn = UITheme.createOutlineButton("⇅ Swap Start & Destination");
        swapBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        swapBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        swapBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        swapBtn.addActionListener(e -> {
            int s = startCombo.getSelectedIndex();
            int d = destCombo.getSelectedIndex();
            startCombo.setSelectedIndex(d);
            destCombo.setSelectedIndex(s);
        });
        routeCard.add(swapBtn);
        routeCard.add(Box.createVerticalStrut(8));

        // Destination Location
        JLabel toLabel = new JLabel("Destination Location:");
        toLabel.setFont(UITheme.FONT_BOLD);
        toLabel.setForeground(UITheme.TEXT_MUTED);
        toLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        routeCard.add(toLabel);
        routeCard.add(Box.createVerticalStrut(4));

        destModel = new DefaultComboBoxModel<>();
        destCombo = new JComboBox<>(destModel);
        destCombo.setFont(UITheme.FONT_REGULAR);
        destCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        destCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        routeCard.add(destCombo);
        routeCard.add(Box.createVerticalStrut(14));

        // Prominent Button 1: Find Shortest Distance (Dijkstra)
        JButton btnDijkstra = UITheme.createPrimaryButton("⚡ Find Shortest Distance");
        btnDijkstra.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnDijkstra.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDijkstra.setToolTipText("Find route with minimum total metres using Dijkstra's Algorithm");
        btnDijkstra.addActionListener(e -> {
            String src = (String) startCombo.getSelectedItem();
            String dst = (String) destCombo.getSelectedItem();
            executeShortestDistance(src, dst);
        });
        routeCard.add(btnDijkstra);
        routeCard.add(Box.createVerticalStrut(8));

        // Prominent Button 2: Find Fewest Stops (BFS)
        JButton btnBFS = UITheme.createSecondaryButton("🎯 Find Fewest Stops");
        btnBFS.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBFS.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnBFS.setToolTipText("Find route with minimum edge hops using Breadth-First Search");
        btnBFS.addActionListener(e -> {
            String src = (String) startCombo.getSelectedItem();
            String dst = (String) destCombo.getSelectedItem();
            executeFewestStops(src, dst);
        });
        routeCard.add(btnBFS);

        sidebar.add(routeCard);
        sidebar.add(Box.createVerticalStrut(12));

        // --- Card 2: Campus Management (Add Location & Add Path) ---
        JPanel manageCard = UITheme.createCardPanel();
        manageCard.setLayout(new BoxLayout(manageCard, BoxLayout.Y_AXIS));

        JLabel manageTitle = new JLabel("Campus Management");
        manageTitle.setFont(UITheme.FONT_SECTION);
        manageTitle.setForeground(UITheme.TEXT_MAIN);
        manageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        manageCard.add(manageTitle);
        manageCard.add(Box.createVerticalStrut(10));

        JButton addLocBtn = UITheme.createOutlineButton("➕ Add Location");
        addLocBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        addLocBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addLocBtn.addActionListener(e -> {
            AddLocationDialog dlg = new AddLocationDialog(this, graph);
            dlg.setVisible(true);
        });
        manageCard.add(addLocBtn);
        manageCard.add(Box.createVerticalStrut(6));

        JButton addPathBtn = UITheme.createOutlineButton("🛣️ Add Road / Path");
        addPathBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        addPathBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addPathBtn.addActionListener(e -> {
            AddPathDialog dlg = new AddPathDialog(this, graph);
            dlg.setVisible(true);
        });
        manageCard.add(addPathBtn);
        manageCard.add(Box.createVerticalStrut(6));

        JButton resetBtn = UITheme.createOutlineButton("🔄 Reset Campus Graph");
        resetBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resetBtn.setForeground(UITheme.TEXT_MUTED);
        resetBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        resetBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Reset the graph to the default 8 campus locations and initial connections?",
                    "Confirm Reset", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                graph.initializeDefaultCampus();
                mapPanel.clearRoute();
                clearResultDisplay();
                JOptionPane.showMessageDialog(this, "Graph restored to default state.", "Reset Complete", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        manageCard.add(resetBtn);

        sidebar.add(manageCard);
        sidebar.add(Box.createVerticalStrut(12));

        // --- Card 3: Quick Test Cases (1-Click Evaluation of Required Cases) ---
        JPanel testCard = UITheme.createCardPanel();
        testCard.setLayout(new BoxLayout(testCard, BoxLayout.Y_AXIS));

        JLabel testTitle = new JLabel("Expected Test Cases");
        testTitle.setFont(UITheme.FONT_SECTION);
        testTitle.setForeground(UITheme.TEXT_MAIN);
        testTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        testCard.add(testTitle);
        testCard.add(Box.createVerticalStrut(8));

        testCard.add(createTestCaseButton("Test 1: Gate → Canteen (Shortest: 150m)", e -> {
            startCombo.setSelectedItem("Gate");
            destCombo.setSelectedItem("Canteen");
            executeShortestDistance("Gate", "Canteen");
        }));
        testCard.add(Box.createVerticalStrut(4));

        testCard.add(createTestCaseButton("Test 2: Gate → Gate (Boundary: 0m)", e -> {
            startCombo.setSelectedItem("Gate");
            destCombo.setSelectedItem("Gate");
            executeShortestDistance("Gate", "Gate");
        }));
        testCard.add(Box.createVerticalStrut(4));

        testCard.add(createTestCaseButton("Test 3: Gate → Hostel (Invalid Location)", e -> {
            executeShortestDistance("Gate", "Hostel");
        }));
        testCard.add(Box.createVerticalStrut(4));

        testCard.add(createTestCaseButton("Test 4(a): Admin → Canteen (Fewest: 1 stop)", e -> {
            startCombo.setSelectedItem("Admin");
            destCombo.setSelectedItem("Canteen");
            executeFewestStops("Admin", "Canteen");
        }));
        testCard.add(Box.createVerticalStrut(4));

        testCard.add(createTestCaseButton("Test 4(b): Admin → Canteen (Shortest: 100m)", e -> {
            startCombo.setSelectedItem("Admin");
            destCombo.setSelectedItem("Canteen");
            executeShortestDistance("Admin", "Canteen");
        }));

        sidebar.add(testCard);
        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JButton createTestCaseButton(String label, ActionListener al) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(Color.WHITE);
        btn.addActionListener(al);
        return btn;
    }

    /**
     * Bottom Result Card: Displays algorithm badge, path breadcrumb, metrics, steps, and error banner.
     */
    private JPanel createResultCard() {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(14, 10));

        // Top Row: Algorithm Badge + Status
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        algoBadge = new JLabel("Dijkstra's Algorithm (Shortest Distance)");
        algoBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        algoBadge.setForeground(UITheme.PRIMARY);

        JPanel algoPill = new JPanel();
        algoPill.setBackground(UITheme.PRIMARY_LIGHT);
        algoPill.setBorder(new EmptyBorder(4, 10, 4, 10));
        algoPill.add(algoBadge);
        topRow.add(algoPill, BorderLayout.WEST);

        // Error Banner (initially hidden)
        errorBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        errorBanner.setBackground(UITheme.DANGER_LIGHT);
        errorBanner.setBorder(BorderFactory.createLineBorder(UITheme.DANGER, 1));
        errorBannerLabel = new JLabel("Error message");
        errorBannerLabel.setFont(UITheme.FONT_BOLD);
        errorBannerLabel.setForeground(UITheme.DANGER);
        errorBanner.add(errorBannerLabel);
        errorBanner.setVisible(false);
        topRow.add(errorBanner, BorderLayout.CENTER);

        card.add(topRow, BorderLayout.NORTH);

        // Center: Path Breadcrumbs & Step Details
        JPanel centerBody = new JPanel(new BorderLayout(8, 8));
        centerBody.setOpaque(false);

        JPanel pathRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        pathRow.setOpaque(false);
        JLabel pathTitle = new JLabel("Complete Route: ");
        pathTitle.setFont(UITheme.FONT_BOLD);
        pathTitle.setForeground(UITheme.TEXT_MUTED);

        pathLabel = new JLabel("Gate → Admin → Library → Canteen");
        pathLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pathLabel.setForeground(UITheme.TEXT_MAIN);

        pathRow.add(pathTitle);
        pathRow.add(pathLabel);
        centerBody.add(pathRow, BorderLayout.NORTH);

        // Step-by-step turn details
        stepsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        stepsContainer.setOpaque(false);
        centerBody.add(stepsContainer, BorderLayout.CENTER);

        card.add(centerBody, BorderLayout.CENTER);

        // Right Metrics: Total Distance & Total Stops
        JPanel metricsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        metricsPanel.setOpaque(false);

        distanceValLabel = new JLabel("150 m");
        distanceValLabel.setFont(UITheme.FONT_METRIC_VAL);
        distanceValLabel.setForeground(UITheme.PRIMARY);

        stopsValLabel = new JLabel("3");
        stopsValLabel.setFont(UITheme.FONT_METRIC_VAL);
        stopsValLabel.setForeground(UITheme.SECONDARY);

        intermediateValLabel = new JLabel("2");
        intermediateValLabel.setFont(UITheme.FONT_METRIC_VAL);
        intermediateValLabel.setForeground(UITheme.WARNING);

        JPanel distBox = UITheme.createMetricBox("Total Distance", "150 m", UITheme.PRIMARY);
        JPanel stopsBox = UITheme.createMetricBox("Stops (Hops)", "3", UITheme.SECONDARY);
        JPanel intermediateBox = UITheme.createMetricBox("Intermediate", "2", UITheme.WARNING);

        metricsPanel.add(distBox);
        metricsPanel.add(stopsBox);
        metricsPanel.add(intermediateBox);

        card.add(metricsPanel, BorderLayout.EAST);

        return card;
    }

    private void refreshLocationDropdowns() {
        String currentStart = (String) startCombo.getSelectedItem();
        String currentDest = (String) destCombo.getSelectedItem();

        startModel.removeAllElements();
        destModel.removeAllElements();

        List<String> locations = graph.getAllLocationNames();
        for (String loc : locations) {
            startModel.addElement(loc);
            destModel.addElement(loc);
        }

        if (currentStart != null && graph.hasLocation(currentStart)) {
            startCombo.setSelectedItem(currentStart);
        } else if (graph.hasLocation("Gate")) {
            startCombo.setSelectedItem("Gate");
        }

        if (currentDest != null && graph.hasLocation(currentDest)) {
            destCombo.setSelectedItem(currentDest);
        } else if (graph.hasLocation("Canteen")) {
            destCombo.setSelectedItem("Canteen");
        }
    }

    public void executeShortestDistance(String src, String dst) {
        NavigationResult res = GraphAlgorithms.findShortestDistance(graph, src, dst);
        displayResult(res);
    }

    public void executeFewestStops(String src, String dst) {
        NavigationResult res = GraphAlgorithms.findFewestStops(graph, src, dst);
        displayResult(res);
    }

    private void displayResult(NavigationResult res) {
        algoBadge.setText(res.getAlgorithmName());

        if (res.isSuccess()) {
            errorBanner.setVisible(false);
            pathLabel.setVisible(true);
            pathLabel.setText(res.getFormattedPath());
            pathLabel.setForeground(UITheme.TEXT_MAIN);

            // Update metrics
            updateMetricBox(0, res.getTotalDistance() + " m");
            updateMetricBox(1, String.valueOf(res.getTotalStops()));
            updateMetricBox(2, String.valueOf(res.getIntermediateStops()));

            // Update step-by-step badges
            stepsContainer.removeAll();
            stepsContainer.setVisible(true);
            for (String step : res.getStepDetails()) {
                JLabel pill = new JLabel(step);
                pill.setFont(UITheme.FONT_SMALL);
                pill.setForeground(UITheme.TEXT_MUTED);
                pill.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.BORDER, 1),
                        BorderFactory.createEmptyBorder(2, 6, 2, 6)
                ));
                stepsContainer.add(pill);
            }
            stepsContainer.revalidate();
            stepsContainer.repaint();

            // Highlight path on map
            mapPanel.setActiveRoute(res);
        } else {
            // Error handling
            errorBannerLabel.setText("⚠️ " + res.getErrorMessage());
            errorBanner.setVisible(true);
            pathLabel.setVisible(false);
            stepsContainer.setVisible(false);

            updateMetricBox(0, "—");
            updateMetricBox(1, "—");
            updateMetricBox(2, "—");

            mapPanel.clearRoute();
        }

        resultCard.revalidate();
        resultCard.repaint();
    }

    private void updateMetricBox(int boxIndex, String value) {
        // Child 2 of resultCard is metricsPanel
        Component comp = ((BorderLayout) resultCard.getLayout()).getLayoutComponent(BorderLayout.EAST);
        if (comp instanceof JPanel) {
            JPanel metricsPanel = (JPanel) comp;
            if (boxIndex < metricsPanel.getComponentCount()) {
                JPanel box = (JPanel) metricsPanel.getComponent(boxIndex);
                Component valComp = ((BorderLayout) box.getLayout()).getLayoutComponent(BorderLayout.CENTER);
                if (valComp instanceof JLabel) {
                    ((JLabel) valComp).setText(value);
                }
            }
        }
    }

    private void clearResultDisplay() {
        errorBanner.setVisible(false);
        pathLabel.setText("Select starting point and destination to compute route.");
        stepsContainer.removeAll();
        updateMetricBox(0, "0 m");
        updateMetricBox(1, "0");
        updateMetricBox(2, "0");
        resultCard.revalidate();
        resultCard.repaint();
    }
}
