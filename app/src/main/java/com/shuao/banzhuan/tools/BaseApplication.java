package com.shuao.banzhuan.tools;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;

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


        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
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