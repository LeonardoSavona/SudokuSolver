package com.example.sudoku.solver;

import java.util.*;

public class SudokuSolver {

    private static final int MAX_ITERATIONS = 10000;

    private final Sudoku sudoku;
    private final Map<Coordinate, Integer> sudokuScheme;
    private final int size;

    private final int[] possibleNumbers;

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
            iterateOverCells(iterations);
            solved = isSolved(sudoku.getSudoku());
            iterations++;

            if (!solved && (iterations < 10 || iterations % 100 == 0))
                System.out.println("Solution after "+iterations+" iterations: \n"+ sudoku);
        }

        if (solved)
            System.out.println("Solved after "+iterations+" iterations");

        return sudoku;
    }

    private void iterateOverCells(int iterations) {
        for (Map.Entry<Coordinate, Integer> entry : sudokuScheme.entrySet()) {
            if (entry.getValue() == 0) {
                emptyCells.putIfAbsent(entry.getKey(), new HashSet<>());

                searchNumberForCell(entry.getKey());
//                    if (iterations > 0)
//                        analyzePossibleNumbers(coordinate);
            }
        }
    }

    private void analyzePossibleNumbers(Coordinate coordinate) {
        Set<Integer> possibleNumbers = emptyCells.get(coordinate);
        // Controllo rispetto ai numeri possibili delle altre caselle
        int r = analyzePossibleNumbersOfRaw(coordinate);
        if (r != 0) {
            possibleNumbers.removeIf(i -> i != r);
            clearRawPossibilites(coordinate, r);
            if (isNumberFound(possibleNumbers, coordinate)) return;
        }

        int c = analyzePossibleNumbersOfColumn(coordinate);
        if (c != 0) {
            possibleNumbers.removeIf(i -> i != c);
            if (isNumberFound(possibleNumbers, coordinate)) return;
        }

        int s = analyzePossibleNumbersOfSquare(coordinate);
        if (s != 0) {
            possibleNumbers.removeIf(i -> i != s);
            if (isNumberFound(possibleNumbers, coordinate)) return;
        }
    }

    private void clearRawPossibilites(Coordinate coordinate, int r) {
        Set<Integer> possibilites;

        for (int i = coordinate.getColumn(); i < size; i++) {
            possibilites = emptyCells.get(new Coordinate(coordinate.getRaw(), i));
            if (possibilites != null) possibilites.removeIf(n -> n != r);
        }

        for (int i = coordinate.getColumn(); i > 0; i--) {
            possibilites = emptyCells.get(new Coordinate(coordinate.getRaw(), i));
            if (possibilites != null) possibilites.removeIf(n -> n != r);
        }
    }

    private int analyzePossibleNumbersOfSquare(Coordinate coordinate) {
        return 0;
    }

    private int analyzePossibleNumbersOfColumn(Coordinate coordinate) {
        return 0;
    }

    private int analyzePossibleNumbersOfRaw(Coordinate coordinate) {
        Set<Integer> coordinatePossibleNumbers = emptyCells.get(coordinate);
        if (coordinatePossibleNumbers == null) return 0;

        Set<Integer> possibleNumbers = new HashSet<>();
        Set<Integer> possibilites;

        for (int i = coordinate.getColumn(); i < size; i++) {
             possibilites = emptyCells.get(new Coordinate(coordinate.getRaw(), i));
            if (possibilites != null) possibleNumbers.addAll(possibilites);
        }

        for (int i = coordinate.getColumn(); i > 0; i--) {
            possibilites = emptyCells.get(new Coordinate(coordinate.getRaw(), i));
            if (possibilites != null) possibleNumbers.addAll(possibilites);
        }

        for (int coordinatePossibleNumber : coordinatePossibleNumbers) {
            if (!possibleNumbers.contains(coordinatePossibleNumber)) return coordinatePossibleNumber;
        }

        return 0;
    }

    private void searchNumberForCell(Coordinate coordinate) {
        int raw = coordinate.getRaw();
        int col = coordinate.getColumn();

        Set<Integer> possibleNumbers = emptyCells.get(coordinate);

        // Controllo rispetto ai numeri già inseriti
        List<Integer> rawMissingNumbers = analyzeRow(raw);
        if (!possibleNumbers.isEmpty()) {
            possibleNumbers.removeIf(n -> !rawMissingNumbers.contains(n));
        } else {
            possibleNumbers.addAll(rawMissingNumbers);
        }
        if (isNumberFound(possibleNumbers, coordinate)) return;

        List<Integer> colMissingNumbers = analyzeColumn(col);
        possibleNumbers.removeIf(n -> !colMissingNumbers.contains(n));
        if (isNumberFound(possibleNumbers, coordinate)) return;

        List<Integer> squareMissingNumbers = analyzeSquare(raw, col);
        possibleNumbers.removeIf(n -> !squareMissingNumbers.contains(n));
        if (isNumberFound(possibleNumbers, coordinate)) return;

        emptyCells.get(coordinate).addAll(possibleNumbers);
    }

    private boolean isNumberFound(Set<Integer> possibleNumbers, Coordinate coordinate) {
        if (possibleNumbers.size() == 1) {
            emptyCells.remove(coordinate);
            sudokuScheme.put(coordinate, (Integer) possibleNumbers.toArray()[0]);
            return true;
        }
        return false;
    }

    private List<Integer> analyzeSquare(int raw, int col) {
        List<Integer> squareNumbers = new ArrayList<>();

        Set<Coordinate> coordinates = coordinatesSquares.get(new Coordinate(raw, col));
        for (Coordinate coordinate : coordinates) {
            int num = sudokuScheme.get(coordinate);
            if (num != 0) {
                squareNumbers.add(num);
            }
        }

        return getMissingNumbersFromList(squareNumbers);
    }

    private List<Integer> analyzeColumn(int column) {
        List<Integer> columnNumbers = new ArrayList<>();
        for (int i = 0; i < sudoku.getSize(); i++) {
            columnNumbers.add(sudokuScheme.get(new Coordinate(i, column)));
        }
        return getMissingNumbersFromList(columnNumbers);
    }

    private List<Integer> analyzeRow(int raw) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < sudoku.getSize(); i++) {
            list.add(sudokuScheme.get(new Coordinate(raw, i)));
        }
        return getMissingNumbersFromList(list);
    }

    private List<Integer> getMissingNumbersFromList(List<Integer> numbers) {
        List<Integer> result = new ArrayList<>();
        for (int i : possibleNumbers) {
            if (!numbers.contains(i)) result.add(i);
        }
        return result;
    }

    private boolean isSolved(Map<Coordinate, Integer> sudoku) {
        return !sudoku.containsValue(0);
    }
}
