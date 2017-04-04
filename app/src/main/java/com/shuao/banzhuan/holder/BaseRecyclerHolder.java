package com.shuao.banzhuan.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by flyonthemap on 2017/3/20.
 * 这是RecyclerView的基类，主要负责控件的加载和数据填充
 */

public abstract class BaseRecyclerHolder<Data> extends RecyclerView.ViewHolder  {
    protected Data mData;
    public BaseRecyclerHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    public Data getData(){
        return mData;
    }
    // 绑定数据
    public void bind(Data data){
        mData = data;
        refreshView(data);
    }
    // 初始化Holder中的控件
    public  abstract void initView(View view);
    // 填充控件中的数据
    public abstract void refreshView(Data data);
}
