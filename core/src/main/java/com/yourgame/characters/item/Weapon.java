package com.yourgame.characters.item;

import com.badlogic.gdx.graphics.Texture;

public class Weapon extends Item {

    Texture missle;

    public Weapon(int x, int y, Texture texture, Texture missle) {
        super(x, y, texture);
        this.missle = missle;

        this.missle = missle;
    }


    @Override
    void use() {

    }
}
