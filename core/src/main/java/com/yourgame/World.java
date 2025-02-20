package com.yourgame;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourgame.utils.PerlinNoise;

import java.util.Random;

public class World {

    public final int height;
    public final int width;
    private long seed;
    private static final float SCALE = 0.1f;
    public final float[][] heightMap;
    public final int TILE_SIZE;

    private World(final int height, final int width, int TILE_SIZE, long seed) {
        this.height = height;
        this.width = width;
        this.seed = seed;
        heightMap = new float[width][height];
        this.TILE_SIZE = TILE_SIZE;
        createMap();
    }

    public static World create(final int height, final int width, int TILE_SIZE) {
        long seed = new Random().nextLong();
        return new World(height, width, TILE_SIZE,seed);
    }

    private void createMap() {
        PerlinNoise perlinNoise = new PerlinNoise(seed);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Generate height using the seeded Perlin noise
                float noiseValue = perlinNoise.noise(x * SCALE, y * SCALE);
                heightMap[x][y] = noiseValue;
            }
        }
    }

    public void render(TextureRegion tiles[][],  Batch batch) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float height = heightMap[x][y];
                TextureRegion texture;

                if (height < -0.2f) {
                    texture = tiles[0][0]; // Water
                } else if (height < 0) {

                    if (isBeach(x, y)) {
                        texture = tiles[0][2];
                    } else {
                        texture = tiles[1][0];
                    }
                } else if (height < 0.4f) {
                    texture = tiles[1][0]; // Grass
                } else {
                    texture = tiles[0][1]; //Tree
                }

                batch.draw(texture, x * TILE_SIZE , y * TILE_SIZE);
            }
        }

    }

    private boolean isBeach(int blockX, int blockY) {
        int leftContrX = Math.max(blockX - 2, 0);
        int leftContrY = Math.max(blockY - 2, 0);
        int radius = 2;

        return checkBlockArea(leftContrX, leftContrY, leftContrX, leftContrY, radius);
    }


    private boolean checkBlockArea(int x, int y, final int leftX, final int leftY, final int radius) {

        int blockAreaWidth = radius * 2;

        if (heightMap[Math.min(x, width - 1)][Math.min(y, height - 1)] < -0.2) { //If block is Water
            return true;
        }

        if (y - leftY >= blockAreaWidth && x - leftX >= blockAreaWidth) {
            //System.out.println("X ist gleich " + (leftX - x));
            //System.out.println("Y ist gleich " + (leftY - y));
            return false;
        }

        x = x + 1;

        if ((x - leftX) > blockAreaWidth) {
            x = leftX;
            y += 1;
        }

        return checkBlockArea(x, y, leftX, leftY, radius);

    }
}

