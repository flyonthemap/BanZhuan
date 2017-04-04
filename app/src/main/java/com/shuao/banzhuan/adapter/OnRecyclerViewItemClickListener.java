package com.shuao.banzhuan.adapter;

import android.view.View;

/*
* 设置RecyclerView的条目点击事件
* position 表示条目的位置，id表示条目中那个按钮被点击
* */
public interface OnRecyclerViewItemClickListener {
    public void onItemClick(int position, int id);
}
