package com.copsrobbers.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;

public class CopsAndRobbersV1 extends ApplicationAdapter {

    private final Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    private final Array<Rectangle> tiles = new Array<>();
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture robberImg;
    private Texture copImg;
    private Rectangle robber;
    private Rectangle cop;

    @Override
    public void create() {
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1f);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 640);
        camera.update();

        renderer = new OrthogonalTiledMapRenderer(map);

        robberImg = new Texture(Gdx.files.internal("robber_walk.PNG"));
        copImg = new Texture(Gdx.files.internal("cop_walk.png"));

        batch = new SpriteBatch();
        robber = new Rectangle();
        robber.x = 32*2;
        robber.y = 32*11;
        robber.width = 32;
        robber.height = 32;

        cop = new Rectangle();
        cop.x = 32*4;
        cop.y = 32*4;
        cop.width = 32;
        cop.height = 32;

    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
            updateRobber(MOVES.LEFT);
        }
        if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
            updateRobber(MOVES.RIGHT);
        }
        if (Gdx.input.isKeyJustPressed(Keys.UP)) {
            updateRobber(MOVES.UP);
        }
        if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
            updateRobber(MOVES.DOWN);
        }
        renderer.setView(camera);
        renderer.render();
        if (robber.x < 320) {
            camera.position.x = 320;
        } else if (robber.x > 2880) {
            camera.position.x = 2880;
        } else {
            camera.position.x = robber.x;
        }
        if (robber.y < 320) {
            camera.position.y = 320;
        } else if (robber.y > 2880) {
            camera.position.y = 2880;
        } else {
            camera.position.y = robber.y;
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(robberImg, robber.x, robber.y, robber.width, robber.height);
        batch.draw(copImg, cop.x, cop.y, cop.width, cop.height);
        batch.end();

    }

    private void updateRobber(MOVES move) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("walls");
        rectPool.freeAll(tiles);
        tiles.clear();
        int i = (int) Math.floor(robber.x / robber.width);
        int j = (int) Math.floor(robber.y / robber.height);
        Cell cell = null;
        float curX = robber.x;
        float curY = robber.y;
        switch (move) {
            case LEFT:
                cell = layer.getCell(i - 1, j);
                robber.x -= robber.width;
                break;
            case RIGHT:
                cell = layer.getCell(i + 1, j);
                robber.x += robber.width;
                break;
            case UP:
                cell = layer.getCell(i, j + 1);
                robber.y += robber.height;
                break;
            case DOWN:
                cell = layer.getCell(i, j - 1);
                robber.y -= robber.height;
                break;
        }

        if (cell != null) {
            Rectangle rect = rectPool.obtain();
            rect.set(i, j, 1, 1);
            tiles.add(rect);
        }
        if (tiles.size != 0) {
            robber.x = curX;
            robber.y = curY;
           
        }
        else{
            robber.x = (float) (Math.floor(robber.x / robber.width) * robber.width);
            robber.y = (float) (Math.floor(robber.y / robber.width) * robber.width);
        }
        
        updateCop();

    }

    private void updateCop() {
    }

    @Override
    public void dispose() {
        robberImg.dispose();
        batch.dispose();

    }


    private enum MOVES {LEFT, RIGHT, UP, DOWN}
}
