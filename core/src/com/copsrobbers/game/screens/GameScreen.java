package com.copsrobbers.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.copsrobbers.game.CopsAndRobbersV1;
import com.copsrobbers.game.GameManager;
import com.copsrobbers.game.MapManager;
import com.copsrobbers.game.algorithm.Node;
import com.copsrobbers.game.characters.Cop;
import com.copsrobbers.game.characters.Robber;
import com.copsrobbers.game.items.Coin;
import com.copsrobbers.game.items.Item;
import com.copsrobbers.game.items.Weapon;
import com.copsrobbers.game.listeners.SimpleDirectionGestureDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen implements Screen {

    private final Game game;
    private final Stage stage;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Robber robber;
    private List<Cop> cops;
    private boolean isGameEnded = false;
    private MapManager mapManager;
    private Label coins;
    private Label level;
    private Label weaponCount;


    public GameScreen(Game game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        create();
    }

    public void create() {
        cops = new ArrayList<>();
        mapManager = MapManager.initialize();
        TiledMap map = mapManager.generate(GameManager.getLevel());
        renderer = new OrthogonalTiledMapRenderer(map, 1);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapManager.getScreenWidth(), mapManager.getMapHeight());
        camera.update();

        batch = new SpriteBatch();
        Rectangle robberRect = new Rectangle();
        setRandomPos(map, robberRect, AREA.MIDDLE);

        robberRect.width = mapManager.getTileWidth();
        robberRect.height = mapManager.getTileHeight();

        robber = new Robber(robberRect, () -> game.setScreen(new NextLevelScreen(game)));

        int copCount = 2;
        for (int i = 0; i < copCount; i++) {
            Rectangle copRect = new Rectangle();
            setRandomPos(map, copRect, AREA.values()[i]);
            copRect.width = mapManager.getTileWidth();
            copRect.height = mapManager.getTileHeight();
            Cop cop = new Cop(copRect, () -> {
                isGameEnded = true;
                game.setScreen(new EndScreen(game));

            });
            cops.add(cop);
        }
        Rectangle coinRect = new Rectangle();
        setRandomPos(map, coinRect, AREA.BOTTOM_RIGHT);
        coinRect.width = mapManager.getTileWidth();
        coinRect.height = mapManager.getTileHeight();
        Coin coin = new Coin(coinRect);
        mapManager.addItem(coin);


        Rectangle weaponRect = new Rectangle();
        setRandomPos(map, weaponRect, AREA.BOTTOM_LEFT);
        weaponRect.width = mapManager.getTileWidth();
        weaponRect.height = mapManager.getTileHeight();
        Weapon weapon = new Weapon(weaponRect);
        mapManager.addItem(weapon);

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
        coins.setPosition(mapManager.getScreenWidth() / 2 + 3 * mapManager.getTileWidth(), mapManager.getScreenHeight() - mapManager.getTileHeight());
        stage.addActor(coins);

        level = new Label("Level: " + GameManager.getLevel(), labelStyle);
        level.setSize(Gdx.graphics.getWidth(), mapManager.getTileHeight());
        level.setAlignment(Align.center);
        level.setY(mapManager.getScreenHeight() - mapManager.getTileHeight());
        stage.addActor(level);

        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

            @Override
            public void onUp() {
                if (!isGameEnded) {
                    robber.update(MOVES.UP);
                    robber.setWalking(true);
                    for (Cop cop : cops) {
                        cop.update(robber);
                    }

                }
            }

            @Override
            public void onRight() {
                if (!isGameEnded) {
                    robber.update(MOVES.RIGHT);
                    robber.setWalking(true);
                    for (Cop cop : cops) {
                        cop.update(robber);
                    }
                }
            }

            @Override
            public void onLeft() {
                if (!isGameEnded) {
                    robber.update(MOVES.LEFT);
                    robber.setWalking(true);
                    for (Cop cop : cops) {
                        cop.update(robber);
                    }
                }
            }

            @Override
            public void onDown() {
                if (!isGameEnded) {
                    robber.update(MOVES.DOWN);
                    robber.setWalking(true);
                    for (Cop cop : cops) {
                        cop.update(robber);
                    }
                }
            }

        }));

        ImageButton weaponBtn = new ImageButton(CopsAndRobbersV1.gameSkin);
        weaponBtn.setSize(mapManager.getTileWidth(), mapManager.getTileHeight());
        Texture weaponTexture = new Texture("EMP.png");
        TextureRegion[] regions = TextureRegion.split(weaponTexture, 32, 32)[0];

        weaponBtn.getStyle().imageUp = new TextureRegionDrawable(regions[0]);
        weaponBtn.getStyle().imageDown = new TextureRegionDrawable(regions[1]);
        weaponBtn.setPosition(mapManager.getScreenWidth() - 2 * mapManager.getTileWidth(), 0);
        weaponBtn.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (GameManager.getWeapons() > 0)
                    robber.highlightTargets(stage, cops);

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }
        });
        stage.addActor(weaponBtn);
        weaponCount = new Label("" + GameManager.getWeapons(), labelStyle);
        weaponCount.setPosition(mapManager.getScreenWidth() - mapManager.getTileWidth(), mapManager.getTileHeight() * 0.50f);
        stage.addActor(weaponCount);


    }

    private void setRandomPos(TiledMap map, Rectangle charRect, AREA area) {
        ArrayList<Node> cells = new ArrayList<>();

        TiledMapTileLayer background = (TiledMapTileLayer) map.getLayers().get("background");
        int startX, startY, endX, endY;
        int height = background.getHeight() / background.getTileHeight();
        int width = background.getWidth() / background.getTileWidth();

        Random random = new Random();

        switch (area) {
            case TOP_LEFT:
                startX = 1;
                startY = height - height / 3;
                endX = width / 3;
                endY = height - 1;
                break;
            case TOP_RIGHT:
                startX = width - width / 3;
                startY = height - height / 3;
                endX = width - 1;
                endY = height - 1;
                break;
            case BOTTOM_LEFT:
                startX = 1;
                startY = 1;
                endX = width / 3;
                endY = height / 3;
                break;
            case BOTTOM_RIGHT:
                startX = width - width / 3;
                startY = 1;
                endX = width;
                endY = height / 3;
                break;
            case MIDDLE:
                startX = width / 3;
                startY = height / 3;
                endX = 2 * width / 3;
                endY = 2 * height / 3;
                break;
            default:
                startX = 1;
                startY = 1;
                endX = width - 1;
                endY = height - 1;
                break;
        }
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                Cell cell = background.getCell(i, j);
                if (cell != null) {
                    cells.add(new Node(i, j));
                }
            }
        }

        Node pos = cells.get(random.nextInt(cells.size()));
        charRect.x = pos.getX() * mapManager.getTileWidth();
        charRect.y = pos.getY() * mapManager.getTileHeight();
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
//        Vector3 v3 = new Vector3();
//        v3.set(robber.getX(),robber.getY(),0);
//        camera.position.lerp(v3,0.01f);
        updateCoins();
        updateWeaponCount();
        updateLevel();

        if (!isGameEnded) {
            MOVES move = getMove();
            if (move != null) {
                robber.update(move);
                for (Cop cop : cops) {
                    cop.update(robber);
                }
            }

        }

        renderer.setView(camera);
        renderer.render();
        // Commeting camera movement with player
//        if (robber.getX() > mapManager.getMapWidth()- 2*mapManager.getTileSize()) {
//            camera.position.x = mapManager.getMapWidth();
//        } else
//            if (robber.getX() > 400) {
//            camera.position.x = 400;
//        } else {
//           camera.position.x = robber.getX();
//        }
//        if (robber.getY() < mapManager.getMapHeight()) {
//            camera.position.y = mapManager.getMapHeight();
//        } else if (robber.getY() > 400) {
//            camera.position.y = 400;
//        } else {
//           camera.position.y = robber.getY();
//        }
//          camera.position.y = utils.getScreenWidth()/2 - (utils.getMapHeight()*utils.getScale())/2;


        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(robber.getRegion(Gdx.graphics.getDeltaTime()), robber.getX(), robber.getY(), robber.getWidth(), robber.getHeight());
        for (Cop cop : cops) {
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
        Gdx.input.setInputProcessor(stage);
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
        batch.dispose();

    }


    public enum MOVES {LEFT, RIGHT, UP, DOWN}

    public enum AREA {BOTTOM_LEFT, TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, MIDDLE}
}
