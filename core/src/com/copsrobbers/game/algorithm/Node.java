package com.copsrobbers.game.algorithm;

/**
 * General purpose class for accessing cell coordinates.
 * */
public class Node {
    private final int x;
    private final int y;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
