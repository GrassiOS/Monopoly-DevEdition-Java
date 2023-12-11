/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import java.util.ArrayList;


public class Player {
    private int id;
    private int cash;
    private ArrayList<Property> properties; // Assuming you have a Property class
    private int tileIn;
    private int tileWasIn;
    private String name;
    private boolean inJail;
    private int jailTurns;
   
    private JLabel playerLabel;

    public Player(int id, String name, int initialCash, int x, int y) {
    this.id = id;
    this.name = name;
    this.cash = initialCash;
    this.tileIn = 0;
    this.inJail = false;
    this.jailTurns = 0;

    playerLabel = new JLabel();
        playerLabel.setForeground(Color.RED);
        playerLabel.setBounds(x, y, 40, 40); // Adjust size as needed
        String imagePath = "resources/p" + id + ".png";
        System.out.println("Loading image from path: " + imagePath);
        try {
            File imageFile = new File(imagePath);
            Image image = ImageIO.read(imageFile);
            playerLabel.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public JLabel getPlayerLabel() {
        return playerLabel;
    }

    
    public void display() {
        playerLabel.getParent().setComponentZOrder(playerLabel, 0);

            // Ensure the panel is repainted after updating the location and z-order
            playerLabel.getParent().revalidate();
            playerLabel.getParent().repaint();
    }
    
    public void rollAndMove(int steps, Runnable onAnimationComplete) {
    int tileSize = 72;
    int totalSteps = steps * tileSize;

    Timer timer = new Timer(5, new ActionListener() {
        int currentStep = 0;

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (currentStep < totalSteps) {
                int remainder = currentStep % tileSize;
                int tileNumber = (currentStep / tileSize + tileIn) % 40;

                if (remainder < tileSize) {
                    move(tileNumber);
                }

                switch (tileNumber / 10) {
                    case 0:
                        playerLabel.setLocation(playerLabel.getX() - 1, playerLabel.getY());
                        break;
                    case 1:
                        playerLabel.setLocation(playerLabel.getX(), playerLabel.getY() - 1);
                        break;
                    case 2:
                        playerLabel.setLocation(playerLabel.getX() + 1, playerLabel.getY());
                        break;
                    case 3:
                        playerLabel.setLocation(playerLabel.getX(), playerLabel.getY() + 1);
                        break;
                }

                currentStep += 1;
            } else {
                ((Timer) evt.getSource()).stop();
                tileWasIn = tileIn; // Update tileWasIn before onAnimationComplete
                tileIn = (tileIn + steps) % 40;

                if (onAnimationComplete != null) {
                    onAnimationComplete.run();
                }
            }
        }
    });

    timer.start();
}


    
    private void move(int tileNumber) {
        // Implement the logic to move the player based on tileNumber if needed
        // For now, I'll just print the tileNumber as an example
        //System.out.println("Moving to tile: " + tileNumber);
    }
    
    public int getId() {
        return id;
    }
    
    public void receiveCash(int amount) {
        this.cash = this.cash + amount;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }
    
     public void payCash(int cash) {
        if(this.cash >= cash)
        this.cash = this.cash - cash;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }


    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    public int getTileIn() {
        return tileIn;
    }

    public void setTileIn(int tileIn) {
        this.tileIn = tileIn;
    }

    public String getName() {
        return name;
    }

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }

    public int getJailTurns() {
        return jailTurns;
    }
    
    public void incrementJailTurns() {
        this.jailTurns++;
    }

    public void setJailTurns(int jailTurns) {
        this.jailTurns = jailTurns;
    }
    
    public int getTileWasIn() {
        return tileWasIn;
    }
    
    public void addProperty(Property property) {
        if (properties == null) {
            properties = new ArrayList<>();
        }
        properties.add(property);
    }
    
    public int tilesLeftToReach(int targetTileId) {
        int currentPosition = getTileIn();

        // Calculate the number of tiles between the current position and the target position
        int tilesBetween = (targetTileId - currentPosition + 40) % 40;

        // Calculate the number of tiles left to reach the target
        return (tilesBetween == 0) ? 0 : (40 - tilesBetween);
    }
    
}
