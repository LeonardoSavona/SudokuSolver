package com.example.sudoku.solver;

import java.util.Objects;

public class Coordinate {

    private int raw;
    private int column;

    public Coordinate(int raw, int column) {
        this.raw = raw;
        this.column = column;
    }

    public int getRaw() {
        return raw;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return raw == that.raw && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(raw, column);
    }
}
