package com.shuao.banzhuan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuao.banzhuan.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by flyonthemap on 16/8/4.
 */
public class BaseActivity extends AppCompatActivity {
    public final static List<BaseActivity> mActivities = new LinkedList<BaseActivity>();
    public static BaseActivity activity;



    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//		receiver=new KillAllReceiver();
//		IntentFilter filter=new IntentFilter("com.shuao.banzhuan.tools");
//		registerReceiver(receiver, filter);


        synchronized (mActivities) {
            mActivities.add(this);
        }
        init();
        initView();
        initToolbar();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (mActivities) {
            mActivities.remove(this);
        }
//		if(receiver!=null){
//			unregisterReceiver(receiver);
//			receiver=null;
//		}
    }
    /*
    * kill all the activity
    * */
    public void killAll() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new LinkedList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            activity.finish();
        }
//        kill the main activity
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    // 初始化Toolbar
    protected void initToolbar() {
    }
    // 初始化视图文件
    protected void initView() {
    }
    // 初始化和View不相关的资源文件
    protected void init() {
    }
}