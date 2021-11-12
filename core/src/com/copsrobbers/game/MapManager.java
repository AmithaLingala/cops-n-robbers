package com.copsrobbers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
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
    private int tileWidth;
    private int tileHeight;
    private int screenWidth;
    private int screenHeight;
    private float mapWidth;
    private float mapHeight;
    private Rectangle gate;
    private int rowTileCount;
    private int columnTileCount;
    private int texturesize = 32;

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public int getTexturesize() {
        return texturesize;
    }

    public int getRowTileCount() {
        return rowTileCount;
    }

    public void setRowTileCount(int rowTileCount) {
        this.rowTileCount = rowTileCount;
    }

    public int getColumnTileCount() {
        return columnTileCount;
    }

    public void setColumnTileCount(int columnTileCount) {
        this.columnTileCount = columnTileCount;
    }

    public TiledMap getMap() {
        return map;
    }

    private void setMap(TiledMap map) {
        this.map = map;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    private void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    private void setScreenHeight(int screenHeight) {
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
        this.gate.x *= tileWidth;
        this.gate.y *= tileHeight;
    }

    public MapManager() {
        items = new ArrayList<>();
        this.setRowTileCount(30);
        this.setColumnTileCount(30);
        this.setScreenWidth(Gdx.graphics.getWidth());
        this.setScreenHeight(Gdx.graphics.getHeight());
        this.setTileHeight((int)getScreenHeight()/getColumnTileCount());
        this.setTileWidth((int)getScreenWidth()/getRowTileCount());
    }

    public static MapManager initialize() {
        MapManager.instance = new MapManager();
        return MapManager.instance;
    }

    public static MapManager obtain() {
        return MapManager.instance;
    }


    public void init(TiledMap map) {

        this.setMap(map);

        TiledMapTileLayer layer = getLayer(Layers.BACKGROUND);
        this.setMapWidth(layer.getWidth());
        this.setMapHeight(layer.getHeight());

    }

    public TiledMap generate(int levelNumber) {

        CellModel[][] cells = new CellModel[getRowTileCount()][getColumnTileCount()];
        LevelGenerator level = new LevelGenerator(cells);
        level.generate(levelNumber);
        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer background = new TiledMapTileLayer(getScreenWidth(), getScreenHeight(), getTileWidth(), getTileHeight());
        TiledMapTileLayer walls = new TiledMapTileLayer(getScreenWidth(), getScreenHeight(), getTileWidth(), getTileHeight());
        TiledMapTileLayer obstacles = new TiledMapTileLayer(getScreenWidth(), getScreenHeight(), getTileWidth(), getTileHeight());

        background.setName(Layers.BACKGROUND.getType());
        walls.setName(Layers.WALLS.getType());
        obstacles.setName(Layers.OBSTACLES.getType());

        Texture tiles = new Texture(Gdx.files.internal("textures.png"));
        TextureRegion[][] textureRegions = TextureRegion.split(tiles, getTexturesize(), getTexturesize());
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


    public void addItem(Item item) {
        items.add(item);
    }

    public void highlightTile(CellModel tile, boolean highlight) {
        TiledMapTileLayer obstacle = getLayer(Layers.OBSTACLES);
        TiledMapTileLayer background = getLayer(Layers.BACKGROUND);
        Texture tiles = new Texture(Gdx.files.internal("textures.png"));
        TextureRegion[][] textureRegions = TextureRegion.split(tiles, getTexturesize(),getTexturesize());
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
        if (tile.isWall()) {
            return;
        }
        TiledMapTileLayer tileLayer = getLayer(tile);
        tileLayer.setCell(tile.getRow(), tile.getColumn(), null);
        TiledMapTileLayer newLayer = getLayer(layer);
        Texture tiles = new Texture(Gdx.files.internal("textures.png"));
        TextureRegion[][] textureRegions = TextureRegion.split(tiles, getTexturesize(), getTexturesize());
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
