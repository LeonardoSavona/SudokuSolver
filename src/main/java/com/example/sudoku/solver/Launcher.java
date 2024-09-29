package com.example.sudoku.solver;

import java.io.File;

public class Launcher {

    public static void main(String[] args) throws Exception {
        File sudokuFile = new File(Launcher.class.getClassLoader().getResource("sudoku/sudoku_easy").toURI());
        File sudokuSolutionFile = new File(Launcher.class.getClassLoader().getResource("solutions/sudoku_easy").toURI());

        System.out.println("Loading sudoku..");
        Sudoku sudoku = new Sudoku(sudokuFile);
        Sudoku sudokuSolution = new Sudoku(sudokuSolutionFile);

        System.out.println("Start to solve sudoku..");
        SudokuSolver sudokuSolver = new SudokuSolver();
        Sudoku solvedSudoku = sudokuSolver.solve(sudoku);

        System.out.println("Result: \n"+solvedSudoku);
        System.out.println("Solution: \n"+sudokuSolution);
        if (solvedSudoku.equals(sudokuSolution)) {
            System.out.println("Suduko solved successfully!");
        } else {
            System.out.println("Failed to solve sudoku");
        }
    }
}
