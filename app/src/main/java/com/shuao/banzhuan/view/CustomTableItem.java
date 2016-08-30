package com.shuao.banzhuan.view;

/**
 * Created by flyonthemap on 16/8/15.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuao.banzhuan.R;

import java.util.ArrayList;

public class CustomTableItem extends LinearLayout {
    private Context context = null;
    private boolean isRead = false;
    private ArrayList<View> viewList = new ArrayList();
    private int[] headWidthArr = null;
    private String rowType = "0";

    // 自定义表格条目
    public CustomTableItem(Context context) {
        super(context);
    }
    public CustomTableItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomTableItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    /*
    * buildItem
    *
    */
    public void buildItem(Context context,String rowType,ArrayList<ItemCell> itemCells
            ,int[] headWidthArr,boolean isRead){
        this.setOrientation(LinearLayout.VERTICAL);
        this.context = context;
        this.headWidthArr =headWidthArr.clone();
        this.rowType = rowType;

        this.addCell(itemCells);
    }
    // 逐个来添加条目
    private void addCell(ArrayList<ItemCell> itemCells){
        this.removeAllViews();
        // 水平布局
        LinearLayout secondLayout = new LinearLayout(context);
        secondLayout.setOrientation(LinearLayout.HORIZONTAL);
        // 在
        secondLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        this.addView(secondLayout);
        int cellIndex = 0;
        for(int i=0;i < itemCells.size();i++){
            ItemCell itemCell = itemCells.get(i);
            int endIndex = cellIndex+itemCell.getCellSpan();

            int width = getCellWidth(cellIndex,endIndex);
            cellIndex = endIndex;
        if(itemCell.getCellType() == CellTypeEnum.LABEL){
                TextView view = (TextView)getLabelView();
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                secondLayout.addView(view);
                viewList.add(view);
            }
            //逐个添加竖直的分割线，分割线的颜色为蓝色
            if(i != itemCells.size() - 1){
                LinearLayout v_line = (LinearLayout) getVerticalLine();
                v_line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT));
                secondLayout.addView(v_line);
            }
        }
    }
    // 根据ItemCell条目的类型
    public void refreshData(ArrayList<ItemCell> itemCells){
        for(int i=0;i<itemCells.size();i++){
            ItemCell itemCell = itemCells.get(i);
            if(itemCell.getCellType() == CellTypeEnum.LABEL){
                TextView view = (TextView) viewList.get(i);
                view.setText(itemCell.getCellValue());
            }else if(itemCell.getCellType() == CellTypeEnum.DIGIT){
                EditText view= (EditText) viewList.get(i);
                view.setText(itemCell.getCellValue());
                this.setEditView(view);
                this.setOnKeyBorad(view);
            }else if(itemCell.getCellType() == CellTypeEnum.STRING){
                EditText view= (EditText) viewList.get(i);
                view.setText(itemCell.getCellValue());
                this.setEditView(view);
            }
        }
    }
    // 获得竖直的线
    private View getVerticalLine(){
        return LayoutInflater.from(context).inflate(R.layout.atom_line_v_view, null);
    }
    private int getCellWidth(int cellStart,int cellEnd){
        int width = 0;
        for(int i=cellStart;i<cellEnd;i++){
            width = this.headWidthArr[i] + width;
        }
        return width;
    }
    private View getLabelView(){
        return (View) LayoutInflater.from(context).inflate(R.layout.atom_text_view, null);
    }

    private void setEditView(EditText edtText1){
        if(this.isRead){
            edtText1.setEnabled(false);
        }else{

        }
    }
    private void setOnKeyBorad(EditText edtText1){
        if(!this.isRead){

        }
    }
    public String getRowType() {
        return rowType;
    }
}
