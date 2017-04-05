package com.shuao.banzhuan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.fragment.FragmentFactory;
import com.shuao.banzhuan.tools.UiTools;

/**
 * Created by flyonthemap on 2017/4/5.
 */


public class MainAdapter extends FragmentStatePagerAdapter {
    private String[] tabNames;
    public MainAdapter(FragmentManager fm) {
        super(fm);
        tabNames = UiTools.getStringArray(R.array.tab_names);
    }

    //  创建Fragment使用的是工厂设计模式
    @Override
    public Fragment getItem(int position) {
        return FragmentFactory.createFragment(position);
    }

    @Override
    public int getCount() {
        return tabNames.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }

}