package com.example.sudoku.solver;

import java.io.File;

public class Launcher {

    private static final String LEVEL = "expert";

    public static void main(String[] args) throws Exception {
        File sudokuFile = new File(Launcher.class.getClassLoader().getResource("sudoku/sudoku_"+LEVEL).toURI());
        File sudokuSolutionFile = new File(Launcher.class.getClassLoader().getResource("solutions/sudoku_"+LEVEL).toURI());

        System.out.println("Loading sudoku..");
        Sudoku sudoku = new Sudoku(sudokuFile);
        Sudoku sudokuSolution = new Sudoku(sudokuSolutionFile);

        System.out.println("Start to solve sudoku..");
        SudokuSolver sudokuSolver = new SudokuSolver(sudoku);

        long start = System.currentTimeMillis();
        Sudoku solvedSudoku = sudokuSolver.solve();
        double time = (System.currentTimeMillis()-start);

        System.out.println("Result: \n"+ConsolePrinter.getSudokuAsString(solvedSudoku));
        System.out.println("Solution: \n"+ConsolePrinter.getSudokuAsString(sudokuSolution));
        if (solvedSudoku.equals(sudokuSolution)) {
            System.out.println("Sudoko solved successfully in "+time+"ms !");
        } else {
            System.out.println("Failed to solve sudoku");
        }
    }
}
