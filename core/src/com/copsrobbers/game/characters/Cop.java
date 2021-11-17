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

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    private int dist =0;

    public Cop(Rectangle bounds, GameListener gl) {
        super(bounds);
        this.gl = gl;
        Texture copImg = new Texture(Gdx.files.internal("cop_walk.png"));
        setCharImg(copImg);
    }

    public void freezeCop() {
        this.freeze = true;
        Texture copImg = new Texture(Gdx.files.internal("cop_frozen.png"));
        setCharImg(copImg);
    }

    public void unFreezeCop() {
        this.freeze = false;
        Texture copImg = new Texture(Gdx.files.internal("cop_walk.png"));
        setCharImg(copImg);
    }

    private LinkedList<Integer> catchRobber(Robber robber, LinkedList<LinkedList<Integer>> paths) {
        GraphManager G = new GraphManager();
        ArrayList<ArrayList<Integer>> adj = G.generateGraph();
        int block = Math.min(10, paths.size());
        for (LinkedList<Integer> path : paths) {
            for (int i = 0; i < path.size(); i++) {
                adj.get(path.get(i)).clear();
            }
        }

        int x = (int) this.getX() / this.getMapManager().getTextureSize();
        int y = (int) this.getY() / this.getMapManager().getTextureSize();
        int copPos = (x * this.getMapManager().getColumnTileCount()) + y;

        int rx = (int) robber.getX() / this.getMapManager().getTextureSize();
        int ry = (int) robber.getY() / this.getMapManager().getTextureSize();
        int robberPos = (rx * this.getMapManager().getColumnTileCount()) + ry;

        return G.printShortestDistance(adj, copPos, robberPos);
    }

    public LinkedList<Integer> update(Robber robber, LinkedList<LinkedList<Integer>> oldPaths) {
        if(this.getX() == robber.getX() && this.getY() == robber.getY()) {
            gl.endGame();
            return new LinkedList<>();
        }
        LinkedList<Integer> path = new LinkedList<>(catchRobber(robber, oldPaths));
        if(path.size()==0) {
            path = new LinkedList<>(catchRobber(robber, new LinkedList<>()));
        }
        if (!freeze && path.size()-2 >= 0) {
            int x = path.get(path.size() - 2) / this.getMapManager().getColumnTileCount();
            int y = path.get(path.size() - 2) % this.getMapManager().getColumnTileCount();
            x = x * this.getMapManager().getTileWidth();
            y = y * this.getMapManager().getTileHeight();
            if (x == robber.getX() && y == robber.getY()) {
                gl.endGame();
            } else {
                this.setX(x);
                this.setY(y);
                double xPow = Math.pow(robber.getX()- this.getX(),2);
                double yPow = Math.pow(robber.getY()- this.getY(),2);
                this.setDist((int) Math.sqrt( xPow + yPow));
            }
        }
        unFreezeCop();
        return path;
    }
}
