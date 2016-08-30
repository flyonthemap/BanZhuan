package com.shuao.banzhuan.tools;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by flyonthemap on 16/8/4.
 * 这个类可以获取到Application提供的相关信息，比如handler和context
 * 注意必须在AndroidManifest注册这个应用才会有效
 */

public class BaseApplication extends Application {
    private static BaseApplication application;
    private static int mainTid;
    private static Handler mainHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        mainTid = android.os.Process.myTid();
        mainHandler = new Handler(getMainLooper());

        //设置主题
        ThemeConfig theme = ThemeConfig.CYAN;
        theme = new ThemeConfig.Builder()
                .build();
        /* 配置功能 */
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setCropSquare(true)
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new PicassoImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(new PicassoPauseOnScrollListener(false, true))
                .build();
        GalleryFinal.init(coreConfig);
    }

//    获取全局的应用信息
    public static Context getApplication() {
        return application;
    }
//    获取主线程的id
    public static int getMainTid(){
        return mainTid;
    }
//    获取主线程的handler
    public static Handler getMainHandler() {
        return mainHandler;
    }


}