package com.copsrobbers.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.algorithm.GraphManager;
import com.copsrobbers.game.listeners.GameListener;

import java.util.ArrayList;
import java.util.LinkedList;


public class Cop extends Character {
private final GameListener gl;
private boolean freeze = false;
    public Cop(Rectangle bounds, GameListener gl){
        super(bounds);
        this.gl = gl;
        Texture copImg = new Texture(Gdx.files.internal("cop_walk.png"));
        setCharImg(copImg);
    }

    public void freezeCop(){
        this.freeze = true;
        Texture copImg = new Texture(Gdx.files.internal("cop_frozen.png"));
        setCharImg(copImg);
    }
    public void unFreezeCop(){
        this.freeze = false;
        Texture copImg = new Texture(Gdx.files.internal("cop_walk.png"));
        setCharImg(copImg);
    }

    private LinkedList<Integer> catchRobber( Robber robber){
        GraphManager G = new GraphManager();
        ArrayList<ArrayList<Integer>> adj = G.generateGraph();

        int x = (int)this.getX() / this.getMapManager().getTextureSize();
        int y = (int)this.getY() / this.getMapManager().getTextureSize();
        int copPos = (x * this.getMapManager().getColumnTileCount()) + y;

        int rx = (int)robber.getX() / this.getMapManager().getTextureSize();
        int ry = (int)robber.getY() / this.getMapManager().getTextureSize();
        int robberPos = (rx * this.getMapManager().getColumnTileCount()) + ry;

        return G.printShortestDistance(adj,copPos,robberPos);
    }
    public void update(Robber robber) {
        if(!freeze) {
            LinkedList<Integer> path = catchRobber(robber);
            if (path.size() - 2 <= 0) {
                gl.endGame();
            } else {
                int x = path.get(path.size() - 2) / this.getMapManager().getColumnTileCount();
                int y = path.get(path.size() - 2) % this.getMapManager().getColumnTileCount();

                this.setX(x * this.getMapManager().getTileWidth());
                this.setY(y * this.getMapManager().getTileHeight());
            }
        }
        unFreezeCop();
    }


}
