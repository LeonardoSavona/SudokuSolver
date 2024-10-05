package com.example.sudoku.solver.strategy.candidates;

import com.example.sudoku.solver.entity.Cell;
import com.example.sudoku.solver.entity.Sudoku;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CoupleOfCandidatesStrategy extends CandidatesStrategy {

    public CoupleOfCandidatesStrategy(Sudoku sudoku) {
        super(sudoku);
    }

    @Override
    protected void applyCandidatesStrategy(Set<Cell> cells) {
        Map<Cell, Set<Cell>> map = new HashMap<>();
        for (Cell cell : cells) {
            if (cell.getValue() == 0 && cell.getPossibleValues().size() == 2) {
                map.put(cell, getCellsThatHasCellPossibleValues(cell, cells.stream()
                        .filter(c -> c.getValue() == 0 && !c.equals(cell)).collect(Collectors.toSet())));
            }
        }

        for (Map.Entry<Cell, Set<Cell>> entry : map.entrySet()) {
            Cell cell = entry.getKey();
            Set<Cell> cellSet = entry.getValue();

            if (cellSet.size() > 0) {
                Set<Cell> cellsCouple = new HashSet<>(cellSet);
                cellsCouple.add(cell);

                Set<Integer> possibleCellsNumbers = cellsCouple.stream()
                        .map(Cell::getPossibleValues)
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet());

                if (cellsCouple.size() == possibleCellsNumbers.size()) {
                    for (int possibleCellNumber : possibleCellsNumbers) {
                        cells.stream()
                                .filter(c -> !cellsCouple.contains(c))
                                .forEach(c -> c.removePossibleValue(possibleCellNumber));
                    }
                }
            }
        }
    }

    private Set<Cell> getCellsThatHasCellPossibleValues(Cell cell, Set<Cell> cells) {
        return cells.stream()
                .filter(c -> haveSamePossibleValues(c.getPossibleValues(), cell.getPossibleValues()))
                .collect(Collectors.toSet());
    }

    private boolean haveSamePossibleValues(Set<Integer> possibleValues, Set<Integer> possibleValues2) {
        if (possibleValues.size() != possibleValues2.size()) return false;
        return possibleValues.containsAll(possibleValues2);
    }

}
