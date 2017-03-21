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
import com.shuao.banzhuan.holder.BaseRecyclerHolder;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.tools.UiTools;

/**
 * Created by flyonthemap on 2017/3/21.
 */

public class NewAppAdapter extends BaseAdapter<AppInfo> {
    private Context mContext;

    public NewAppAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.app_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).bind(getData().get(position));
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivIcon;
        private TextView tvSize;
        private TextView tvDesc;

        public MyViewHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tv_item_title);
            ivIcon = (ImageView) view.findViewById(R.id.iv_item_icon);
            tvSize = (TextView) view.findViewById(R.id.tv_item_size);
        }

        public void bind(AppInfo data) {
            if (data != null) {
                tvName.setText(data.getName());
                tvSize.setText(Formatter.formatFileSize(UiTools.getContext(),
                        data.getSize()));
//            mBitmapUtils.display(ivIcon, HttpHelper.URL + "image?name="
//                    + data.iconUrl);
            }
        }
    }


}