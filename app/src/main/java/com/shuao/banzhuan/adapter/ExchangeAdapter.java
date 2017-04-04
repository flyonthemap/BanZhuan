package com.shuao.banzhuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.holder.ExchangeHolder;
import com.shuao.banzhuan.model.ExchangeInfo;

/**
 * Created by flyonthemap on 16/8/13.
 * ExchangeAdapter
 */
public class ExchangeAdapter extends BaseAdapter<ExchangeInfo> {
    private Context mContext;
    public ExchangeAdapter(Context context){
        mContext = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.exchange_item, parent, false);
        return new ExchangeHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ExchangeHolder) holder).bind(getData().get(position));
    }
}
