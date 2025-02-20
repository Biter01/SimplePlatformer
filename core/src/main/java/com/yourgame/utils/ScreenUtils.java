package com.yourgame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public final class ScreenUtils {

    private ScreenUtils() {
        // Privater Konstruktor, um Instanziierung zu verhindern
    }

    public static float screenScale() {
        return Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
    }

    public static void renderDebugRect(float x, float y, float width, float height, OrthographicCamera camera) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, width, height* ScreenUtils.screenScale()); // (x, y, width, height)
        shapeRenderer.end();
    }


}
