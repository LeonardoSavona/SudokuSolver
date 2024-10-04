package com.example.sudoku.solver;

import com.example.sudoku.solver.entity.Sudoku;
import com.example.sudoku.solver.helper.ConsolePrinter;
import com.example.sudoku.solver.helper.JSONHelper;

import java.io.File;

public class Launcher {

    private static final String LEVEL = "master-7";

    public static void main(String[] args) throws Exception {
        File sudokuFile = new File(Launcher.class.getClassLoader().getResource("sudoku/sudoku_"+LEVEL).toURI());
        File sudokuSolutionFile = new File(Launcher.class.getClassLoader().getResource("solutions/sudoku_"+LEVEL).toURI());

        System.out.println("Loading sudoku..");
        Sudoku sudoku = new Sudoku(sudokuFile);
        Sudoku sudokuSolution = new Sudoku(sudokuSolutionFile);

        System.out.println("Start to solve sudoku..");
        SudokuSolver sudokuSolver = new SudokuSolver(sudoku);

        JSONHelper.addSudoku(sudoku);
        long start = System.currentTimeMillis();
        Sudoku solvedSudoku = sudokuSolver.solve();
        double time = (System.currentTimeMillis()-start);

        System.out.println("Result: \n"+ ConsolePrinter.getSudokuAsStandardString(solvedSudoku));
        JSONHelper.saveChronology();

        System.out.println("Solution: \n"+ConsolePrinter.getSudokuAsStandardString(sudokuSolution));
        if (solvedSudoku.equals(sudokuSolution)) {
            System.out.println("Sudoko solved successfully in "+time+"ms !");
        } else {
            System.out.println("Failed to solve sudoku");
        }
    }
}
