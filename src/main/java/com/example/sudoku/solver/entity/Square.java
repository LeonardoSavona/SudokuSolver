package com.example.sudoku.solver.entity;

import java.util.*;

public class Square {

    private final List<SquareColumn> columns;
    private final List<SquareRow> rows;
    private Set<Cell> cells = new HashSet<>();

    public Square(Set<Cell> cells) {
        this.cells = cells;
        this.columns = extractColumns();
        this.rows = extractRow();
    }

    private List<SquareColumn> extractColumns() {
        Map<Integer, SquareColumn> columnMap = new HashMap<>();
        cells.forEach(c -> {
            columnMap.computeIfAbsent(c.getCoordinate().getColumn(), k -> new SquareColumn(c.getCoordinate().getColumn()))
                    .addCell(c);
        });
        return new ArrayList<>(columnMap.values());
    }

    private List<SquareRow> extractRow() {
        Map<Integer, SquareRow> rowMap = new HashMap<>();
        cells.forEach(c -> {
            rowMap.computeIfAbsent(c.getCoordinate().getRow(), k -> new SquareRow(c.getCoordinate().getRow()))
                    .addCell(c);
        });
        return new ArrayList<>(rowMap.values());
    }

    public List<SquareColumn> getColumns() {
        return columns;
    }

    public List<SquareRow> getRows() {
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
