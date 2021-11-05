package com.copsrobbers.game.characters;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.CopsAndRobbersV1;
import com.copsrobbers.game.items.Item;
import com.copsrobbers.game.listeners.GameListener;
import com.copsrobbers.game.listeners.LevelListener;
import com.copsrobbers.game.screens.GameScreen;

import java.util.ArrayList;

public class Robber extends Character {
    private LevelListener ll;

    public Robber(Rectangle bounds, LevelListener ll){
        super(bounds);
        this.ll = ll;

    }
    public void collectItem(){

    }

    @Override
    public void update(GameScreen.MOVES move) {
        super.update(move);
        ArrayList<Item> tempItems = new ArrayList<>();
        for (Item item: utils.getItems()) {
            if(item.isCollided(this)){
                item.collect();
                tempItems.add(item);
            }
        }
        for (Item item: tempItems) {
            utils.getItems().remove(item);
        }
        tryEscape();
    }
    public void tryEscape(){
        Rectangle gate = utils.getGate();
        if(gate.x == this.x && gate.y == this.y){
            ll.nextLevel();
        }
    }
}
