package com.example.sudoku.solver.strategy;

import com.example.sudoku.solver.entity.Sudoku;

public abstract class Strategy {

    protected final Sudoku sudoku;

    public Strategy(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    public abstract void apply();
}
