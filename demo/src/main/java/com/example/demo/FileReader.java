package com.example.demo;

import com.example.demo.cells.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by Aismael on 21.08.2017.
 * a Reader for an Tab Separated Sheet
 * The used Matrix is Dynamic it is Slower than an
 * Straight up [][]Array but more Flexible in Size and handling
 */
@Component
public class FileReader {

    public CellMatrix getCellMatrix() {
        return cellMatrix;
    }

    private  CellMatrix cellMatrix=new CellMatrix();
    private int rows,cols=0;

    @Autowired
    public FileReader(String s) {
        openFile(s);
    }


    public void openFile(String s) {
        try {
            Resource resource = new ClassPathResource(s);
            InputStream iStream = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            //because of using an dynamic Matrix we don't need the Dimension Line
            br.readLine();
            String lineJustFetched;
            while ((lineJustFetched = br.readLine()) != null) {
                rows++;
                cols=0;
                String[] tokens = lineJustFetched.split("\\t");
                for(String elem:tokens){
                    cols++;
                    makeCellsFromInput(elem,rows,cols);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeCellsFromInput(String elem, int rows, int cols) {
        try {
        switch (elem.charAt(0)){
            case (char) 61://=
                this.cellMatrix.addAtPosition(new Cell(this.cellMatrix,elem.substring(1)),rows,cols);
                break;
            case (char) 39://'
                this.cellMatrix.addAtPosition(new Cell(elem.substring(1)),rows,cols);
                break;
            default:
                this.cellMatrix.addAtPosition(new Cell(Integer.parseInt(elem)),rows,cols);
                break;
        }
        }catch (NumberFormatException e){
            this.cellMatrix.addAtPosition(new Cell(e,"Unidentified Number"),rows,cols);
        }catch (StringIndexOutOfBoundsException e){
            this.cellMatrix.addAtPosition(new Cell(),rows,cols);
        }
    }

}