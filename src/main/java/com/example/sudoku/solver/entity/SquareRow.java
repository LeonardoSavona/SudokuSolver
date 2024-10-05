package com.example.sudoku.solver.entity;

import java.util.*;
import java.util.stream.Collectors;

public class SquareRow {

    private final int index;
    private Set<Cell> cells = new HashSet<>();

    public SquareRow(int index) {
        this.index = index;
    }

    public SquareRow(Set<Cell> cells, int index) {
        this.cells = cells;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Set<Cell> getCells() {
        return cells;
    }

    public void addCell(Cell cell) {
        this.cells.add(cell);
    }

    public List<Integer> getPossibleValues() {
        return cells.stream()
                .flatMap(cell -> cell.getPossibleValues().stream())
                .collect(Collectors.toList());
    }

    public Set<Integer> getPossibleValuesPresentInEveryCells() {
        Map<Integer, Long> frequencyMap = getPossibleValues().stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()));
        Set<Integer> result = new HashSet<>();
        for (Map.Entry<Integer, Long> entry : frequencyMap.entrySet()) {
            if (entry.getValue() == getCellsWithPossibleValues().size()) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    private Set<Cell> getCellsWithPossibleValues() {
        return cells.stream().filter(c -> c.getPossibleValues().size() > 1).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "Row" + cells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SquareRow row = (SquareRow) o;
        return index == row.index && cells.equals(row.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, cells);
    }
}
