package com.example.sudoku.solver;

import com.example.sudoku.solver.entity.*;
import com.example.sudoku.solver.helper.ConsolePrinter;
import com.example.sudoku.solver.helper.JSONHelper;
import com.example.sudoku.solver.strategy.candidates.BasicStrategy;
import com.example.sudoku.solver.strategy.candidates.CoupleOfCandidatesStrategy;
import com.example.sudoku.solver.strategy.SquaresStrategy;
import com.example.sudoku.solver.strategy.candidates.PossibleValuesStrategy;
import com.example.sudoku.solver.strategy.candidates.TrioOfCandidatesStrategy;

import java.util.*;

public class SudokuSolver {

    private static final int MAX_ITERATIONS = 10;
    private final Sudoku sudoku;
    private final BasicStrategy basicStrategy;
    private final PossibleValuesStrategy possibleValuesStrategy;
    private final CoupleOfCandidatesStrategy coupleOfCandidatesStrategy;
    private final SquaresStrategy squaresStrategy;
    private final TrioOfCandidatesStrategy trioOfCandidatesStrategy;

    public SudokuSolver(Sudoku sudoku){
        this.sudoku = sudoku;
        this.basicStrategy = new BasicStrategy(sudoku);
        this.possibleValuesStrategy = new PossibleValuesStrategy(sudoku);
        this.squaresStrategy = new SquaresStrategy(sudoku);
        this.coupleOfCandidatesStrategy = new CoupleOfCandidatesStrategy(sudoku);
        this.trioOfCandidatesStrategy = new TrioOfCandidatesStrategy(sudoku);
    }

    public Sudoku solve() {
        int iterations = 0;
        boolean solved = false;

        JSONHelper.addSudoku(sudoku);
        while (!solved && iterations < MAX_ITERATIONS) {
            for (Cell cell : sudoku.getSudoku()) {
                if (cell.getValue() == 0) {
                    basicStrategy.apply(cell);
                }
            }
            JSONHelper.addSudoku(sudoku);

            for (Cell cell : sudoku.getSudoku()) {
                if (cell.getValue() == 0) {
                    possibleValuesStrategy.apply(cell);
                }
            }
            JSONHelper.addSudoku(sudoku);

            squaresStrategy.apply();
            JSONHelper.addSudoku(sudoku);

            trioOfCandidatesStrategy.apply();
            JSONHelper.addSudoku(sudoku);

            coupleOfCandidatesStrategy.apply();
            JSONHelper.addSudoku(sudoku);

            solved = isCompleted(sudoku.getSudoku());
            iterations++;

            System.out.println("Solution after "+iterations+" iterations: \n"+ ConsolePrinter.getSudokuAsStandardString(sudoku));
        }

        if (solved)
            System.out.println("Solved after "+iterations+" iterations");

        return sudoku;
    }

    private boolean isCompleted(List<Cell> sudoku) {
        return sudoku.stream().noneMatch(c -> c.getValue() == 0);
    }
}
