package com.example.sudoku.solver;

import java.util.List;
import java.util.Map;

public class ConsolePrinter {

    public static String getSudokuAsStandardString(Sudoku sudoku) {
        StringBuilder result = new StringBuilder();
        for (int r = 0; r < sudoku.getSize(); r++){
            for (int c = 0; c < sudoku.getSize(); c++) {
                result.append(
                        sudoku.getSudoku().get(new Coordinate(r,c))
                ).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static String getSudokuAsString(Sudoku sudoku) {
        StringBuilder result = new StringBuilder();
        int i = 0;

        for (int r = 0; r < sudoku.getSize(); r++) {
            result.append(getColor(-1))
                    .append("+")
                    .append(new String(new char[sudoku.getSize()]).replace("\0", "-----+"))
                    .append("\n");
            for (int c = 0; c < sudoku.getSize(); c++) {
                result.append("|")
                        .append(getColor(sudoku.getSudoku().get(new Coordinate(r,c))))
                        .append(String.format("  %d  ", sudoku.getSudoku().get(new Coordinate(r,c))))
                        .append(getColor(-1));
            }
            result.append("|\n");
        }

        result.append("+")
                .append(new String(new char[sudoku.getSize()]).replace("\0", "-----+"));
        return result.toString();
    }

    private static String getColor(Integer num) {
        if (num <= 0) {
            return "\u001B[0m";
        }
        switch (num % 9) {
            case 0:
                return "\u001B[30m";
            case 1:
                return "\u001B[31m";
            case 2:
                return "\u001B[32m";
            case 3:
                return "\u001B[33m";
            case 4:
                return "\u001B[34m";
            case 5:
                return "\u001B[35m";
            case 6:
                return "\u001B[36m";
            case 7:
                return "\u001B[37m";
            case 8:
                return "\u001B[38m";
            default:
                return "\u001B[0m";
        }
    }
}
