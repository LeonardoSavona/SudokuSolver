package com.example.sudoku.solver;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Sudoku {

    private List<List<Integer>> sudoku = new ArrayList<>();

    public Sudoku(File sudokuFile) throws Exception {
        loadSudoku(sudokuFile);
    }

    public Sudoku(List<List<Integer>> sudoku) {
        this.sudoku = sudoku;
    }

    private void loadSudoku(File sudokuFile) throws Exception {
        try (FileReader fileReader = new FileReader(sudokuFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line = bufferedReader.readLine();
            while (line != null) {
                List<Integer> row = Arrays.stream(line.split(" "))
                        .map(Integer::new)
                        .collect(Collectors.toList());

                sudoku.add(row);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public int getSize() {
        return sudoku.size();
    }

    public List<List<Integer>> getSudoku() {
        return sudoku;
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
        return sudoku.equals(sudoku1.sudoku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sudoku);
    }
}
