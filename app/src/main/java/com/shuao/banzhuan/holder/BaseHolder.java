package com.shuao.banzhuan.holder;

import android.view.View;

/**
 * Created by flyonthemap on 16/8/9.
 * 这里ListView中Holder的拆分部分
 */

public abstract class BaseHolder<Data>  {
    private View contentView;
    private Data data;
    public BaseHolder(){
        // new BaseHolder的时候会初始化数据
        contentView = initView();
        //将Tag设置为contentView的存储结构
        contentView.setTag(this);
    }
    // 能够在Adapter中获取view对象
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
    // 初始化Holder中的控件
    public  abstract View initView();
    // 填充控件中的数据
    public abstract void refreshView(Data data);
}