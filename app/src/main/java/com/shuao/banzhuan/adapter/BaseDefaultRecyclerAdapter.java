package com.shuao.banzhuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.holder.AppHolder;
import com.shuao.banzhuan.holder.BaseRecyclerHolder;
import com.shuao.banzhuan.holder.MoreHolder;
import com.shuao.banzhuan.holder.MoreViewHolder;
import com.shuao.banzhuan.manager.ThreadManager;
import com.shuao.banzhuan.tools.UiTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by flyonthemap on 2017/3/20.
 */
public abstract class BaseDefaultRecyclerAdapter<Data> extends RecyclerView.Adapter<BaseRecyclerHolder> {
    protected Context mContext;
    protected List<Data> mData;
    protected LayoutInflater inflater;
    // 默认条目类型
    protected static final int DEFAULT_ITEM = 0;
    // 加载更多的条目类型
    protected static final int MORE_ITEM = 1;

    public List<Data> getData() {
        return mData;
    }



    public BaseDefaultRecyclerAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BaseDefaultRecyclerAdapter(Context context, List<Data> data) {
        if (data == null) data = new ArrayList<>();
        this.mContext = context;
        this.mData = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BaseDefaultRecyclerAdapter(Context context, Data[] datas) {
        this.mContext = context;
        this.mData = new ArrayList<Data>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Collections.addAll(mData, datas);
    }
    public void setData(List<Data> data) {
        this.mData = data;
    }

    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerHolder baseRecyclerHolder;
        if (viewType == DEFAULT_ITEM) {
            baseRecyclerHolder = getDefaultHolder(parent);

        } else {
            // 获取加载更多的条目类型
            View view = inflater.inflate(R.layout.load_more, parent, false);
            baseRecyclerHolder = new MoreViewHolder(view,hasMore());
        }
        return baseRecyclerHolder;
    }

    private boolean hasMore() {
        return true;
    }

    // 获取当前的条目是什么类型
    @Override
    public int getItemViewType(int position) {
        if (position == mData.size()) { // 当前是最后一个条目
            return MORE_ITEM;
        }
        return getInnerItemViewType(position); // 如果不是最后一个条目 返回默认类型
    }

    protected int getInnerItemViewType(int position) {
        return DEFAULT_ITEM;
    }

    // app的条目数+加载更多的条目数
    @Override
    public int getItemCount() {
        return mData.size()+1;
    }
    // 获取默认的条目类型
    public abstract BaseRecyclerHolder getDefaultHolder(ViewGroup parent);
    private MoreHolder holder;
    public void loadMore() {
        ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                // 在子线程中加载更多
                final List<Data> newData = onLoadMore();
                UiTools.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if(newData == null){

                            holder.setData(MoreHolder.LOAD_ERROR);//
                        }else if(newData.size() < 10){
                            Log.d(Config.DEBUG,"has no more....");
                            holder.setData(MoreHolder.HAS_NO_MORE);
                        }else{
                            // 成功了
                            Log.d(Config.DEBUG,"load success..");
                            holder.setData(MoreHolder.HAS_MORE);
                            mData.addAll(newData);//  给listView之前的集合添加一个新的集合
                            notifyDataSetChanged();// 刷新界面

                        }

                    }
                });


            }
        });
    }

    protected abstract List<Data> onLoadMore();
}
