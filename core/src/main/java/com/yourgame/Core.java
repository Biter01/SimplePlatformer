package com.yourgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.yourgame.characters.Move;
import com.yourgame.characters.Player;

public class Core extends ApplicationAdapter {
    private static final int TILE_SIZE = 16;
    private SpriteBatch batch;
    private Texture blockSpriteSheet;
    private Texture gunTexture;
    private OrthographicCamera camera;
    TextureRegion[][] tiles;
    private World gameWorld;
    private FrameBuffer frameBuffer;
    private Texture frameBufferTexture;
    private float screenScale;
    private Player player;
    private Texture playerTexture;


    @Override
    public void create () {
        screenScale =  Gdx.graphics.getWidth()/(float)(Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        blockSpriteSheet = new Texture(Gdx.files.internal("tilemapRev6.png"));
        gunTexture = new Texture(Gdx.files.internal("LaserWaffe.png"));

        int FRAME_ROWS = 3;
        int FRAME_COLS = 3;
        tiles = TextureRegion.split(blockSpriteSheet,
            blockSpriteSheet.getWidth() / FRAME_COLS,
            blockSpriteSheet.getHeight() / FRAME_ROWS);


        gameWorld = World.create((int)(200*screenScale), 200);

        float xCordinate = gameWorld.width * TILE_SIZE / 2.f;
        float yCordinate = gameWorld.height * TILE_SIZE / 2.f;

        player = new Player(xCordinate, yCordinate, 100, "Spieler1", "Spieler1.png");
        playerTexture = player.getTexture();

        camera = new OrthographicCamera(gameWorld.width * TILE_SIZE , (gameWorld.height * TILE_SIZE ) /screenScale);

        camera.position.set((float) (gameWorld.width * TILE_SIZE) / 2, (float) (gameWorld.height * TILE_SIZE )  / 2, 0);
        //camera.zoom = 0.0625f;
        camera.update();

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, gameWorld.width * TILE_SIZE, (int)(gameWorld.height * TILE_SIZE), false);
        frameBufferTexture = frameBuffer.getColorBufferTexture();
        frameBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);


        camera.zoom = 0.2f;

        renderToFrameBuffer();
        camera.update();
    }

    private void renderToFrameBuffer() {
        frameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.enableBlending();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        gameWorld.render(tiles, TILE_SIZE, batch);

        batch.end();

        frameBuffer.end();
    }

    @Override
    public void render () {

        float playerHghDiff = playerTexture.getHeight() *1.9f - playerTexture.getHeight();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        final int MAX_X_CORDINATE = gameWorld.width * TILE_SIZE - (playerTexture.getWidth()-8);
        final int MAX_Y_CORDINATE = gameWorld.height * TILE_SIZE - playerTexture.getHeight();
        final int MIN_X_CORDINATE = -8;
        final float MIN_Y_CORDINATE = playerTexture.getHeight();

        int gunRotation = 1;
        boolean flip = false;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.move(Move.LEFT, 4);
            player.x = Math.max(player.x,MIN_X_CORDINATE);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.move(Move.RIGHT, 4);
            player.x = Math.min(player.x, MAX_X_CORDINATE);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.move(Move.UP, 4*screenScale);
            player.y = Math.min(player.y, MAX_Y_CORDINATE);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.move(Move.DOWN, 4*screenScale);
            player.y= Math.max(player.y, MIN_Y_CORDINATE);
        }


        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            zoomOut();
        }

        updateCameraPosition(playerHghDiff);

        batch.draw(frameBufferTexture, 0, 0, gameWorld.width * TILE_SIZE, gameWorld.height * TILE_SIZE);
        batch.draw(playerTexture, player.x, player.y-playerHghDiff/2,
            (float) playerTexture.getWidth() / 2, (float) playerTexture.getHeight() / 2,
            playerTexture.getWidth(), playerTexture.getHeight(),
            1, screenScale, 0,
            0, 0, playerTexture.getWidth(), playerTexture.getHeight(),
            false, false);
        batch.draw(gunTexture, player.x, (player.y-playerHghDiff/2)-2,
            (float) gunTexture.getWidth() / 2, (float) gunTexture.getHeight() / 2,
            gunTexture.getWidth(), gunTexture.getHeight(),
            1, screenScale, gunRotation,
            0, 0, gunTexture.getWidth(), gunTexture.getHeight(),
            false, flip);

        camera.update();

        batch.end();
    }

    private void updateCameraPosition(float playerHghDiff) {
        // Berechne den Mittelpunkt des Spielers (unter Berücksichtigung des Y-Versatzes)
        float playerCenterX = player.x + playerTexture.getWidth() / 2f;
        float playerCenterY = (player.y - playerHghDiff / 2f) + playerTexture.getHeight() / 2f;

        // Bestimme die halbe Breite und Höhe des Kamerafensters (unter Berücksichtigung des Zooms)
        float cameraHalfWidth = camera.viewportWidth * camera.zoom / 2f;
        float cameraHalfHeight = camera.viewportHeight * camera.zoom / 2f;

        // Klemme den Kameramittelpunkt an die Grenzen der Spielwelt
        playerCenterX = Math.max(cameraHalfWidth, Math.min(playerCenterX, gameWorld.width * TILE_SIZE - cameraHalfWidth));
        playerCenterY = Math.max(cameraHalfHeight, Math.min(playerCenterY, gameWorld.height * TILE_SIZE - cameraHalfHeight));

        // Setze die Kameraposition neu und aktualisiere die Kamera
        camera.position.set(playerCenterX, playerCenterY, 0);
        camera.update();
    }


    private void zoomOut() {
        // Weltgröße in Pixeln (ohne screenScale)
        float worldWidthPixels = gameWorld.width * TILE_SIZE;
        float worldHeightPixels = gameWorld.height * TILE_SIZE;

        float viewportWidth = camera.viewportWidth;
        float viewportHeight = camera.viewportHeight;

        float zoomX = worldWidthPixels / viewportWidth;
        float zoomY = worldHeightPixels / viewportHeight;
        float desiredZoom = Math.max(zoomX, zoomY);

        // Kamera zentrieren und Zoomfaktor anpassen – dabei screenScale berücksichtigen
        camera.position.set(worldWidthPixels / 2, worldHeightPixels / 2f, 0);
        camera.zoom = (desiredZoom / screenScale);
        camera.update();
    }


    @Override
    public void dispose () {
        batch.dispose();
        playerTexture.dispose();
        blockSpriteSheet.dispose();
    }

}
