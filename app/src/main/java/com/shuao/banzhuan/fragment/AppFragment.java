package com.shuao.banzhuan.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.shuao.banzhuan.adapter.BaseAdapter;
import com.shuao.banzhuan.adapter.NewAppAdapter;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.holder.ViewPagerHolder;
import com.shuao.banzhuan.manager.ThreadManager;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.protocol.AppProtocol;
import com.shuao.banzhuan.tools.UiTools;
import com.shuao.banzhuan.tools.ViewUtils;
import com.shuao.banzhuan.view.LoadingPage;
import com.shuao.banzhuan.wrapper.ILoadCallback;
import com.shuao.banzhuan.wrapper.LoadMoreAdapterWrapper;
import com.shuao.banzhuan.wrapper.OnLoad;

import java.util.ArrayList;
import java.util.List;


public class AppFragment extends BaseFragment {
    private List<AppInfo> data;
    private List<String> pictures; //  轮播条目需要展示的数据
    private RecyclerView recyclerView;
    // App加载协议
    private final AppProtocol appProtocol = new AppProtocol();
    private static final int DEFAULT_ITEM = 0;
    private static final int MORE_ITEM = 1;

    // 当Fragment挂载的activity创建的时候调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeState();
    }


    @Override
    public View createSuccessView() {
        if(recyclerView != null){
            Log.d(Config.DEBUG,"--->result!=null");
            // 移除之前的ListView
            ViewUtils.removeFromParent(recyclerView);
        }
        recyclerView = new RecyclerView(getActivity());
        ViewPagerHolder holder=new ViewPagerHolder();
        pictures = new ArrayList<>();
        pictures.add("haha");
        pictures.add("haha");
        if(pictures != null){
            holder.setData(pictures);
        }
        // 得到holder里面管理的view对象
        View contentView = holder.getContentView();



        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        // 设置APPAdapter
        final NewAppAdapter newAppAdapter= new NewAppAdapter(getActivity());
        newAppAdapter.appendData(data);
        BaseAdapter baseAdapter = new LoadMoreAdapterWrapper(newAppAdapter, new OnLoad() {
            @Override
            public void load(int pagePosition, int pageSize, final ILoadCallback callback) {
                ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<AppInfo> load = appProtocol.load(data.size());
                        Log.d(Config.TAG,"--->"+data.size());
                        if(load != null){
                            Log.d(Config.TAG,"--->load is not null");
                            data.addAll(data);
                        }
                        newAppAdapter.appendData(load);
                        // 在UI线程中更新数据
                        UiTools.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess();
                            }
                        });
                    }
                });



            }
        });


        recyclerView.setAdapter(baseAdapter);
        // 第二个参数 慢慢滑动的时候是否加载图片 false  加载   true 不加载
        //  第三个参数  飞速滑动的时候是否加载图片  true 不加载
//        listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true));
//        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_default);  // 设置如果图片加载中显示的图片
//        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_default);// 加载失败显示的图片

        return recyclerView;
    }


    @Override
    protected LoadingPage.LoadResult load() {
        Log.d(Config.DEBUG,"AppFragment load...");
        AppProtocol protocol=new AppProtocol();
        data = protocol.load(0);  // load index 从哪个位置开始获取数据   0  20  40 60
//        pictures = protocol.getPictures();
        return getResultByResponse(data);
    }


}
