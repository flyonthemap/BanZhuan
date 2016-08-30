package com.shuao.banzhuan.holder;

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
 */
public class AppHolder extends BaseHolder<AppInfo> {

    private TextView tvName;
    private ImageView ivIcon;
    private TextView tvSize;
    private TextView tvDesc;
    private RatingBar rbStar;

    @Override
    public View initView() {
        View view = View.inflate(UiTools.getContext(), R.layout.app_item,
                null);
        tvName = (TextView) view.findViewById(R.id.tv_item_title);
        ivIcon = (ImageView) view.findViewById(R.id.iv_item_icon);
        tvSize = (TextView) view.findViewById(R.id.tv_item_size);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        if (data != null) {
            tvName.setText(data.getName()                      );
            tvSize.setText(Formatter.formatFileSize(UiTools.getContext(),
                    data.getSize()));
//            mBitmapUtils.display(ivIcon, HttpHelper.URL + "image?name="
//                    + data.iconUrl);
        }
    }
}
