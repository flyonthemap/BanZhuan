package com.shuao.banzhuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.tools.SPUtils;
import com.shuao.banzhuan.tools.UiTools;

/**
 *  Created by flyonthemap on 16/8/11.
 *  每次启动的时候展示的欢迎界面
 */
public class WelcomeActivity extends Activity {
    private static final int TIME = 1000;
    private static final int GO_HOME = 1000;
    private static final int GO_LOGIN = 1001;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GO_HOME:
                    goHome();
                    break;
                case GO_LOGIN:
                    goLogin();
                    break;

            }
            super.handleMessage(msg);
        }
    };

    private void goLogin() {
        Intent i = new Intent(WelcomeActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void goHome() {
        Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        saveRegisterSate();

    }
    private void saveRegisterSate(){
        // 用sharedPreference保存是否登录的状态，如果没有登录成功的话，则每次都启动登录界面
        Config.isRegister = (boolean) SPUtils.get(UiTools.getContext(),Config.IS_LOGIN,false);
        if(Config.isRegister == true){
            handler.sendEmptyMessageDelayed(GO_HOME,TIME);
        }else {
            handler.sendEmptyMessageDelayed(GO_LOGIN ,TIME);
        }
    }
}
