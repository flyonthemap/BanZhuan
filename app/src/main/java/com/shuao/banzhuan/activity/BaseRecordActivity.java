package com.shuao.banzhuan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.adapter.TableViewAdapter;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.tools.UiTools;
import com.shuao.banzhuan.view.CellTypeEnum;
import com.shuao.banzhuan.view.HeadItemCell;
import com.shuao.banzhuan.view.ItemCell;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by flyonthemap on 16/8/15.
 *
 */
public abstract class BaseRecordActivity extends BaseActivity {

    protected LayoutInflater inflater;
    protected int[] arrHeadWidth = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void init() {
        inflater = LayoutInflater.from(this);
        Resources resources = this.getResources();
        // 获取手机屏幕的宽度
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        Config.HEAD_WIDTH = UiTools.px2dip(width/3);
    }
    // 获取竖直线
    protected View getVerticalLine(){
        return inflater.inflate(R.layout.atom_line_v_view, null);
    }
    // 添加头部的竖直线
    protected void addHeaderVLine(LinearLayout headLayout){
        LinearLayout v_line = (LinearLayout)getVerticalLine();
        v_line.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        headLayout.addView(v_line);
    }
    // 绘制表头
    protected void drawHead(HashMap headMap,LinearLayout headLayout){
        arrHeadWidth = new int[headMap.size()];
        int width ;
        for(int i = 0;i < headMap.size();i++){
            HeadItemCell itemCell = (HeadItemCell)headMap.get(i+"");
            String name = itemCell.getCellValue();
            width = UiTools.dip2px(itemCell.getWidth());
            TextView headView = (TextView) inflater.inflate(R.layout.atom_head_text_view, null);
            if(headView != null){
                headView.setText(Html.fromHtml(name));
                headView.setWidth(width);
                headLayout.addView(headView);
            }
            arrHeadWidth[i] = width;
            // 如果不是最后一个条目，则添加竖线
            if(i != headMap.size()-1){
                this.addHeaderVLine(headLayout);
            }
        }
    }
    // 初始化表头的HashMap
    protected void initHeadHashMap(HashMap headMap,String headName){

        //根据表头的数目对屏幕大小进行均分
        HeadItemCell itemCell = new HeadItemCell(headName,Config.HEAD_WIDTH);
        headMap.put(headMap.size()+"", itemCell);
    }
    // 增加列内容
    protected void addRows(HashMap rowMap,int colSpan,String cellValue,CellTypeEnum cellType){
        ItemCell itemCell = new ItemCell(cellValue,cellType,colSpan);
        rowMap.put(rowMap.size()+"", itemCell);
    }
    /*
    * 抽象出标题名称和标题内容，这两个内容在不同的Activity中是不同的
    *
    * */
    protected abstract void initHeadTitle();
    public abstract void setContent(HashMap contentHashMap);
}


