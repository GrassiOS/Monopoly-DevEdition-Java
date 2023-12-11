/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;

public class Street extends Property {
    private int numberOfHouses;
    private int rent1House;
    private int rent2Houses;
    private int rent3Houses;
    private int rent4Houses;

    public Street(int id, String name, int set, int normalRent, int buyPrice,
                  int rent1House, int rent2Houses, int rent3Houses, int rent4Houses) {
        super(id, name, set, normalRent, buyPrice);
        this.numberOfHouses = 0;  // Initially, no houses
        this.rent1House = rent1House;
        this.rent2Houses = rent2Houses;
        this.rent3Houses = rent3Houses;
        this.rent4Houses = rent4Houses;
    }

    public int getNumberOfHouses() {
        return numberOfHouses;
    }

    public int getRent() {
        switch (numberOfHouses) {
            case 1:
                return rent1House;
            case 2:
                return rent2Houses;
            case 3:
                return rent3Houses;
            case 4:
                return rent4Houses;
            default:
                return getNormalRent();
        }
    }
}

