package com.shuao.banzhuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by flyonthemap on 2017/3/20.
 */

public abstract class MyBaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Context mContext;
    protected List<T> mData;
    protected LayoutInflater inflater;
    // 默认条目类型
    private static final int DEFAULT_ITEM = 0;
    // 加载更多的条目类型
    private static final int MORE_ITEM = 1;

    public MyBaseRecyclerAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MyBaseRecyclerAdapter(Context context, List<T> data) {
        if (data == null) data = new ArrayList<>();
        this.mContext = context;
        this.mData = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MyBaseRecyclerAdapter(Context context, T[] datas) {
        this.mContext = context;
        this.mData = new ArrayList<T>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Collections.addAll(mData, datas);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size()) { // 当前是最后一个条目
            return MORE_ITEM;
        }
        return getInnerItemViewType(position); // 如果不是最后一个条目 返回默认类型
    }

    private int getInnerItemViewType(int position) {
        return DEFAULT_ITEM;
    }





    /**
     * 更新数据，替换原有数据
     */
    public void updateItems(List<T> items) {
        mData = items;
        notifyDataSetChanged();
    }

    /**
     * 插入一条数据
     */
    public void addItem(T item) {
        mData.add(0, item);
        notifyItemInserted(0);
    }

    /**
     * 插入一条数据
     */
    public void addItem(T item, int position) {
        position = Math.min(position, mData.size());
        mData.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 在列表尾添加一串数据
     */
    public void addItems(List<T> items) {
        int start = mData.size();
        mData.addAll(items);
        notifyItemRangeChanged(start, items.size());
    }

    /**
     * 移除一条数据
     */
    public void removeItem(int position) {
        if (position > mData.size() - 1) {
            return;
        }
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 移除一条数据
     */
    public void removeItem(T item) {
        int position = 0;
        ListIterator<T> iterator = mData.listIterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (next == item) {
                iterator.remove();
                notifyItemRemoved(position);
            }
            position++;
        }
    }

    /**
     * 清除所有数据
     */
    public void removeAllItems() {
        mData.clear();
        notifyDataSetChanged();
    }


}
