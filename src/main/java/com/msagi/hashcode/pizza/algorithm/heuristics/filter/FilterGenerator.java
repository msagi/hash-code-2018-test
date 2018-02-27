package com.msagi.hashcode.pizza.algorithm.heuristics.filter;

import com.msagi.hashcode.pizza.model.Slice;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilterGenerator {

    public static void main(@NotNull final String[] args) {
        if (args.length != 1) {
            System.out.println("Usage FilterGenerator <MaxNumberOfCellsPerSize>");
            System.exit(-1);
        }
        final int maxNumberOfCellsPerSlice = Integer.parseInt(args[0]);

        //initialize filters
        final List<List<Filter>> filters = new ArrayList<>();
        for (int i = 0; i <= maxNumberOfCellsPerSlice; i++) {
            filters.add(new ArrayList<>());
        }

        final int mapSize = 2 * maxNumberOfCellsPerSlice - 1;
        @SuppressWarnings("UnnecessaryLocalVariable") final int center = maxNumberOfCellsPerSlice;
        final int centerIndex = center - 1;

        for (int firstRow = 0; firstRow < mapSize; firstRow++) {
            for (int firstColumn = 0; firstColumn < mapSize; firstColumn++) {
                for (int lastRow = firstRow; lastRow < mapSize; lastRow++) {
                    for (int lastColumn = firstColumn; lastColumn < mapSize; lastColumn++) {

                        if (firstRow > centerIndex || firstColumn > centerIndex || lastRow < centerIndex || lastColumn < centerIndex) {
                            //the area does not include the center, hence it is invalid
                            continue;
                        }

                        final int numberOfCells = Slice.getNumberOfCells(firstRow, firstColumn, lastRow, lastColumn);
                        if (numberOfCells > maxNumberOfCellsPerSlice) {
                            continue;
                        }

                        filters.get(numberOfCells)
                                .add(new Filter(
                                        firstRow - centerIndex,
                                        firstColumn - centerIndex,
                                        lastRow - centerIndex,
                                        lastColumn - centerIndex
                                ));

                    }
                }
            }
        }

        int totalNumberOfFilters = 0;
        final StringBuilder builder = new StringBuilder();
        builder.append("null,\nnull,\n");
        for (int i = 2; i <= maxNumberOfCellsPerSlice; i++) {
            final List<Filter> f = filters.get(i);
            final int numberOfFilters = f.size();
            totalNumberOfFilters += numberOfFilters;
            builder.append(String.format("new Filter[] { //cells:%d, filters:%d\n", i, numberOfFilters));
            for (int filterIndex = 0; filterIndex < f.size(); filterIndex++) {
                final Filter filter = f.get(filterIndex);
                builder
                    .append(String.format("\tnew Filter(%3d, %3d, %3d, %3d)",
                        filter.firstRowOffset,
                        filter.firstColumnOffset,
                        filter.lastRowOffset,
                        filter.lastColumnOffset));
                if (filterIndex + 1 < numberOfFilters) { builder.append(",");}
                builder.append("\n");
            }
            builder.append("}");
            if (i + 1 < filters.size()) { builder.append(",");}
            builder.append("\n");
        }
        builder.append("//").append(totalNumberOfFilters).append(" filters total.\n");
        System.out.println(builder.toString());
    }
}
