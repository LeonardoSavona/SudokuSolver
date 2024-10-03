package com.example.sudoku.solver.core;

import com.example.sudoku.solver.helper.ConsolePrinter;
import com.example.sudoku.solver.helper.Helper;
import com.example.sudoku.solver.helper.JSONHelper;

import java.util.*;
import java.util.stream.Collectors;

public class SudokuSolver {

    private static final int MAX_ITERATIONS = 100;

    private final Sudoku sudoku;
    private final List<Cell> sudokuScheme;

    private final int[] possibleNumbers;

    private final Map<Coordinate, Set<Coordinate>> coordinatesSquares;
    private Set<Square> squares;

    public SudokuSolver(Sudoku sudoku){
        this.sudoku = sudoku;
        int size = sudoku.getSize();
        this.sudokuScheme = sudoku.getSudoku();

        this.possibleNumbers = new int[size];
        for (int i = 0; i < size; i++) {
            possibleNumbers[i] = i+1;
        }

        coordinatesSquares = Helper.getCoordinatesSquare(size);
        squares = Helper.getSquares(sudoku, coordinatesSquares);
    }

    public Sudoku solve() {
        int iterations = 0;
        boolean solved = false;
        while (!solved && iterations < MAX_ITERATIONS) {
            iterateOverCells();
            solved = isSolved(sudoku.getSudoku());
            iterations++;

            if (iterations < 10 || iterations % 10 == 0)
                JSONHelper.addSudoku(sudoku);
                System.out.println("Solution after "+iterations+" iterations: \n"+ ConsolePrinter.getSudokuAsString(sudoku));
        }

        if (solved)
            System.out.println("Solved after "+iterations+" iterations");

        return sudoku;
    }

    private void iterateOverCells() {
        for (Cell cell : sudokuScheme) {
            if (cell.getValue() == 0) {
                searchNumberForCell(cell);
            }
        }
    }

    private void searchNumberForCell(Cell cell) {
        if (cell.getPossibleValues().size() == 1) {
            cell.setValue(cell.getPossibleValues().stream().findFirst().get());
            cell.clearPossibleValues();
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

        JSONHelper.addSudoku(sudoku);

        // per ogni possibleValue controllare se è l'unico presenta nella riga, colonna o quadrato, se è l'unico, allora settarlo come unico possible value e lanciare isNumberFound()
        for (int possibleValue : cell.getPossibleValues()) {
            if (!isPresentInOtherRowsPossibleValues(cell, possibleValue) ||
                    !isPresentInOtherColumnsPossibleValues(cell, possibleValue) ||
                    !isPresentInOtherSquaresPossibleValues(cell, possibleValue)) {

                cell.clearPossibleValues();
                cell.addPossibleValue(possibleValue);
                break;
            }
        }
        if (isNumberFound(cell)) return;
    }

    private boolean isPresentInOtherSquaresPossibleValues(Cell cell, int possibleValue) {
        Square square = squares.stream().filter(s -> s.getCells().contains(cell)).findFirst().get();
        return square.getCells().stream()
                .anyMatch(c -> c.getValue() == 0 && (c.getPossibleValues().isEmpty() || c.getPossibleValues().contains(possibleValue)));
    }

    private boolean isPresentInOtherRowsPossibleValues(Cell cell, int possibleValue) {
        return sudokuScheme.stream()
                .filter(c -> c.getCoordinate().getRow() == cell.getCoordinate().getRow() &&
                        c.getCoordinate().getColumn() != cell.getCoordinate().getColumn())
                .collect(Collectors.toSet())
                .stream()
                .anyMatch(c -> c.getValue() == 0 && (c.getPossibleValues().isEmpty() || c.getPossibleValues().contains(possibleValue)));
    }

    private boolean isPresentInOtherColumnsPossibleValues(Cell cell, int possibleValue) {
        return sudokuScheme.stream()
                .filter(c -> c.getCoordinate().getColumn() == cell.getCoordinate().getColumn() &&
                                c.getCoordinate().getRow() != cell.getCoordinate().getRow())
                .collect(Collectors.toSet())
                .stream()
                .anyMatch(c -> c.getValue() == 0 && (c.getPossibleValues().isEmpty() || c.getPossibleValues().contains(possibleValue)));
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
                isNumberFound(c);
            }
        });
    }

    private void clearOtherColumnCellsPossibleValues(Cell cell) {
        sudokuScheme.forEach(c -> {
            if (c.getCoordinate().getColumn() == cell.getCoordinate().getColumn() && !c.getPossibleValues().isEmpty()){
                c.removePossibleValue(cell.getValue());
                isNumberFound(c);
            }
        });
    }

    private void clearOtherSquareCellsPossibleValues(Cell cell) {
        Set<Coordinate> coordinates = coordinatesSquares.get(cell.getCoordinate());
        for (Coordinate coordinate : coordinates) {
            Cell cell1 = sudoku.getCellByCoordinate(coordinate);
            if (!cell1.getPossibleValues().isEmpty()) {
                cell1.removePossibleValue(cell.getValue());
                isNumberFound(cell1);
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
