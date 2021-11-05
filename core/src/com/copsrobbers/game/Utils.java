package com.copsrobbers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.items.Item;

import java.util.ArrayList;

public class Utils {
    private static Utils instance;
    private TiledMap map;
    private float tilesize;
    private float screenWidth;
    private float screenHeight;
    private float mapWidth;
    private float mapHeight;
    private Rectangle gate;
    private int score = 0;
    private ArrayList<Item> items;

    private Utils() {
        items = new ArrayList<>();
    }

    public static Utils init(TiledMap map) {
//        if(instance!=null) {
//            return instance;
//        }

        instance = new Utils();
        instance.map = map;
        instance.setScreenWidth(Gdx.graphics.getWidth());
        instance.setScreenHeight(Gdx.graphics.getHeight());

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("background");
        instance.setTilesize(layer.getTileWidth());
        instance.setMapWidth(layer.getWidth());
        instance.setMapHeight(layer.getHeight());
        // instance.setTilesize(instance.tilesize*instance.scale);
        return instance;
    }

    public static Utils obtain() {
        return Utils.instance;
    }

    public TiledMap getMap() {
        return map;
    }

    private void setMap(TiledMap map) {
        this.map = map;
    }

    public float getTilesize() {
        return tilesize;
    }

    private void setTilesize(float tilesize) {
        this.tilesize = tilesize;
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
        this.gate.x *= tilesize;
        this.gate.y *= tilesize;
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

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public boolean hasWall(int x, int y) {
        TiledMapTileLayer walls = (TiledMapTileLayer) map.getLayers().get("walls");
        return walls.getCell(x, y) != null;

    }

    public void updateScore(int score) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbersV1.class.getName());
        int curScore = this.getScore();
        curScore = curScore + score;
        this.setScore(curScore);
        int maxScore = prefs.getInteger("MaxScore", 0);
        if(maxScore<curScore){
            prefs.putInteger("MaxScore", curScore);
        }
    }


    public void addItem(Item item) {
        items.add(item);
    }
}
