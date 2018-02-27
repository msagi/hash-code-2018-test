package com.msagi.hashcode.pizza.model;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Model {

    public static final byte TOMATO = 1;
    private static final byte MUSHROOM = 2;

    public final int rows;
    public final int columns;
    public final int minNumberOfEachIngredientsPerSlice; // 1..1000
    public final int maxNumberOfCellsPerSlice; // 1..1000

    @NotNull
    public final byte[][] pizza; // 1..1_000_000 (4MB)

    public Model(@NotNull final String path) {
        final Scanner scanner;
        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }

        rows = scanner.nextInt();
        columns = scanner.nextInt();
        minNumberOfEachIngredientsPerSlice = scanner.nextInt();
        maxNumberOfCellsPerSlice = scanner.nextInt();
        scanner.nextLine();

        pizza = new byte[rows][columns];
        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            final String row = scanner.nextLine();
            for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
                final char c = row.charAt(columnIndex);
                switch(c) {
                    case 'M': pizza[rowIndex][columnIndex] = MUSHROOM; break;
                    case 'T': pizza[rowIndex][columnIndex] = TOMATO;  break;
                    default: throw new IllegalArgumentException("pizza");
                }
            }
        }

        scanner.close();
    }
}
