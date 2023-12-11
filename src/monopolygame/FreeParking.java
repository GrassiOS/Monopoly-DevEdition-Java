/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;

public class FreeParking extends Tile {
    
    public int cashIn = 0;
    
    public FreeParking(int id, String name) {
        super(id, name);
    }
    
    
    
    public void addCashToParking(int cashIn)
    {
        this.cashIn =this.cashIn + cashIn;
    }
    
    public int getCashIn()
    {
        return this.cashIn;
    }
    
    public void cashWithdraw()
    {
        this.cashIn = 0;
    }
}

