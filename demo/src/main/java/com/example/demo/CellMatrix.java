package com.example.demo;

import com.example.demo.cells.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aismael on 21.08.2017.
 * an Dynamic Cell Matrix
 *
 */
public class CellMatrix {

    private List<List<Cell>>  dynamic2DMatrix = new ArrayList<List<Cell>>();
    private int rows = 0;
    private int cols = 0;

    public List<List<Cell>> getDynamic2DMatrix() {
        return dynamic2DMatrix;
    }

    /**
     * Constructor for an 1*1 CellMatrix
     */
    public CellMatrix() {
        dynamic2DMatrix.add(new ArrayList<Cell>());
        rows++;
        dynamic2DMatrix.get(0).add(new Cell());
    }

    /**
     * Ads an Cell at defined Coordinates in the Matrix,
     * if the Matrix is to small it will expanded
     * @param cell the Cell who should be insert
     * @param rows number of row where the Cell would be imported
     * @param cols number of column where the Cell would be imported
     * @return
     */
    public Cell addAtPosition(Cell cell, int rows, int cols) {
        int rowsT = rows - 1;
        int colsT = cols - 1;
        if (rowsT > (dynamic2DMatrix.size()-1)) {
            for (int i = rowsT - this.rows; i >= 0; i--) {
                dynamic2DMatrix.add(new ArrayList<Cell>());
                this.rows++;
            }
        }

        if (colsT > dynamic2DMatrix.get(rowsT).size()-1) {
            for (int i = colsT - dynamic2DMatrix.get(rowsT).size(); i >= 0; i--) {
                dynamic2DMatrix.get(rowsT).add(new Cell());
            }
        }
        dynamic2DMatrix.get(rowsT).set(colsT, cell);
        return dynamic2DMatrix.get(rowsT).get(colsT);
    }

    /**
     * get an Cell at an Position
     * @param x  number of row
     * @param y  number of column
     * @return the Cell
     */
    public Cell getPosition(int x, int y) {
        try {
            return dynamic2DMatrix.get(x-1).get(y-1);
        }catch (IndexOutOfBoundsException e){
            return new Cell(e,"OutOfRange");
        }
    }

    /**
     * get Maximum filled row-number
     * @return row-number
     */
    public int getMaxRows() {
        return dynamic2DMatrix.size();
    }

    /**
     * get Maximum filled column-number
     * @return column-number
     */
    public int getMaxCols() {

        dynamic2DMatrix.forEach(x -> {
            if (x.size() > this.cols) {
                this.cols = x.size();
            }
        });
        return this.cols;
    }
}