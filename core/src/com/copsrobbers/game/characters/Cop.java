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
        for (LinkedList<Integer> path : paths) {
            for (Integer pos : path) {
                adj.get(pos).clear();
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

    public LinkedList<Integer> update(Robber robber, LinkedList<LinkedList<Integer>> paths) {
        LinkedList<Integer> path = new LinkedList<>();
        path.addAll(catchRobber(robber, paths));
        if (!freeze && path.size() - 2 >= 0) {
            int x = path.get(path.size() - 2) / this.getMapManager().getColumnTileCount();
            int y = path.get(path.size() - 2) % this.getMapManager().getColumnTileCount();
            x = x * this.getMapManager().getTileWidth();
            y = y * this.getMapManager().getTileHeight();
            if (x == robber.getX() && y == robber.getY()) {
                gl.endGame();
            } else {
                this.setX(x);
                this.setY(y);
            }
        }
        unFreezeCop();
        return path;
    }


}
