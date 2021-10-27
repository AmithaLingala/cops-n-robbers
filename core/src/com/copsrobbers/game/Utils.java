package com.copsrobbers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Utils {
    private TiledMap map;
    private float tilesize;
    private  float screenWidth;

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

    private float screenHeight;
    private float mapWidth;
    private float mapHeight;
    private static  Utils instance;
    private Utils(){

    }

    public static Utils init(TiledMap map){
        if(instance!=null) {
            return instance;
        }

        instance = new Utils();
        instance.map = map;
        instance.setScreenWidth(Gdx.graphics.getWidth());
        instance.setScreenHeight( Gdx.graphics.getHeight());

        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("background");
        instance.setTilesize(layer.getTileWidth());
        instance.setMapWidth(layer.getWidth());
        instance.setMapHeight(layer.getHeight());
       // instance.setTilesize(instance.tilesize*instance.scale);
        return instance;
    }
    public boolean hasWall(int x, int y){
        TiledMapTileLayer walls = (TiledMapTileLayer)map.getLayers().get("walls");
        return walls.getCell(x,y)!=null;

    }

    public static Utils obtain(){
        return Utils.instance;
    }
}
