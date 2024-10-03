package com.example.sudoku.solver.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "Row" + cells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return index == row.index && cells.equals(row.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, cells);
    }
}
