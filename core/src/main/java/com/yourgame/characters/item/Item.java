package com.yourgame.characters.item;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yourgame.utils.AngleSlope;

public interface  Item {

    void use(float x, float y, AngleSlope clickObject);

    void update(float deltaTime);

    Texture getTexture();

    void draw(SpriteBatch batch, float x, float y, AngleSlope clickObj);

}
