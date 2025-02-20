package com.yourgame.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yourgame.utils.ScreenUtils;
import com.yourgame.characters.item.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Player extends Character {
    private Texture texture;
    private final List<Item> inventory;
    private int selected = 0;
    public final float playerHghDiff;
    private float itemX;
    private float itemY;

    public Player(float x, float y, int health, String name, String texturePath) {
        super(x, y, health, name);
        texture = new Texture(texturePath);
        this.inventory = new ArrayList<>();
        playerHghDiff = texture.getHeight() * ScreenUtils.screenScale() - texture.getHeight();
        itemY = y-playerHghDiff/2 - 2;
        itemX = x;

    }

    @Override
    public void move(Move direction, float speed) {
        switch (direction) {
            case UP:
                y += speed ;
                break;
            case DOWN:
                y -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
            case LEFT:
                x -= speed;
                break;
        }

    }

    @Override
    public void useItem() {
        Item item = getSelectedItem();

        item.use(itemX, y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        //draw player


        batch.draw(texture, x, y-playerHghDiff/2,
            (float) texture.getWidth() / 2, (float) texture.getHeight() / 2,
            texture.getWidth(), texture.getHeight(),
            1, ScreenUtils.screenScale(), 0,
            0, 0, texture.getWidth(), texture.getHeight(),
            false, false);

    }

    public void drawMainItem(SpriteBatch batch) {
        Texture itemTextue = getSelectedItem().getTexture();
        itemX = x;
        itemY = y-playerHghDiff/2 - 2;
        batch.draw(itemTextue, itemX, itemY,
            (float) itemTextue.getWidth() / 2, (float) itemTextue.getHeight() / 2,
            itemTextue.getWidth(), itemTextue.getHeight(),
            1, ScreenUtils.screenScale(), 0,
            0, 0, itemTextue.getWidth(), itemTextue.getHeight(),
            false, false);
    }


    public void removeItem(int pos) {
    }

    public void addItem(Item item) {
        inventory.addLast(item);
    }

    public Item selectItem(int pos) {
        selected = pos;
        return inventory.get(pos);
    }

    public Item getSelectedItem(){
        if(inventory.isEmpty()) {
            throw new InventoryException("Inventory is empty");
        }
        return inventory.get(selected);
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPlayerCenter() {
        float playerCenterX = x + getTexture().getWidth() / 2f;
        float playerCenterY = y - (playerHghDiff / 2f) + getTexture().getHeight() / 2f;
        return new Vector2(playerCenterX, playerCenterY);
    }

}
