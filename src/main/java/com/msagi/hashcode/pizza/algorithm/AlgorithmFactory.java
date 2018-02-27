package com.msagi.hashcode.pizza.algorithm;

import com.msagi.hashcode.pizza.algorithm.heuristics.filter.filtermatch.FilterMatchAlgorithm;
import com.msagi.hashcode.pizza.algorithm.heuristics.filter.growup.GrowUpAlgorithm;

public final class AlgorithmFactory {
    public static final int FILTER_MATCH = 0;
    public static final int GROW_UP = 1;

    private AlgorithmFactory() {
    }

    public static Algorithm getAlgorithm(final int id) {
        switch (id) {
            case FILTER_MATCH:
                return new FilterMatchAlgorithm();
            case GROW_UP:
                return new GrowUpAlgorithm();
            default:
                throw new IllegalArgumentException("unknown algorithm ID");
        }
    }
}
