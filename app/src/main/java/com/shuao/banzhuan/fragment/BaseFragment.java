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
 * Created by flyonthemap on 16/7/29.
 * 共性代码提取到基类中，如何增加代码的可阅读性，将代码拆分到其他的类中
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
            ViewUtils.removeParent(loadingPage);
        }


        return loadingPage;
    }


    public void changeState(){
        if(loadingPage != null){
            loadingPage.changeState();
        }
    }

    //    根据不同的数据，返回不同的状态
    public LoadingPage.LoadResult checkData(List data) {
//        Log.d(Config.DEBUG,""+data.size());
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
