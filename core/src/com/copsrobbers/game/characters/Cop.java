package com.copsrobbers.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.algorithm.GraphManager;
import com.copsrobbers.game.listeners.GameListener;

import java.util.ArrayList;
import java.util.LinkedList;


public class Cop extends Character {
private GameListener gl;
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
        int copPos = (int) (((this.getX() /this.getWidth() ) * (this.getMapManager().getMapWidth()/this.getMapManager().getTileWidth())) + (this.getY()/this.getHeight()));
        int robberPos = (int) (((robber.getX() /robber.getWidth() ) * (this.getMapManager().getMapWidth()/this.getMapManager().getTileWidth())) + (robber.getY()/robber.getHeight()));

        return G.printShortestDistance(adj,copPos,robberPos);
    }
    public void update(Robber robber) {
        if(!freeze) {
            LinkedList<Integer> path = catchRobber(robber);
            if (path.size() - 2 <= 0) {
                gl.endGame();
            } else {
                int x = (int) (path.get(path.size() - 2) / (this.getMapManager().getMapWidth() / this.getMapManager().getTileWidth()));
                int y = (int) (path.get(path.size() - 2) % (this.getMapManager().getMapWidth() / this.getMapManager().getTileWidth()));

                this.setX(x * this.getWidth());
                this.setY(y * this.getHeight());
            }
        }
        unFreezeCop();
    }


}
