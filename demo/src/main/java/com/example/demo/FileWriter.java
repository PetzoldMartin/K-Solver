package com.example.demo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by Aismael on 26.08.2017.
 */
public class FileWriter   {

    private final String SEPARATOR = "\t";
    private final String END_OF_LINE = "\n";
    private final String fileNameI;

    public FileWriter(String filenameI) {
        this.fileNameI=filenameI;
    }

    public String doParse(CellMatrix cellMatrix) {
        CellMatrix matrix=cellMatrix;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.getDynamic2DMatrix().size(); i++) {
            for (int o = 0; o < matrix.getDynamic2DMatrix().get(i).size(); o++) {
                sb.append(matrix.getDynamic2DMatrix().get(i).get(o).getValue().toString());
                if (o <( matrix.getDynamic2DMatrix().get(i).size()-1))
                    sb.append(SEPARATOR);
                else
                    sb.append(END_OF_LINE);
            }
        }
        return sb.toString();
    }

    public void write(FileReader f){
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(".").getFile() + fileNameI);//\demo\build\classes\main
        try {
            if (file.createNewFile()) {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file))) {

            String content = this.doParse(f.getCellMatrix());

            bw.write(content);

            // no need to close it.
            //bw.close();

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        }
    }


}
