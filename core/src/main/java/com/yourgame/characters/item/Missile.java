package com.yourgame.characters.item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Missile {
    private Texture texture;
    private Vector2 position;
    private float speed;

    public Missile(Texture texture, Vector2 startPosition, float speed) {
        this.texture = texture;
        this.position = startPosition;
        this.speed = speed;
    }

    public void update(float deltaTime) {
        position.x -= speed * deltaTime;  // Move sideways
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }
}
