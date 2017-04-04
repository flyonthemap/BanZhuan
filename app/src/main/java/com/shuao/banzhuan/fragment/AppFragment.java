package com.shuao.banzhuan.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.shuao.banzhuan.adapter.AppAdapter;
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


/*
* 负责App界面的显示
* */
public class AppFragment extends BaseFragment {
    private List<AppInfo> data;
    private List<String> pictures; //  轮播条目需要展示的数据
    private RecyclerView recyclerView;
    // App加载协议
    private final AppProtocol appProtocol = new AppProtocol();

    // 当Fragment挂载的activity创建的时候调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 当Activity首次创建的时候，会访问服务器请求加载数据
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        // 设置APPAdapter
        final AppAdapter appAdapter = new AppAdapter(getActivity());
        appAdapter.appendData(data);
        LoadMoreAdapterWrapper baseAdapter = new LoadMoreAdapterWrapper(appAdapter);
        baseAdapter.setOnLoad(new OnLoad() {
            @Override
            public void load(final ILoadCallback callback) {
                ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        data = appProtocol.load(data.size());
                        UiTools.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(data != null){
                                    appAdapter.appendData(data);
                                    // 在UI线程中更新数据
                                    callback.onSuccess();
                                    if(data.size()<Config.PER_PAGE_COUNT)
                                        callback.onFailure();
                                }
                            }
                        });


                    }
                });
            }
        });




        recyclerView.setAdapter(baseAdapter);

        return recyclerView;
    }


    @Override
    protected LoadingPage.LoadResult load() {
//        Log.d(Config.DEBUG,"AppFragment load...");
        AppProtocol protocol=new AppProtocol();
        data = protocol.load(0);  // load index 从哪个位置开始获取数据   0  20  40 60
//        pictures = protocol.getPictures();
        return getResultByResponse(data);
    }


}
