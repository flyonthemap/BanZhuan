package com.shuao.banzhuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.holder.AppHolder;
import com.shuao.banzhuan.holder.BaseRecyclerHolder;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.tools.UiTools;

/**
 * Created by flyonthemap on 2017/3/21.
 */

public class AppAdapter extends BaseAdapter<AppInfo> {
    private Context mContext;

    public AppAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.app_item, parent, false);
        return new AppHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((AppHolder) holder).bind(getData().get(position));
    }




}