package com.msagi.hashcode.pizza.algorithm.heuristics.filter.filtermatch;

import com.msagi.hashcode.pizza.algorithm.Algorithm;
import com.msagi.hashcode.pizza.algorithm.heuristics.filter.Filter;
import com.msagi.hashcode.pizza.algorithm.heuristics.filter.Filters;
import com.msagi.hashcode.pizza.model.Slice;
import com.msagi.hashcode.pizza.model.Model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This algorithm tries to find scoring cells by matching predefined filter patterns to the pizza.
 * It tries to match the largest filter first in order to find the largest scoring slice.
 *
 * @see com.msagi.hashcode.pizza.algorithm.heuristics.filter.FilterGenerator
 */
public class FilterMatchAlgorithm implements Algorithm {

    //fill map to detect collisions
    private int[][] fillMap;
    private int sliceId = 0;

    //pizza aka ingredients map
    private byte[][] pizza;

    private int maxRowIndex;
    private int maxColumnIndex;

    private int minNumberOfIngredients;

    @NotNull
    @Override
    public List<Slice> solve(@NotNull final Model model) {

        final int rows = model.rows;
        final int columns = model.columns;
        fillMap = new int[rows][columns];
        maxRowIndex = rows - 1;
        maxColumnIndex = columns - 1;

        pizza = model.pizza;

        final int maxNumberOfCells = model.maxNumberOfCellsPerSlice;
        final int minNumberOfCells = model.minNumberOfEachIngredientsPerSlice * 2;

        minNumberOfIngredients = model.minNumberOfEachIngredientsPerSlice;

        final List<Slice> slices = new ArrayList<>();

        for (int row = 0; row <= maxRowIndex; row++) {
            for (int column = 0; column <= maxColumnIndex; column++) {
                filterMatch:
                for (int filterSize = maxNumberOfCells; filterSize >= minNumberOfCells; filterSize--) {
                    for (final Filter filter : Filters.filters[filterSize]) {
                        final Slice slice = applyFilter(row, column, filter);
                        if (slice != null) {
                            slices.add(slice);
                            break filterMatch;
                        }
                    }
                }
            }
        }

        return slices;
    }

    private Slice applyFilter(int centerRow, int centerColumn, Filter filter) {

        final int firstRow = centerRow + filter.firstRowOffset;
        if (firstRow < 0) {
            return null;
        }
        final int firstColumn = centerColumn + filter.firstColumnOffset;
        if (firstColumn < 0) {
            return null;
        }
        final int lastRow = centerRow + filter.lastRowOffset;
        if (lastRow > maxRowIndex) {
            return null;
        }
        final int lastColumn = centerColumn + filter.lastColumnOffset;
        if (lastColumn > maxColumnIndex) {
            return null;
        }

        //collision check with fillMap (and ingredients count on the way)
        int numberOfTomatoes = 0;
        int numberOfMushrooms = 0;
        for (int row = firstRow; row <= lastRow; row++) {
            for (int column = firstColumn; column <= lastColumn; column++) {
                //collision detection
                if (fillMap[row][column] != 0) {
                    return null;
                }
                if (pizza[row][column] == Model.TOMATO) {
                    numberOfTomatoes++;
                } else {
                    numberOfMushrooms++;
                }
            }
        }

        if (numberOfTomatoes < minNumberOfIngredients || numberOfMushrooms < minNumberOfIngredients) {
            return null;
        }

        //we found a scoring slice
        sliceId++;
        final Slice slice = new Slice(sliceId, firstRow, firstColumn, lastRow, lastColumn);

        //register it in the fill map
        for (int row = firstRow; row <= lastRow; row++) {
            for (int column = firstColumn; column <= lastColumn; column++) {
                fillMap[row][column] = sliceId;
            }
        }

        return slice;
    }
}
