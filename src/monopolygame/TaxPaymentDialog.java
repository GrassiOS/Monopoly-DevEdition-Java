/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaxPaymentDialog extends JDialog {
    private int taxAmount;
    private boolean paymentConfirmed;

    public TaxPaymentDialog(JFrame parent, String payerName, String description, int amount) {
        super(parent, "Tax Payment", true);
        this.taxAmount = amount;
        this.paymentConfirmed = false;

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JLabel messageLabel = new JLabel("It's tax time for " + payerName + "!");
        JLabel descriptionLabel = new JLabel("Tax for " + description + ": $" + amount);
        JLabel infoLabel = new JLabel("You must pay the tax.");

        JButton payButton = new JButton("Pay");

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paymentConfirmed = true;
                dispose();  // Close the dialog
            }
        });

        panel.add(messageLabel);
        panel.add(descriptionLabel);
        panel.add(infoLabel);
        panel.add(payButton);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isPaymentConfirmed() {
        return paymentConfirmed;
    }

    public void showDialog() {
        setVisible(true);
    }
}
