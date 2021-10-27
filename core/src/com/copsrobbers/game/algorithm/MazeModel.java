package com.copsrobbers.game.algorithm;

public class MazeModel {

    /**
     * Collection to represent an entire maze
     */
    private final CellModel[][] cellModels;

    public MazeModel(int rows, int columns) {

        cellModels = new CellModel[rows][columns];
        for(int row=0; row <cellModels.length; row++) {
            for(int col=0; col<cellModels[row].length; col++) {
                CellModel cellModel = new CellModel(row, col);
                cellModels[row][col] = cellModel;
            }
        }
    }

    /**
     * Get {@link #cellModels}
     */
    public CellModel[][] getCellModels() {
        return cellModels;
    }
}
