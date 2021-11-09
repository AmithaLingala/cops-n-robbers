package com.copsrobbers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.copsrobbers.game.algorithm.CellModel;
import com.copsrobbers.game.algorithm.LevelGenerator;
import com.copsrobbers.game.items.Item;
import com.copsrobbers.game.overlay.TiledMapActor;

import java.util.ArrayList;

public class MapManager {
    private static MapManager instance;
    private final ArrayList<Item> items;
    private TiledMap map;
    private float tileSize;
    private float screenWidth;
    private float screenHeight;
    private float mapWidth;
    private float mapHeight;
    private Rectangle gate;
    private int weaponCount = 0;

    public MapManager() {
        items = new ArrayList<>();
    }

    public static MapManager initialize() {
        MapManager.instance = new MapManager();
        return MapManager.instance;
    }

    public static MapManager obtain() {
        return MapManager.instance;
    }

    public int getWeaponCount() {
        return weaponCount;
    }

    public void setWeaponCount(int weaponCount) {
        this.weaponCount = weaponCount;
    }

    public void init(TiledMap map) {

        this.setMap(map);
        this.setScreenWidth(Gdx.graphics.getWidth());
        this.setScreenHeight(Gdx.graphics.getHeight());
        TiledMapTileLayer layer = getLayer(Layers.BACKGROUND);
        this.setTileSize(layer.getTileWidth());
        this.setMapWidth(layer.getWidth());
        this.setMapHeight(layer.getHeight());

    }

