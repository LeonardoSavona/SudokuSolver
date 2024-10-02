package com.example.sudoku.solver;

import java.util.ArrayList;
import java.util.List;

public class Row {

    private final int index;
    private List<Cell> cells = new ArrayList<>();

    public Row(int index) {
        this.index = index;
    }

    public Row(List<Cell> cells, int index) {
        this.cells = cells;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void addCell(Cell cell) {
        this.cells.add(cell);
    }
}
