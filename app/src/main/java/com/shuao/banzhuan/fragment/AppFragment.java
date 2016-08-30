package com.shuao.banzhuan.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;

import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.shuao.banzhuan.activity.MainActivity;
import com.shuao.banzhuan.adapter.ListBaseAdapter;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.holder.ViewPagerHolder;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.protocol.AppProtocol;
import com.shuao.banzhuan.tools.UiTools;
import com.shuao.banzhuan.tools.ViewUtils;
import com.shuao.banzhuan.view.BaseListView;
import com.shuao.banzhuan.view.LoadingPage;

import java.util.ArrayList;
import java.util.List;


public class AppFragment extends BaseFragment {
    private List<AppInfo> data;
    private List<String> pictures; //  顶部ViewPager 显示界面的数据
    private BaseListView listView;
    // 当Fragment挂载的activity创建的时候调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeState();
        Log.d(Config.DEBUG,AppFragment.class.getSimpleName());
    }


    @Override
    public View createSuccessView() {
        if(listView != null){
            // 移除之前的ListView
            ViewUtils.removeParent(listView);
        }
        listView = new BaseListView(UiTools.getContext());
        ViewPagerHolder holder=new ViewPagerHolder();
        pictures = new ArrayList<>();
        pictures.add("haha");
        pictures.add("haha");
        if(pictures != null){
            holder.setData(pictures);
        }
        // 得到holder里面管理的view对象
        View contentView = holder.getContentView();

        // 把holder里的view对象即ViewPager添加到listView的上面
        listView.addHeaderView(contentView);
        Log.d("hehe","createSuccessView");


        listView.setAdapter(new ListBaseAdapter(data,listView){

            @Override
            protected List<AppInfo> onLoadMore() {
                AppProtocol protocol=new AppProtocol();
                List<AppInfo> load = protocol.load(data.size());
                data.addAll(load);
                Log.d(Config.DEBUG,"listView adapter.....");
                return load;
            }
        });




        // 第二个参数 慢慢滑动的时候是否加载图片 false  加载   true 不加载
        //  第三个参数  飞速滑动的时候是否加载图片  true 不加载
//        listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true));
//        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_default);  // 设置如果图片加载中显示的图片
//        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_default);// 加载失败显示的图片

        return listView;
    }


    @Override
    protected LoadingPage.LoadResult load() {
        Log.d(Config.DEBUG,"AppFragment load...");
        AppProtocol protocol=new AppProtocol();
        data = protocol.load(0);  // load index 从哪个位置开始获取数据   0  20  40 60
//        pictures = protocol.getPictures();
        return checkData(data);
    }
}
