package com.shuao.banzhuan.holder;

import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.tools.PicassoUtils;
import com.shuao.banzhuan.tools.UiTools;

/**
 * Created by flyonthemap on 16/8/16.
 * 应用信息详情页
 */
public class DetailInfoHolder extends BaseHolder<AppInfo>{
    private ImageView iv_item_icon;
    private TextView tv_item_title,tv_item_bonus,tv_item_size,tv_item_version;


    @Override
    public View initView() {
        View view = UiTools.inflate(R.layout.app_detail_info);
        iv_item_icon = (ImageView) view.findViewById(R.id.iv_item_icon);
        tv_item_bonus = (TextView) view.findViewById(R.id.tv_bonus);
        tv_item_size = (TextView) view.findViewById(R.id.tv_item_size);
        tv_item_title = (TextView) view.findViewById(R.id.tv_item_title);
        tv_item_version = (TextView) view.findViewById(R.id.tv_item_version);
        Log.d(Config.DEBUG,"initView");
        return view;
    }

    // 对控件的赋值
    @Override
    public void refreshView(AppInfo appInfo) {
        // 应用大小
        if(appInfo != null){
//            Log.d(Config.DEBUG,appInfo.getAppId());
//            Log.d(Config.DEBUG,appInfo.getName());
//            Log.d(Config.DEBUG,appInfo.getVersion());
//            Log.d(Config.DEBUG,appInfo.getBonus()+"");
            Log.d(Config.DEBUG,appInfo.getSize()+"");
//            Log.d(Config.DEBUG,appInfo.getTaskType());
            tv_item_size.setText("大小:"+Formatter.formatFileSize(UiTools.getContext(),appInfo.getSize()));
            // 设置应用的名字
            tv_item_title.setText(appInfo.getName());
            // 设置应用的版本
            tv_item_version.setText("版本号:"+appInfo.getVersion());
            // 设置应用的奖励金额
            tv_item_bonus.setText(appInfo.getBonus()+"万");
            // 根据图片的URL地址,!!!!!这里的URL地址还要继续进行完善
            PicassoUtils.loadImageWithHolder(Config.BASE_IMAGE_URL+"?name="+appInfo.getIconUrl(),R.drawable.ic_default,iv_item_icon);
        }
    }
}
