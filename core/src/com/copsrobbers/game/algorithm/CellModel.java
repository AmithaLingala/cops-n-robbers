package com.copsrobbers.game.algorithm;

import java.beans.PropertyChangeSupport;

public class CellModel{

        private final int row, column;
        private boolean isWall;
        //support to fire property change events
        private PropertyChangeSupport pcs;

        public CellModel(int row, int column)  {
            this(row, column, false);
        }

        public CellModel(int row, int column, boolean isWall) {
            this.row = row;
            this.column = column;
            this.isWall = isWall;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof CellModel)) return false;
            CellModel other = (CellModel)obj;
            return row == other.getRow() && column == other.getColumn();
        }

        public void setPropertChangeSupport(PropertyChangeSupport pcs) {
            this.pcs = pcs;
        }

        private void firePropertyChange(String name, Object oldValue, Object newValue) {
            if(pcs != null) {
                pcs.firePropertyChange(name, oldValue, newValue);
            }
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
            Object old = this.isWall;
            this.isWall = isWall;
            firePropertyChange("Wall", old, isWall);
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
            return  "["+ (isWall ? "Wall " : "Path " ) +  row + "-" + column + "]";
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return 17*row + 31*column;
        }
    }

