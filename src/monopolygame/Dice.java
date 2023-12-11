/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;

import java.awt.Container;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;
import javax.swing.SwingUtilities;

public class Dice {

    private List<Image> diceImages;
    private JLabel die1Label;
    private JLabel die2Label;
    private Random random;
    
    private boolean doubleRolled;

    public Dice() {
        // Load dice images
        loadDiceImages();

        // Initialize JLabels for displaying dice images
        die1Label = new JLabel();
        die2Label = new JLabel();

        // Initialize Random for rolling dice
        random = new Random();
    }

    private void loadDiceImages() {
        diceImages = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            String imagePath = "resources/dice" + i + ".png";
            System.out.println("Loading image from path: " + imagePath);
            try {
                File imageFile = new File(imagePath);
                Image image = ImageIO.read(imageFile);
                diceImages.add(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int rollDice() {
        return random.nextInt(6) + 1;
    }
    
    public interface AnimationCallback {
    void onAnimationComplete(int finalTotal);
}

public int rollDiceAnimation(Container container, AnimationCallback callback) {
        Timer timer = new Timer(100, null);

        int[] animationStep = {0};
        doubleRolled = false;

        ActionListener animationListener = new ActionListener() {
            int[] finalTotal = {0};

            @Override
            public void actionPerformed(ActionEvent e) {
                if (animationStep[0] < 20) {
                    int die1 = (int) (Math.random() * 6) + 1;
                    int die2 = (int) (Math.random() * 6) + 1;

                    SwingUtilities.invokeLater(() -> updateDiceImages(die1, die2));

                    finalTotal[0] = die1 + die2;

                    doubleRolled = (die1 == die2);

                    animationStep[0]++;
                } else {
                    timer.stop();
                    System.out.println("Final Total: " + finalTotal[0]);
                    callback.onAnimationComplete(finalTotal[0]);
                    System.out.println("Double Rolled: " + doubleRolled);

                    // Notify whether a double was rolled
                    if (doubleRolled) {
                        System.out.println("Roll again because of a double!");
                    }
                }
            }
        };

        timer.addActionListener(animationListener);
        timer.start();

        return 0;
    }

public boolean isDoubleRolled() {
    return doubleRolled;
}







    // Inside the Dice class

private void updateDiceImages(int die1, int die2) {
    Image die1Image = getDiceImage(die1);
    Image die2Image = getDiceImage(die2);

    die1Label.setIcon(new ImageIcon(die1Image));
    die2Label.setIcon(new ImageIcon(die2Image));

    // Set the bounds for the dice labels with fixed coordinates
    die1Label.setBounds(276, 348, die1Image.getWidth(null), die1Image.getHeight(null));
    die2Label.setBounds(421, 348, die2Image.getWidth(null), die2Image.getHeight(null));

    // Set the z-order to ensure the dice labels are displayed on top
    die1Label.getParent().setComponentZOrder(die1Label, 0);
    die1Label.getParent().setComponentZOrder(die2Label, 0);

    // Ensure the panel is repainted after updating the images and bounds
    die1Label.getParent().revalidate();
    die1Label.getParent().repaint();
}





    public Image getDiceImage(int value) {
        int index = Math.max(1, Math.min(value, 6)) - 1;
        return diceImages.get(index);
    }

    public void addToContainer(Container container) {
        container.add(die1Label);
        container.add(die2Label);
    }
}

