package com.example.demo.cells;

import com.example.demo.CellMatrix;

import java.util.ArrayList;

/**
 * Created by Aismael on 21.08.2017.
 * a Cell of the Cell-Matrix the different Constructors
 * decide the Cell-type
 * an Alternative off the Cell-type decision
 * can be realized with subtyping
 */
public class Cell {
    private  Exception exception;
    CellType cellType=CellType.EMPTY;
    CellMatrix cellMatrix;
    Integer number = null;
    String text = null;
    SignType signtype = null;
    String expression = null;
    ArrayList<Cell> expressionList=new ArrayList<Cell>();
    int rows;
    char cols;

    public Cell(){
        this.cellType = CellType.EMPTY;
    }
    public Cell(Integer number) {
        this.cellType = CellType.NUMBER;
        this.number=number;
    }

    public Cell(String text) {
        this.cellType = CellType.TEXT;
        this.text = text;
    }

    public Cell(CellMatrix cellMatrix, String expressionString) {
        this.cellType = CellType.EXPRESSION;
        this.cellMatrix = cellMatrix;
        this.expression=readExpressionString(expressionString);
    }

    public Cell(Exception e,String error){
        this.cellType = CellType.ERROR;
        this.text="#"+error;
        this.exception=e;
    }
    public Cell(SignType signtype) {
        this.cellType = CellType.SIGN;
        this.signtype = signtype;
    }

    public Cell(CellMatrix cellMatrix,int rows,char cols){
        this.cellType = CellType.REFERENCE;
        this.cellMatrix = cellMatrix;
        this.rows=rows;
        this.cols=cols;
    }



    private Integer bigCharToInt(char in){
        char ch = 'A';
        int columnsNumber = in - ch + 1;
        return columnsNumber;
    }
    private String readExpressionString(String Expression){
        String[] tokens =Expression.split("(?=-)|(?=\\+)|(?=\\*)|(?=\\/)");
        for(String elem:tokens){
            parseExpressionElement(elem);
        }
        return Expression;
    }

    private Cell getReference(){
        return cellMatrix.getPosition(rows,bigCharToInt(cols));
    }

    private void parseExpressionElement(String elem){
        if((char)65<=elem.charAt(0)&&elem.charAt(0)<=(char)90){
            expressionList.add(new Cell(cellMatrix, Integer.parseInt((""+elem.charAt(1))),elem.charAt(0)));
        }
        if((char)49<=elem.charAt(0)&&elem.charAt(0)<=(char)57){
            expressionList.add(new Cell(Integer.parseInt(elem)));
        }
        if(elem.charAt(0)==(char)42){//*
            expressionList.add(new Cell(SignType.MUL));
            parseExpressionElement(elem.substring(1));
        }
        if(elem.charAt(0)==(char)43){//+
            expressionList.add(new Cell(SignType.ADD));
            parseExpressionElement(elem.substring(1));
        }
        if(elem.charAt(0)==(char)45){//-
            expressionList.add(new Cell(SignType.SUB));
            parseExpressionElement(elem.substring(1));
        }
        if(elem.charAt(0)==(char)47){///
            expressionList.add(new Cell(SignType.DIV));
            parseExpressionElement(elem.substring(1));
        }
    }

    private Integer solveExpression() throws ClassCastException,NumberFormatException,IndexOutOfBoundsException{
        int i=0;
        Integer sum =makeCellToInt(expressionList.get(0));
        for (Cell cell: expressionList) {
            if(cell.getValue() instanceof SignType){
                switch ((SignType)cell.getValue()){
                    case ADD:
                        sum=sum+makeCellToInt(expressionList.get(i+1));
                        break;
                    case SUB:
                        sum=sum-makeCellToInt(expressionList.get(i+1));
                        break;
                    case MUL:
                        sum=sum*makeCellToInt(expressionList.get(i+1));
                        break;
                    case DIV:
                        sum=sum/makeCellToInt(expressionList.get(i+1));
                        break;
                }
            }
            i++;
        }

        return sum;
    }

    private int makeCellToInt(Cell cell) throws ClassCastException,NumberFormatException{
        if(cell.getValue() instanceof Integer) {
            return (Integer) cell.getValue();
        }else{
            return Integer.parseInt((String)cell.getValue());
        }
    }

    public Object getValue() {
        switch (this.cellType) {
            case TEXT:
                return this.text;
            case ERROR:
                if(this.text!=null) {
                    return this.text;
                }else{
                    return "#Reference Error";
                }
            case NUMBER:
                return this.number;
            case SIGN:
                return this.signtype;
            case EXPRESSION:
                try {
                    return solveExpression();
                }catch (ClassCastException e){
                    this.cellType=CellType.ERROR;
                    return "#Expression Failure";
                }catch (NumberFormatException e){
                    this.cellType=CellType.ERROR;
                    String str="#"+e.getMessage().split(":")[1].split("#")[1];
                    return str.substring(0, str.length() - 1)+" from an other Cell";

                }catch (IndexOutOfBoundsException e){
                    this.cellType=CellType.ERROR;
                    return "#Unknown Reference Error";
                }
            case EMPTY:
                return "";
            case REFERENCE:
                return getReference().getValue();
            default:
                return "#Unknown Cell";
        }
    }

    public CellType getCellType() {
        return cellType;
    }


}

