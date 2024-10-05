package com.example.sudoku.solver.strategy.candidates;

import com.example.sudoku.solver.entity.Cell;
import com.example.sudoku.solver.entity.Sudoku;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TrioOfCandidatesStrategy extends CandidatesStrategy {

    public TrioOfCandidatesStrategy(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    protected void applyCandidatesStrategy(Set<Cell> cells) {
        Map<Cell, Set<Cell>> map = new HashMap<>();
        for (Cell cell : cells) {
            if (cell.getValue() == 0) {
                map.put(cell, getCellsThatContainsCellPossibleValues(cell, cells.stream()
                        .filter(c -> c.getValue() == 0 && !c.equals(cell)).collect(Collectors.toSet())));
            }
        }

        for (Map.Entry<Cell, Set<Cell>> entry : map.entrySet()) {
            Cell cell = entry.getKey();
            Set<Cell> cellSet = entry.getValue();

            if (cellSet.size() == 2) {
                Set<Cell> cellsTrio = new HashSet<>(cellSet);
                cellsTrio.add(cell);

                Set<Integer> possibleCellsNumbers = cellsTrio.stream()
                        .map(Cell::getPossibleValues)
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet());

                if (cellsTrio.size() == possibleCellsNumbers.size()) {
                    for (int possibleCellNumber : possibleCellsNumbers) {
                        cells.stream()
                                .filter(c -> !cellsTrio.contains(c))
                                .forEach(c -> c.removePossibleValue(possibleCellNumber));
                    }
                }
            }
        }
    }

    private Set<Cell> getCellsThatContainsCellPossibleValues(Cell cell, Set<Cell> cells) {
        return cells.stream()
                .filter(c -> c.getPossibleValues().containsAll(cell.getPossibleValues()))
                .collect(Collectors.toSet());
    }

}
