package com.yourgame.characters.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Weapon extends Item {
    private final Texture missileTexture;
    private final List<Missile> activeMissiles = new ArrayList<>();

    public Weapon(Texture texture, Texture missle) {
        super(texture);
        this.missileTexture = missle;
    }


    @Override
    public void use(float x, float y) {
        // Spawn a new missile at a position (e.g., player's position)
        activeMissiles.add(new Missile(missileTexture, new Vector2(x, y), 200));
    }

    public void update(float deltaTime) {
        Iterator<Missile> iterator = activeMissiles.iterator();
        while (iterator.hasNext()) {
            Missile missile = iterator.next();
            missile.update(deltaTime);
            // Remove missiles if they go off-screen
            if (missile.getPosition().x > 800) { // Example boundary

            }
        }
    }

    public List<Missile> getMissiles() {
        return activeMissiles;
    }
}

