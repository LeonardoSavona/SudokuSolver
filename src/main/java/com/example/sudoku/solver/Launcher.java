package com.example.sudoku.solver;

import com.example.sudoku.solver.entity.Sudoku;
import com.example.sudoku.solver.helper.ConsolePrinter;
import com.example.sudoku.solver.helper.JSONHelper;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class Launcher {

    private static final String[] ALL_LEVELS = new String[]{
            "easy",
            "medium",
            "hard",
            "expert",
            "master",
            "master-2",
            "master-3",
            "master-4",
            "master-5",
            "master-6",
            "master-7",
            "master-8",
            "master-9",
            "master-10",
            "master-11",
            "master-12",
            "master-13",
            "master-14",
            "master-15",
            "extreme",
            "extreme-2",
            "world_hardest"
    };
    private static final String LEVEL = "world_hardest";
    private static final boolean DO_ALL_LEVELS = false;

    public static void main(String[] args) throws Exception {
        if (DO_ALL_LEVELS) {
            LinkedHashMap<String, String> result = new LinkedHashMap<>();
            for (String levelName : ALL_LEVELS) {
                File sudokuFile = new File(Launcher.class.getClassLoader().getResource("sudoku/sudoku_"+levelName).toURI());
                File sudokuSolutionFile = new File(Launcher.class.getClassLoader().getResource("solutions/sudoku_"+levelName).toURI());
                result.put(levelName, String.valueOf(solveLevel(sudokuFile, sudokuSolutionFile)));
            }
            for (Map.Entry<String, String> l : result.entrySet()) {
                System.out.println(l.getKey() + " : " + l.getValue());
            }
        } else {
            File sudokuFile = new File(Launcher.class.getClassLoader().getResource("sudoku/sudoku_"+LEVEL).toURI());
            File sudokuSolutionFile = new File(Launcher.class.getClassLoader().getResource("solutions/sudoku_"+LEVEL).toURI());
            solveLevel(sudokuFile, sudokuSolutionFile);
        }
    }

    private static boolean solveLevel(File sudokuFile, File sudokuSolutionFile) throws Exception {
        System.out.println("Loading sudoku..");
        Sudoku sudoku = new Sudoku(sudokuFile);
        Sudoku sudokuSolution = new Sudoku(sudokuSolutionFile);

        System.out.println("Start to solve sudoku..");
        SudokuSolver sudokuSolver = new SudokuSolver(sudoku);

        long start = System.currentTimeMillis();
        Sudoku solvedSudoku = sudokuSolver.solve();
        double time = (System.currentTimeMillis()-start);

        System.out.println("Result: \n"+ ConsolePrinter.getSudokuAsStandardString(solvedSudoku));
        System.out.println("Solution: \n"+ConsolePrinter.getSudokuAsStandardString(sudokuSolution));
        JSONHelper.saveChronology();
        if (solvedSudoku.equals(sudokuSolution)) {
            System.out.println("Sudoko solved successfully in "+time+"ms !");
            return true;
        } else {
            System.out.println("Failed to solve sudoku");
            return false;
        }
    }
}
