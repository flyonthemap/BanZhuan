package com.shuao.banzhuan.view;

/**
 * Created by flyonthemap on 16/8/15.
 */
public class HeadItemCell extends ItemCell{
    private int width = 0;
    public HeadItemCell(String cellValue,int width){
        super(cellValue,CellTypeEnum.LABEL);
        this.width = width;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
}

