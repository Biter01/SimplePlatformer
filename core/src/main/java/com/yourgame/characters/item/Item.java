package com.yourgame.characters.item;

import com.badlogic.gdx.graphics.Texture;
import com.yourgame.characters.Move;

public class Item {
    public float x;
    public float y;
    private Texture texture;

    public Item(float x, float y, Texture texture) {
        this.x = x;
        this.y= y;
        this.texture = texture;
    }

    public void move(Move direction, float value) {
        switch (direction) {
            case UP:
                y -= value;
                break;
            case DOWN:
                y += value;
                break;
            case RIGHT:
                x += value;
                break;
            case LEFT:
                x -= value;
                break;
        }
    }
}
