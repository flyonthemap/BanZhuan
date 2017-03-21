package com.shuao.banzhuan.fragment;

/**
 * Created by flyonthemap on 16/8/4.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.tools.ViewUtils;
import com.shuao.banzhuan.view.LoadingPage;

import java.util.List;

/**
 * BaseFragment抽取Fragment中共性的加载提示逻辑,将Fragment中要显示的界面内容抽取为LoadingPage
 */
public abstract class  BaseFragment extends Fragment {

    private LoadingPage loadingPage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 之前的frameLayout的parent的是viewPager
        if (loadingPage == null) {

            loadingPage = new LoadingPage(getActivity()) {

                @Override
                public View createSuccessView() {
                    return BaseFragment.this.createSuccessView();
                }

                @Override
                public LoadResult load() {
                    return BaseFragment.this.load();
                }
            };
        }else{
            // 从父容器中移除当前视图
            ViewUtils.removeFromParent(loadingPage);
        }


        return loadingPage;
    }


    public void changeState(){
        if(loadingPage != null){
            loadingPage.changeState();
        }
    }

    //    根据不同的数据，返回不同的状态
    public LoadingPage.LoadResult getResultByResponse(List data) {
        if(data == null){
            return LoadingPage.LoadResult.error;
        }else{
            if(data.size() == 0){
                return LoadingPage.LoadResult.empty;
            }else{
                return LoadingPage.LoadResult.success;
            }
        }

    }

    //    创建成功加载的界面
    public abstract View createSuccessView();
    //    从服务器加载数据
    protected abstract LoadingPage.LoadResult load();
}
