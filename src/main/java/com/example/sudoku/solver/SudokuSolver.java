package com.example.sudoku.solver;

import java.util.*;

public class SudokuSolver {

    private static final int MAX_ITERATIONS = 10000;

    private final Sudoku sudoku;
    private final List<List<Integer>> sudokuScheme;

    private final int[] possibleNumbers;
    private final int size;

    private final Map<Coordinate, Set<Integer>> emptyCells = new HashMap<>();
    private final Map<Coordinate, Set<Coordinate>> coordinatesSquares;

    public SudokuSolver(Sudoku sudoku){
        this.sudoku = sudoku;
        this.size = sudoku.getSize();
        this.sudokuScheme = sudoku.getSudoku();

        this.possibleNumbers = new int[size];
        for (int i = 0; i < size; i++) {
            possibleNumbers[i] = i+1;
        }

        coordinatesSquares = Helper.getCoordinatesSquare(size);
    }

    public Sudoku solve() {
        int iterations = 0;
        boolean solved = false;
        while (!solved && iterations < MAX_ITERATIONS) {
            iterateOverCells();
            solved = isSolved(sudoku.getSudoku());
            iterations++;

            if (!solved && (iterations < 10 || iterations % 100 == 0))
                System.out.println("Solution after "+iterations+" iterations: \n"+ sudoku);
        }

        if (solved)
            System.out.println("Solved after "+iterations+" iterations");

        return sudoku;
    }

    private void iterateOverCells() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (sudokuScheme.get(r).get(c) == 0) {
                    Coordinate coordinate = new Coordinate(r, c);
                    emptyCells.putIfAbsent(coordinate, new HashSet<>());

                    searchNumberForCell(coordinate);
                }
            }
        }
    }

    private void searchNumberForCell(Coordinate coordinate) {
        int raw = coordinate.getRaw();
        int col = coordinate.getColumn();

        Set<Integer> possibleNumbers = emptyCells.get(coordinate);

        List<Integer> rawMissingNumbers = analyzeRow(sudokuScheme.get(raw));
        if (!possibleNumbers.isEmpty()) {
            possibleNumbers.removeIf(n -> !rawMissingNumbers.contains(n));
        } else {
            possibleNumbers.addAll(rawMissingNumbers);
        }
        if (isNumberFound(possibleNumbers, coordinate)) return;

        List<Integer> colMissingNumbers = analyzeColumn(sudokuScheme, col);
        possibleNumbers.removeIf(n -> !colMissingNumbers.contains(n));
        if (isNumberFound(possibleNumbers, coordinate)) return;

        List<Integer> squareMissingNumbers = analyzeSquare(raw, col);
        possibleNumbers.removeIf(n -> !squareMissingNumbers.contains(n));
        if (isNumberFound(possibleNumbers, coordinate)) return;

        emptyCells.get(coordinate).addAll(possibleNumbers);
    }

    private boolean isNumberFound(Set<Integer> possibleNumbers, Coordinate coordinate) {
        int raw = coordinate.getRaw();
        int col = coordinate.getColumn();

        if (possibleNumbers.size() == 1) {
            emptyCells.remove(coordinate);
            sudokuScheme.get(raw).set(col, (Integer) possibleNumbers.toArray()[0]);
            return true;
        }
        return false;
    }

    private List<Integer> analyzeSquare(int raw, int col) {
        List<Integer> squareNumbers = new ArrayList<>();

        Set<Coordinate> coordinates = coordinatesSquares.get(new Coordinate(raw, col));
        for (Coordinate coordinate : coordinates) {
            int coordinateRaw = coordinate.getRaw();
            int coordinateCol = coordinate.getColumn();

            int num = sudokuScheme.get(coordinateRaw).get(coordinateCol);
            if (num != 0) {
                squareNumbers.add(num);
            }
        }

        return getMissingNumbersFromList(squareNumbers);
    }

    private List<Integer> analyzeColumn(List<List<Integer>> sudoku, int column) {
        List<Integer> columnNumbers = new ArrayList<>();
        for (List<Integer> raw : sudoku) {
            if (raw.get(column) != 0)
                columnNumbers.add(raw.get(column));
        }

        return getMissingNumbersFromList(columnNumbers);
    }

    private List<Integer> analyzeRow(List<Integer> row) {
        return getMissingNumbersFromList(row);
    }

    private List<Integer> getMissingNumbersFromList(List<Integer> numbers) {
        List<Integer> result = new ArrayList<>();
        for (int i : possibleNumbers) {
            if (!numbers.contains(i)) result.add(i);
        }
        return result;
    }

    private boolean isSolved(List<List<Integer>> sudoku) {
        for (List<Integer> row : sudoku) {
            if (row.contains(0)) return false;
        }
        return true;
    }
}
