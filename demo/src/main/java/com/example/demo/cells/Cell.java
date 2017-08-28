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

    /**
     * Constructor for an empty cell
     */
    public Cell(){
        this.cellType = CellType.EMPTY;
    }

    /**
     * Constructor for an number Cell
     * @param number
     */
    public Cell(Integer number) {
        this.cellType = CellType.NUMBER;
        this.number=number;
    }

    /**
     * Constructor for an Text Cell
     * @param text
     */
    public Cell(String text) {
        this.cellType = CellType.TEXT;
        this.text = text;
    }

    /**
     * Constructor for an Expression cell
     * @param cellMatrix
     * @param expressionString
     */
    public Cell(CellMatrix cellMatrix, String expressionString) {
        this.cellType = CellType.EXPRESSION;
        this.cellMatrix = cellMatrix;
        this.expression=readExpressionString(expressionString);
    }

    /**
     * Constructor for an Cell who has an Error at reading
     * @param e
     * @param error
     */
    public Cell(Exception e,String error){
        this.cellType = CellType.ERROR;
        this.text="#"+error;
        this.exception=e;
    }

    /**
     * A mathematik Sign Cell
     * @param signtype
     */
    public Cell(SignType signtype) {
        this.cellType = CellType.SIGN;
        this.signtype = signtype;
    }

    /**
     * A cell which is an Reference to an other Cell
     * @param cellMatrix
     * @param rows
     * @param cols
     */
    public Cell(CellMatrix cellMatrix,int rows,char cols){
        this.cellType = CellType.REFERENCE;
        this.cellMatrix = cellMatrix;
        this.rows=rows;
        this.cols=cols;
    }

    /**
     * Confirms an Single Alphabet Char to an Number
     * A=1, B=2, C=3 usw
     * @param in
     * @return
     */
    private Integer bigCharToInt(char in){
        char ch = 'A';
        int columnsNumber = in - ch + 1;
        return columnsNumber;
    }

    /**
     * Parses an Expression String into an Expression Array
     * @param Expression
     * @return
     */
    private String readExpressionString(String Expression){
        String[] tokens =Expression.split("(?=-)|(?=\\+)|(?=\\*)|(?=\\/)");
        for(String elem:tokens){
            parseExpressionElement(elem);
        }
        return Expression;
    }

    /**
     * finds  the Reference of an Reference Cell
     * @return
     */
    private Cell getReference(){
        return cellMatrix.getPosition(rows,bigCharToInt(cols));
    }

    /**
     * Parses the Elements of an clean Expression String into the Possible Cells
     * @param elem
     */
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

    /**
     * Try to Solve the Expression Array from left to right
     * @return the result
     * @throws ClassCastException
     * @throws NumberFormatException
     * @throws IndexOutOfBoundsException
     */
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

    /**
     * try to parse an Cell to an Int Value
     * @param cell
     * @return int Value
     * @throws ClassCastException
     * @throws NumberFormatException
     */
    private int makeCellToInt(Cell cell) throws ClassCastException,NumberFormatException{
        if(cell.getValue() instanceof Integer) {
            return (Integer) cell.getValue();
        }else{
            return Integer.parseInt((String)cell.getValue());
        }
    }

    /**
     * return the Value of an Cell by his CellType
     * @return Value as String or Integer
     */
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

