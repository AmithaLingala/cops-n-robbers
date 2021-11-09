package com.copsrobbers.game.characters;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.copsrobbers.game.MapManager;
import com.copsrobbers.game.algorithm.CellModel;
import com.copsrobbers.game.items.Item;
import com.copsrobbers.game.listeners.LevelListener;
import com.copsrobbers.game.overlay.TiledMapActor;
import com.copsrobbers.game.screens.GameScreen;

import java.util.ArrayList;

public class Robber extends Character {
    private LevelListener ll;
    ArrayList<CellModel> neighbours;
    ArrayList<TiledMapActor> actors;

    public Robber(Rectangle bounds, LevelListener ll){
        super(bounds);
        this.ll = ll;
        this.neighbours = new ArrayList<>();
        this.actors = new ArrayList<>();

    }
    public void collectItem(){

    }

    @Override
    public void update(GameScreen.MOVES move) {
        super.update(move);
        clearTargets();
        ArrayList<Item> tempItems = new ArrayList<>();
        for (Item item: mapManager.getItems()) {
            if(item.isCollided(this)){
                item.collect();
                tempItems.add(item);
            }
        }
        for (Item item: tempItems) {
            mapManager.getItems().remove(item);
        }
        tryEscape();
    }
    public void tryEscape(){
        Rectangle gate = mapManager.getGate();
        if(gate.x == this.x && gate.y == this.y){
            ll.nextLevel();
        }
    }

    public void highlightTargets(Stage stage) {
        clearTargets();
        int mapWidth = (int) mapManager.getMapWidth();
        int mapHeight = (int) mapManager.getMapHeight();
        int x = (int) (this.getX()/ mapManager.getTileSize());
        int y = (int) (this.getY()/ mapManager.getTileSize());

        if(x+1< mapWidth){
            neighbours.add(new CellModel((x + 1) , y));
        }
        if(x-1>=0){
            neighbours.add(new CellModel((x - 1) , y));
        }
        if(y+1<mapHeight){
            neighbours.add(new CellModel(x  , y+1));
        }
        if(y-1>=0){
            neighbours.add(new CellModel(x , y-1));
        }
        for(CellModel cell:neighbours) {
            updateCellType(cell);
            mapManager.highlightTile(cell,true);
            TiledMapActor actor = mapManager.setEvent(cell.getRow(),cell.getColumn(),mapManager.getLayer(cell),stage,new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    mapManager.updateTileType(cell, MapManager.Layers.BACKGROUND);
                    clearTargets();
                }
            });
            actors.add(actor);
        }

    }
    public void clearTargets(){
        for(CellModel cell:neighbours) {
            updateCellType(cell);
            mapManager.highlightTile(cell,false);
        }
        for (TiledMapActor actor:actors){
            actor.clear();
        }
        neighbours.clear();
        actors.clear();
    }
    private void updateCellType(CellModel cell){
        int x = cell.getRow();
        int y = cell.getColumn();
        cell.setBox(mapManager.hasObstacle(x,y));
        cell.setWall(mapManager.hasWall(x,y));
    }

}
