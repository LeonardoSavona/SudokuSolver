package com.example.sudoku.solver.helper;

import com.example.sudoku.solver.entity.Cell;
import com.example.sudoku.solver.entity.Sudoku;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class JSONHelper {

    private static final String FILE_PATH = "html/sudoku.json";
    private static final JSONArray ITERATIONS = new JSONArray();
    private static int iterations = 0;

    public static void saveChronology() throws URISyntaxException {
        File sudokuFile = new File(
                JSONHelper.class.getClassLoader().getResource(FILE_PATH).toURI());

        try (FileWriter fileWriter = new FileWriter(sudokuFile)) {
            fileWriter.write(ITERATIONS.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addSudoku(Sudoku sudoku) {
        ITERATIONS.add(getSudokuAsJSONObject(sudoku));
        iterations++;
    }

    public static JSONObject getSudokuAsJSONObject(Sudoku sudoku) {
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Cell cell : sudoku.getSudoku()) {
            JSONObject jsonCell = new JSONObject();
            jsonCell.put("row", cell.getCoordinate().getRow());
            jsonCell.put("column", cell.getCoordinate().getColumn());
            jsonCell.put("value", cell.getValue());
            jsonCell.put("possible_values", new ArrayList<>(cell.getPossibleValues()));

            jsonArray.add(jsonCell.clone());
        }

        object.put("cells", jsonArray.clone());
        return object;
    }
}
