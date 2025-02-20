package com.yourgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.yourgame.characters.Player;
import com.yourgame.characters.item.Item;
import com.yourgame.characters.item.Missile;
import com.yourgame.characters.item.Weapon;
import com.yourgame.utils.ScreenUtils;


public class GameRenderer {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Player player;
    private TextureRegion[][] tiles;
    private World gameWorld;
    private Item item;
    private FrameBuffer frameBuffer;
    private Texture frameBufferTexture;

    public GameRenderer(World gameWorld, Player player, Item item, OrthographicCamera camera,FrameBuffer frameBuffer, SpriteBatch batch) {
        this.batch = batch;
        this.gameWorld = gameWorld;
        this.player = player;
        this.item = item;
        this.camera = camera;
        this.frameBuffer = frameBuffer;
        frameBufferTexture = frameBuffer.getColorBufferTexture();
        frameBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public void render() {

        batch.setProjectionMatrix(camera.combined);
        // Draw world
        batch.draw(frameBufferTexture, 0, 0, gameWorld.width * 16, gameWorld.height * 16);
        // Draw player
        player.draw(batch);

        Vector3 worldCoordinates = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float a = worldCoordinates.x - player.getPlayerCenter().x;
        float b = worldCoordinates.y - player.getPlayerCenter().y;
        double bDiva = b / a;

        double deg = (Math.atan2(b, a))/(2*Math.PI) * 360;
        double angle = deg;
        if(deg < 0) {
          angle =  Math.abs(deg + 180) +180;
        }

        // Draw missiles
        Weapon weapon = (Weapon) item;
        for (Missile missile : weapon.getMissiles()) {
            batch.draw(missile.getTexture(), missile.getPosition().x, missile.getPosition().y);
        }

        player.drawMainItem(batch);

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

}
