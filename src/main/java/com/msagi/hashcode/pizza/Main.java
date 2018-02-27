package com.msagi.hashcode.pizza;

import com.msagi.hashcode.pizza.algorithm.Algorithm;
import com.msagi.hashcode.pizza.algorithm.AlgorithmFactory;
import com.msagi.hashcode.pizza.model.Slice;
import com.msagi.hashcode.pizza.algorithm.heuristics.filter.filtermatch.FilterMatchAlgorithm;
import com.msagi.hashcode.pizza.model.Model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class Main {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private void execute(@NotNull String inputFilePath) {
        final Model model = new Model(inputFilePath);
        logger.debug(String.format("Input file has been parsed to model (size: %dx%d, minIngredients:%d, maxCells:%d).",
                model.rows,
                model.columns,
                model.minNumberOfEachIngredientsPerSlice,
                model.maxNumberOfCellsPerSlice));

        final Algorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmFactory.GROW_UP); //filter 894448, growup: 901839

        long startTimeMs = System.currentTimeMillis();
        final List<Slice> solution = algorithm.solve(model);
        final long runtimeMs = System.currentTimeMillis() - startTimeMs;
        logger.debug("Run time: " + runtimeMs);

        int totalScore = 0;
        final StringBuilder builder = new StringBuilder();
        for (final Slice slice : solution) {
            totalScore += slice.numberOfCells;
            builder.append(slice.toString()).append("\n");
        }
        logger.debug("\n" + builder.toString());
        logger.debug("Total score: " + totalScore);

        System.out.println(solution.size());
        for (final Slice slice : solution) {
            System.out.format("%d %d %d %d\n", slice.firstRow, slice.firstColumn, slice.lastRow, slice.lastColumn);
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: Main <InputFilePath>");
            System.exit(-1);
        }
        final Main main = new Main();
        main.execute(args[0]);
    }
}
