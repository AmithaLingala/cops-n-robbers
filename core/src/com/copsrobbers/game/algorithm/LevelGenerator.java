package com.copsrobbers.game.algorithm;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.characters.Cop;
import com.copsrobbers.game.characters.Robber;
import com.copsrobbers.game.items.Coin;
import com.copsrobbers.game.items.Weapon;
import com.copsrobbers.game.listeners.GameListener;
import com.copsrobbers.game.listeners.LevelListener;
import com.copsrobbers.game.managers.MapManager;
import com.copsrobbers.game.screens.GameScreen;

import java.util.ArrayList;
import java.util.Random;

public class LevelGenerator {

    private static final int[][] DIRECTIONS = { //distance of 2 to each side
            {0, -2}, // north
            {0, 2}, // south
            {2, 0}, // east
            {-2, 0}, // west
    };
    private final CellModel[][] cells;
    private final Random random;
    private final long delay = 0;
    private final MapManager mapManager;


    public LevelGenerator(CellModel[][] cells) {
        this.cells = cells;
        random = new Random();
        mapManager = MapManager.obtain();

    }

    public void generate(int level) {

        /* Start with a grid full of cellModelViews in state wall (not a path). */
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new CellModel(i, j, false);
            }
        }

        //Pick a random cell
        int threshold = 5;
        int prob = Math.min(level, threshold);
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                boolean flag = random.nextInt(100) < prob * 2;
                cells[i][j].setBox(flag);
            }
        }
        for (int i = 0; i < cells[0].length; i++) {
            cells[0][i].setWall(true);
            cells[cells.length - 1][i].setWall(true);
        }
        for (int i = 0; i < cells.length; i++) {
            cells[i][0].setWall(true);
            cells[i][cells[0].length - 1].setWall(true);
        }
        Rectangle gate = new Rectangle();
        gate.x = 1;
        gate.y = cells[0].length - 1;
        cells[(int) gate.x][(int) gate.y].setWall(false);
        cells[(int) gate.x][(int) gate.y].setGate(true);
        cells[(int) gate.x][(int) gate.y].setBox(false);
    }

    public void generateItems(int level) {
        int threshold = 5;
        int prob = Math.min(level, threshold);
        for (int i = 0; i < prob * 2; i++) {
            Rectangle coinRect = new Rectangle();
            setRandomPos(coinRect, GameScreen.AREA.values()[i % 5]);
            coinRect.width = mapManager.getTileWidth();
            coinRect.height = mapManager.getTileHeight();
            Coin coin = new Coin(coinRect);
            mapManager.addItem(coin);
        }
        prob = random.nextInt(prob) + 1;
        for (int i = 0; i < prob; i++) {
            Rectangle weaponRect = new Rectangle();
            setRandomPos(weaponRect, GameScreen.AREA.values()[(i + 2) % 5]);
            weaponRect.width = mapManager.getTileWidth();
            weaponRect.height = mapManager.getTileHeight();
            Weapon weapon = new Weapon(weaponRect);
            mapManager.addItem(weapon);
        }
    }

    public void generateCops(int copCount, GameListener gl) {
        for (int i = 0; i < copCount; i++) {
            Rectangle copRect = new Rectangle();
            setRandomPos(copRect, GameScreen.AREA.values()[i]);
            copRect.width = mapManager.getTileWidth();
            copRect.height = mapManager.getTileHeight();
            Cop cop = new Cop(copRect, gl);
            mapManager.addCop(cop);
        }
    }

    public void generateRobber(LevelListener ll) {
        Rectangle robberRect = new Rectangle();
        robberRect.width = mapManager.getTileWidth();
        robberRect.height = mapManager.getTileHeight();
        setRandomPos(robberRect, GameScreen.AREA.MIDDLE);
        mapManager.setRobber(new Robber(robberRect, ll));
    }

    private void setRandomPos(Rectangle charRect, GameScreen.AREA area) {
        ArrayList<Node> cells = new ArrayList<>();

        TiledMapTileLayer background = (TiledMapTileLayer) mapManager.getLayer(MapManager.Layers.BACKGROUND);
        int startX, startY, endX, endY;
        int height = background.getHeight() / background.getTileHeight();
        int width = background.getWidth() / background.getTileWidth();

        Random random = new Random();

        switch (area) {
            case TOP_LEFT:
                startX = 1;
                startY = height - height / 3;
                endX = width / 3;
                endY = height - 1;
                break;
            case TOP_RIGHT:
                startX = width - width / 3;
                startY = height - height / 3;
                endX = width - 1;
                endY = height - 1;
                break;
            case BOTTOM_LEFT:
                startX = 1;
                startY = 1;
                endX = width / 3;
                endY = height / 3;
                break;
            case BOTTOM_RIGHT:
                startX = width - width / 3;
                startY = 1;
                endX = width;
                endY = height / 3;
                break;
            case MIDDLE:
                startX = width / 3;
                startY = height / 3;
                endX = 2 * width / 3;
                endY = 2 * height / 3;
                break;
            default:
                startX = 1;
                startY = 1;
                endX = width - 1;
                endY = height - 1;
                break;
        }
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                // Find cells that are accessible
                if (!mapManager.canMove(i, j) || mapManager.hasCop(i, j) || mapManager.hasItem(i, j)) {
                    continue;
                }
                cells.add(new Node(i, j));
            }
        }

        Node pos = cells.get(random.nextInt(cells.size()));
        charRect.x = pos.getX() * mapManager.getTileWidth();
        charRect.y = pos.getY() * mapManager.getTileHeight();
    }


}