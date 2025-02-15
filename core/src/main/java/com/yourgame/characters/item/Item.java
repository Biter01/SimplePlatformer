package com.yourgame.characters.item;

import com.badlogic.gdx.graphics.Texture;
import com.yourgame.characters.Move;

public abstract class Item {
    public float x;
    public float y;
    private Texture texture;

    public Item(float x, float y, Texture texture) {
        this.x = x;
        this.y= y;
        this.texture = texture;
    }

    abstract void use();


}
