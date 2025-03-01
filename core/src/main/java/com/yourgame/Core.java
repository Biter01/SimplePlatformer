package com.yourgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.yourgame.characters.Move;
import com.yourgame.characters.Player;
import com.yourgame.characters.item.Item;
import com.yourgame.characters.item.Weapon;
import com.yourgame.utils.ScreenUtils;

public class Core extends ApplicationAdapter {
    private GameRenderer renderer;
    public final int TILE_SIZE = 16;
    private SpriteBatch batch;
    private Texture gunTexture;
    private OrthographicCamera camera;
    TextureRegion[][] tiles;
    private World gameWorld;
    private FrameBuffer frameBuffer;
    private Player player;
    private Texture laserTexture;
    private Item item;
    private Texture blockSpriteSheet;
    private float speed;

    @Override
    public void create () {
         speed = 150f; // Scales movement with time

        batch = new SpriteBatch();
        blockSpriteSheet = new Texture(Gdx.files.internal("tilemap.png"), true);
        gunTexture = new Texture(Gdx.files.internal("LaserWaffe.png"));
        laserTexture = new Texture(Gdx.files.internal("Laser.png"));

        createBlockTiles();

        int worldHeight = (int)(200* ScreenUtils.screenScale());
        gameWorld = World.create(worldHeight, 200, TILE_SIZE);

        float playerX = gameWorld.width * TILE_SIZE / 2.f;
        float playerY = gameWorld.height * TILE_SIZE / 2.f;

        player = new Player(playerX, playerY, 100, "Spieler1", "SpielerSuit.png");
        item = new Weapon(gunTexture, laserTexture);
        player.addItem(item);

        camera = new OrthographicCamera(gameWorld.width * TILE_SIZE , (gameWorld.height * TILE_SIZE ) /ScreenUtils.screenScale());

        camera.position.set((float) (gameWorld.width * TILE_SIZE) / 2, (float) (gameWorld.height * TILE_SIZE )  / 2, 0);
        //camera.zoom = 0.0625f;
        camera.update();

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, gameWorld.width * TILE_SIZE, (int)(gameWorld.height * TILE_SIZE), false);
        renderer = new GameRenderer(gameWorld, player, item, camera, frameBuffer, batch);
        renderer.renderToFrameBuffer(tiles);

        camera.zoom = 0.2f;

        //camera.update();
    }



    @Override
    public void render () {
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        handleInput();
        updadteGameLogic(Gdx.graphics.getDeltaTime());
        renderer.render();
        camera.update();

        batch.end();
    }

    private void updadteGameLogic(float deltaTime) {
        item.update(deltaTime);
        updateCameraPosition(player.playerHghDiff);
    }

    private void handleInput() {
        final int MAX_X_CORDINATE = gameWorld.width * TILE_SIZE - (player.getTexture().getWidth()-8);
        final int MAX_Y_CORDINATE = gameWorld.height * TILE_SIZE - player.getTexture().getHeight();
        final int MIN_X_CORDINATE = -8;
        final float MIN_Y_CORDINATE = player.getTexture().getHeight();

        float movementSpeed = speed  * Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.move(Move.LEFT, movementSpeed);
            player.x = Math.max(player.x,MIN_X_CORDINATE);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.move(Move.RIGHT, movementSpeed);
            player.x = Math.min(player.x, MAX_X_CORDINATE);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.move(Move.UP, movementSpeed *1.5f);
            player.y = Math.min(player.y, MAX_Y_CORDINATE);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.move(Move.DOWN, movementSpeed*1.5f);
            player.y= Math.max(player.y, MIN_Y_CORDINATE);
        }


        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            zoomOut();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            player.useItem();
        }


    }

    private void updateCameraPosition(float playerHghDiff) {
        float movementSpeed = speed  * Gdx.graphics.getDeltaTime();

        // Berechne den Mittelpunkt des Spielers (unter Berücksichtigung des Y-Versatzes)
        float playerCenterX = player.x + player.getTexture().getWidth() / 2f;
        float playerCenterY = (player.y - playerHghDiff / 2f) + player.getTexture().getHeight() / 2f;

        // Bestimme die halbe Breite und Höhe des Kamerafensters (unter Berücksichtigung des Zooms)
        float cameraHalfWidth = camera.viewportWidth * camera.zoom / 2f;
        float cameraHalfHeight = camera.viewportHeight * camera.zoom / 2f;

        // Klemme den Kameramittelpunkt an die Grenzen der Spielwelt
        playerCenterX = Math.max(cameraHalfWidth, Math.min(playerCenterX, gameWorld.width * TILE_SIZE - cameraHalfWidth));
        playerCenterY = Math.max(cameraHalfHeight, Math.min(playerCenterY, gameWorld.height * TILE_SIZE - cameraHalfHeight));

        // Setze die Kameraposition neu und aktualisiere die Kamera
        camera.position.set(new Vector3(playerCenterX, playerCenterY, 0));
        camera.update();

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
        camera.zoom = (desiredZoom / ScreenUtils.screenScale());
        camera.update();
    }

    @Override
    public void dispose () {
        batch.dispose();
        player.getTexture().dispose();
        blockSpriteSheet.dispose();
        blockSpriteSheet.dispose(); // Dispose tilemap texture
        gunTexture.dispose(); // Dispose weapon texture
        laserTexture.dispose(); // Dispose laser texture
        frameBuffer.dispose();
    }


    private void createBlockTiles() {
        int FRAME_ROWS = 3;
        int FRAME_COLS = 3;
        tiles = TextureRegion.split(blockSpriteSheet,
            blockSpriteSheet.getWidth() / FRAME_COLS,
            blockSpriteSheet.getHeight() / FRAME_ROWS);

        for (TextureRegion[] regionRow : tiles) {
            for (TextureRegion region : regionRow) {
                region.flip(false, true);
            }
        }
    }
}
