/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentDialog extends JDialog {

    private int option;

    public PaymentDialog(Frame owner, String playerName, String propertyName, int rentAmount) {
        super(owner, "Payment", true);
        setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel(playerName + ", you need to pay rent for " + propertyName + ": $" + rentAmount);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton payButton = new JButton("Pay");

        payButton.addActionListener(e -> {
            option = JOptionPane.YES_OPTION;
            dispose();
        });

        buttonPanel.add(payButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    public int showDialog() {
        setVisible(true);
        return option;
    }
}
