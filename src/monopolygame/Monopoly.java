/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;


import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Monopoly extends JFrame {
    private JPanel mainPanel;
    private JButton endTurnButton;
    private JLabel[] userInfoLabels; 

    private Random random = new Random();
    private int diceResult = 0;
    private boolean doubleRolled;
    private boolean diceRolled;
    private boolean additionalRollPending;

    private boolean updatePlayerInfo;
    
    private ArrayList<Tile> tiles;
    private ArrayList<Player> players;
    private int currentPlayerIndex;

    private Dice dice;

    public Monopoly() {
        setTitle("Monopoly");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dice = new Dice();
        mainPanel = new JPanel();

        mainPanel.setBackground(new Color(38,38,38));
        
        mainPanel.setLayout(null);
        setContentPane(mainPanel);

        // Display the board image
        displayImage("resources/boardNEW.png", 0, 0, 800, 800);

        // Create the Dice instance
        dice.addToContainer(mainPanel);  // Add dice components after the board

        // Add buttons with corresponding actions
        addButton("resources/ROLL_NEW.png", 810, 508, this::rollDice);
        addButton("resources/PROPERTIES_NEW.png", 810, 568, this::openPropertyCarouselDialog);
        addButton("resources/TRADE_NEW.png", 810, 628, this::initiateTrade);
        addButton("resources/BANKRUPT_NEW.png", 810, 687, this::changeToRed);
        
        // Create the "End Turn" button
        endTurnButton = addButton("resources/ENDTURN_NEW.png", 810, 742, this::endTurn);
        endTurnButton.setEnabled(false);

        players = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Player player = new Player(i, "Player" + i, 1500, 742, 742);
            mainPanel.add(player.getPlayerLabel());
            players.add(player);
            player.display();
            
        }
        
        initializeTiles();
        
        userInfoLabels = new JLabel[4];  // Add this line
    for (int i = 0; i < 4; i++) {
        userInfoLabels[i] = new JLabel();  // Add this line
        userInfoLabels[i].setForeground(Color.WHITE);  // Set text color to white
        userInfoLabels[i].setBounds(810, 10 + i * 50, 180, 40);  // Set bounds as needed
        mainPanel.add(userInfoLabels[i]);  // Add the label to the main panel
    }
        
        currentPlayerIndex = 0;
        doubleRolled = false;
        additionalRollPending = false;
        
        updateAllUserInfoLabels();
        startTurn();
    }

    private void displayImage(String imagePath, int x, int y, int width, int height) {
        try {
            File imageFile = new File(imagePath);
            System.out.println("Absolute Path: " + imageFile.getAbsolutePath()); // Print absolute path
            Image image = ImageIO.read(imageFile);
            ImageIcon icon = new ImageIcon(image);
            JLabel label = new JLabel(icon);
            label.setBounds(x, y, width, height);
            mainPanel.add(label);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JButton addButton(String imagePath, int x, int y, ActionListener actionListener) {
        try {
            Image image = ImageIO.read(new File(imagePath));
            ImageIcon icon = new ImageIcon(image);
            JButton button = new JButton(icon);
            button.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
            button.addActionListener(actionListener);
            mainPanel.add(button);
            return button;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }    
    

private void startTurn() {
    //updateAllUserInfoLabels();
    // Enable the "Roll Dice" button to allow the player to roll the dice
    
    handleJail(players.get(currentPlayerIndex));

    
    enableButton("resources/ROLL.png", this::rollDice);

    // Disable other buttons until the dice are rolled
    disableOtherButtons();

    // Enable the "End Turn" button to allow the player to end their turn
    endTurnButton.setEnabled(true);

    // Reset the diceRolled flag at the start of each turn
    diceRolled = false;
    doubleRolled = false; // Reset the doubleRolled flag
}

private void rollDice(ActionEvent e) {
    // Disable the "Roll Dice" button to prevent multiple clicks
    disableButton("resources/ROLL.png");

    if (!diceRolled) {
        // Roll both dice for the current player only if dice are not already rolled
        dice.rollDiceAnimation(mainPanel, total -> {
            // Update the GUI with the final total
            diceResult = total;
            System.out.println("Move: " + diceResult);

            // Perform the player movement only after the dice animation completes
            SwingUtilities.invokeLater(() -> {
                Player currentPlayer = players.get(currentPlayerIndex);
                currentPlayer.rollAndMove(diceResult, this::printPlayerInfo);

                // Check for a double and update the doubleRolled flag
                doubleRolled = (dice.isDoubleRolled());

                // Enable the "End Turn" button for the next turn
                enableButton("resources/ENDTURN.png", this::endTurn);

                // Enable other buttons only if a double is not rolled
                if (!doubleRolled) {
                    enableOtherButtons();
                    // Enable the "Roll Dice" button for the next turn
                    enableButton("resources/ROLL.png", this::rollDice);
                } else {
                    // If it's a double, allow the player to roll again
                    // Disable other buttons until the next roll is completed
                    disableOtherButtons();

                    // Set a flag to indicate that the player has an additional roll
                    additionalRollPending = true;
                }
            });
        });

        // Set the diceRolled flag to true after rolling the dice
        diceRolled = true;
    }
}

private void endTurn(ActionEvent e) {
    // Disable the "End Turn" button to prevent multiple clicks
    endTurnButton.setEnabled(false);

    // Disable all buttons to prevent actions until the next player's turn
    disableAllButtons();

    if (!additionalRollPending) {
            // Only move to the next player's turn if no additional roll is pending
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

            // Move to the next player's turn after a short delay
            Timer delayTimer = new Timer(1000, evt -> SwingUtilities.invokeLater(this::startTurn));
            delayTimer.setRepeats(false);
            delayTimer.start();
        } else {
            // Reset the flags for the additional roll
            additionalRollPending = false;
            diceRolled = false;

            // Start the additional turn immediately
            startTurn();
        }
}





private void disableOtherButtons() {
    // Disable other buttons during dice rolling
    disableButton("resources/PROPERTIES_NEW.png");
    disableButton("resources/TRADE_NEW.png");
    disableButton("resources/BANKRUPT_NEW.png");
}

private void enableOtherButtons() {
    // Enable other buttons after dice rolling finishes
    enableButton("resources/PROPERTIES_NEW.png", this::openPropertyCarouselDialog);
    enableButton("resources/TRADE_NEW.png", this::changeToBlue);
    enableButton("resources/BANKRUPT_NEW.png", this::changeToRed);
}




private void disableAllButtons() {
    // Disable all buttons to prevent actions until the next player's turn
    disableButton("resources/ROLL_NEW.png");
    disableButton("resources/PROPERTIES_NEW.png");
    disableButton("resources/TRADE_NEW.png");
    disableButton("resources/BANKRUPT_NEW.png");
}


    private void enableButton(String imagePath, ActionListener actionListener) {
    // Assuming you have a button with the specified image path
    JButton button = findButtonByImagePath(imagePath);

    if (button != null) {
        button.setEnabled(true);
        button.addActionListener(actionListener);
    }
}

private void disableButton(String imagePath) {
    // Assuming you have a button with the specified image path
    JButton button = findButtonByImagePath(imagePath);

    if (button != null) {
        button.setEnabled(false);
        // Remove any existing action listeners
        for (ActionListener listener : button.getActionListeners()) {
            button.removeActionListener(listener);
        }
    }
}

// Replace this method with your actual button creation logic
private JButton findButtonByImagePath(String imagePath) {
    // This is a placeholder implementation; replace it with your actual code
    // Loop through your buttons and compare image paths to find the correct button
    // Make sure to adjust this code based on your actual button creation logic

    Component[] components = mainPanel.getComponents();
    for (Component component : components) {
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            // Replace "getIconPathFromButton" with your actual method to get the button's image path
            String buttonImagePath = getIconPathFromButton(button);

            if (buttonImagePath != null && buttonImagePath.equals(imagePath)) {
                return button;
            }
        }
    }

    return null;
}

// Replace this method with your actual logic to get the image path from a button
private String getIconPathFromButton(JButton button) {
    // This is a placeholder; replace it with your actual logic
    // You may need to store image paths as client properties when creating buttons
    // Adjust this method based on your actual button creation code

    if (button.getIcon() instanceof ImageIcon) {
        return ((ImageIcon) button.getIcon()).getDescription();
    }

    return null;
}
    
    private void changeToPurple(ActionEvent e) {
        mainPanel.setBackground(Color.MAGENTA);
    }

    private void changeToOrange(ActionEvent e) {
        mainPanel.setBackground(Color.ORANGE);
    }

    private void changeToGreen(ActionEvent e) {
        mainPanel.setBackground(Color.GREEN);
    }

    private void changeToBlue(ActionEvent e) {
        mainPanel.setBackground(Color.BLUE);
    }

    private void changeToRed(ActionEvent e) {
        mainPanel.setBackground(Color.RED);
    }
    
    public JPanel getMainPanel() {
    return mainPanel;
    }
    
private void printPlayerInfo() {
    System.out.println("Player Info:");
    Player currentPlayer = players.get(currentPlayerIndex);
    int tileIndex = currentPlayer.getTileIn();
    int previousTileIndex = currentPlayer.getTileWasIn();

    if (tileIndex >= 0 && tileIndex < tiles.size() && previousTileIndex >= 0 && previousTileIndex < tiles.size()) {
        Tile currentTile = tiles.get(tileIndex);
        Tile previousTile = tiles.get(previousTileIndex);

        System.out.println("Current Tile: " + currentTile.getName() + " (ID: " + currentTile.getId() + ")");
        System.out.println("Previous Tile: " + previousTile.getName() + " (ID: " + previousTile.getId() + ")");

        // Check if the player passed the "Go" tile
        if (previousTileIndex > tileIndex && previousTileIndex != 0 && currentPlayer.isInJail() != true) {
            System.out.println("Passed Go - Collecting $200");
            currentPlayer.setCash(currentPlayer.getCash() + 200);
            updateAllUserInfoLabels();
        }

        // Check the type of the current tile using switch
        switch (currentTile.getClass().getSimpleName()) {
            case "Street":
                System.out.println("Type: Street");
                System.out.println("Type: Property!!!!");
                handleUnownedProperty((Property) currentTile);
                break;
            case "Station":
                System.out.println("Type: Station");
                System.out.println("Type: Property!!!!");
                handleUnownedProperty((Property) currentTile);
                break;
            case "CommunityChest":
                System.out.println("Type: Community Chest");
                break;
            case "Tax":
                System.out.println("Type: Tax");
                handleTax((Tax) currentTile);
                break;
            case "Jail":
                System.out.println("Type: Jail");
                break;
            case "FreeParking":
                System.out.println("Type: Free Parking");
                break;
            case "GoToJail":
                System.out.println("Type: Go To Jail");
                handleGoToJail((GoToJail) currentTile);
                break;
            case "Go":
                System.out.println("Type: Go!");
                break;
            case "Property":
                System.out.println("Type: Property!!!!");
                handleUnownedProperty((Property) currentTile);
                break;
            default:
                System.out.println("Type: Regular Tile");
                break;
        }
        // Add more cases for other tile types as needed
    } else {
        System.out.println("Invalid tile index for the player.");
    }
}
    
    
    
    
    private void initializeTiles() {
        tiles = new ArrayList<>();

        tiles.add(new Go(0, "Go"));

        tiles.add(new Street(1, "Python", 1, 2, 60, 30, 90, 170, 250));
        tiles.add(new CommunityChest(2, "Community Chest 1"));
        tiles.add(new Street(3, "Ruby", 1, 4, 60, 60, 150, 300, 500));
        tiles.add(new Tax(4, "Coding Bug", 200));
        tiles.add(new Station(5, "Google", 9, 25, 200));
        tiles.add(new Street(6, "React Native", 2, 6, 100, 90, 200, 550, 800));
        tiles.add(new CommunityChest(7, "Community Chest 2"));
        tiles.add(new Street(8, "Flutter", 2, 6, 100, 90, 200, 550, 800));
        tiles.add(new Street(9, "Xamarin", 2, 8, 120, 100, 250, 600, 850));
        
        tiles.add(new Jail(10,"Jail"));
        
        tiles.add(new Street(11, "HTML", 3, 10, 140, 130, 280, 650, 900));
        tiles.add(new Property(12, "VSCode", 10, 30, 150));
        tiles.add(new Street(13, "CSS", 3, 10, 140, 150, 330, 680, 900));
        tiles.add(new Street(14, "Javascript", 3, 12, 160, 180, 350, 700, 950));
        tiles.add(new Station(15, "Meta", 9, 25, 200));
        tiles.add(new Street(16, "Java", 4, 16, 180, 200, 380, 750, 1000));
        tiles.add(new CommunityChest(17, "Community Chest 3"));
        tiles.add(new Street(18, "C#", 4, 16, 180, 200, 380, 750, 1000));
        tiles.add(new Street(19, "Swift", 4, 18, 200, 220, 400, 800, 1150));
        
        tiles.add(new FreeParking(20,"Free Parking"));
        
        tiles.add(new Street(21, "C", 5, 20, 220, 250, 440, 900, 1250));
        tiles.add(new CommunityChest(22, "Community Chest 4"));
        tiles.add(new Street(23, "C++", 5, 20, 220, 260, 450, 920, 1250));
        tiles.add(new Street(24, "Rust", 5, 22, 240, 280, 480, 950, 1400));
        tiles.add(new Station(25, "OpenAI", 9, 25, 200));
        tiles.add(new Street(26, "Windows", 6, 24, 260, 300, 500, 1000, 1500));
        tiles.add(new Street(27, "iOS", 6, 24, 260, 350, 600, 1150, 1650));
        tiles.add(new Property(28, "Xcode", 10, 30, 150));
        tiles.add(new Street(29, "Linux", 6, 26, 280, 380, 650, 1250, 1780));
        
        tiles.add(new GoToJail(30,"Go To Jail"));
        
        tiles.add(new Street(31, "SQL", 7, 30, 300, 400, 680, 1300, 1800));
        tiles.add(new Street(32, "R", 7, 32, 300, 400, 680, 1300, 1800));
        tiles.add(new CommunityChest(33, "Community Chest 5"));
        tiles.add(new Street(34, "Julia", 7, 35, 320, 420, 700, 1400, 1900));
        tiles.add(new Station(35, "Apple", 9, 25, 200));
        tiles.add(new Tile(36,"Gamble/FreeSquare"));
        tiles.add(new Street(37, "Unity", 8, 40, 350, 450, 750, 1450, 2000));
        tiles.add(new Tax(38, "Syntax Error", 100));
        tiles.add(new Street(39, "Unreal", 8, 50, 400, 500, 800, 1500, 2200));

    }

    private void handleUnownedProperty(Property property) {
    // Check if the property is unowned
    if (property.getOwner() == null) {
        String propertyName = property.getName();
        int propertyPrice = property.getBuyPrice();

        // Show the custom property purchase dialog on the EDT
        SwingUtilities.invokeLater(() -> {
            PPD dialog = new PPD(this, propertyName, propertyPrice);
            int option = dialog.showDialog();

            if (option == JOptionPane.YES_OPTION) {
                // Deduct the property price from the player's cash
                Player currentPlayer = players.get(currentPlayerIndex);
                currentPlayer.payCash(propertyPrice);
                updateAllUserInfoLabels();

                // Set the player as the owner of the property
                property.setOwner(currentPlayer);

                // Add the purchased property to the player's list
                currentPlayer.addProperty(property);

                // Print information about the purchase
                System.out.println(currentPlayer.getName() + " purchased " + propertyName + " for $" + propertyPrice);
            }
        });
    } else {
        // Property is owned by someone, show payment dialog
        String propertyName = property.getName();
        int rentAmount = property.getRent();
        Player owner = property.getOwner();

        // Check if the property owner is in jail
        if (owner.isInJail()) {
            // Inform the player that the owner is in jail and they don't have to pay
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "The owner, " + owner.getName() + ", is in jail. No rent is due.",
                    "Owner in Jail",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            // Show the custom payment dialog on the EDT
            SwingUtilities.invokeLater(() -> {
                Player currentPlayer = players.get(currentPlayerIndex);
                PaymentDialog dialog = new PaymentDialog(this, currentPlayer.getName(), propertyName, rentAmount);
                int option = dialog.showDialog();

                if (option == JOptionPane.YES_OPTION) {
                    currentPlayer.payCash(rentAmount);

                    // Add the rent amount to the property owner's cash
                    owner.receiveCash(rentAmount);
                    updateAllUserInfoLabels();

                    // Print information about the payment
                    System.out.println(currentPlayer.getName() + " paid $" + rentAmount + " rent to " + owner.getName() + " for " + propertyName);
                }
            });
        }
    }
}
    
    private void handleFreeParking(FreeParking freeP) {
        if(freeP.getCashIn() > 0) {
            SwingUtilities.invokeLater(() -> {
             Player currentPlayer = players.get(currentPlayerIndex);
             
             String message = currentPlayer.getId() + " receives $" + freeP.getCashIn();
            JOptionPane.showMessageDialog(null, message, "Free Parking Reward", JOptionPane.INFORMATION_MESSAGE);
             
             currentPlayer.receiveCash(freeP.getCashIn());
             freeP.cashWithdraw();
             
             
        });
        }
    }
    
    private void handleTax(Tax tax) {
    int taxAmount = tax.getTaxAmount();

    SwingUtilities.invokeLater(() -> {
        Player currentPlayer = players.get(currentPlayerIndex);

        // Add the tax amount to the Free Parking tile
         FreeParking freeParkingTile = (FreeParking) tiles.get(20);  // Assuming index 20 is the Free Parking tile
        freeParkingTile.addCashToParking(taxAmount);

        TaxPaymentDialog dialog = new TaxPaymentDialog(this, currentPlayer.getName(), tax.getName(), taxAmount);
        dialog.showDialog();

        if (dialog.isPaymentConfirmed()) {
            currentPlayer.payCash(taxAmount);
            System.out.println(currentPlayer.getName() + " paid $" + taxAmount + " in taxes");
            updateAllUserInfoLabels();
            // Optionally, print information about adding tax to Free Parking
            System.out.println("Tax amount added to Free Parking: $" + taxAmount);
        }
    });
}
    
    private void handleGoToJail(GoToJail goToJail) {
    SwingUtilities.invokeLater(() -> {
        Player currentPlayer = players.get(currentPlayerIndex);
        
        currentPlayer.setInJail(true);
        currentPlayer.rollAndMove(currentPlayer.tilesLeftToReach(10), this::printPlayerInfo);

        // Display a dialog box informing the player about going to jail
        JOptionPane.showMessageDialog(
                mainPanel,
                "You landed on Go To Jail! You are now in Jail.",
                "Go To Jail",
                JOptionPane.INFORMATION_MESSAGE
        );
        
        /*
        // Check if the player wants to pay $50 to exit jail
        int option = JOptionPane.showConfirmDialog(
                mainPanel,
                "Do you want to pay $50 to exit jail?",
                "Pay to Exit Jail",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            // Player chooses to pay $50 to exit jail
            if (currentPlayer.getCash()>= 50) {
                currentPlayer.payCash(50);
                currentPlayer.setInJail(false);
                updateAllUserInfoLabels();
                System.out.println(currentPlayer.getName() + " paid $50 to exit jail.");
            } else {
                // Player doesn't have enough cash to pay
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "You don't have enough cash to pay $50. You remain in Jail.",
                        "Insufficient Funds",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } else {
            // Player chooses not to pay and remains in jail
            System.out.println(currentPlayer.getName() + " chose not to pay and remains in Jail.");
        }
*/

    });
}
    
    private void handleJail(Player currentPlayer) {
    if (currentPlayer.isInJail()) {
        int jailTurns = currentPlayer.getJailTurns();
        
        // Display a dialog box informing the player about being in jail
        JOptionPane.showMessageDialog(
                mainPanel,
                "You are in Jail. Turns left: " + (3 - jailTurns),
                "Jail Information",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Check if the player has completed three turns in jail
        if (jailTurns >= 3) {
            // Player has completed three turns, release them from jail
            currentPlayer.setInJail(false);
            currentPlayer.setJailTurns(0);
            updateAllUserInfoLabels();
            System.out.println(currentPlayer.getName() + " served three turns in jail and is now free.");
        } else {
            // Player has not completed three turns, ask if they want to pay $50 to get out
            int option = JOptionPane.showConfirmDialog(
                    mainPanel,
                    "Do you want to pay $50 to get out of jail?",
                    "Pay to Exit Jail",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                // Player chooses to pay $50 to exit jail
                if (currentPlayer.getCash() >= 50) {
                    currentPlayer.payCash(50);
                    currentPlayer.setInJail(false);
                    currentPlayer.setJailTurns(0);
                    updateAllUserInfoLabels();
                    System.out.println(currentPlayer.getName() + " paid $50 to exit jail.");
                } else {
                    // Player doesn't have enough cash to pay
                    JOptionPane.showMessageDialog(
                            mainPanel,
                            "You don't have enough cash to pay $50. You remain in Jail.",
                            "Insufficient Funds",
                            JOptionPane.WARNING_MESSAGE
                    );
                    currentPlayer.incrementJailTurns();
                    endTurn(null); // Skip the turn
                }
            } else {
                // Player chooses not to pay and remains in jail, increment jail turns
                currentPlayer.incrementJailTurns();
                endTurn(null); // Skip the turn
            }
        }
    }
}




    private int showPropertyPurchaseDialog(String propertyName, int propertyPrice) {
        String message = "Do you want to purchase " + propertyName + " for $" + propertyPrice + "?";
        String title = "Property Purchase";
        int optionType = JOptionPane.YES_NO_OPTION;
        int messageType = JOptionPane.QUESTION_MESSAGE;

        return JOptionPane.showOptionDialog(mainPanel, message, title, optionType, messageType, null, null, null);
    }
    
    private void openPropertyCarouselDialog(ActionEvent e) {
    SwingUtilities.invokeLater(() -> {
        Player currentPlayer = players.get(currentPlayerIndex);
        ArrayList<Property> playerProperties = currentPlayer.getProperties();

        PropertyCarouselDialog dialog = new PropertyCarouselDialog(this, "Property Carousel", playerProperties);
        dialog.setVisible(true);
    });
}
    
    private void updateAllUserInfoLabels() {
    for (int i = 0; i < 4; i++) {
        Player currentPlayer = players.get(i);
        String userInfoText = "Player " + currentPlayer.getId() + ": " + " | $" + currentPlayer.getCash();
        userInfoLabels[i].setText(userInfoText);
    }
}
    
    private Player selectPlayerForTrade() {
    String[] playerNames = new String[players.size()];
    for (int i = 0; i < players.size(); i++) {
        playerNames[i] = players.get(i).getName();
    }

    JComboBox<String> playerComboBox = new JComboBox<>(playerNames);
    JOptionPane.showMessageDialog(null, playerComboBox, "Select Player to Trade With", JOptionPane.PLAIN_MESSAGE);

    int selectedPlayerIndex = playerComboBox.getSelectedIndex();
    if (selectedPlayerIndex != -1) {
        return players.get(selectedPlayerIndex);
    } else {
        return null;  // User canceled the selection
    }
}

private void initiateTrade(ActionEvent e) {
    Player targetPlayer = selectPlayerForTrade();
    if (targetPlayer != null) {
        // Dialog 1: Select properties and money to offer
        TradeOffer tradeOffer = createTradeOfferDialog();
        
        if (tradeOffer != null) {
            // Dialog 2: Specify trade details
            boolean tradeConfirmed = createTradeDetailsDialog(tradeOffer);

            if (tradeConfirmed) {
                // Dialog 3: Confirm trade with the target player
                confirmTradeWithTargetPlayer(targetPlayer, tradeOffer);
            }
        }
    }
}

private void confirmTradeWithTargetPlayer(Player targetPlayer, TradeOffer tradeOffer) {
    JPanel confirmationPanel = new JPanel();
    confirmationPanel.setLayout(new GridLayout(0, 1));

    JLabel offerLabel = new JLabel("Trade Offer Received:\nInitiator: " + players.get(currentPlayerIndex).getName() +
            "\nOffer: $" + tradeOffer.getCashOffered() +
            " and properties: " + tradeOffer.getPropertiesOffered() +
            " - Request: " + tradeOffer.getRequestType());

    confirmationPanel.add(offerLabel);

    int result = JOptionPane.showConfirmDialog(null, confirmationPanel, "Trade Offer Received", JOptionPane.YES_NO_OPTION);

    if (result == JOptionPane.YES_OPTION) {
        System.out.println("Trade Accepted!");
        // Implement logic for handling accepted trade, e.g., updating player details
    } else {
        System.out.println("Trade Rejected.");
        // Implement logic for handling rejected trade
    }
}


private TradeOffer createTradeOfferDialog() {
    JPanel tradeOfferPanel = new JPanel();
    JTextField cashField = new JTextField(10);
    JCheckBox addCashCheckBox = new JCheckBox("Add Cash");

    ArrayList<JCheckBox> propertyCheckboxes = new ArrayList<>();
    for (Property property : players.get(currentPlayerIndex).getProperties()) {
        JCheckBox checkBox = new JCheckBox(property.getName());
        propertyCheckboxes.add(checkBox);
    }

    tradeOfferPanel.setLayout(new GridLayout(0, 1));
    tradeOfferPanel.add(new JLabel("Cash:"));
    tradeOfferPanel.add(cashField);
    tradeOfferPanel.add(addCashCheckBox);
    tradeOfferPanel.add(new JLabel("Properties to Offer:"));

    for (JCheckBox checkBox : propertyCheckboxes) {
        tradeOfferPanel.add(checkBox);
    }

    int result = JOptionPane.showConfirmDialog(null, tradeOfferPanel, "Select Properties and Money to Offer", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        int cashOffered = 0;
        if (addCashCheckBox.isSelected()) {
            try {
                cashOffered = Integer.parseInt(cashField.getText());
            } catch (NumberFormatException ex) {
                // Handle invalid input
                JOptionPane.showMessageDialog(null, "Invalid cash amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        ArrayList<Property> propertiesOffered = new ArrayList<>();
        for (int i = 0; i < propertyCheckboxes.size(); i++) {
            if (propertyCheckboxes.get(i).isSelected()) {
                propertiesOffered.add(players.get(currentPlayerIndex).getProperties().get(i));
            }
        }

        // New: Specify what the initiator wants from the other player
        JComboBox<String> requestComboBox = new JComboBox<>(new String[]{"Money", "Properties"});
        int requestType = JOptionPane.showOptionDialog(null, requestComboBox, "Select What You Want", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

        return new TradeOffer(cashOffered, propertiesOffered, (requestType == JOptionPane.OK_OPTION) ? requestComboBox.getSelectedItem().toString() : null);
    }
    return null;
}

private boolean createTradeDetailsDialog(TradeOffer tradeOffer) {
    JPanel tradeDetailsPanel = new JPanel();
    tradeDetailsPanel.setLayout(new GridLayout(0, 1));

    JLabel offerLabel = new JLabel("Offer: $" + tradeOffer.getCashOffered() + " and properties: " + tradeOffer.getPropertiesOffered() + " - Request: " + tradeOffer.getRequestType());
    tradeDetailsPanel.add(offerLabel);

    int result = JOptionPane.showConfirmDialog(null, tradeDetailsPanel, "Trade Confirmation", JOptionPane.YES_NO_OPTION);
    return result == JOptionPane.YES_OPTION;
}

// Modify the TradeOffer class
private static class TradeOffer {
    private final int cashOffered;
    private final ArrayList<Property> propertiesOffered;
    private final String requestType;

    public TradeOffer(int cashOffered, ArrayList<Property> propertiesOffered, String requestType) {
        this.cashOffered = cashOffered;
        this.propertiesOffered = propertiesOffered;
        this.requestType = requestType;
    }

    public int getCashOffered() {
        return cashOffered;
    }

    public ArrayList<Property> getPropertiesOffered() {
        return propertiesOffered;
    }

    public String getRequestType() {
        return requestType;
    }
}


private void showTradeConfirmationDialog(Player initiator, int cashOffered, ArrayList<Property> propertiesOffered) {
    JPanel confirmationPanel = new JPanel();
    JTextArea tradeDetails = new JTextArea();
    tradeDetails.setEditable(false);
    tradeDetails.append("Trade Offer Received:\n");
    tradeDetails.append("Initiator: " + initiator.getName() + "\n");
    tradeDetails.append("Cash Offered: $" + cashOffered + "\n");
    tradeDetails.append("Properties Offered: " + propertiesOffered + "\n");

    confirmationPanel.add(new JScrollPane(tradeDetails));

    int result = JOptionPane.showConfirmDialog(null, confirmationPanel, "Trade Confirmation", JOptionPane.YES_NO_OPTION);
    if (result == JOptionPane.YES_OPTION) {
        // Implement logic for accepting the trade.
        // You can update player information and handle the exchange of cash and properties here.
        System.out.println("Trade Accepted!");
    } else {
        // Implement logic for rejecting the trade.
        System.out.println("Trade Rejected!");
    }
}

public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Monopoly game = new Monopoly();
            game.setVisible(true);
        });
    }
}