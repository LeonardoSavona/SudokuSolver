package com.example.sudoku.solver.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Column {

    private final int index;
    private List<Cell> cells = new ArrayList<>();

    public Column(int index) {
        this.index = index;
    }

    public Column(List<Cell> cells, int index) {
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

    public List<Integer> getColumnPossibleValues() {
        return getCells().stream()
                .flatMap(cell -> cell.getPossibleValues().stream())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Column" + cells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return index == column.index && cells.equals(column.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, cells);
    }
}
