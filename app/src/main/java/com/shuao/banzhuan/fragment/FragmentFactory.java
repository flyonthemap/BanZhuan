package com.shuao.banzhuan.fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flyonthemap on 16/8/4.
 * Fragment利用工厂设计模式进行生产
 */
public class FragmentFactory {
    // 这里用hashMap来管理数据，来避免无谓的创建和销毁
    private static Map<Integer, BaseFragment> mFragments = new HashMap<Integer, BaseFragment>();
    public static BaseFragment createFragment(int position) {
        BaseFragment fragment = mFragments.get(position);
        // HashMap中没有记录保存之前的Fragment
//        BaseFragment fragment = null;
        if (fragment == null) {

            switch (position){
                case 0:
                    fragment = new AppFragment();
                    break;
                case 1:
                    fragment = new ActivityFragment();
                    break;
            }
            // 在集合中缓存当前的Fragment
            if (fragment != null) {
                mFragments.put(position, fragment);
            }
        }
        return fragment;

    }
}
