package com.campus.navigator.ui;

import com.campus.navigator.model.CampusGraph;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Modern modal dialog for dynamically adding a new campus location.
 */
public class AddLocationDialog extends JDialog {

    private final CampusGraph graph;
    private final JTextField nameField;
    private final JComboBox<String> categoryCombo;
    private final JSpinner xSpinner;
    private final JSpinner ySpinner;
    private boolean success = false;

    public AddLocationDialog(Frame parent, CampusGraph graph) {
        super(parent, "Add Campus Location", true);
        this.graph = graph;

        setLayout(new BorderLayout());
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY);
        headerPanel.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel titleLabel = new JLabel("Add New Campus Location");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JLabel subLabel = new JLabel("Define a new building, junction, or facility on campus");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(new Color(224, 231, 255));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        // Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel nameLabel = new JLabel("Location Name:");
        nameLabel.setFont(UITheme.FONT_BOLD);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        nameField = new JTextField(15);
        nameField.setFont(UITheme.FONT_REGULAR);
        nameField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(nameField, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel catLabel = new JLabel("Category:");
        catLabel.setFont(UITheme.FONT_BOLD);
        formPanel.add(catLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        String[] categories = {"Academic", "Hostel / Residence", "Cafeteria / Dining", "Sports & Recreation", "Auditorium", "Facility / Office"};
        categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(UITheme.FONT_REGULAR);
        formPanel.add(categoryCombo, gbc);

        // Position coordinates
        int count = graph.getVertexCount();
        int defaultX = 220 + (count * 60) % 460;
        int defaultY = 160 + (count * 50) % 300;

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        JLabel xLabel = new JLabel("Map X Position:");
        xLabel.setFont(UITheme.FONT_BOLD);
        formPanel.add(xLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        xSpinner = new JSpinner(new SpinnerNumberModel(defaultX, 50, 900, 10));
        xSpinner.setFont(UITheme.FONT_REGULAR);
        formPanel.add(xSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        JLabel yLabel = new JLabel("Map Y Position:");
        yLabel.setFont(UITheme.FONT_BOLD);
        formPanel.add(yLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        ySpinner = new JSpinner(new SpinnerNumberModel(defaultY, 50, 700, 10));
        ySpinner.setFont(UITheme.FONT_REGULAR);
        formPanel.add(ySpinner, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        buttonPanel.setBackground(UITheme.BG_APP);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));

        JButton cancelBtn = UITheme.createOutlineButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JButton addBtn = UITheme.createPrimaryButton("Add Location");
        addBtn.addActionListener(e -> onAddLocation());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(addBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    private void onAddLocation() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid location name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }

        if (graph.hasLocation(name)) {
            JOptionPane.showMessageDialog(this, "Location '" + name + "' already exists.", "Duplicate Location", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return;
        }

        String category = (String) categoryCombo.getSelectedItem();
        int x = (Integer) xSpinner.getValue();
        int y = (Integer) ySpinner.getValue();

        String result = graph.addLocation(name, x, y, category);
        if ("SUCCESS".equals(result)) {
            success = true;
            JOptionPane.showMessageDialog(this, "Location '" + name + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
