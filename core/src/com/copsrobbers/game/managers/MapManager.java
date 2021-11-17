package com.copsrobbers.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.copsrobbers.game.algorithm.CellModel;
import com.copsrobbers.game.algorithm.LevelGenerator;
import com.copsrobbers.game.characters.Cop;
import com.copsrobbers.game.characters.Robber;
import com.copsrobbers.game.items.Item;
import com.copsrobbers.game.ui.TiledMapActor;

import java.util.ArrayList;
import java.util.List;

public class MapManager {
    private static MapManager instance;
    private final ArrayList<Item> items;
    private final List<Cop> cops;
    private  Robber robber;
    private final int textureSize = 32;
    private final TiledMapTileSet tileSet;
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

    public  MapManager(){
        this(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    }
    public MapManager(int screenWidth, int screenHeight) {
        items = new ArrayList<>();
        cops = new ArrayList<>();
        int prefRows = 25; //TODO Provide a better scaling, may be floats would do the trick
        int scale = screenWidth / textureSize / prefRows;
        if (scale == 0) {
            scale = 1;
        }
        this.setScreenWidth(screenWidth / scale);
        this.setScreenHeight(screenHeight / scale);
        this.setRowTileCount(getScreenWidth() / textureSize);
        this.setColumnTileCount(getScreenHeight() / textureSize);

        this.setTileHeight(getScreenHeight() / getColumnTileCount());
        this.setTileWidth(getScreenWidth() / getRowTileCount());

        Texture texture = new Texture(Gdx.files.internal("textures.png"));

        TextureRegion[][] splitTiles = TextureRegion.split(texture, getTextureSize(), getTextureSize());

        tileSet = new TiledMapTileSet();
        int tid = 0;
        for (TextureRegion[] splitTile : splitTiles) {
            for (TextureRegion textureRegion : splitTile) {
                final StaticTiledMapTile tile = new StaticTiledMapTile(textureRegion);
                tile.setId(tid++);
                tileSet.putTile(tile.getId(), tile);
            }
        }
    }

    public static MapManager initialize(int width, int height) {
        MapManager.instance = new MapManager(width, height);
        return MapManager.instance;
    }

    public static MapManager obtain() {
        return MapManager.instance;
    }

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

    public int getTextureSize() {
        return textureSize;
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

    public void init(TiledMap map) {

        this.setMap(map);

        TiledMapTileLayer layer = getLayer(Layers.BACKGROUND);
        this.setMapWidth(layer.getWidth());
        this.setMapHeight(layer.getHeight());

    }

    public TiledMap generate(CellModel[][] cells) {

        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer background = new TiledMapTileLayer(getScreenWidth(), getScreenHeight(), getTileWidth(), getTileHeight());
        TiledMapTileLayer walls = new TiledMapTileLayer(getScreenWidth(), getScreenHeight(), getTileWidth(), getTileHeight());
        TiledMapTileLayer obstacles = new TiledMapTileLayer(getScreenWidth(), getScreenHeight(), getTileWidth(), getTileHeight());

        background.setName(Layers.BACKGROUND.getType());
        walls.setName(Layers.WALLS.getType());
        obstacles.setName(Layers.OBSTACLES.getType());


        Rectangle gate = new Rectangle();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                if (cells[i][j].isWall()) {
                    cell.setTile(tileSet.getTile(Layers.WALLS.ordinal()));
                    walls.setCell(i, j, cell);
                } else if (cells[i][j].isBox()) {
                    cell.setTile(tileSet.getTile(Layers.OBSTACLES.ordinal()));
                    obstacles.setCell(i, j, cell);
                } else {
                    if (cells[i][j].isGate()) {
                        gate.x = i;
                        gate.y = j;
                    }
                    cell.setTile(tileSet.getTile(Layers.BACKGROUND.ordinal()));
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
        if((x<0||x>getRowTileCount()||y<0||y>getColumnTileCount())){return false;}
        return !(hasWall(x, y) || hasObstacle(x, y));
    }

    public boolean hasCop(int x, int y) {
        for (Cop cop : cops) {
            if (cop.getX() == x * this.getTileWidth() && cop.getY() == y * this.getTileHeight()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasItem(int x, int y) {
        for (Item item : items) {
            if (item.getX() == x * this.getTileWidth() && item.getY() == y * this.getTileHeight()) {
                return true;
            }
        }
        return false;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Cop> getCops() {
        return cops;
    }

    public void addCop(Cop cop) {
        cops.add(cop);
    }


    public Robber getRobber() {
        return robber;
    }

    public void setRobber(Robber robber) {
        this.robber = robber;
    }


    public void highlightTile(CellModel tile, boolean highlight) {
        TiledMapTileLayer obstacle = getLayer(Layers.OBSTACLES);
        TiledMapTileLayer background = getLayer(Layers.BACKGROUND);
        int offset = highlight ? tileSet.size() / 2 : 0;
        if (!tile.isWall() && !tile.isBox()) {
            TiledMapTileLayer.Cell cell = background.getCell(tile.getRow(), tile.getColumn());
            cell.setTile(tileSet.getTile(Layers.BACKGROUND.ordinal() + offset));
        } else if (tile.isBox()) {
            TiledMapTileLayer.Cell cell = obstacle.getCell(tile.getRow(), tile.getColumn());
            cell.setTile(tileSet.getTile(Layers.OBSTACLES.ordinal() + offset));
        }
    }

    public void updateTileType(CellModel tile, Layers layer) {
        if (tile.isWall()) {
            return;
        }
        TiledMapTileLayer tileLayer = getLayer(tile);
        tileLayer.setCell(tile.getRow(), tile.getColumn(), null);
        TiledMapTileLayer newLayer = getLayer(layer);

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tileSet.getTile(layer.ordinal()));
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
