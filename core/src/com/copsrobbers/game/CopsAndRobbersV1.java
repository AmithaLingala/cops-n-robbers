package com.copsrobbers.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import com.copsrobbers.game.algorithm.CellModel;
import com.copsrobbers.game.algorithm.GraphManager;
import com.copsrobbers.game.algorithm.LevelGenerator;
import com.copsrobbers.game.algorithm.Node;
import com.copsrobbers.game.characters.Cop;
import com.copsrobbers.game.characters.Robber;
import com.copsrobbers.game.listeners.GameListener;
import com.copsrobbers.game.listeners.SimpleDirectionGestureDetector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

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
    private Robber robber;
    private Cop cop;
    private boolean isGameEnded = false;
    private Utils utils;


    @Override
    public void create() {
        int tileWidth = 32;
        int tileHeight =32;
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        CellModel cells[][] = new CellModel[screenWidth/tileWidth][screenHeight/tileHeight];
        LevelGenerator level = new LevelGenerator(cells);
        level.generate();

        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer background = new TiledMapTileLayer(screenWidth,screenHeight,tileWidth,tileHeight);
        TiledMapTileLayer walls = new TiledMapTileLayer(screenWidth,screenHeight,tileWidth,tileHeight);
        background.setName("background");
        walls.setName("walls");
        Texture tiles = new Texture(Gdx.files.internal("blackandwhite.png"));
        TextureRegion[][] textureRegions = TextureRegion.split(tiles,tileWidth,tileHeight);

        for(int i=0;i<cells.length;i++){
            for(int j=0; j<cells[0].length;j++){
                Cell cell = new Cell();
                if(cells[i][j].isWall()) {
                    cell.setTile(new StaticTiledMapTile(textureRegions[0][0]));
                    walls.setCell(i,j,cell);
                }
                else{
                    cell.setTile(new StaticTiledMapTile(textureRegions[0][1]));
                    background.setCell(i,j,cell);
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
        setRandomPos(map,robberRect);

        robberRect.width = utils.getTilesize();
        robberRect.height = utils.getTilesize();

        robber = new Robber(robberRect);



        Rectangle copRect = new Rectangle();
        setRandomPos(map,copRect);

        copRect.width = utils.getTilesize();
        copRect.height = utils.getTilesize();

        cop = new Cop(copRect, new GameListener() {
            @Override
            public void endGame() {
                isGameEnded = true;
            }
        });


        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

            @Override
            public void onUp() {
                if(!isGameEnded) {
                    robber.update(MOVES.UP);
                    cop.update(robber);
                }
            }

            @Override
            public void onRight() {
                if(!isGameEnded) {
                    robber.update(MOVES.RIGHT);
                    cop.update(robber);
                }
            }

            @Override
            public void onLeft() {
                if(!isGameEnded) {
                    robber.update(MOVES.LEFT);
                    cop.update(robber);
                }
            }

            @Override
            public void onDown() {
                if(!isGameEnded) {
                    robber.update(MOVES.DOWN);
                    cop.update(robber);
                }
            }

        }));

    }

    private void setRandomPos(TiledMap map, Rectangle charRect) {
        ArrayList<Node> cells = new ArrayList<Node>();
        TiledMapTileLayer background = (TiledMapTileLayer) map.getLayers().get("background");
        for(int i=0;i<background.getWidth();i++){
            for(int j =0;j<background.getHeight();j++)
            {
                Cell cell = background.getCell(i,j);
                if(cell !=null){
                    cells.add(new Node(i,j));
                }
            }
        }

        Random random = new Random();

        Node pos =  cells.get(random.nextInt(cells.size()));
        charRect.x = pos.getX()*utils.getTilesize();
        charRect.y = pos.getY()*utils.getTilesize();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        if(!isGameEnded){
            MOVES move = getMove();
            if(move!=null) {
                robber.update(move);
                cop.update(robber);
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
        batch.draw(copImg, cop.getX(), cop.getY(), cop.getWidth(), cop.getHeight());
        batch.end();

    }

    private MOVES getMove() {
        MOVES move = null;
        if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
            move = MOVES.LEFT;
        }
        else if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
            move = MOVES.RIGHT;
        }
        else if (Gdx.input.isKeyJustPressed(Keys.UP)) {
            move = MOVES.UP;
        }
        else if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
            move = MOVES.DOWN;
        }
        return move;
    }




    private void endGame() {
        isGameEnded = true;
        System.out.println("Game Endu");
    }

    @Override
    public void dispose() {
        robberImg.dispose();
        batch.dispose();

    }


    public enum MOVES {LEFT, RIGHT, UP, DOWN}
}
