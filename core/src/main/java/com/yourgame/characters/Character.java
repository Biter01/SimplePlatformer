package com.yourgame.characters;

public abstract class Character {

    public float x;
    public float y;
    public int health;
    private String name;

    public Character(float x, float y, int health, String name) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.name = name;
    }

    public abstract void move(Move move, float value);

    public abstract void attack();

}
