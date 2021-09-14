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


	@Override
	public void create () {
		map = new TmxMapLoader().load("BlackAndWhiteTiles.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 2.75f);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800,480);
		camera.update();

		robberImg = new Texture(Gdx.files.internal("walka.PNG"));
		batch = new SpriteBatch();
		robber = new Rectangle();
		robber.x = 0;
		robber.y = 0;
		robber.width = 88;
		robber.height = 88;

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		renderer.setView(camera);
		renderer.render();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(robberImg, robber.x, robber.y);
		batch.end();

		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			robber.x = touchPos.x - 88 / 2;
			robber.y = touchPos.y - 88 / 2;
		}
	}
	
	@Override
	public void dispose () {
		robberImg.dispose();
		batch.dispose();

	}
}
