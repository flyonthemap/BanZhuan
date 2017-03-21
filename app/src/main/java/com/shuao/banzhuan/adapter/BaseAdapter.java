package com.shuao.banzhuan.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flyonthemap on 2017/3/21.
 * BaseAdapter只负责数据封装
 */

public abstract class BaseAdapter<Data> extends RecyclerView.Adapter {
    protected List<Data> mData = new ArrayList<>();


    public void updateData(List mData) {
        this.mData.clear();
        appendData(mData);
    }

    public void appendData(List mData) {
        if (mData != null && !mData.isEmpty()) {
            this.mData.addAll(mData);
            notifyDataSetChanged();
        }
    }

    public List<Data> getData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}