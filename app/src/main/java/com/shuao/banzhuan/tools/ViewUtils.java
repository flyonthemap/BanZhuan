package com.shuao.banzhuan.tools;

/**
 * Created by flyonthemap on 16/8/4.
 */

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by flyonthemap on 16/7/28.
 * 从父容器上移除当前视图
 */
public class ViewUtils {
    public static void removeParent(View view){
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup){
            ((ViewGroup) parent).removeView(view);
        }
    }
}
