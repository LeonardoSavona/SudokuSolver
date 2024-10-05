package com.example.sudoku.solver.strategy.candidates;

import com.example.sudoku.solver.entity.Sudoku;

public class HiddenCoupleOfCandidatesStrategy extends CoupleOfCandidatesStrategy {

    public HiddenCoupleOfCandidatesStrategy(Sudoku sudoku) {
        super(sudoku);
    }
}
