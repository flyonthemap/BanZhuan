package com.shuao.banzhuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.holder.TaskHolder;
import com.shuao.banzhuan.model.TaskInfo;

/**
 * Created by flyonthemap on 2017/4/17.
 */

public class TaskAdapter extends BaseAdapter<TaskInfo> {
    private Context mContext;
    public TaskAdapter(Context context){
        mContext = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.my_task_item, parent, false);
        return new TaskHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TaskHolder) holder).bind(getData().get(position));
    }
}
