package com.example.sudoku.solver;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Sudoku {

    private Map<Coordinate, Integer> sudoku = new HashMap<>();
    private int size;

    public Sudoku(File sudokuFile) throws Exception {
        loadSudoku(sudokuFile);
    }

    public Sudoku(Map<Coordinate, Integer> sudoku) {
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
                    sudoku.put(new Coordinate(r, c), tempSudoku.get(r).get(c));
                }
            }
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public int getSize() {
        return this.size;
    }

    public Map<Coordinate, Integer> getSudoku() {
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
