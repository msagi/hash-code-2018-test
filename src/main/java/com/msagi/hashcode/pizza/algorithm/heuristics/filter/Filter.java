package com.msagi.hashcode.pizza.algorithm.heuristics.filter;

/**
 * Data class represents a filter which can turn into a slice by applying a center row and column value.
 * @see com.msagi.hashcode.pizza.model.Slice
 * @see FilterGenerator
 */
public class Filter {
    public final int firstRowOffset;
    public final int firstColumnOffset;
    public final int lastRowOffset;
    public final int lastColumnOffset;

    public Filter(final int firstRowOffset, final int firstColumnOffset, final int lastRowOffset, final int lastColumnOffset) {
        this.firstRowOffset = firstRowOffset;
        this.firstColumnOffset = firstColumnOffset;
        this.lastRowOffset = lastRowOffset;
        this.lastColumnOffset = lastColumnOffset;
    }
}
