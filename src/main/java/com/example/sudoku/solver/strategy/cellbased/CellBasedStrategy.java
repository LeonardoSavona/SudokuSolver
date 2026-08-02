package com.example.sudoku.solver.strategy.cellbased;

import com.example.sudoku.solver.entity.Cell;
import com.example.sudoku.solver.entity.Sudoku;
import com.example.sudoku.solver.strategy.Strategy;

public abstract class CellBasedStrategy extends Strategy {

    protected Cell cell;

    public CellBasedStrategy(Sudoku sudoku) {
        super(sudoku);
    }

    public void apply(Cell cell) {
        this.cell = cell;
        apply();
    }
}
