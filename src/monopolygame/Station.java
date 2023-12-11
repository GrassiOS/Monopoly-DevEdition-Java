/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;

public class Station extends Property {
    public Station(int id, String name, int set, int normalRent, int buyPrice) {
        super(id, name, set, normalRent, buyPrice);
    }

    // Override the getRent method to calculate rent based on the number of stations owned
    @Override
    public int getRent() {
        int numStationsOwned = countStationsOwnedByPlayer();
        switch (numStationsOwned) {
            case 1:
                return getNormalRent();
            case 2:
                return getNormalRent() * 2;
            case 3:
                return getNormalRent() * 4;
            case 4:
                return getNormalRent() * 8;
            default:
                return 0; // No stations owned, no rent
        }
    }

    // Helper method to count the number of stations owned by the player
    private int countStationsOwnedByPlayer() {
        if (getOwner() == null) {
            return 0;
        }

        // Assume all stations have unique set values
        int numStationsOwned = 0;
        for (Tile tile : getOwner().getProperties()) {
            if (tile instanceof Station && ((Station) tile).getSet() == getSet()) {
                numStationsOwned++;
            }
        }

        return numStationsOwned;
    }
}
