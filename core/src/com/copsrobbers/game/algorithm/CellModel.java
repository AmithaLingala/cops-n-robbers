package com.copsrobbers.game.algorithm;

/**
 * CellModel is a class to define the cell type and its position on the map.
 */
public class CellModel {

    private final int row, column;
    private boolean isWall;
    private boolean isGate;
    private boolean isBox;

    /**
     * Constructor for the cell model without a cell type
     * @param row row index
     * @param column column index
     */
    public CellModel(int row, int column) {
        this(row, column, false);
    }

    /**
     * Constructor for the cell model with cell type
     * @param row row index
     * @param column column index
     * @param isWall cell type
     */
    public CellModel(int row, int column, boolean isWall) {
        this.row = row;
        this.column = column;
        this.isWall = isWall;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CellModel)) return false;
        CellModel other = (CellModel) obj;
        return row == other.getRow() && column == other.getColumn();
    }

    /**
     * Get {@link #isWall}
     */
    public boolean isWall() {
        return isWall;
    }


    /**
     * Set {@link #isWall}
     */
    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }


    /**
     * Get {@link #isBox}
     */
    public boolean isBox() {
        return isBox;
    }


    /**
     * Set {@link #isBox}
     */
    public void setBox(boolean isBox) {
        this.isBox = isBox;
    }

    /**
     * Get {@link #isGate}
     */
    public boolean isGate() {
        return isGate;
    }

    /**
     * Set {@link #isGate}
     */
    public void setGate(boolean isGate) {
        this.isGate = isGate;
    }

    /**
     * Get {@link #row}
     */
    public int getRow() {
        return row;
    }

    /**
     * Get {@link #column}
     */
    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "[" + (isWall ? "Wall " : "Path ") + row + "-" + column + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 17 * row + 31 * column;
    }
}

