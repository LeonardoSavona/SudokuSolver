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
                System.out.println("Solution after "+iterations+" iterations: \n"+ ConsolePrinter.getSudokuAsString(sudoku));
        }

        if (solved)
            System.out.println("Solved after "+iterations+" iterations");

        return sudoku;
    }

    private void iterateOverCells(int iterations) {
        for (Cell cell : sudokuScheme) {
            if (cell.getValue() == 0) {
                searchNumberForCell(cell);
            }
        }
    }

    private void searchNumberForCell(Cell cell) {
        if (cell.getPossibleValues().size() == 1) {
            cell.setValue(cell.getPossibleValues().stream().findFirst().get());
            return;
        }

        // Controllo rispetto ai numeri già inseriti
        Set<Integer> rawMissingNumbers = getMissingNumbersFromRow(cell.getCoordinate().getRow());
        if (cell.getPossibleValues().isEmpty())
            cell.setPossibleValues(rawMissingNumbers);

        cell.getPossibleValues().removeIf(n -> !rawMissingNumbers.contains(n));
        if (isNumberFound(cell)) return;

        Set<Integer> colMissingNumbers = getMissingNumbersFromColumn(cell.getCoordinate().getColumn());
        cell.getPossibleValues().removeIf(n -> !colMissingNumbers.contains(n));
        if (isNumberFound(cell)) return;

        Set<Integer> squareMissingNumbers = getMissingNumbersFromSquare(cell);
        cell.getPossibleValues().removeIf(n -> !squareMissingNumbers.contains(n));
        if (isNumberFound(cell)) return;
    }

    private boolean isNumberFound(Cell cell) {
        if (cell.getPossibleValues().size() == 1) {
            cell.setValue((Integer) cell.getPossibleValues().toArray()[0]);
            cell.clearPossibleValues();
            clearOtherCellsPossibleValues(cell);
            return true;
        }
        return false;
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
