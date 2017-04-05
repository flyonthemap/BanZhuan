package com.shuao.banzhuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
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
import com.shuao.banzhuan.view.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by flyonthemap on 16/8/4.
 * Android 登录类的实现
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_username) EditText etUserName;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.btn_login) Button btnLogin;
    @BindView(R.id.btn_register) Button btnRegister; // 登录注册button
    @BindView(R.id.btn_username_clear) Button btnUsernameClear;
    @BindView(R.id.btn_pwd_clear) Button btnPwdClear;
    @BindView(R.id.btn_pwd_eye) Button btnPwdEye;

    public static final int LOGIN_SUCCESS = 2000;
    public static final int LOGIN_ERROR = 2001;
    public static final int LOGIN_PHONE_UNREGISTER = 2002;
    public static final String IS_LOGIN = "isLogin";

    private TextWatcher username_watcher;
    private TextWatcher password_watcher;
    private LoadingDialog loadingDialog;

    private OKClientManager okClientManager;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Config.LOGIN_SUCCESS:
                    loadingDialog.dismiss();
                    // 保存用户的登录状态，确保Activity启动时候的初始界面
                    Toast.makeText(UiTools.getContext(),getString(R.string.login_success),Toast.LENGTH_SHORT).show();
//                    SPUtils.put(UiTools.getContext(),IS_LOGIN,true);
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                // 账号或密码错误
                case Config.LOGIN_ERROR:
                    loadingDialog.dismiss();
                    Toast.makeText(UiTools.getContext(),getString(R.string.account_or_password_is_error),Toast.LENGTH_SHORT).show();
                    break;
                // 手机号为注册
                case LOGIN_PHONE_UNREGISTER:
                    Toast.makeText(UiTools.getContext(),getString(R.string.phone_has_no_register),Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    break;
                // 因为服务器端登录失败
                case Config.LOAD_FAIL_FINISH:
                    loadingDialog.dismiss();
                    Toast.makeText(UiTools.getContext(), getString(R.string.login_fail),Toast.LENGTH_SHORT).show();
                default:
                    loadingDialog.dismiss();
                    break;
            }
        }
    };
  
    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initWatcher();
        etUserName.addTextChangedListener(username_watcher);
        etPassword.addTextChangedListener(password_watcher);

        // 加载对话框的初始化
        loadingDialog = new LoadingDialog(this);

    }

    @Override
    protected void init() {
        okClientManager = OKClientManager.getOkManager();
    }

    //    初始化用户一键清除功能
    private void initWatcher() {
        username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0){
                    btnUsernameClear.setVisibility(View.VISIBLE);
                }else{
                    btnUsernameClear.setVisibility(View.INVISIBLE);
                }
            }
        };

        password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                // 当有字符输入时清除按钮可见
                if(s.toString().length()>0){
                    btnPwdClear.setVisibility(View.VISIBLE);
                }else{
                    btnPwdClear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }
    @OnClick(R.id.btn_pwd_clear)
    void clearPassword(){
        etPassword.setText("");
    }

    @OnClick(R.id.btn_username_clear)
    void clearUsername(){
        etUserName.setText("");
    }

    @OnClick(R.id.btn_login)
    void login(){
        String phoneNum = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        if(!MatchUtil.isPhoneNum(phoneNum)){
            Toast.makeText(LoginActivity.this, R.string.input_true_phoneNum,Toast.LENGTH_SHORT).show();
        }else if(etPassword.getText().toString().equals("")){
            Toast.makeText(LoginActivity.this, R.string.input_password,Toast.LENGTH_SHORT).show();
        }else{
            // 显示对话框
            loadingDialog.show();

            Map<String,String> params = new HashMap<>();
            params.put(Config.PHONE,phoneNum);
            params.put(Config.PASSWORD,password);

            if(okClientManager != null)
                okClientManager.requestGetBySyn(Config.LOGIN_URI, params, new OKClientManager.LoadJsonString() {
                    @Override
                    public void onResponse(String res) {
                        if(res !=  null){
                            JSONObject result = JSONUtils.instanceJsonObject(res);
                            try {
                                switch (result.getInt("code")){
                                    case 0:
                                        handler.sendEmptyMessage(LOGIN_SUCCESS);
                                        break;
                                    case 1:
                                        handler.sendEmptyMessage(LOGIN_ERROR);
                                        break;
                                    case 2:
                                        handler.sendEmptyMessage(LOGIN_PHONE_UNREGISTER);
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            handler.sendEmptyMessageDelayed(Config.LOAD_FAIL_FINISH, 3000);
                        }
                    }
                });

        }
    }

    @OnClick(R.id.btn_pwd_eye)
    void changePwdVisibility(){
        // 密码是否可见的设置
        if(etPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){
            btnPwdEye.setBackgroundResource(R.drawable.eye_close);
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
        }else{
            btnPwdEye.setBackgroundResource(R.drawable.eye_open);
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        etPassword.setSelection(etPassword.getText().toString().length());
    }

    @OnClick(R.id.btn_register)
    void openRegisterAty(){
        Intent launchRegisterActivity  = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(launchRegisterActivity);
        finish();
    }

}
