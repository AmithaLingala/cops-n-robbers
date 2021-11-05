package com.copsrobbers.game.algorithm;

import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.Utils;

import java.util.ArrayList;
import java.util.List;
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
    private long delay = 0;

    public LevelGenerator(CellModel[][] cells) {
        this.cells = cells;
        random = new Random();
    }

    public void generate(int level) {

        /* Start with a grid full of cellModelViews in state wall (not a path). */
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new CellModel(i, j, true);
                //cell[j].setWall(true);
            }
        }

        //Pick a random cell
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
//               int x = random.nextInt(cells.length);
//               int y = random.nextInt(cells[0].length);
                boolean flag = random.nextInt(100) < level * 5;
                cells[i][j].setWall(flag); //set cell to path
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
        gate.y = cells[0].length-1;
        cells[(int) gate.x][(int) gate.y].setWall(false);
        cells[(int) gate.x][(int) gate.y].setGate(true);

        //Compute cell frontier and add it to a frontier collection
        //  Set<CellModel> frontierCells = new HashSet<>(frontierCellsOf(cells[x][y]));

//        while (!frontierCells.isEmpty()){
//
//            //Pick a random cell from the frontier collection
//            CellModel frontierCell;
//            CellModel found = null;
//            long toSkip = random.nextInt(frontierCells.size());
//            for (CellModel cellModel : frontierCells) {
//                if (toSkip > 0) {
//                    toSkip--;
//                    continue;
//                }
//                found = cellModel;
//                break;
//            }
//            frontierCell = found;
//
//            //Get its neighbors: cells in distance 2 in state path (no wall)
//            List<CellModel> frontierNeighbors =  passageCellsOf(frontierCell);
//
//            if(!frontierNeighbors.isEmpty()) {
//                //Pick a random neighbor
//                CellModel neighbor = frontierNeighbors.get(random.nextInt(frontierNeighbors.size()));
//                //Connect the frontier cell with the neighbor
//                connect(frontierCell, neighbor);
//            }
//
//            //Compute the frontier cells of the chosen frontier cell and add them to the frontier collection
//            frontierCells.addAll(frontierCellsOf(frontierCell));
//            //Remove frontier cell from the frontier collection
//            frontierCells.remove( frontierCell);
////            try {
////                Thread.sleep(delay);
////            } catch (InterruptedException ex) { ex.printStackTrace();}
//        }
    }

    //Frontier cells: wall cells in a distance of 2
    private List<CellModel> frontierCellsOf(CellModel cell) {

        return cellsAround(cell, true);
    }

    //Frontier cells: passage (no wall) cells in a distance of 2
    private List<CellModel> passageCellsOf(CellModel cell) {

        return cellsAround(cell, false);
    }

    private List<CellModel> cellsAround(CellModel cell, boolean isWall) {

        List<CellModel> frontier = new ArrayList<>();
        for (int[] direction : DIRECTIONS) {
            int newRow = cell.getRow() + direction[0];
            int newCol = cell.getColumn() + direction[1];
            if (isValidPosition(newRow, newCol) && cells[newRow][newCol].isWall() == isWall) {
                frontier.add(cells[newRow][newCol]);
            }
        }

        return frontier;
    }

    //connects cells which are distance 2 apart
    private void connect(CellModel frontierCellModelView, CellModel neighbour) {

        int inBetweenRow = (neighbour.getRow() + frontierCellModelView.getRow()) / 2;
        int inBetweenCol = (neighbour.getColumn() + frontierCellModelView.getColumn()) / 2;
        frontierCellModelView.setWall(false);
        cells[inBetweenRow][inBetweenCol].setWall(false);
        neighbour.setWall(false);
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < cells.length
                && col >= 0 && col < cells[0].length;
    }

    public LevelGenerator setDelay(long delay) {
        this.delay = delay;
        return this;
    }
}