package com.copsrobbers.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.copsrobbers.game.Utils;
import com.copsrobbers.game.algorithm.CellModel;
import com.copsrobbers.game.algorithm.LevelGenerator;
import com.copsrobbers.game.algorithm.Node;
import com.copsrobbers.game.characters.Cop;
import com.copsrobbers.game.characters.Robber;
import com.copsrobbers.game.items.Coin;
import com.copsrobbers.game.items.Weapon;
import com.copsrobbers.game.listeners.GameListener;
import com.copsrobbers.game.listeners.SimpleDirectionGestureDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen implements Screen {

    static public Skin gameSkin;
    private final Array<Rectangle> tiles = new Array<>();
    private final int copCount = 2;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture robberImg;
    private Texture copImg;
    private Robber robber;
    private List<Cop> cops;
    private boolean isGameEnded = false;
    private Utils utils;
    private final Game game;
    private final Stage stage;
    private Coin coin;
    private Weapon weapon;

    public GameScreen(Game game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        create();
    }

    public void create() {
        int tileWidth = 32;
        int tileHeight = 32;
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        cops = new ArrayList<>();

        CellModel[][] cells = new CellModel[screenWidth / tileWidth][screenHeight / tileHeight];
        LevelGenerator level = new LevelGenerator(cells);
        level.generate(2);

        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer background = new TiledMapTileLayer(screenWidth, screenHeight, tileWidth, tileHeight);
        TiledMapTileLayer walls = new TiledMapTileLayer(screenWidth, screenHeight, tileWidth, tileHeight);
        background.setName("background");
        walls.setName("walls");
        Texture tiles = new Texture(Gdx.files.internal("blackandwhite.png"));
        TextureRegion[][] textureRegions = TextureRegion.split(tiles, tileWidth, tileHeight);

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                Cell cell = new Cell();
                if (cells[i][j].isWall()) {
                    cell.setTile(new StaticTiledMapTile(textureRegions[0][0]));
                    walls.setCell(i, j, cell);
                } else {
                    cell.setTile(new StaticTiledMapTile(textureRegions[0][1]));
                    background.setCell(i, j, cell);
                }

            }
        }
        layers.add(background);
        layers.add(walls);

        //map = new TmxMapLoader().load("BlackAndWhiteTiles.tmx");
        utils = Utils.init(map);
        renderer = new OrthogonalTiledMapRenderer(map, 1);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, utils.getScreenWidth(), utils.getScreenHeight());
        camera.update();

        robberImg = new Texture(Gdx.files.internal("robber_walk.PNG"));
        copImg = new Texture(Gdx.files.internal("cop_walk.png"));

        batch = new SpriteBatch();
        Rectangle robberRect = new Rectangle();
        setRandomPos(map, robberRect, AREA.MIDDLE);

        robberRect.width = utils.getTilesize();
        robberRect.height = utils.getTilesize();

        robber = new Robber(robberRect);

        for (int i = 0; i < copCount; i++) {
            Rectangle copRect = new Rectangle();
            setRandomPos(map, copRect, AREA.values()[i]);
            copRect.width = utils.getTilesize();
            copRect.height = utils.getTilesize();
            Cop cop = new Cop(copRect, new GameListener() {
                @Override
                public void endGame() {
                    isGameEnded = true;
                    game.setScreen(new EndScreen(game));

                }
            });
            cops.add(cop);
        }
        Rectangle coinRect = new Rectangle();
        setRandomPos(map, coinRect, AREA.BOTTOMRIGHT);
        coinRect.width = utils.getTilesize();
        coinRect.height = utils.getTilesize();
        coin = new Coin(coinRect);

        Rectangle weaponRect = new Rectangle();
        setRandomPos(map, weaponRect, AREA.BOTTOMLEFT);
        weaponRect.width = utils.getTilesize();
        weaponRect.height = utils.getTilesize();
        weapon = new Weapon(weaponRect);


        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

            @Override
            public void onUp() {
                if (!isGameEnded) {
                    robber.update(MOVES.UP);
                    for (Cop cop : cops) {
                        cop.update(robber);
                    }

                }
            }

            @Override
            public void onRight() {
                if (!isGameEnded) {
                    robber.update(MOVES.RIGHT);
                    for (Cop cop : cops) {
                        cop.update(robber);
                    }
                }
            }

            @Override
            public void onLeft() {
                if (!isGameEnded) {
                    robber.update(MOVES.LEFT);
                    for (Cop cop : cops) {
                        cop.update(robber);
                    }
                }
            }

            @Override
            public void onDown() {
                if (!isGameEnded) {
                    robber.update(MOVES.DOWN);
                    for (Cop cop : cops) {
                        cop.update(robber);
                    }
                }
            }

        }));

    }

    private void setRandomPos(TiledMap map, Rectangle charRect, AREA area) {
        ArrayList<Node> cells = new ArrayList<Node>();

        TiledMapTileLayer background = (TiledMapTileLayer) map.getLayers().get("background");
        int startX, startY, endX, endY;
        int height = background.getHeight() / background.getTileHeight();
        int width = background.getWidth() / background.getTileWidth();

        Random random = new Random();

        switch (area) {
            case TOPLEFT:
                startX = 1;
                startY = height - height / 3;
                endX = width / 3;
                endY = height - 1;
                break;
            case TOPRIGHT:
                startX = width - width / 3;
                startY = height - height / 3;
                endX = width - 1;
                endY = height - 1;
                break;
            case BOTTOMLEFT:
                startX = 1;
                startY = 1;
                endX = width / 3;
                endY = height / 3;
                break;
            case BOTTOMRIGHT:
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
        charRect.x = pos.getX() * utils.getTilesize();
        charRect.y = pos.getY() * utils.getTilesize();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

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
//        if (robber.getX() < utils.getMapWidth()) {
//            camera.position.x = utils.getMapWidth();
//        } else if (robber.getX() > 2880) {
//            camera.position.x = 2880;
//        } else {
//            camera.position.x = robber.getX();
//        }
//        if (robber.getY() < utils.getMapHeight()) {
//            camera.position.y = utils.getMapHeight();
//        } else if (robber.getY() > 2880) {
//            camera.position.y = 2880;
//        } else {
//            camera.position.y = robber.getY();
//        }
        //  camera.position.y = utils.getScreenWidth()/2 - (utils.getMapHeight()*utils.getScale())/2;

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(robberImg, robber.getX(), robber.getY(), robber.getWidth(), robber.getHeight());
        for (Cop cop : cops) {
            batch.draw(copImg, cop.getX(), cop.getY(), cop.getWidth(), cop.getHeight());
        }
        batch.draw(coin.getRegion(Gdx.graphics.getDeltaTime()), coin.getX(), coin.getY(), coin.getWidth(), coin.getHeight());
        batch.draw(weapon.getRegion(Gdx.graphics.getDeltaTime()), weapon.getX(), weapon.getY(), weapon.getWidth(), weapon.getHeight());
        batch.end();

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
        robberImg.dispose();
        batch.dispose();

    }


    public enum MOVES {LEFT, RIGHT, UP, DOWN}

    public enum AREA {BOTTOMLEFT, TOPRIGHT, TOPLEFT, BOTTOMRIGHT, MIDDLE}
}
