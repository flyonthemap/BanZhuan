package com.shuao.banzhuan.tools;

/**
 * Created by flyonthemap on 16/8/4.
 *
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;


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
    @TargetApi(Build.VERSION_CODES.M)
    public static int getColor(int id) {
        return getContext().getResources().getColor(id,null);
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

}

