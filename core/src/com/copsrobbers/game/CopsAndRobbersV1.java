package com.copsrobbers.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;

public class CopsAndRobbersV1 extends ApplicationAdapter {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Texture robberImg;
    private SpriteBatch batch;
    private Rectangle robber;

    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    private Array<Rectangle> tiles = new Array<Rectangle>();


    @Override
    public void create() {
        map = new TmxMapLoader().load("BlackAndWhiteTiles.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1f);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 320);
        camera.update();

        robberImg = new Texture(Gdx.files.internal("walka.PNG"));

        batch = new SpriteBatch();
        robber = new Rectangle();
        robber.x = 0;
        robber.y = 0;
        robber.width = 32;
        robber.height = 32;

    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        renderer.setView(camera);
        renderer.render();
        camera.update();
        batch.setProjectionMatrix(camera.combined);


        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            updaterobber(touchPos);

        }
        batch.begin();
        batch.draw(robberImg, robber.x, robber.y, robber.width, robber.height);
        batch.end();

    }

    private void updaterobber(Vector3 touchPos) {

        Rectangle robberRect = rectPool.obtain();
        int startX, startY, endX, endY;
        startX = (int) (touchPos.x);
        endX = (int) (touchPos.x + robber.width);
        startY = (int) (touchPos.y);
        endY = (int) (touchPos.y + robber.height);
        getTiles(startX, startY, endX, endY, tiles);
        if (tiles.size != 0) {
            return;
        }

        robber.x = (float) (Math.floor(touchPos.x / robber.width) * robber.width);
        robber.y = (float) (Math.floor(touchPos.y / robber.width) * robber.width);

    }

    private void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("walls");
        rectPool.freeAll(tiles);
        tiles.clear();
        int i = (int) Math.floor(startX / robber.width);
        int j = (int) Math.floor(startY / robber.height);

        Cell cell = layer.getCell(i, j);
        if (cell != null) {
            Rectangle rect = rectPool.obtain();
            rect.set(i, j, 1, 1);
            tiles.add(rect);
        }
    }


    @Override
    public void dispose() {
        robberImg.dispose();
        batch.dispose();

    }
}
