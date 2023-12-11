/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PropertyCarouselDialog extends JDialog {
    private ArrayList<Property> properties;
    private int currentIndex;

    private JLabel propertyLabel;
    private JButton nextButton;
    private JButton backButton;
    private JButton addHouseButton;
    private JButton removeHouseButton;
    private JButton sellButton;

    public PropertyCarouselDialog(JFrame parent, String title, ArrayList<Property> properties) {
        super(parent, title, true);
        this.properties = properties;
        this.currentIndex = 0;

        // Initialize components
        propertyLabel = new JLabel();
        nextButton = new JButton("Next");
        backButton = new JButton("Back");
        addHouseButton = new JButton("Add House");
        removeHouseButton = new JButton("Remove House");
        sellButton = new JButton("Sell");

        // Set layout
        setLayout(new BorderLayout());

        // Add components to the dialog
        add(propertyLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(addHouseButton);
        buttonPanel.add(removeHouseButton);
        buttonPanel.add(sellButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Register button listeners
        nextButton.addActionListener(e -> showNextProperty());
        backButton.addActionListener(e -> showPreviousProperty());
        addHouseButton.addActionListener(e -> addHouseToCurrentProperty());
        removeHouseButton.addActionListener(e -> removeHouseFromCurrentProperty());
        sellButton.addActionListener(e -> sellCurrentProperty());

        // Display the first property
        displayCurrentProperty();

        // Set dialog properties
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    private void displayCurrentProperty() {
        if (properties.isEmpty()) {
            propertyLabel.setIcon(null);
            propertyLabel.setText("No properties owned.");
        } else {
            Property currentProperty = properties.get(currentIndex);
            propertyLabel.setIcon(currentProperty.getImage());
            propertyLabel.setText("Property: " + currentProperty.getName());
        }
    }

    private void showNextProperty() {
        if (!properties.isEmpty()) {
            currentIndex = (currentIndex + 1) % properties.size();
            displayCurrentProperty();
        }
    }

    private void showPreviousProperty() {
        if (!properties.isEmpty()) {
            currentIndex = (currentIndex - 1 + properties.size()) % properties.size();
            displayCurrentProperty();
        }
    }

    private void addHouseToCurrentProperty() {
        // Implement logic to add a house to the current property
        // You can get the current property using properties.get(currentIndex)
        // Add your code here
    }

    private void removeHouseFromCurrentProperty() {
        // Implement logic to remove a house from the current property
        // You can get the current property using properties.get(currentIndex)
        // Add your code here
    }

    private void sellCurrentProperty() {
        // Implement logic to sell the current property
        // You can get the current property using properties.get(currentIndex)
        // Add your code here
    }
}

