package com.shuao.banzhuan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by flyonthemap on 16/8/4.
 */
public class BaseActivity extends AppCompatActivity {
    public final static List<BaseActivity> mActivities = new LinkedList<BaseActivity>();
    public static BaseActivity activity;


//	private KillAllReceiver receiver;
//	private class KillAllReceiver extends BroadcastReceiver{
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			finish();
//		}
//	}


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
    protected void initToolbar() {
    }
    protected void initView() {
    }
    protected void init() {
    }


}