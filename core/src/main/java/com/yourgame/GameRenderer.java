package com.yourgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.yourgame.characters.Player;
import com.yourgame.characters.item.Item;


public class GameRenderer {
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Player player;
    private TextureRegion[][] tiles;
    private final World gameWorld;
    private Item item;
    private final FrameBuffer frameBuffer;
    private final Texture frameBufferTexture;
    private Vector3 clickCordinates;

    public GameRenderer(World gameWorld, Player player, Item item, OrthographicCamera camera,FrameBuffer frameBuffer, SpriteBatch batch) {
        this.batch = batch;
        this.gameWorld = gameWorld;
        this.player = player;
        this.item = item;
        this.camera = camera;
        this.frameBuffer = frameBuffer;
        frameBufferTexture = frameBuffer.getColorBufferTexture();
        frameBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        clickCordinates = new Vector3(player.getPlayerCenter().x, player.getPlayerCenter().y, 0);
    }

    public void render() {
        batch.setProjectionMatrix(camera.combined);
        // Draw world
        batch.draw(frameBufferTexture, 0, 0, gameWorld.width * 16, gameWorld.height * 16);
        // Draw player
        player.draw(batch);

        updateclickCordinates();


        clickCordinates = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));


        player.drawMainItem(batch, clickCordinates);
    }

    public void renderToFrameBuffer(TextureRegion[][] tiles) {
        frameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.enableBlending();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        gameWorld.render(tiles, batch);
        batch.end();

        frameBuffer.end();
    }

    private void updateclickCordinates() {

    }


}
