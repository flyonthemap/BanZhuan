package com.shuao.banzhuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.entity.SubjectInfo;
import com.shuao.banzhuan.holder.AppHolder;
import com.shuao.banzhuan.holder.SubjectHolder;

/**
 * Created by flyonthemap on 2017/4/19.
 */

public class SubjectAdapter extends BaseAdapter<SubjectInfo> {
    private Context mContext;

    public SubjectAdapter(Context context) {
        mContext = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_categroy, parent, false);
        return new SubjectHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SubjectHolder) holder).bind(getData().get(position));
    }
}
