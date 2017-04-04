package com.shuao.banzhuan.holder;

import android.view.View;
import android.widget.ImageView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.tools.PicassoUtils;
import com.shuao.banzhuan.tools.UiTools;

import java.util.List;

/**
 * Created by flyonthemap on 16/8/16.
 * 应用的截图信息
 */
public class DetailScreenHolder extends BaseHolder<AppInfo> {
    private ImageView[] imageViews;
    @Override
    public View initView() {
        View view = View.inflate(UiTools.getContext(), R.layout.detail_screen,null);
        imageViews = new ImageView[5];
        imageViews[0] = (ImageView) view.findViewById(R.id.screen_1);
        imageViews[1] = (ImageView) view.findViewById(R.id.screen_2);
        imageViews[2] = (ImageView) view.findViewById(R.id.screen_3);
        imageViews[3] = (ImageView) view.findViewById(R.id.screen_4);
        imageViews[4] = (ImageView) view.findViewById(R.id.screen_5);
        return view;
    }

    @Override
    public void refreshView(AppInfo appInfo) {
        // 获取图片的URL信息
        List<String> screen = appInfo.getScreen();
//        appInfo.setIconUrl(Config.ICON_URL);
        for (int i = 0; i < imageViews.length; i++) {
            if(i < 5){
                //根据图片的URL来加载图片
                PicassoUtils.loadImageWithSize(Config.BASE_IMAGE_URL+"?name="+screen.get(i),90,150,imageViews[i]);
                imageViews[i].setVisibility(View.VISIBLE);
            }else {
                imageViews[i].setVisibility(View.GONE);
            }
        }
    }
}
