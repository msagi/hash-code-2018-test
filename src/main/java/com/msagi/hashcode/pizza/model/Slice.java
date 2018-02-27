package com.msagi.hashcode.pizza.model;

public class Slice {

    public final int id;

    public final int firstRow;
    public final int firstColumn;
    public final int lastRow;
    public final int lastColumn;

    public final int numberOfCells;

    public Slice(final int id, final int firstRow, final int firstColumn, final int lastRow, final int lastColumn) {
        this.id = id;
        this.firstRow = firstRow;
        this.firstColumn = firstColumn;
        this.lastRow = lastRow;
        this.lastColumn = lastColumn;
        numberOfCells = getNumberOfCells(firstRow, firstColumn, lastRow, lastColumn);
    }

    public static int getNumberOfCells(final int firstRow, final int firstColumn, final int lastRow, final int lastColumn) {
        return (1 + lastRow - firstRow) * (1 + lastColumn - firstColumn);
    }

    @Override
    public String toString() {
        return String.format("#%d %d,%d -> %d,%d", id, firstRow, firstColumn, lastRow, lastColumn);
    }

}
