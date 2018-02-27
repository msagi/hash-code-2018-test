package com.msagi.hashcode.pizza.algorithm.heuristics.filter.growup;

import com.msagi.hashcode.pizza.algorithm.Algorithm;
import com.msagi.hashcode.pizza.algorithm.heuristics.filter.Filter;
import com.msagi.hashcode.pizza.algorithm.heuristics.filter.Filters;
import com.msagi.hashcode.pizza.model.Model;
import com.msagi.hashcode.pizza.model.Slice;
import com.msagi.hashcode.pizza.utils.PizzaUtils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This algorithm tries to find scoring cells by matching predefined filter patterns to the pizza.
 * It tries to match the smallest filter with the minimum amount of required ingredients. In the next round
 * then it tires to upgrade these filters by including the largest free cells possible to maximise score.
 *
 * @see com.msagi.hashcode.pizza.algorithm.heuristics.filter.FilterGenerator
 */
public class GrowUpAlgorithm implements Algorithm {

    @NotNull
    final Logger logger = LoggerFactory.getLogger(getClass());

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

        Filter[] filters;
        final List<GrowUpSlice> growUpSlices = new ArrayList<>();

        for (int row = 0; row <= maxRowIndex; row++) {
            for (int column = 0; column <= maxColumnIndex; column++) {
                filterMatch:
                for (int filterSizeIndex = minNumberOfCells; filterSizeIndex <= maxNumberOfCells; filterSizeIndex++) {
                    filters = Filters.filters[filterSizeIndex];
                    for (int filterIndex = 0; filterIndex < filters.length; filterIndex++) {
                        final GrowUpSlice slice = applyMinimumQualifyingFilter(row, column, filterSizeIndex, filterIndex);
                        if (slice != null) {
                            growUpSlices.add(slice);
                            break filterMatch;
                        }
                    }
                }
            }
        }

        Filter growUpSliceFilter;
        final List<Slice> slices = new ArrayList<>();
        for (int sliceIndex = 0; sliceIndex < growUpSlices.size(); sliceIndex++) {
            final GrowUpSlice growUpSlice = growUpSlices.get(sliceIndex);
            if (growUpSlice.filterSizeIndex == maxNumberOfCells) {
                //cannot grow this grow-up-slice any further so just translate it to a slice
                growUpSliceFilter = Filters.filters[growUpSlice.filterSizeIndex][growUpSlice.filterIndex];
                slices.add(
                        new Slice(growUpSlice.id,
                                growUpSlice.centerRow + growUpSliceFilter.firstRowOffset,
                                growUpSlice.centerColumn + growUpSliceFilter.firstColumnOffset,
                                growUpSlice.centerRow + growUpSliceFilter.lastRowOffset,
                                growUpSlice.centerColumn + growUpSliceFilter.lastColumnOffset
                        )
                );
            } else {
                //try to grow slice
                filterMatch:
                for (int filterSizeIndex = maxNumberOfCells; filterSizeIndex >= growUpSlice.filterSizeIndex; filterSizeIndex--) {
                    filters = Filters.filters[filterSizeIndex];
                    for (int filterIndex = 0; filterIndex < filters.length; filterIndex++) {
                        final Slice slice = applyGrowUpFilter(growUpSlice.id, growUpSlice.centerRow, growUpSlice.centerColumn, filters[filterIndex]);
                        if (slice != null) {
                            slices.add(slice);
                            break filterMatch;
                        }
                    }
                }
            }
        }

        logger.debug("\n" + PizzaUtils.toString(model, fillMap, slices));

        return slices;
    }

    private GrowUpSlice applyMinimumQualifyingFilter(int centerRow, int centerColumn, int filterSizeIndex, int filterIndex) {

        final Filter filter = Filters.filters[filterSizeIndex][filterIndex];

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
        final GrowUpSlice slice = new GrowUpSlice(sliceId, centerRow, centerColumn, filterSizeIndex, filterIndex);

        //register it in the fill map
        for (int row = firstRow; row <= lastRow; row++) {
            for (int column = firstColumn; column <= lastColumn; column++) {
                fillMap[row][column] = sliceId;
            }
        }

        return slice;
    }


    private Slice applyGrowUpFilter(int sliceId, int centerRow, int centerColumn, Filter filter) {

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
                if (fillMap[row][column] != 0 && fillMap[row][column] != sliceId) {
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
