package com.example.sudoku.solver;

import java.util.*;
import java.util.stream.Collectors;

public class SudokuSolver {

    private static final int MAX_ITERATIONS = 10000;

    private final Sudoku sudoku;
    private final List<Cell> sudokuScheme;
    private final int size;

    private final int[] possibleNumbers;

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
        for (Cell cell : sudokuScheme) {
            if (cell.getValue() == 0) {
                searchNumberForCell(cell);
//                if (cell.getValue() == 0)
//                    analyzePossibleNumbers(cell);
            }
        }
    }
//
//    private void analyzePossibleNumbers(Cell cell) {
//        // Controllo rispetto ai numeri possibili delle altre caselle
//        int r = analyzePossibleNumbersOfRaw(cell);
//        if (r != 0) {
//            possibleNumbers.removeIf(i -> i != r);
//            clearRawPossibilites(coordinate, r);
//            if (isNumberFound(possibleNumbers, coordinate)) return;
//        }
//
//        int c = analyzePossibleNumbersOfColumn(coordinate);
//        if (c != 0) {
//            possibleNumbers.removeIf(i -> i != c);
//            if (isNumberFound(possibleNumbers, coordinate)) return;
//        }
//
//        int s = analyzePossibleNumbersOfSquare(coordinate);
//        if (s != 0) {
//            possibleNumbers.removeIf(i -> i != s);
//            if (isNumberFound(possibleNumbers, coordinate)) return;
//        }
//    }
//
//    private void clearRawPossibilites(Coordinate coordinate, int r) {
//        Set<Integer> possibilites;
//
//        for (int i = coordinate.getColumn(); i < size; i++) {
//            possibilites = emptyCells.get(new Coordinate(coordinate.getRow(), i));
//            if (possibilites != null) possibilites.removeIf(n -> n != r);
//        }
//
//        for (int i = coordinate.getColumn(); i > 0; i--) {
//            possibilites = emptyCells.get(new Coordinate(coordinate.getRow(), i));
//            if (possibilites != null) possibilites.removeIf(n -> n != r);
//        }
//    }
//
//    private int analyzePossibleNumbersOfSquare(Coordinate coordinate) {
//        return 0;
//    }
//
//    private int analyzePossibleNumbersOfColumn(Coordinate coordinate) {
//        return 0;
//    }
//
//    private int analyzePossibleNumbersOfRaw(Coordinate coordinate) {
//        Set<Integer> coordinatePossibleNumbers = emptyCells.get(coordinate);
//        if (coordinatePossibleNumbers == null) return 0;
//
//        Set<Integer> possibleNumbers = new HashSet<>();
//        Set<Integer> possibilites;
//
//        for (int i = coordinate.getColumn(); i < size; i++) {
//             possibilites = emptyCells.get(new Coordinate(coordinate.getRow(), i));
//            if (possibilites != null) possibleNumbers.addAll(possibilites);
//        }
//
//        for (int i = coordinate.getColumn(); i > 0; i--) {
//            possibilites = emptyCells.get(new Coordinate(coordinate.getRow(), i));
//            if (possibilites != null) possibleNumbers.addAll(possibilites);
//        }
//
//        for (int coordinatePossibleNumber : coordinatePossibleNumbers) {
//            if (!possibleNumbers.contains(coordinatePossibleNumber)) return coordinatePossibleNumber;
//        }
//
//        return 0;
//    }

    private void searchNumberForCell(Cell cell) {
        if (cell.getPossibleValues().size() == 1) {
            cell.setValue(cell.getPossibleValues().stream().findFirst().get());
            return;
        }

        // Controllo rispetto ai numeri già inseriti
        Set<Integer> rawMissingNumbers = getMissingNumbersFromRow(cell.getCoordinate().getRow());
        if (isNumberFound(rawMissingNumbers, cell)) return;

        Set<Integer> colMissingNumbers = getMissingNumbersFromColumn(cell.getCoordinate().getColumn());
        colMissingNumbers.removeIf(n -> !rawMissingNumbers.contains(n));
        if (isNumberFound(colMissingNumbers, cell)) return;

        Set<Integer> squareMissingNumbers = getMissingNumbersFromSquare(cell);
        squareMissingNumbers.removeIf(n -> !colMissingNumbers.contains(n));
        if (isNumberFound(squareMissingNumbers, cell)) {
            return;
        }
    }

    private boolean isNumberFound(Set<Integer> possibleNumbers, Cell cell) {
        if (possibleNumbers.size() == 1) {
            cell.setValue((Integer) possibleNumbers.toArray()[0]);
            cell.clearPossibleValues();
            clearOtherCellsPossibleValues(cell);
            return true;
        } else {
            cell.setPossibleValues(possibleNumbers);
            return false;
        }
    }

    private void clearOtherCellsPossibleValues(Cell cell) {
        clearOtherRowCellsPossibleValues(cell);
        clearOtherColumnCellsPossibleValues(cell);
        clearOtherSquareCellsPossibleValues(cell);
    }

    private void clearOtherRowCellsPossibleValues(Cell cell) {
        sudokuScheme.forEach(c -> {
            if (c.getCoordinate().getRow() == cell.getCoordinate().getRow() && !c.getPossibleValues().isEmpty()){
                c.removePossibleValue(cell.getValue());
            }
        });
    }

    private void clearOtherColumnCellsPossibleValues(Cell cell) {
        sudokuScheme.forEach(c -> {
            if (c.getCoordinate().getColumn() == cell.getCoordinate().getColumn() && !c.getPossibleValues().isEmpty()){
                c.removePossibleValue(cell.getValue());
            }
        });
    }

    private void clearOtherSquareCellsPossibleValues(Cell cell) {
        Set<Coordinate> coordinates = coordinatesSquares.get(cell.getCoordinate());
        for (Coordinate coordinate : coordinates) {
            Cell cell1 = sudoku.getCellByCoordinate(coordinate);
            if (!cell1.getPossibleValues().isEmpty()) {
                cell1.removePossibleValue(cell.getValue());
            }
        }
    }

    private Set<Integer> getMissingNumbersFromSquare(Cell cell) {
        Set<Integer> squareNumbers = new HashSet<>();
        Set<Coordinate> coordinates = coordinatesSquares.get(cell.getCoordinate());
        for (Coordinate coordinate : coordinates) {
            Cell cell1 = sudoku.getCellByCoordinate(coordinate);
            if (cell1.getValue() != 0) squareNumbers.add(cell1.getValue());
        }
        return getMissingNumbersFromList(squareNumbers);
    }

    private Set<Integer> getMissingNumbersFromColumn(int column) {
        Set<Integer> columnNumbers = sudokuScheme.stream()
                .filter(c -> c.getCoordinate().getColumn() == column && c.getValue() != 0)
                .map(Cell::getValue)
                .collect(Collectors.toSet());
        return getMissingNumbersFromList(columnNumbers);
    }

    private Set<Integer> getMissingNumbersFromRow(int row) {
        Set<Integer> rowNumbers = sudokuScheme.stream()
                .filter(c -> c.getCoordinate().getRow() == row && c.getValue() != 0)
                .map(Cell::getValue)
                .collect(Collectors.toSet());
        return getMissingNumbersFromList(rowNumbers);
    }

    private Set<Integer> getMissingNumbersFromList(Set<Integer> numbers) {
        Set<Integer> result = new HashSet<>();
        for (int i : possibleNumbers) {
            if (!numbers.contains(i)) result.add(i);
        }
        return result;
    }

    private boolean isSolved(List<Cell> sudoku) {
        return sudoku.stream().noneMatch(c -> c.getValue() == 0);
    }
}
