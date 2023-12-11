/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopolygame;

import javax.swing.ImageIcon;

public class Tile {
    private int id;
    private String name;
    private ImageIcon image;

    public Tile(int id, String name) {
        this.id = id;
        this.name = name;
        this.image = new ImageIcon("resources/prop" + id + ".png");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ImageIcon getImage() {
        return image;
    }
}
