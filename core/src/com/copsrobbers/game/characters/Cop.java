package com.copsrobbers.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.algorithm.GraphManager;
import com.copsrobbers.game.algorithm.Node;
import com.copsrobbers.game.listeners.GameListener;
import com.copsrobbers.game.managers.MapManager;

import java.util.ArrayList;
import java.util.LinkedList;


public class Cop extends Character {
    private final GameListener gl;
    private boolean freeze = false;
    private int dist = 0;

    public Cop(Rectangle bounds, GameListener gl) {
        super(bounds);
        this.gl = gl;
        Texture copImg = new Texture(Gdx.files.internal("cop_walk.png"));
        setCharImg(copImg);
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
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

    public boolean canReachRobber(Robber robber) {
        LinkedList<Integer> path = new LinkedList<>(catchRobber(robber, new LinkedList<>()));
        return path.size() > 0;
    }

    private LinkedList<Integer> catchRobber(Robber robber, LinkedList<LinkedList<Integer>> paths) {
        GraphManager G = new GraphManager();
        ArrayList<ArrayList<Integer>> adj = G.generateGraph();
        for (LinkedList<Integer> path : paths) {
            for (int i = 0; i < path.size(); i++) {
                adj.get(path.get(i)).clear();
            }
        }

        int copPos = this.getMapManager().convertPosToIndex(this.getX(),this.getY());
        int robberPos = this.getMapManager().convertPosToIndex(robber.getX(), robber.getY());

        return G.printShortestDistance(adj, copPos, robberPos);
    }

    public LinkedList<Integer> update(Robber robber, LinkedList<LinkedList<Integer>> oldPaths) {
        LinkedList<Integer> path = new LinkedList<>();
        if (freeze) {
            unFreezeCop();
            int curPos = this.getMapManager().convertPosToIndex(this.getX(),this.getY());
            path.add(curPos);
            return path;
        }
        if (this.getX() == robber.getX() && this.getY() == robber.getY()) {
            gl.endGame();
            return new LinkedList<>();
        }
        path.addAll(catchRobber(robber, oldPaths));
        if (path.size() == 0) {
            path = new LinkedList<>(catchRobber(robber, new LinkedList<>()));
        }
        if (path.size() - 2 >= 0) {
            Node pos = this.getMapManager().convertIndexToPos(path.get(path.size() - 2));
            if (pos.getX() == robber.getX() && pos.getY() == robber.getY()) {
                gl.endGame();
            } else {
                this.setX(pos.getX());
                this.setY(pos.getY());
                double xPow = Math.pow(robber.getX() - this.getX(), 2);
                double yPow = Math.pow(robber.getY() - this.getY(), 2);
                this.setDist((int) Math.sqrt(xPow + yPow));
            }
        }
        return path;
    }
}
