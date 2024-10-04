package com.example.sudoku.solver.entity;

import com.example.sudoku.solver.helper.ConsolePrinter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Sudoku {

    private List<Cell> sudoku = new ArrayList<>();
    private int size;

    public Sudoku(File sudokuFile) throws Exception {
        loadSudoku(sudokuFile);
    }

    public Sudoku(List<Cell> sudoku) {
        this.sudoku = sudoku;
    }

    private void loadSudoku(File sudokuFile) throws Exception {
        try (FileReader fileReader = new FileReader(sudokuFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            List<List<Integer>> tempSudoku = new ArrayList<>();
            String line = bufferedReader.readLine();
            while (line != null) {
                List<Integer> row = Arrays.stream(line.split(" "))
                        .map(Integer::new)
                        .collect(Collectors.toList());

                tempSudoku.add(row);
                line = bufferedReader.readLine();
            }
            this.size = tempSudoku.size();
            for (int r = 0; r < getSize(); r++) {
                for (int c = 0; c < getSize(); c++) {
                    sudoku.add(
                            new Cell(
                                    new Coordinate(r, c),
                                    tempSudoku.get(r).get(c)
                            )
                    );
                }
            }
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public int getSize() {
        return this.size;
    }

    public List<Cell> getSudoku() {
        return sudoku;
    }

    public Cell getCellByCoordinate(Coordinate coordinate) {
        return sudoku.stream().filter(c -> c.getCoordinate().equals(coordinate)).findFirst().get();
    }

    @Override
    public String toString() {
        return ConsolePrinter.getSudokuAsStandardString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sudoku sudoku1 = (Sudoku) o;
        for (Cell cell : sudoku) {
            Cell sudoku1Cell = sudoku1.getCellByCoordinate(cell.getCoordinate());
            if (cell.getValue() != sudoku1Cell.getValue()) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sudoku);
    }
}
