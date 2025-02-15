package com.yourgame.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yourgame.characters.item.Item;
import java.util.ArrayList;
import java.util.List;

public class Player extends Character{
    private Texture texture;
    private final List<Item> inventory;
    private int selected = 0;

    public Player(float x, float y, int health, String name, String texturePath) {
        super(x, y, health, name);
        texture = new Texture(texturePath);
        this.inventory = new ArrayList<>();
    }

    @Override
    public void move(Move direction, float value) {
        switch (direction) {
            case UP:
                y += value;
                break;
            case DOWN:
                y -= value;
                break;
            case RIGHT:
                x += value;
                break;
            case LEFT:
                x -= value;
                break;
        }

    }

    @Override
    public void useItem(Item item) {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    public void removeItem(int pos) {
    }

//    public void addItem(Item item) {
//        inventory.addLast(item);
//    }

    public Item selectItem(int pos) {
        selected = pos;
        return inventory.get(pos);
    }

    public Item getSelectedItem(){
        if(inventory.isEmpty()) {
            throw new InventoryException("Inventory is empty");
        }
        return inventory.get(selected);
    }


    public Texture getTexture() {
        return texture;
    }

}
