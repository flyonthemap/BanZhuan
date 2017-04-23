package com.shuao.banzhuan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.adapter.AppAdapter;
import com.shuao.banzhuan.adapter.SubjectAdapter;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.entity.SubjectInfo;
import com.shuao.banzhuan.holder.ViewPagerHolder;
import com.shuao.banzhuan.manager.ThreadManager;
import com.shuao.banzhuan.protocol.AppProtocol;
import com.shuao.banzhuan.protocol.SubjectProtocol;
import com.shuao.banzhuan.tools.UiTools;
import com.shuao.banzhuan.tools.ViewUtils;
import com.shuao.banzhuan.view.LoadingPage;
import com.shuao.banzhuan.wrapper.ILoadCallback;
import com.shuao.banzhuan.wrapper.LoadMoreAdapterWrapper;
import com.shuao.banzhuan.wrapper.OnLoad;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by flyonthemap on 16/8/4.
 */
public class ActivityFragment extends BaseFragment {
    RecyclerView recyclerView;
    private List<SubjectInfo> subjectInfos;
    private final SubjectProtocol subjectProtocol = new SubjectProtocol();
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeState();
    }

    @Override
    public View createSuccessView() {
        if(recyclerView != null){
            // 移除之前的ListView
            ViewUtils.removeFromParent(recyclerView);
        }
        recyclerView = new RecyclerView(getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        // 设置APPAdapter
        final SubjectAdapter appAdapter = new SubjectAdapter(getActivity());
        appAdapter.appendData(subjectInfos);
        LoadMoreAdapterWrapper baseAdapter = new LoadMoreAdapterWrapper(appAdapter);

        baseAdapter.setOnLoad(new OnLoad() {
            @Override
            public void load(final ILoadCallback callback) {
                ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        subjectInfos = subjectProtocol.load(subjectInfos.size());
                        UiTools.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(subjectInfos != null){
                                    appAdapter.appendData(subjectInfos);
                                    // 在UI线程中更新数据
                                    callback.onSuccess();
                                    if(subjectInfos.size()<Config.PER_PAGE_COUNT)
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
        subjectInfos = subjectProtocol.load(0);
        if(subjectInfos != null){
            return LoadingPage.LoadResult.success;
        }
        return LoadingPage.LoadResult.error;
    }


}
