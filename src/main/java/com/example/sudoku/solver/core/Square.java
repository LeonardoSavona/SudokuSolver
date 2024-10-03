package com.example.sudoku.solver.core;

import java.util.*;
import java.util.stream.Collectors;

public class Square {

    private final List<Column> columns;
    private final List<Row> rows;
    private Set<Cell> cells = new HashSet<>();

    public Square(Set<Cell> cells) {
        this.cells = cells;
        this.columns = extractColumns();
        this.rows = extractRow();
    }

    private List<Column> extractColumns() {
        Map<Integer, Column> columnMap = new HashMap<>();
        cells.forEach(c -> {
            columnMap.computeIfAbsent(c.getCoordinate().getColumn(), k -> new Column(c.getCoordinate().getColumn()))
                    .addCell(c);
        });
        return new ArrayList<>(columnMap.values());
    }

    private List<Row> extractRow() {
        Map<Integer, Row> rowMap = new HashMap<>();
        cells.forEach(c -> {
            rowMap.computeIfAbsent(c.getCoordinate().getRow(), k -> new Row(c.getCoordinate().getRow()))
                    .addCell(c);
        });
        return new ArrayList<>(rowMap.values());
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public Set<Cell> getCells() {
        return cells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return cells.equals(square.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cells);
    }

    @Override
    public String toString() {
        return "Square{" +
                "columns=" + columns +
                ", rows=" + rows +
                '}';
    }
}
