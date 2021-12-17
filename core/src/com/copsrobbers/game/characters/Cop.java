package com.copsrobbers.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.algorithm.GraphManager;
import com.copsrobbers.game.algorithm.Node;
import com.copsrobbers.game.listeners.GameListener;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class for the Cop inherited from Character class
 */

public class Cop extends Character {
    private final GameListener gl;
    private boolean freeze = false;
    private int dist = 0;

    /**
     * Constructor
     * @param bounds Rectangle for cop position and size.
     * @param gl game listener
     */
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

    /**
     * Method to Freeze cop
     */
    public void freezeCop() {
        this.freeze = true;
        Texture copImg = new Texture(Gdx.files.internal("cop_frozen.png"));
        setCharImg(copImg);
    }

    /**
     * Method to unfreeze cop
     */
    public void unFreezeCop() {
        this.freeze = false;
        Texture copImg = new Texture(Gdx.files.internal("cop_walk.png"));
        setCharImg(copImg);
    }

    /**
     * Method to check if there is path between cop and robber
     * @param robber Robber
     * @return returns true if there is a path else returns false
     */
    public boolean canReachRobber(Robber robber) {
        LinkedList<Integer> path = new LinkedList<>(catchRobber(robber, new LinkedList<>()));
        return path.size() > 0;
    }

    /**
     * Method to catch robber
     * @param robber robber
     * @param paths Graph to catch the robber
     * @return returns the shortest path to catch the robber
     */
    private LinkedList<Integer> catchRobber(Robber robber, LinkedList<LinkedList<Integer>> paths) {
        GraphManager G = new GraphManager();
        ArrayList<ArrayList<Integer>> adj = G.generateGraph();
        for (LinkedList<Integer> path : paths) {
            for (int i = 0; i < path.size(); i++) {
                adj.get(path.get(i)).clear();
            }
        }

        int copPos = this.getMapManager().convertPosToIndex(this.getX(), this.getY());
        int robberPos = this.getMapManager().convertPosToIndex(robber.getX(), robber.getY());

        return G.findShortestDistance(adj, copPos, robberPos);
    }

    /**
     * Method to update cop's position on the map
     * @param robber Robber
     * @param oldPaths Graph to search robber
     * @return returns the path from cop to robber
     */
    public LinkedList<Integer> update(Robber robber, LinkedList<LinkedList<Integer>> oldPaths) {
        LinkedList<Integer> path = new LinkedList<>();
        if (freeze) {
            unFreezeCop();
            int curPos = this.getMapManager().convertPosToIndex(this.getX(), this.getY());
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
