package com.yourgame.characters.item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.yourgame.utils.AngleSlope;

class Missile {
    private final Texture texture;
    private final Vector2 position;
    private final float speed;
    public final AngleSlope flightPath;


    Missile(Texture texture, Vector2 startPosition, float speed, AngleSlope flightPath) {
        this.texture = texture;
        this.position = startPosition;
        this.speed = speed;
        this.flightPath = flightPath;
    }

    void update(float deltaTime) {
        float angleRad = (float) Math.toRadians(flightPath.getAngle());

        // Compute the velocity components
        float vx = (float) Math.cos(angleRad) * speed;
        float vy = (float) Math.sin(angleRad) * speed;

        // Invert the velocity if your missile should travel in the opposite direction.
        position.x += (-vx) * deltaTime;
        position.y += (-vy) * deltaTime;

    }

    Vector2 getPosition() {
        return position;
    }

    Texture getTexture() {
        return texture;
    }
}
