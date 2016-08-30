package com.shuao.banzhuan.view;

/**
 * Created by flyonthemap on 16/8/15.
 */

public class ItemCell {
    private String cellValue = "";
    private int cellSpan = 1;
    private CellTypeEnum cellType = CellTypeEnum.LABEL;
    private int colNum = 0;
    //private int rowType = 0;

    private boolean isChange = false;
    public ItemCell(String cellValue,CellTypeEnum cellType,int cellSpan){
        this.cellValue = cellValue;
        this.cellType = cellType;
        this.cellSpan = cellSpan;
    }
    public ItemCell(String cellValue, CellTypeEnum cellType){
        this(cellValue,cellType,1);
    }
    public void setColNum(int colNum){
        this.colNum = colNum;
    }
    public int getColNum(){
        return this.colNum;
    }
    //	public void setRowType(int rowType){
//		this.rowType = rowType;
//	}
//	public int getRowType(){
//		return this.rowType;
//	}
    public String getCellValue(){
        return cellValue;
    }
    public void setCellValue(String value){
        this.cellValue = value;
    }
    public CellTypeEnum getCellType(){
        return cellType;
    }
    public int getCellSpan(){
        return cellSpan;
    }
    public void setIsChange(boolean isChange){
        this.isChange = isChange;
    }
    public boolean getIsChange(){
        return this.isChange;
    }

}
