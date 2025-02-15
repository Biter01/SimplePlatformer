package com.yourgame.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yourgame.characters.item.Item;

public abstract class Character {

    public float x;
    public float y;
    public int health;
    private String name;

    public Character(float x, float y, int health, String name) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.name = name;
    }




    public String getName() {
        return getName();
    }

    public abstract void move(Move move, float value);

    public abstract void useItem(Item item);
    public abstract void draw(SpriteBatch batch);

}
