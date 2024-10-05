package com.example.sudoku.solver.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Cell {

    private final Coordinate coordinate;
    private int value;
    private Set<Integer> possibleValues = new HashSet<>();

    public Cell(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Cell(Coordinate coordinate, int value) {
        this.coordinate = coordinate;
        this.value = value;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getValue() {
        return value;
    }

    public Set<Integer> getPossibleValues() {
        return possibleValues;
    }

    public void addPossibleValue(Integer possibleValue) {
        possibleValues.add(possibleValue);
    }

    public void clearPossibleValues() {
        possibleValues.clear();
    }

    public void removePossibleValue(Integer value) {
        possibleValues.remove(value);
    }

    public void setPossibleValues(Set<Integer> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Cell{" + coordinate +
                ", value=" + value +
                ", possibleValues=" + possibleValues +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return coordinate.equals(cell.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }
}
