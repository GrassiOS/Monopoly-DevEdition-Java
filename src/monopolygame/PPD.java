/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PPD extends JDialog {

    private int option;

    public PPD(Frame owner, String propertyName, int propertyPrice) {
        super(owner, "Property Purchase", true);
        setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel("Do you want to purchase " + propertyName + " for $" + propertyPrice + "?");
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        yesButton.addActionListener(e -> {
            option = JOptionPane.YES_OPTION;
            dispose();
        });

        noButton.addActionListener(e -> {
            option = JOptionPane.NO_OPTION;
            dispose();
        });

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    public int showDialog() {
        setVisible(true);
        return option;
    }
}

