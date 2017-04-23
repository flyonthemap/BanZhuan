package com.shuao.banzhuan;

/**
 * Created by flyonthemap on 2017/3/8.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextPaint;
import android.widget.TextView;
import android.widget.Toast;


import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.entity.UserInfo;
import com.shuao.banzhuan.tools.MyCrashHandler;
import com.shuao.banzhuan.tools.ThreadPoolService;
import com.shuao.banzhuan.view.CustomDialog;

import org.apache.http.client.CookieStore;

import java.util.ArrayList;


/**
 * @Title: CRongApplication.java
 * @Description: 应用程序的主程序
 * @author lanhaizhong
 * @date 2013年7月3日 上午10:31:30
 * @version V1.0 Copyright (c) 2013 Company,Inc. All Rights Reserved.
 */
public class BanZhuanApplication extends Application {

    public static BanZhuanApplication application;// 实例化一个app
    private static ArrayList<Activity> activityStack;// activity启动栈，记录栈中的activity实例
    public static ThreadPoolService service;
    public static UserInfo user = null;
    public static CookieStore cookieStore;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        application = this;

        activityStack = new ArrayList<Activity>();
        service = new ThreadPoolService();
        MyCrashHandler handler = MyCrashHandler.getInstance();
        handler.init(getApplicationContext());
        //把异常处理的handler设置到主线程里面
        Thread.setDefaultUncaughtExceptionHandler(handler);
        super.onCreate();
    }



    public static BanZhuanApplication getInstance() {
        return application;
    }

    /**
     * 添加Activity
     */
    public void addActivity(Activity activity) {
// 判断当前集合中不存在该Activity
        if (!activityStack.contains(activity)) {
            activityStack.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity(Activity activity) {
//判断当前集合中存在该Activity
        if (activityStack.contains(activity)) {
            activityStack.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : activityStack) {
            activity.finish();
        }
    }
}

