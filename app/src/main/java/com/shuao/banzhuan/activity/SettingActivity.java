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
import com.shuao.banzhuan.tools.SPUtils;
import com.shuao.banzhuan.tools.UiTools;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.HttpUrl;

/**
 * Created by flyonthemap on 16/8/4.
 * 创建用户设置的界面
 */
public class SettingActivity extends BaseActivity{
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.btn_change_password)
    Button btnChangePassword;




    @Override
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_setting);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

    }
    @Override
    protected void initView() {
        setContentView(R.layout.activity_settting);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_logout)
    void exitApplication(){
        SPUtils.clear(UiTools.getContext());
        Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
        startActivity(intent);
        killAll();
    }
    @OnClick(R.id.btn_change_password)
    void changePassword(){
        Intent intent = new Intent(SettingActivity.this,ChangePwdActivity.class);
        startActivity(intent);
    }

}
