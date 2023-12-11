/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;

public class Tax extends Tile {
    private int taxAmount;

    public Tax(int id, String name, int taxAmount) {
        super(id, name);
        this.taxAmount = taxAmount;
    }

    public int getTaxAmount() {
        return taxAmount;
    }
}
