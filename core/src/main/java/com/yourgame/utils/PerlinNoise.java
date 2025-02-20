package com.yourgame.utils;

import java.util.Random;

public class PerlinNoise {
    private int[] p = new int[512]; // Permutation table, filled based on the seed

    // Constructor to initialize Perlin noise with a seed
    public PerlinNoise(long seed) {
        int[] permutation = new int[256];

        // Initialize the permutation array with values 0-255
        for (int i = 0; i < 256; i++) {
            permutation[i] = i;
        }

        // Shuffle the permutation array based on the seed
        Random random = new Random(seed);
        for (int i = 255; i > 0; i--) {
            int swapIndex = random.nextInt(i + 1);
            int temp = permutation[i];
            permutation[i] = permutation[swapIndex];
            permutation[swapIndex] = temp;
        }

        // Fill p with the shuffled values, repeated twice
        for (int i = 0; i < 512; i++) {
            p[i] = permutation[i % 256];
        }
    }

    // Fade function
    public static float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    // Linear interpolation function
    public static float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    // Gradient function
    public static float grad(int hash, float x, float y) {
        int h = hash & 15;
        float u = h < 8 ? x : y;
        float v = h < 4 ? y : 0;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    // The main noise function
    public float noise(float x, float y) {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;

        x -= (float) Math.floor(x);
        y -= (float) Math.floor(y);

        float u = fade(x);
        float v = fade(y);

        int a = p[X] + Y;
        int aa = p[a];
        int ab = p[a + 1];
        int b = p[X + 1] + Y;
        int ba = p[b];
        int bb = p[b + 1];

        return lerp(v, lerp(u, grad(p[aa], x, y), grad(p[ba], x - 1, y)),
            lerp(u, grad(p[ab], x, y - 1), grad(p[bb], x - 1, y - 1)));
    }
}
