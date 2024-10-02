package com.example.sudoku.solver;

import java.util.List;

public class ConsolePrinter {

    public static String getSudokuAsStandardString(Sudoku sudoku) {
        StringBuilder result = new StringBuilder();
        for (List<Integer> row : sudoku.getSudoku()) {
            for (Integer num : row) {
                result.append(num).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static String getSudokuAsString(Sudoku sudoku) {
        StringBuilder result = new StringBuilder();
        for (List<Integer> row : sudoku.getSudoku()) {
            result.append(getColor(-1))
                    .append("+")
                    .append(new String(new char[row.size()]).replace("\0", "-----+"))
                    .append("\n");

            for (Integer num : row) {
                result.append("|")
                        .append(getColor(num))
                        .append(String.format("  %d  ", num))
                        .append(getColor(-1));
            }
            result.append("|\n");
        }
        result.append("+")
                .append(new String(new char[sudoku.getSudoku().get(0).size()]).replace("\0", "-----+"));
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
