package com.msagi.hashcode.pizza.utils;

import com.msagi.hashcode.pizza.model.Slice;
import com.msagi.hashcode.pizza.model.Model;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PizzaUtils {
    private PizzaUtils() {
    }

    /**
     * Convert the given fill map and slices data to string.
     * @param model The model to use.
     * @param fillMap The fill map to use.
     * @param slices The slices to use.
     * @return The string representation of given data.
     */
    public static String toString(@NotNull final Model model, @NotNull final int[][] fillMap, @NotNull final List<Slice> slices) {
        final StringBuilder builder = new StringBuilder();

        for (int row = 0; row < model.rows; row++) {
            for (int column = 0; column < model.columns; column++) {
                final int sliceId = fillMap[row][column];
                builder.append(String.format("%5d ", sliceId));
            }
            builder.append("\n");
        }
        builder.append("\n");

        final int[][] map = new int[model.rows][model.columns];
        for (final Slice slice : slices) {
            for (int row = slice.firstRow; row <= slice.lastRow; row++) {
                for (int column = slice.firstColumn; column <= slice.lastColumn; column++) {
                    map[row][column] = slice.id;
                }
            }
        }

        for (int row = 0; row < model.rows; row++) {
            for (int column = 0; column < model.columns; column++) {
                final int sliceId = map[row][column];
                builder.append(String.format("%5d ", sliceId));
            }
            builder.append("\n");
        }
        builder.append("\n");

        for (int row = 0; row < model.rows; row++) {
            for (int column = 0; column < model.columns; column++) {
                builder.append(String.format("%c ", model.pizza[row][column] == Model.TOMATO ? 'T' : 'M'));
            }
            builder.append("\n");
        }
        builder.append("\n");

        for (final Slice slice : slices) {
            builder.append(slice.toString()).append(" ").append(slice.numberOfCells).append("\n");
        }
        return builder.toString();
    }
}
