package com.yourgame.characters.item;

import com.badlogic.gdx.graphics.Texture;
import com.yourgame.characters.Move;

public abstract class Item {
    public float x;
    public float y;
    private final Texture texture;

    public Item(Texture texture) {
        this.texture = texture;
    }

    public abstract void use(float x, float y);

    public Texture getTexture() {
        return texture;
    }

    public abstract void update(float deltaTime);
}
