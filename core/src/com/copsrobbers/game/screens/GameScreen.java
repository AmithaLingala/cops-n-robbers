package com.copsrobbers.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.copsrobbers.game.CopsAndRobbers;
import com.copsrobbers.game.algorithm.CellModel;
import com.copsrobbers.game.algorithm.LevelGenerator;
import com.copsrobbers.game.managers.GameManager;
import com.copsrobbers.game.managers.MapManager;
import com.copsrobbers.game.characters.Cop;
import com.copsrobbers.game.characters.Robber;
import com.copsrobbers.game.items.Item;
import com.copsrobbers.game.listeners.SimpleDirectionGestureDetector;

public class GameScreen implements Screen {

    private final Game game;
    private final Stage stage;
    private final MapManager mapManager;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Robber robber;
    private boolean isGameEnded = false;
    private Label coins;
    private Label level;
    private Label weaponCount;
    private ImageButton weaponBtn;

    public GameScreen(Game game) {
        this.game = game;
        mapManager = MapManager.obtain();
        this.stage = new Stage(new FitViewport(mapManager.getScreenWidth(), mapManager.getScreenHeight()));
        create();
    }

    public void create() {
        int levelNumber = GameManager.getLevel();
        CellModel[][] cells = new CellModel[mapManager.getRowTileCount()][mapManager.getColumnTileCount()];
        LevelGenerator levelGen = new LevelGenerator(cells);
        levelGen.generate(levelNumber);

        TiledMap map = mapManager.generate(cells);

        levelGen.generateCops(2, () -> {
            isGameEnded = true;
            game.setScreen(new EndScreen(game));
        });

        levelGen.generateItems(levelNumber);

        levelGen.generateRobber(() -> {
            isGameEnded = true;
            game.setScreen(new NextLevelScreen(game));
        });
        robber = mapManager.getRobber();

        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapManager.getScreenWidth(), mapManager.getScreenHeight());
        camera.update();
        batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Amble-Light.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 15;
        parameter.borderWidth = 1;
        parameter.color = Color.WHITE;
        BitmapFont font24 = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;

        coins = new Label("Coins: " + GameManager.getCoins(), labelStyle);
        coins.setSize(Gdx.graphics.getWidth(), mapManager.getTileHeight());
        coins.setY(mapManager.getScreenHeight() - mapManager.getTileHeight());
        coins.setX(mapManager.getScreenWidth() * 0.5f + mapManager.getTileWidth());
        stage.addActor(coins);

        level = new Label("Level: " + GameManager.getLevel(), labelStyle);
        level.setSize(Gdx.graphics.getWidth(), mapManager.getTileHeight());
        level.setX(mapManager.getScreenWidth() * 0.5f - mapManager.getTileWidth() * 2);
        level.setY(mapManager.getScreenHeight() - mapManager.getTileHeight());
        stage.addActor(level);

        weaponBtn = new ImageButton(CopsAndRobbers.gameSkin);
        weaponBtn.setSize(mapManager.getTileWidth() * 2, mapManager.getTileHeight());
        Texture weaponTexture = new Texture("EMP.png");
        TextureRegion[] regions = TextureRegion.split(weaponTexture, mapManager.getTextureSize(), mapManager.getTextureSize())[0];

        weaponBtn.getStyle().imageUp = new TextureRegionDrawable(regions[0]);
        weaponBtn.getStyle().imageDown = new TextureRegionDrawable(regions[1]);
        weaponBtn.setPosition(mapManager.getScreenWidth() * 0.5f - mapManager.getTileWidth() * 0.5f, 0);

        stage.addActor(weaponBtn);
        weaponCount = new Label("" + GameManager.getWeapons(), labelStyle);
        weaponCount.setPosition(mapManager.getScreenWidth() * 0.5f + mapManager.getTileWidth(), mapManager.getTileHeight() * 0.40f);
        stage.addActor(weaponCount);


    }


    private void updateCoins() {
        coins.setText("Coins: " + GameManager.getCoins());
    }

    private void updateWeaponCount() {
        weaponCount.setText("" + GameManager.getWeapons());
    }

    private void updateLevel() {
        level.setText("Level: " + GameManager.getLevel());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        updateCoins();
        updateWeaponCount();
        updateLevel();

        if (!isGameEnded) {
            MOVES move = getMove();
            if (move != null) {
                robber.update(move);
                for (Cop cop : mapManager.getCops()) {
                    cop.update(robber);
                }
            }

        }

        renderer.setView(camera);
        renderer.render();

//        Vector3 v3 = new Vector3();
//        v3.set(robber.getX(),robber.getY(),0);
//        camera.position.lerp(v3,0.25f);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(robber.getRegion(Gdx.graphics.getDeltaTime()), robber.getX(), robber.getY(), robber.getWidth(), robber.getHeight());
        for (Cop cop : mapManager.getCops()) {
            batch.draw(cop.getCharImg(), cop.getX(), cop.getY(), cop.getWidth(), cop.getHeight());
        }
        for (Item item : mapManager.getItems()) {
            batch.draw(item.getRegion(Gdx.graphics.getDeltaTime()), item.getX(), item.getY(), item.getWidth(), item.getHeight());
        }
        batch.end();
        stage.act();
        stage.draw();


    }

    private MOVES getMove() {
        MOVES move = null;
        if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
            move = MOVES.LEFT;
        } else if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
            move = MOVES.RIGHT;
        } else if (Gdx.input.isKeyJustPressed(Keys.UP)) {
            move = MOVES.UP;
        } else if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
            move = MOVES.DOWN;
        }
        return move;
    }


    @Override
    public void show() {

        weaponBtn.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (GameManager.getWeapons() > 0)
                    robber.highlightTargets(stage, mapManager.getCops());
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }
        });

        SimpleDirectionGestureDetector directionGestureDetector = new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

            @Override
            public void onUp() {
                if (!isGameEnded) {
                    robber.update(MOVES.UP);
                    robber.setWalking(true);
                    for (Cop cop : mapManager.getCops()) {
                        cop.update(robber);
                    }

                }
            }

            @Override
            public void onRight() {
                if (!isGameEnded) {
                    robber.update(MOVES.RIGHT);
                    robber.setWalking(true);
                    for (Cop cop : mapManager.getCops()) {
                        cop.update(robber);
                    }
                }
            }

            @Override
            public void onLeft() {
                if (!isGameEnded) {
                    robber.update(MOVES.LEFT);
                    robber.setWalking(true);
                    for (Cop cop : mapManager.getCops()) {
                        cop.update(robber);
                    }
                }
            }

            @Override
            public void onDown() {
                if (!isGameEnded) {
                    robber.update(MOVES.DOWN);
                    robber.setWalking(true);
                    for (Cop cop : mapManager.getCops()) {
                        cop.update(robber);
                    }
                }
            }

        });
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(directionGestureDetector);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        GameManager.reset();
        batch.dispose();
    }


    public enum MOVES {LEFT, RIGHT, UP, DOWN}

    public enum AREA {BOTTOM_LEFT, TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, MIDDLE}
}
