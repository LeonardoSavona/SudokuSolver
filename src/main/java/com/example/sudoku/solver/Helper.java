package com.example.sudoku.solver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Helper {

    public static Map<Coordinate, Set<Coordinate>> getCoordinatesSquare(int size) {
        Map<Coordinate, Set<Coordinate>> coordinatesSquares = new HashMap<>();

        int sq = (int) Math.sqrt(size);

        for (int r = 0; r < size; r += sq) {
            for (int c = 0; c < size; c += sq) {
                Coordinate coordinate = new Coordinate(r,c);
                Set<Coordinate> otherCoordinates = getOtherCoordinates(coordinate, sq);

                coordinatesSquares.put(coordinate, otherCoordinates);
            }
        }

        Map<Coordinate, Set<Coordinate>> toAdd = new HashMap<>();
        for (Map.Entry<Coordinate, Set<Coordinate>> entry : coordinatesSquares.entrySet()) {
            for (Coordinate coordinate : entry.getValue()) {

                Set<Coordinate> coordinatesToAdd = new HashSet<>(entry.getValue());
                coordinatesToAdd.add(entry.getKey());
                coordinatesToAdd.remove(coordinate);
                toAdd.put(coordinate, coordinatesToAdd);
            }
        }

        coordinatesSquares.putAll(toAdd);
        return coordinatesSquares;
    }

    private static Set<Coordinate> getOtherCoordinates(Coordinate coordinate, int sq) {
        int raw = coordinate.getRow();
        int col = coordinate.getColumn();

        Set<Coordinate> result = new HashSet<>();
        for (int r = raw; r < raw + sq; r++){
            for (int c = col; c < col + sq; c++) {
                result.add(new Coordinate(r, c));
            }
        }

        return result;
    }
}
