package com.shuao.banzhuan.activity;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.tools.JSONUtils;
import com.shuao.banzhuan.tools.MatchUtil;
import com.shuao.banzhuan.tools.OKClientManager;
import com.shuao.banzhuan.tools.SPUtils;
import com.shuao.banzhuan.tools.UiTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by flyonthemap on 2017/4/18.
 */

public class ChangePwdActivity extends BaseActivity {
    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private static final String OLD_PASSWORD = "oldPassword";
    private static final String NEW_PASSWORD = "newPassword";
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    @Override
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_change_password);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.btn_confirm)
    void confirm(){
        String oldPassword = etOldPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        Log.e(Config.TAG,"old=" +oldPassword +"new=" + newPassword);
        String phoneNum = (String) SPUtils.get(UiTools.getContext(),Config.PHONE,"");
        if(oldPassword.equals("")){
            Toast.makeText(UiTools.getContext(),"请输入旧密码",Toast.LENGTH_SHORT).show();
        }else if ("".equals(newPassword)) {
            Toast.makeText(this, R.string.null_password, Toast.LENGTH_SHORT).show();
            return;
        } else if (newPassword.length() < 6) {
            Toast.makeText(this, R.string.short_password, Toast.LENGTH_SHORT).show();
            return;
        }else if (MatchUtil.isAllNumber(newPassword)) {
            Toast.makeText(this, R.string.number_password, Toast.LENGTH_SHORT).show();
        } else if (!MatchUtil.isLicitPassword(newPassword)) {
            Toast.makeText(this, R.string.error_password, Toast.LENGTH_SHORT).show();
            return;
        }else{
            Map<String,String> params = new HashMap<>();
            params.put(OLD_PASSWORD,oldPassword);
            params.put(NEW_PASSWORD,newPassword);
            params.put(Config.PHONE,phoneNum);
            OKClientManager.getOkManager().requestPostBySyn(Config.CHANGE_PWD_URI, params, new OKClientManager.LoadJsonString() {
                @Override
                public void onResponse(String res) {
                    JSONObject result = JSONUtils.instanceJsonObject(res);
                    try {
                        switch (result.getInt(Config.CODE)){
                            case SUCCESS:
                                UiTools.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UiTools.getContext(),"修改成功，请重新登录",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ChangePwdActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case FAIL:
                                UiTools.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UiTools.getContext(),"修改失败，请稍后重试",Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
