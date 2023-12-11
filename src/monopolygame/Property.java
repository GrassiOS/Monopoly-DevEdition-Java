/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;

import java.util.ArrayList;

public class Property extends Tile {
    private Player owner;
    private int set;
    private int normalRent;
    private int buyPrice;

    public Property(int id, String name, int set, int normalRent, int buyPrice) {
        super(id, name);
        this.set = set;
        this.normalRent = normalRent;
        this.buyPrice = buyPrice;
        this.owner = null; // Initially, no owner
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getSet() {
        return set;
    }

    public int getNormalRent() {
        return normalRent;
    }

    public int getBuyPrice() {
        return buyPrice;
    }
    
    public int getRent() {
        return normalRent;
    }
}

