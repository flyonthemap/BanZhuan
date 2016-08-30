package com.shuao.banzhuan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuao.banzhuan.view.LoadingPage;

/**
 * Created by flyonthemap on 16/8/4.
 */
public class ActivityFragment extends BaseFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeState();
    }

    @Override
    public View createSuccessView() {
        return null;
    }

    @Override
    protected LoadingPage.LoadResult load() {
        return LoadingPage.LoadResult.error;
    }
}
