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
import com.copsrobbers.game.characters.Cop;
import com.copsrobbers.game.characters.Robber;
import com.copsrobbers.game.items.Item;
import com.copsrobbers.game.listeners.SimpleDirectionGestureDetector;
import com.copsrobbers.game.managers.GameManager;
import com.copsrobbers.game.managers.MapManager;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Class to implement Game Screen
 */
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
        mapManager.reset();
        this.stage = new Stage(new FitViewport(mapManager.getScreenWidth(), mapManager.getScreenHeight()));
        create();
    }

    /**
     * Method to hook into the lifecycle of LibGDX, called only once during start of the game loop thread
     */
    public void create() {
        int levelNumber = GameManager.getLevel();
        CellModel[][] cells = new CellModel[mapManager.getRowTileCount()][mapManager.getColumnTileCount()];
        LevelGenerator levelGen = new LevelGenerator(cells);
        levelGen.generate(levelNumber);

        TiledMap map = mapManager.generate(cells);


        levelGen.generateRobber(() -> {
            isGameEnded = true;
            game.setScreen(new NextLevelScreen(game));
        });
        robber = mapManager.getRobber();

        levelGen.generateCops(() -> {
            isGameEnded = true;
            game.setScreen(new EndScreen(game));
        });

        levelGen.generateItems();

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

    /**
     * Method to update collected coin on the game screen
     */
    private void updateCoins() {
        coins.setText("Coins: " + GameManager.getCoins());
    }

    /**
     * Method to update collected weapons on the game screen
     */
    private void updateWeaponCount() {
        weaponCount.setText("" + GameManager.getWeapons());
    }

    /**
     * Method to update level on the game screen
     */
    private void updateLevel() {
        level.setText("Level: " + GameManager.getLevel());
    }

    /**
     * Method to update character position on the game screen
     * @param move direction to move the character
     */
    private void updateCharacterPos(MOVES move) {
        robber.update(move);
        if (isGameEnded) return;
        robber.setWalking(true);
        LinkedList<LinkedList<Integer>> ignoredPaths = new LinkedList<>();
        LinkedList<Integer> copsPos = new LinkedList<>();

        Collections.sort(mapManager.getCops(), (a, b) ->
                Integer.compare(a.getDist(), b.getDist())
        );
        // to avoid cops overlapping each other
        for (Cop cop : mapManager.getCops()) {
            copsPos.add(mapManager.convertPosToIndex(cop.getX(), cop.getY()));
        }
        ignoredPaths.add(copsPos);
        for (Cop cop : mapManager.getCops()) {
            // current cop position should not be ignored
            ignoredPaths.get(0).remove(mapManager.convertPosToIndex(cop.getX(), cop.getY()));
            ignoredPaths.add(cop.update(robber, ignoredPaths));
            ignoredPaths.get(0).add(mapManager.convertPosToIndex(cop.getX(), cop.getY()));
        }
    }

    /**
     * Overridden LibGDX method to render game scenes to the screen, called every time the game screen needs to be rendered
     * @param delta interval for the current frame
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        updateCoins();
        updateWeaponCount();
        updateLevel();
        MOVES move = getMove();
        if (move != null) {
            updateCharacterPos(move);
        }

        renderer.setView(camera);
        renderer.render();

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

    /**
     * Method to get the movement of player by key press
     * @return the move from players input
     */
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
            public void onDirChange(MOVES move) {
                updateCharacterPos(move);
            }
        });
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(directionGestureDetector);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Overridden lifecycle method of LibGDX to resize the game, called every time the size of the game screen is changed
     * @param width new width
     * @param height new height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        camera.update();
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

    /**
     * Overridden lifecycle method of LibGDX to method clean up and free all resources, called when the game is being destroyed
     */
    @Override
    public void dispose() {
        GameManager.reset();
        batch.dispose();
    }

    /**
     * Enumerator with the possible moves
     */
    public enum MOVES {LEFT, RIGHT, UP, DOWN}

    /**
     * Enumerator with possible sub areas of map
     */

    public enum AREA {BOTTOM_LEFT, TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, MIDDLE}
}
