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
import com.copsrobbers.game.algorithm.Node;
import com.copsrobbers.game.characters.Cop;
import com.copsrobbers.game.characters.Robber;
import com.copsrobbers.game.items.Item;
import com.copsrobbers.game.ui.TiledMapActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage map related functionalities
 */
public class MapManager {
    private static MapManager instance;
    private final ArrayList<Item> items;
    private final List<Cop> cops;
    private final int textureSize = 32;
    private final TiledMapTileSet tileSet;
    private Robber robber;
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

    public MapManager() {
        this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * Constructor
     * @param screenWidth Device screen width
     * @param screenHeight Device screen height
     */
    public MapManager(int screenWidth, int screenHeight) {
        items = new ArrayList<>();
        cops = new ArrayList<>();
        int prefRows = 20;
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

    /**
     * Method to create MapManager instance with given width and height
     * @param width width of the screen
     * @param height height of the screen
     * @return returns MapManager instance with given width and height
     */
    public static MapManager initialize(int width, int height) {
        MapManager.instance = new MapManager(width, height);
        return MapManager.instance;
    }

    /**
     * Method to obtain MapManager instance
     * @return returns MapManager instance
     */
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

    /**
     * Method to generate Tilemap from the given grid of cells
     * @param cells cells grid with position and type
     * @return Tilemap with given grid of cells
     */
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

    /**
     * Method to check if there is wall in the given position
     * @param x x coordinate
     * @param y y coordinate
     * @return returns true if there is a wall else returns false
     */
    public boolean hasWall(int x, int y) {
        TiledMapTileLayer walls = getLayer(Layers.WALLS);
        return walls.getCell(x, y) != null;
    }

    /**
     * Method to check if there is Obstacle in the given position
     * @param x x coordinate
     * @param y y coordinate
     * @return returns true if there is a Obstacle else returns false
     */
    public boolean hasObstacle(int x, int y) {
        TiledMapTileLayer obstacles = getLayer(Layers.OBSTACLES);
        return obstacles.getCell(x, y) != null;
    }

    /**
     * Method to check if movement is available in a given position
     * @param x x coordinate
     * @param y y coordinate
     * @return returns true if cell is empty else returns false
     */
    public boolean canMove(int x, int y) {
        if ((x < 0 || x > getRowTileCount() || y < 0 || y > getColumnTileCount())) {
            return false;
        }
        return !(hasWall(x, y) || hasObstacle(x, y));
    }

    /**
     * Method to check if there is cop in the given position
     * @param x x coordinate
     * @param y y coordinate
     * @return returns true if there is a cop else returns false
     */
    public boolean hasCop(int x, int y) {
        for (Cop cop : cops) {
            if (cop.getX() == x * this.getTileWidth() && cop.getY() == y * this.getTileHeight()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Method to check if there is item in the given position
     * @param x x coordinate
     * @param y y coordinate
     * @return returns true if there is a item else returns false
     */
    public boolean hasItem(int x, int y) {
        for (Item item : items) {
            if (item.getX() == x * this.getTileWidth() && item.getY() == y * this.getTileHeight()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Method to check if there is robber in the given position
     * @param x x coordinate
     * @param y y coordinate
     * @return returns true if there is a robber else returns false
     */
    public boolean hasRobber(int x, int y) {
        if (robber == null)
            return false;
        return (robber.getX() == x * this.getTileWidth() && robber.getY() == y * this.getTileHeight());

    }
    /**
     * Method to check if there cell is occupied
     * @param x x coordinate
     * @param y y coordinate
     * @return returns true if there is occupied else returns false
     */
    public boolean isOccupied(int x, int y) {
        return (!canMove(x, y) || hasCop(x, y) || hasItem(x, y) || hasRobber(x, y));
    }

    /**
     * Method to add item to the items list
     * @param item Item
     */
    public void addItem(Item item) {
        items.add(item);
    }

    public List<Cop> getCops() {
        return cops;
    }

    /**
     * Method to convert given position to index of graph
     * @param x x coordinate
     * @param y y coordinate
     * @return index for the given position
     */
    public Integer convertPosToIndex(float x, float y) {
        int Ix = (int) (x / this.getTextureSize());
        int Iy = (int) (y / this.getTextureSize());
        return (Ix * this.getColumnTileCount()) + Iy;
    }

    /**
     * Method to convert given index to position on map
     * @param pos index of the graph
     * @return position from the given index
     */
    public Node convertIndexToPos(int pos) {
        int x = pos / this.getColumnTileCount();
        int y = pos % this.getColumnTileCount();
        x = x * this.getTileWidth();
        y = y * this.getTileHeight();
        return new Node(x,y);
    }

    /**
     * Method to add cop to the cops list
     * @param cop Cop
     */
    public void addCop(Cop cop) {
        cops.add(cop);
    }


    public Robber getRobber() {
        return robber;
    }

    public void setRobber(Robber robber) {
        this.robber = robber;
    }

    /**
     * Method to highlight/unhighlight tile on the map
     * @param tile tile to highlight/unhighlight
     * @param highlight true to highlight and false to unhighlight
     */

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

    /**
     * Method to update Tile type
     * @param tile tile to update type
     * @param layer tile layer
     */
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

    /**
     * Method to get cell based on position and layer name
     * @param x x coordinate
     * @param y y coordinate
     * @param layerName layer name
     * @return returns cell in given layer at given position
     */
    public TiledMapTileLayer.Cell getCell(int x, int y, String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        return layer.getCell(x, y);
    }

    /**
     * Method to get cell based on position
     * @param x x coordinate
     * @param y y coordinate
     * @return returns cell in given position
     */
    public TiledMapTileLayer.Cell getCell(int x, int y) {
        for (MapLayer layer : map.getLayers()) {
            TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
            if (tileLayer.getCell(x, y) != null) {
                return tileLayer.getCell(x, y);
            }
        }
        return null;
    }

    /**
     * Method to get map layer by layer name
     * @param layerName layer name
     * @return returns map layer
     */
    public TiledMapTileLayer getLayer(String layerName) {
        return (TiledMapTileLayer) map.getLayers().get(layerName);
    }
    /**
     * Method to get tiled map layer by layer
     * @param layer Layer
     * @return returns map layer
     */
    public TiledMapTileLayer getLayer(Layers layer) {
        return getLayer(layer.getType());
    }
    /**
     * Method to get tiled map layer by cell
     * @param cell CellModel cell
     * @return returns map layer
     */
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

    public void reset() {
        instance.getCops().clear();
        instance.getItems().clear();
        instance.setRobber(null);
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
