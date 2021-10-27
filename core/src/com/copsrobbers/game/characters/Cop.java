package com.copsrobbers.game.characters;

import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.algorithm.GraphManager;
import com.copsrobbers.game.listeners.GameListener;

import java.util.ArrayList;
import java.util.LinkedList;


public class Cop extends Character {
GameListener gl;
    public Cop(Rectangle bounds, GameListener gl){
        super(bounds);
        this.gl = gl;


    }

    private LinkedList<Integer> catchRobber( Robber robber){
        GraphManager G = new GraphManager();
        ArrayList<ArrayList<Integer>> adj = G.generateGraph();
        int copPos = Integer.parseInt((int)(this.x /this.width )+ "" + (int)(this.y/this.height));
        int robberPos = Integer.parseInt((int) Math.floor(robber.x / robber.width) + "" + (int) Math.floor(robber.y / robber.height));

        LinkedList<Integer> path = G.printShortestDistance(adj,copPos,robberPos);

        return path;
    }
    public void update(Robber robber) {

        LinkedList<Integer> path = catchRobber(robber);

        int x = (int) (path.get(path.size()-2)/ (this.utils.getMapWidth()/this.utils.getTilesize()));
        int y = (int) (path.get(path.size()-2)%( this.utils.getMapWidth()/this.utils.getTilesize()));

        this.x = x* this.width;
        this.y = y*this.height;
        if(path.size()-2<=0){
            gl.endGame();
        }

    }


}
