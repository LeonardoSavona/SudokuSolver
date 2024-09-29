package com.example.sudoku.solver;

import java.util.*;

public class SudokuSolver {

    private static final int MAX_ITERATIONS = 10000;

    private final Sudoku sudoku;
    private final List<List<Integer>> sudokuScheme;

    private final int[] possibleNumbers;
    private final int size;

    private final Map<Coordinate, Set<Integer>> emptySquares = new HashMap<>();
    private final Map<Coordinate, Set<Coordinate>> coordinatesSquares = new HashMap<>();

    public SudokuSolver(Sudoku sudoku){
        this.sudoku = sudoku;
        size = sudoku.getSize();
        sudokuScheme = sudoku.getSudoku();

        possibleNumbers = new int[size];
        for (int i = 0; i < size; i++) {
            possibleNumbers[i] = i+1;
        }

        createCoordinatesSquares();
    }

    public Sudoku solve() {
        int iterations = 0;
        boolean solved = false;
        while (!solved && iterations < MAX_ITERATIONS) {
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    if (sudokuScheme.get(r).get(c) == 0) {
                        Coordinate coordinate = new Coordinate(r, c);
                        emptySquares.putIfAbsent(coordinate, new HashSet<>());

                        addPossibleNumberForSquare(coordinate);
                    }
                }
            }

            solved = isSolved(sudoku.getSudoku());
            iterations++;

            if (!solved)
                System.out.println("Solution after "+iterations+" iterations: \n"+sudoku.toString());
        }

        System.out.println("Solved after "+iterations+" iterations");

        return sudoku;
    }

    private void createCoordinatesSquares() {
        int sq = (int) Math.sqrt(size);

        for (int r = 0; r < size; r += sq) {
            for (int c = 0; c < size; c += sq) {
                Coordinate coordinate = new Coordinate(r,c);
                Set<Coordinate> otherCoordinates = getOtherCoordinates(coordinate, sq);

                coordinatesSquares.put(coordinate, otherCoordinates);
            }
        }

        Map<Coordinate, Set<Coordinate>> toAdd = new HashMap<>();
        for (Map.Entry<Coordinate, Set<Coordinate>> entry : coordinatesSquares.entrySet()) {
            for (Coordinate coordinate : entry.getValue()) {

                Set<Coordinate> coordinatesToAdd = new HashSet<>(entry.getValue());
                coordinatesToAdd.add(entry.getKey());
                coordinatesToAdd.remove(coordinate);
                toAdd.put(coordinate, coordinatesToAdd);
            }
        }

        coordinatesSquares.putAll(toAdd);
    }

    private Set<Coordinate> getOtherCoordinates(Coordinate coordinate, int sq) {
        int raw = coordinate.getRaw();
        int col = coordinate.getColumn();

        Set<Coordinate> result = new HashSet<>();
        for (int r = raw; r < raw + sq; r++){
            for (int c = col; c < col + sq; c++) {
                result.add(new Coordinate(r, c));
            }
        }

        return result;
    }

    private void addPossibleNumberForSquare(Coordinate coordinate) {
        int raw = coordinate.getRaw();
        int col = coordinate.getColumn();

        Set<Integer> possibleNumbers = emptySquares.get(coordinate);

        List<Integer> rawMissingNumbers = analyzeRow(sudokuScheme.get(raw));
        if (!possibleNumbers.isEmpty())
            possibleNumbers.removeIf(n -> !rawMissingNumbers.contains(n));

        if (rawMissingNumbers.size() == 1){
            sudokuScheme.get(raw).set(col, rawMissingNumbers.get(0));
            return;
        } else {
            possibleNumbers.addAll(rawMissingNumbers);
        }

        List<Integer> colMissingNumbers = analyzeColumn(sudokuScheme, col);
        colMissingNumbers.removeIf(n -> !rawMissingNumbers.contains(n));

        if (colMissingNumbers.size() == 1) {
            sudokuScheme.get(raw).set(col, colMissingNumbers.get(0));
            return;
        } else {
            possibleNumbers.addAll(colMissingNumbers);
        }

        List<Integer> squareMissingNumbers = analyzeSquare(raw, col);
        squareMissingNumbers.removeIf(n -> !colMissingNumbers.contains(n));

        if (squareMissingNumbers.size() == 1) {
            sudokuScheme.get(raw).set(col, squareMissingNumbers.get(0));
            return;
        } else {
            possibleNumbers.addAll(squareMissingNumbers);
        }

        emptySquares.get(coordinate).addAll(possibleNumbers);
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
