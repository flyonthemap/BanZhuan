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

    public static BanZhuanApplication instance;// 实例化一个app
    private static ArrayList<Activity> activityStack;// activity启动栈，记录栈中的activity实例
    public static ThreadPoolService service;
    public static UserInfo user = null;
    public static CookieStore cookieStore;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        instance = this;

        activityStack = new ArrayList<Activity>();
        service = new ThreadPoolService();
        MyCrashHandler handler = MyCrashHandler.getInstance();
        handler.init(getApplicationContext());
        //把异常处理的handler设置到主线程里面
        Thread.setDefaultUncaughtExceptionHandler(handler);
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        // 程序安全退出
        for (Activity activity : activityStack) {
            activity.finish();
        }
        activityStack.clear();
        super.onTerminate();
    }

    /**
     * 将应用程序的任务栈中的一activity实例添加到activityStack中
     *
     * @param activity
     *            一个activity实例
     */
    public static void addActivity2Stack(Activity activity) {
        instance.activityStack.add(activity);
    }

    /**
     * 经activity实例从activityStack中移除
     *
     * @param activity
     *            一个activity实例
     */
    public static void removeActivityFromStack(Activity activity) {
        instance.activityStack.remove(activity);
    }

    public static BanZhuanApplication getInstance() {
        return instance;
    }

    /**
     * 将中文字体设置为粗体字 android默认不支持中文
     *
     * @param tv
     */
    public static void setBoldText(TextView tv) {
        TextPaint tp2 = tv.getPaint();
        tp2.setFakeBoldText(true);
    }

    public static SharedPreferences getOrSharedPrefences(Context context) {
        return context.getSharedPreferences(Config.ORPREFERENCES, Context.MODE_PRIVATE);

    }

    public static void showResultToast(int result, Context context) {
        switch (result) {
            case Config.NO_RESPONSE:
                Toast.makeText(context, R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
            case Config.S_EXCEPTION:
                Toast.makeText(context, R.string.server_exception, 0).show();
                break;
            case Config.RESPONESE_EXCEPTION:
                Toast.makeText(context, R.string.responese_exception, 0).show();
                break;
            case Config.TIMEOUT:
                Toast.makeText(context, R.string.timeout, 0).show();
                break;
            case Config.NO_NETWORK:
                Toast.makeText(context, R.string.no_network, 0).show();
                break;
            case Config.NULLPARAMEXCEPTION:
                Toast.makeText(context, R.string.nullparamexception, 0).show();
                break;
            case Config.SERVER_EXCEPTION:
                Toast.makeText(context, R.string.server_exception, 0).show();
                break;
            case Config.RELOGIN:
                CustomDialog dialog = new CustomDialog(context, new CustomDialog.ButtonRespond() {

                    @Override
                    public void buttonRightRespond() {
                        Activity activity = activityStack.get(0);
                        activityStack.remove(0);// 把登录界面提出来
                        BanZhuanApplication.instance.onTerminate();
                        activityStack.add(activity);// 重新放到栈中
                    }

                    @Override
                    public void buttonLeftRespond() {
                        // TODO Auto-generated method stub
                        BanZhuanApplication.instance.onTerminate();
                    }
                });
                dialog.setDialogTitle(R.string.relogin);
                dialog.setDialogMassage(R.string.relogin_message);
                dialog.setLeftButtonText(R.string.exit_app);
                dialog.setRightButtonText(R.string.relogin);
                dialog.setCancelable(false);
                dialog.show();
                break;
            case 4005:
                Toast.makeText(context, "缺少参数", 0).show();
                break;
            case 4006:
                Toast.makeText(context, "参数值不能为空", 0).show();
                break;
            default:
                Toast.makeText(context, "请求响应失败，错误号" + result, 0).show();
                break;
        }
    }
}

