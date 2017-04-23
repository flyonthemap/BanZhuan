package com.shuao.banzhuan.tools;

/**
 * Created by flyonthemap on 16/8/4.
 *
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;


public class UiTools
{
    public static String[] getStringArray(int tabNames)
    {

        return getResource().getStringArray(tabNames);
    }

    public static Context getContext()
    {
        return BaseApplication.getApplication();
    }

    public static Resources getResource()
    {
        return getContext().getResources();
    }

    //    dip转换成px
    public static int dip2px(int dip) {
        final float scale = getResource().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
    public static int px2dip(int px) {
        final float scale = getResource().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


    //  如果在UI Thread中执行，则直接执行代码；反之，则在Handler中执行代码
    public static void runOnUiThread(Runnable runnable) {
        if(isRunOnUIThread()){
            runnable.run();
        }else {
            BaseApplication.getMainHandler().post(runnable);
        }
    }
    public static boolean isRunOnUIThread() {
        // 获取当前线程id, 如果当前线程id和主线程id相同, 那么当前就是主线程
        if(android.os.Process.myTid() == BaseApplication.getMainTid())
        {
            return true;
        }
        return false;
    }
    public static int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return getContext().getResources().getColor(id, null);
        }else{
            return getContext().getResources().getColor(id);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(int id) {
        return getResource().getDrawable(id,null);
    }

    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    public static int getDimens(int homePictureHeight) {
        return (int) getResource().getDimension(homePictureHeight);
    }
    /**
     * 延迟执行 任务
     * @param run   任务
     * @param time  延迟的时间
     */
    public static void postDelayed(Runnable run, int time) {
        BaseApplication.getMainHandler().postDelayed(run, time);
    }
    /**
     * 取消任务
     * @param auToRunTask
     */
    public static void cancel(Runnable auToRunTask) {
        BaseApplication.getMainHandler().removeCallbacks(auToRunTask);
    }
    /**
     *  1、获取main在窗体的可视区域
     *  2、获取main在窗体的不可视区域高度
     *  3、判断不可视区域高度
     *      1、大于100：键盘显示  获取Scroll的窗体坐标
     *                           算出main需要滚动的高度，使scroll显示。
     *      2、小于100：键盘隐藏
     *
     * @param main 根布局
     * @param scroll 需要显示的最下方View
     */
    public static void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                // 获取不可使区域的高度
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 100) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int scrollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, scrollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }
}

