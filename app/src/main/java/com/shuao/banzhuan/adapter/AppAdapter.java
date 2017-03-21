package com.shuao.banzhuan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.holder.AppHolder;
import com.shuao.banzhuan.holder.BaseRecyclerHolder;
import com.shuao.banzhuan.model.AppInfo;

import java.util.List;

/**
 * Created by flyonthemap on 2017/3/20.
 * 将AppViewHolder和数据结合起来
 */

public class AppAdapter extends BaseDefaultRecyclerAdapter<AppInfo>{

    public AppAdapter(Context context, List<AppInfo> data) {
        super(context,data);
    }


    @Override
    public BaseRecyclerHolder getDefaultHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.app_item, parent, false);
        return new AppHolder(view);
    }



    @Override
    protected List<AppInfo> onLoadMore() {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerHolder holder, int position) {

        if(position<mData.size())
            holder.bind(mData.get(position));
        else
            holder.bind(2);
    }

}
