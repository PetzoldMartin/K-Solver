package com.example.demo;

import com.example.demo.cells.Cell;
import com.example.demo.cells.CellType;
import com.example.demo.cells.SignType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Several Cell, Cellmatrix, File reader and Writer Tests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    private DemoApplication tester = new DemoApplication();

    @Test
    public void TextCellTest() throws Exception {
        Cell tc1 = new Cell("test");
        assertThat(tc1.getValue()).isEqualTo("test");
        assertThat(tc1.getValue().getClass()).isEqualTo(new String().getClass());
        assertThat(tc1.getCellType()).isEqualTo(CellType.TEXT);
    }

    @Test
    public void NumberCellTest() throws Exception {
        Cell tc1 = new Cell(1);
        assertThat(tc1.getValue()).isEqualTo(1);
        assertThat(tc1.getValue().getClass()).isEqualTo(new Integer(1).getClass());
        assertThat(tc1.getCellType()).isEqualTo(CellType.NUMBER);
    }

    @Test
    public void SignCellTest() throws Exception {
        Cell tc1 = new Cell(SignType.ADD);
        assertThat(tc1.getValue()).isEqualTo(SignType.ADD);
        assertThat(tc1.getValue().getClass()).isEqualTo(SignType.ADD.getClass());
        assertThat(tc1.getCellType()).isEqualTo(CellType.SIGN);
    }

    @Test
    public void EmptyCellTest() throws Exception {
        Cell tc1 = new Cell();
        assertThat(tc1.getValue()).isEqualTo("");
        assertThat(tc1.getCellType()).isEqualTo(CellType.EMPTY);
    }

    @Test
    public void ReferenceTest() throws Exception {
        Cell tc1 = new Cell(new CellMatrix(), 1, 'A');
        assertThat(tc1.getValue()).isEqualTo("");
        assertThat(tc1.getCellType()).isEqualTo(CellType.REFERENCE);
    }

    @Test
    public void CellMatrixAddTest() throws Exception {
        CellMatrix cellMatrix = new CellMatrix();
        assertThat(cellMatrix.getPosition(1, 1).getCellType()).isEqualTo(CellType.EMPTY);
        assertThat(cellMatrix.addAtPosition(new Cell(1), 3, 3).getCellType()).isEqualTo(CellType.NUMBER);
        assertThat(cellMatrix.getPosition(3, 3).getValue()).isEqualTo(1);
        assertThat(cellMatrix.getMaxCols()).isEqualTo(3);
        assertThat(cellMatrix.getMaxRows()).isEqualTo(3);
    }


    @Test
    public void FileReader() throws Exception {
        FileReader fileReader=tester.fileReader;
        assertThat(fileReader.getCellMatrix().getMaxCols()).isEqualTo(4);
        assertThat(fileReader.getCellMatrix().getMaxRows()).isEqualTo(3);

        assertThat(fileReader.getCellMatrix().getPosition(1,1).getCellType()).isEqualTo(CellType.NUMBER);
        assertThat(fileReader.getCellMatrix().getPosition(1,1).getValue()).isEqualTo(12);

        assertThat(fileReader.getCellMatrix().getPosition(1,2).getCellType()).isEqualTo(CellType.EXPRESSION);
        assertThat(fileReader.getCellMatrix().getPosition(1,2).getValue()).isEqualTo(-4);

        assertThat(fileReader.getCellMatrix().getPosition(1,3).getCellType()).isEqualTo(CellType.NUMBER);
        assertThat(fileReader.getCellMatrix().getPosition(1,3).getValue()).isEqualTo(3);

        assertThat(fileReader.getCellMatrix().getPosition(1,4).getCellType()).isEqualTo(CellType.TEXT);
        assertThat(fileReader.getCellMatrix().getPosition(1,4).getValue()).isEqualTo("Sample");

        assertThat(fileReader.getCellMatrix().getPosition(2,1).getCellType()).isEqualTo(CellType.EXPRESSION);
        assertThat(fileReader.getCellMatrix().getPosition(2,1).getValue()).isEqualTo(4);

        assertThat(fileReader.getCellMatrix().getPosition(2,2).getCellType()).isEqualTo(CellType.EXPRESSION);
        assertThat(fileReader.getCellMatrix().getPosition(2,2).getValue()).isEqualTo(-16);

        assertThat(fileReader.getCellMatrix().getPosition(2,3).getCellType()).isEqualTo(CellType.EXPRESSION);
        assertThat(fileReader.getCellMatrix().getPosition(2,3).getValue()).isEqualTo(-4);

        assertThat(fileReader.getCellMatrix().getPosition(2,4).getCellType()).isEqualTo(CellType.TEXT);
        assertThat(fileReader.getCellMatrix().getPosition(2,4).getValue()).isEqualTo("Spread");

        assertThat(fileReader.getCellMatrix().getPosition(3,1).getCellType()).isEqualTo(CellType.TEXT);
        assertThat(fileReader.getCellMatrix().getPosition(3,1).getValue()).isEqualTo("Test");

        assertThat(fileReader.getCellMatrix().getPosition(3,2).getCellType()).isEqualTo(CellType.EXPRESSION);
        assertThat(fileReader.getCellMatrix().getPosition(3,2).getValue()).isEqualTo(1);

        assertThat(fileReader.getCellMatrix().getPosition(3,3).getCellType()).isEqualTo(CellType.NUMBER);
        assertThat(fileReader.getCellMatrix().getPosition(3,3).getValue()).isEqualTo(5);

        assertThat(fileReader.getCellMatrix().getPosition(3,4).getCellType()).isEqualTo(CellType.TEXT);
        assertThat(fileReader.getCellMatrix().getPosition(3,4).getValue()).isEqualTo("Sheet");
    }

    @Test
    public void ErrorFileReader() throws Exception {
        FileReader fileReader=tester.fileReader2;
        assertThat(fileReader.getCellMatrix().getPosition(3,2).getValue()).isEqualTo("#Expression Failure");;
        assertThat(fileReader.getCellMatrix().getPosition(2,2).getValue()).isEqualTo("#Reference Error from an other Cell");
        assertThat(fileReader.getCellMatrix().getPosition(1,1).getValue()).isEqualTo("#Unidentified Number");
        assertThat(fileReader.getCellMatrix().getPosition(2,1).getValue()).isEqualTo("#Reference Error");
        assertThat(fileReader.getCellMatrix().getPosition(2,5).getValue()).isEqualTo("#OutOfRange");
        assertThat(fileReader.getCellMatrix().getPosition(3,4).getValue()).isEqualTo("#OutOfRange from an other Cell");
        assertThat(fileReader.getCellMatrix().getPosition(2,4).getValue()).isEqualTo("#OutOfRange from an other Cell");
        assertThat(fileReader.getCellMatrix().getPosition(1,4).getValue()).isEqualTo("#OutOfRange from an other Cell");
        assertThat(fileReader.getCellMatrix().getPosition(3,3).getValue()).isEqualTo("");
        assertThat(fileReader.getCellMatrix().getPosition(3,1).getValue()).isEqualTo("#Unknown Reference Error");
    }

    @Test
    public void FileWriter() throws Exception {
        FileReader fileReader = tester.fileReader;
        FileWriter fileWriter = new FileWriter("");
        assertThat(fileWriter.doParse(tester.fileReader.getCellMatrix())).isEqualTo("12	-4	3	Sample\n4	-16	-4	Spread\nTest	1	5	Sheet\n");
        assertThat(fileWriter.doParse(tester.fileReader2.getCellMatrix())).isEqualTo("#Unidentified Number\t#Reference Error from an other Cell\t3\t#Reference Error\n" + "#Reference Error\t#Reference Error\t#Reference Error\t#Reference Error\n" + "#Reference Error\t#Reference Error\t\t#Reference Error\n");

    }
}
