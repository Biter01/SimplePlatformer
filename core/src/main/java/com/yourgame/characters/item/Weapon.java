package com.yourgame.characters.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yourgame.utils.AngleSlope;
import com.yourgame.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Weapon implements Item {
    private final Texture missileTexture;
    private final List<Missile> activeMissiles = new ArrayList<>();
    public final Texture texture;

    public Weapon(Texture texture, Texture missle) {
        this.texture = texture;
        this.missileTexture = missle;
    }

    @Override
    public void use(float x, float y, AngleSlope clickObject) {
        // Spawn a new missile at a position (e.g., player's position)
        activeMissiles.add(new Missile(missileTexture, new Vector2(x, y), 400, clickObject));
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

    private List<Missile> getMissiles() {
        return activeMissiles;
    }


    public Texture getTexture() {
        return texture;
    }

    @Override
    public void draw(SpriteBatch batch, float x, float y, AngleSlope clickObj) {
        float angle = clickObj.getAngle();

        Texture itemTextue = getTexture();

        float scalex;
        float scaley;
        scaley = ScreenUtils.screenScale();
        scalex = 1;

        batch.draw(itemTextue, x, y,
            (float) itemTextue.getWidth() / 2, (float) itemTextue.getHeight() / 2,
            itemTextue.getWidth(), itemTextue.getHeight(),
            scalex, scaley, angle,
            0, 0, itemTextue.getWidth(), itemTextue.getHeight(),
            false, false);

        drawMissiles(batch, angle);

    }


    private void drawMissiles(Batch batch, float angle) {
        for (Missile missile : getMissiles()) {
            Texture missileTexture = missile.getTexture();
            batch.draw(missileTexture, missile.getPosition().x, missile.getPosition().y,
                missileTexture.getWidth(),missileTexture.getHeight(),
                missileTexture.getWidth(), missileTexture.getHeight(),
                1,ScreenUtils.screenScale(),0,
                0,0,missileTexture.getWidth(),missileTexture.getHeight(),
                false,false
            );
        }
    }

}

