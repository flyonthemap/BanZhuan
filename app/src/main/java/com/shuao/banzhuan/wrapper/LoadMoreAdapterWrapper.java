package com.shuao.banzhuan.wrapper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.adapter.BaseAdapter;

/**
 * Created by flyonthemap on 2017/3/21.
 * 这个类只负责加载更多的逻辑
 *
 */

public class LoadMoreAdapterWrapper<T> extends BaseAdapter<T>  {
    private BaseAdapter mAdapter;
    private boolean hasMoreData = true;
    private OnLoad mOnLoad;

    public LoadMoreAdapterWrapper(BaseAdapter adapter) {
        mAdapter = adapter;
    }
    public  void setOnLoad(OnLoad load){
        this.mOnLoad = load;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.list_item_no_more) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new NoMoreItemVH(view);
        } else if (viewType == R.layout.list_item_loading) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new LoadingItemVH(view);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingItemVH) {
            requestData();
        } else if (holder instanceof NoMoreItemVH) {

        } else {
            mAdapter.onBindViewHolder(holder, position);
        }
    }


    private void requestData() {

        //网络请求,如果是异步请求，则在成功之后的回调中添加数据，并且调用notifyDataSetChanged方法，hasMoreData为true
        //如果没有数据了，则hasMoreData为false，然后通知变化，更新recyclerView

        if (mOnLoad != null) {
            mOnLoad.load( new ILoadCallback() {
                @Override
                public void onSuccess() {

                    notifyDataSetChanged();
                    hasMoreData = true;
                }

                @Override
                public void onFailure() {
                    hasMoreData = false;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            if (hasMoreData) {
                return R.layout.list_item_loading;
            } else {
                return R.layout.list_item_no_more;
            }
        } else {
            return mAdapter.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 1;
    }


    static class LoadingItemVH extends RecyclerView.ViewHolder {

        public LoadingItemVH(View itemView) {
            super(itemView);
        }

    }

    static class NoMoreItemVH extends RecyclerView.ViewHolder {

        public NoMoreItemVH(View itemView) {
            super(itemView);
        }
    }

}


