package com.shuao.banzhuan.holder;

import android.view.View;

/**
 * Created by flyonthemap on 16/8/9.
 * holder和view之间形成了一种互相引用的结构
 */

public abstract class BaseHolder<Data>  {
    private View contentView;
    private Data data;
    public BaseHolder(){
        // new BaseHolder的时候会初始化数据
        contentView = initView();
        // 做标记
        contentView.setTag(this);
    }
    public View getContentView() {
        return contentView;
    }

    public void setData(Data data){
        this.data = data;
        refreshView(data);
    }
    public Data getData(){
        return data;
    }
    // 创建界面
    public  abstract View initView();
    // 根据数据刷新界面
    public abstract void refreshView(Data data);
}