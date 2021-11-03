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
        int copPos = (int) (((this.x /this.width ) * (this.utils.getMapWidth()/this.utils.getTilesize())) + (this.y/this.height));
        int robberPos = (int) (((robber.x /robber.width ) * (this.utils.getMapWidth()/this.utils.getTilesize())) + (robber.y/robber.height));

        return G.printShortestDistance(adj,copPos,robberPos);
    }
    public void update(Robber robber) {

        LinkedList<Integer> path = catchRobber(robber);
        if(path.size()-2<=0){
            gl.endGame();
        }
        else {
            int x = (int) (path.get(path.size() - 2) / (this.utils.getMapWidth() / this.utils.getTilesize()));
            int y = (int) (path.get(path.size() - 2) % (this.utils.getMapWidth() / this.utils.getTilesize()));

            this.x = x * this.width;
            this.y = y * this.height;
        }

    }


}
