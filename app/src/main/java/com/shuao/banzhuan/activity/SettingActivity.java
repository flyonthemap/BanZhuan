package com.shuao.banzhuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.tools.OKClientManager;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;

/**
 * Created by flyonthemap on 16/8/4.
 * 创建用户设置的界面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private Button btn_logout,btn_change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_register);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

    }
    @Override
    protected void initView() {
        setContentView(R.layout.activity_settting);
//        btn_logout = (Button) findViewById(R.id.btn_logout);
//        btn_change_password = (Button) findViewById(R.id.btn_change_password);
//        btn_logout.setOnClickListener(this);
//        btn_change_password.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
//                OKClientManager.getOkManager().asyncByURL(Config.LOGOUT_URL, new OKClientManager.LoadJsonObject() {
//                    @Override
//                    public void onResponse(JSONObject result) throws JSONException {
//                        if(result != null)
//                            Log.d(Config.DEBUG,result.toString());
//
//                    }
//                });
//                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.btn_change_password:
                Log.d(Config.DEBUG,"hahaha");
                OKClientManager.getOkManager().asyncByURL(Config.HOME_URL, new OKClientManager.LoadJsonObject() {
                    @Override
                    public void onResponse(JSONObject result) throws JSONException {
                        Log.d(Config.DEBUG,result.toString());
                    }
                });
                break;
        }
    }
}
