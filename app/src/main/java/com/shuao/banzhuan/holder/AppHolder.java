package com.shuao.banzhuan.holder;

import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.tools.UiTools;


/**
 * Created by dmtec on 2016/8/23.
 *
 */
public class AppHolder extends BaseRecyclerHolder<AppInfo> {

    private TextView tvName;
    private ImageView ivIcon;
    private TextView tvSize;
    private TextView tvDesc;

    public AppHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void initView(View view) {

        tvName = (TextView) view.findViewById(R.id.tv_item_title);
        ivIcon = (ImageView) view.findViewById(R.id.iv_item_icon);
        tvSize = (TextView) view.findViewById(R.id.tv_item_size);
    }



    @Override
    public void refreshView(AppInfo data) {
        if (data != null) {
            tvName.setText(data.getName());
            tvSize.setText(Formatter.formatFileSize(UiTools.getContext(),
                    data.getSize()));
//            mBitmapUtils.display(ivIcon, HttpHelper.URL + "image?name="
//                    + data.iconUrl);
        }
    }
}