    public TiledMap generate(int levelNumber) {
        int tileWidth = 32;
        int tileHeight = 32;
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        CellModel[][] cells = new CellModel[screenWidth / tileWidth][screenHeight / tileHeight];
        LevelGenerator level = new LevelGenerator(cells);
        level.generate(levelNumber);
        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer background = new TiledMapTileLayer(screenWidth, screenHeight, tileWidth, tileHeight);
        TiledMapTileLayer walls = new TiledMapTileLayer(screenWidth, screenHeight, tileWidth, tileHeight);
        TiledMapTileLayer obstacles = new TiledMapTileLayer(screenWidth, screenHeight, tileWidth, tileHeight);

        background.setName(Layers.BACKGROUND.getType());
        walls.setName(Layers.WALLS.getType());
        obstacles.setName(Layers.OBSTACLES.getType());

        Texture tiles = new Texture(Gdx.files.internal("textures.png"));
        TextureRegion[][] textureRegions = TextureRegion.split(tiles, tileWidth, tileHeight);
        Rectangle gate = new Rectangle();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                if (cells[i][j].isWall()) {
                    cell.setTile(new StaticTiledMapTile(textureRegions[0][0]));
                    walls.setCell(i, j, cell);
                } else if (cells[i][j].isBox()) {
                    cell.setTile(new StaticTiledMapTile(textureRegions[0][2]));
                    obstacles.setCell(i, j, cell);
                } else {
                    if (cells[i][j].isGate()) {
                        gate.x = i;
                        gate.y = j;
                    }
                    cell.setTile(new StaticTiledMapTile(textureRegions[0][1]));
                    background.setCell(i, j, cell);
                }

            }
        }
        layers.add(background);
        layers.add(walls);
        layers.add(obstacles);
        init(map);
        setGate(gate);
        return map;
    }

    public TiledMap getMap() {
        return map;
    }

    private void setMap(TiledMap map) {
        this.map = map;
    }

    public float getTileSize() {
        return tileSize;
    }

    private void setTileSize(float tileSize) {
        this.tileSize = tileSize;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    private void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    private void setScreenHeight(float screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getMapWidth() {
        return mapWidth;
    }

    private void setMapWidth(float mapWidth) {
        this.mapWidth = mapWidth;
    }

    public float getMapHeight() {
        return mapHeight;
    }

    private void setMapHeight(float mapHeight) {
        this.mapHeight = mapHeight;
    }

    public Rectangle getGate() {
        return gate;
    }

    public void setGate(Rectangle gate) {
        this.gate = gate;
        this.gate.x *= tileSize;
        this.gate.y *= tileSize;
    }

    public int getScore() {

        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbersV1.class.getName());
        return prefs.getInteger("CurrScore", 0);
    }

    public void setScore(int score) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbersV1.class.getName());
        prefs.putInteger("CurrScore", score);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public boolean hasWall(int x, int y) {
        TiledMapTileLayer walls = getLayer(Layers.WALLS);
        return walls.getCell(x, y) != null;
    }

    public boolean hasObstacle(int x, int y) {
        TiledMapTileLayer obstacles = getLayer(Layers.OBSTACLES);
        return obstacles.getCell(x, y) != null;
    }

    public boolean canMove(int x, int y) {
        return !(hasWall(x, y) || hasObstacle(x, y));
    }

    public void updateScore(int score) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbersV1.class.getName());
        int curScore = this.getScore();
        curScore = curScore + score;
        this.setScore(curScore);
        int maxScore = prefs.getInteger("MaxScore", 0);
        if (maxScore < curScore) {
            prefs.putInteger("MaxScore", curScore);
        }
    }

    public void updateWeaponCount(int count) {
        weaponCount += count;
        setWeaponCount(weaponCount);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void highlightTile(CellModel tile, boolean highlight) {
        TiledMapTileLayer obstacle = getLayer(Layers.OBSTACLES);
        TiledMapTileLayer background = getLayer(Layers.BACKGROUND);
        Texture tiles = new Texture(Gdx.files.internal("textures.png"));
        TextureRegion[][] textureRegions = TextureRegion.split(tiles, (int) getTileSize(), (int) getTileSize());
        int row = highlight ? 1 : 0;
        if (!tile.isWall() && !tile.isBox()) {
            TiledMapTileLayer.Cell cell = background.getCell(tile.getRow(), tile.getColumn());
            cell.setTile(new StaticTiledMapTile(textureRegions[row][1]));
        } else if (tile.isBox()) {
            TiledMapTileLayer.Cell cell = obstacle.getCell(tile.getRow(), tile.getColumn());
            cell.setTile(new StaticTiledMapTile(textureRegions[row][2]));
        }
    }

    public void updateTileType(CellModel tile, Layers layer) {
        if(tile.isWall()){
            return;
        }
        TiledMapTileLayer tileLayer = getLayer(tile);
        tileLayer.setCell(tile.getRow(), tile.getColumn(), null);
        TiledMapTileLayer newLayer = getLayer(layer);
        Texture tiles = new Texture(Gdx.files.internal("textures.png"));
        TextureRegion[][] textureRegions = TextureRegion.split(tiles, 32, 32);
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(new StaticTiledMapTile(textureRegions[0][layer.ordinal()]));
        newLayer.setCell(tile.getRow(), tile.getColumn(), cell);

    }

    public TiledMapTileLayer.Cell getCell(int x, int y, String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        return layer.getCell(x, y);
    }

    public TiledMapTileLayer.Cell getCell(int x, int y) {
        for (MapLayer layer : map.getLayers()) {
            TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
            if (tileLayer.getCell(x, y) != null) {
                return tileLayer.getCell(x, y);
            }
        }
        return null;
    }

    public TiledMapTileLayer getLayer(String layerName) {
        return (TiledMapTileLayer) map.getLayers().get(layerName);
    }

    public TiledMapTileLayer getLayer(Layers layer) {
        return getLayer(layer.getType());
    }

    public TiledMapTileLayer getLayer(CellModel cell) {
        if (cell.isWall()) {
            return getLayer(Layers.WALLS);
        }
        if (cell.isBox()) {
            return getLayer(Layers.OBSTACLES);
        }
        return getLayer(Layers.BACKGROUND);
    }

    public TiledMapActor setEvent(int x, int y, TiledMapTileLayer tiledLayer, Stage stage, ClickListener cl) {
        TiledMapTileLayer.Cell cell = getCell(x, y);
        TiledMapActor actor = new TiledMapActor(map, tiledLayer, cell);
        actor.setBounds(x * tiledLayer.getTileWidth(), y * tiledLayer.getTileHeight(), tiledLayer.getTileWidth(),
                tiledLayer.getTileHeight());
        stage.addActor(actor);
        actor.addListener(cl);
        return actor;
    }

    public enum Layers {
        WALLS("walls"), BACKGROUND("background"), OBSTACLES("obstacles");
        private final String type;

        Layers(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
