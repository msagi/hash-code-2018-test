package com.msagi.hashcode.pizza.algorithm;

import com.msagi.hashcode.pizza.model.Slice;
import com.msagi.hashcode.pizza.model.Model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Algorithm {

    @NotNull
    List<Slice> solve(@NotNull final Model model);
}
