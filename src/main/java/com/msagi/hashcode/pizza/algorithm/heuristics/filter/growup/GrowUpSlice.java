package com.msagi.hashcode.pizza.algorithm.heuristics.filter.growup;

public class GrowUpSlice {

    public final int id;
    public final int centerRow;
    public final int centerColumn;
    public final int filterSizeIndex;
    public final int filterIndex;

    public GrowUpSlice(int id, int centerRow, int centerColumn, int filterSizeIndex, int filterIndex) {
        this.id = id;
        this.centerRow = centerRow;
        this.centerColumn = centerColumn;
        this.filterSizeIndex = filterSizeIndex;
        this.filterIndex = filterIndex;
    }
}
