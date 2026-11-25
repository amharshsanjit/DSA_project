package com.campus.navigator.ui;

import com.campus.navigator.model.CampusGraph;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Modern modal dialog for dynamically connecting two campus locations with a road/path.
 */
public class AddPathDialog extends JDialog {

    private final CampusGraph graph;
    private final JComboBox<String> fromCombo;
    private final JComboBox<String> toCombo;
    private final JSpinner distanceSpinner;
    private boolean success = false;

    public AddPathDialog(Frame parent, CampusGraph graph) {
        super(parent, "Add Campus Road / Path", true);
        this.graph = graph;

        setLayout(new BorderLayout());
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.SECONDARY);
        headerPanel.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel titleLabel = new JLabel("Add Connecting Road / Walkway");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JLabel subLabel = new JLabel("Create a bidirectional road between two existing campus locations");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(new Color(224, 242, 254));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 6, 8, 6);

        List<String> locations = graph.getAllLocationNames();
        String[] locArray = locations.toArray(new String[0]);

        // Location 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        JLabel fromLabel = new JLabel("First Location:");
        fromLabel.setFont(UITheme.FONT_BOLD);
        formPanel.add(fromLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        fromCombo = new JComboBox<>(locArray);
        fromCombo.setFont(UITheme.FONT_REGULAR);
        formPanel.add(fromCombo, gbc);

        // Location 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35;
        JLabel toLabel = new JLabel("Second Location:");
        toLabel.setFont(UITheme.FONT_BOLD);
        formPanel.add(toLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        toCombo = new JComboBox<>(locArray);
        toCombo.setFont(UITheme.FONT_REGULAR);
        if (locArray.length > 1) {
            toCombo.setSelectedIndex(1);
        }
        formPanel.add(toCombo, gbc);

        // Distance in metres
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.35;
        JLabel distLabel = new JLabel("Distance (metres):");
        distLabel.setFont(UITheme.FONT_BOLD);
        formPanel.add(distLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        distanceSpinner = new JSpinner(new SpinnerNumberModel(75, 1, 5000, 5));
        distanceSpinner.setFont(UITheme.FONT_REGULAR);
        formPanel.add(distanceSpinner, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        buttonPanel.setBackground(UITheme.BG_APP);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));

        JButton cancelBtn = UITheme.createOutlineButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JButton addBtn = UITheme.createSecondaryButton("Add Road");
        addBtn.addActionListener(e -> onAddPath());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(addBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    private void onAddPath() {
        String loc1 = (String) fromCombo.getSelectedItem();
        String loc2 = (String) toCombo.getSelectedItem();
        int distance = (Integer) distanceSpinner.getValue();

        if (loc1 == null || loc2 == null) {
            JOptionPane.showMessageDialog(this, "Please select both locations.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (loc1.equalsIgnoreCase(loc2)) {
            JOptionPane.showMessageDialog(this, "Cannot create a road between a location and itself.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (distance <= 0) {
            JOptionPane.showMessageDialog(this, "Distance must be a positive number greater than 0.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String result = graph.addPath(loc1, loc2, distance);
        if ("SUCCESS".equals(result)) {
            success = true;
            JOptionPane.showMessageDialog(this,
                    "Road added between '" + loc1 + "' and '" + loc2 + "' (" + distance + " m) successfully!",
                    "Road Added", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
