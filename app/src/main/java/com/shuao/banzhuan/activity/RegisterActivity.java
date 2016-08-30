package com.shuao.banzhuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.manager.ThreadManager;
import com.shuao.banzhuan.tools.JudgeUtils;
import com.shuao.banzhuan.tools.LogUtils;
import com.shuao.banzhuan.tools.OKClientManager;
import com.shuao.banzhuan.tools.SPUtils;
import com.shuao.banzhuan.tools.UiTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by flyonthemap on 16/8/4.
 * 这个类是用来进行登录注册的类
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_user_name,et_phone_num,et_password,et_auth_code;
    private Button btn_register,btn_get_code;
    private OKClientManager okClientManager;
    private Timer timer = null;
    private TimerTask timeTask = null;
    // 倒计时时间为60s
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_phone_num = (EditText) findViewById(R.id.et_phone_num);
        et_auth_code = (EditText) findViewById(R.id.et_auth_code);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_get_code = (Button) findViewById(R.id.btn_auth_code);
        if(btn_get_code != null)
            btn_get_code.setOnClickListener(this);
        if(btn_register != null)
            btn_register.setOnClickListener(this);

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
    protected void init() {
        okClientManager = OKClientManager.getOkManager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                register();
                break;
            case R.id.btn_auth_code:
                if(!JudgeUtils.isPhoneNumberValid(et_phone_num.getText().toString())){
                    Toast.makeText(this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                }else {
                    getCode();
                }
                break;
        }
    }

    //提供给服务器合适的数据，进行注册
    private void register() {
        if(et_auth_code.getText().toString().equals("")|| et_auth_code.getText().toString().length() < 6) {
            Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
        }else if(et_user_name.getText().toString().equals("")){
            Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show();
        }else if(et_password.getText().toString().equals("")){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
        }else if(et_phone_num.getText().toString().equals("")){
            Toast.makeText(this,"手机号不能为空",Toast.LENGTH_SHORT).show();}
        else {
            // 提交网络数据
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Config.USER_NAME,et_user_name.getText().toString());
                jsonObject.put(Config.MOBILE,et_phone_num.getText().toString());
                jsonObject.put(Config.PASSWORD,et_password.getText().toString());
                jsonObject.put(Config.SMS_CODE,et_auth_code.getText().toString());
                LogUtils.d(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(okClientManager != null){
                okClientManager.postJsonString(Config.USER_REGISTER_URL, jsonObject.toString(),
                        new OKClientManager.LoadJsonObject() {
                            @Override
                            public void onResponse(JSONObject result) {
                                try {
                                    Log.d(Config.DEBUG,result.toString());
                                    if(result != null)
                                        switch (result.getInt(Config.CODE)){
                                            case 0:
                                                handler.sendEmptyMessage(Config.REGISTER_SUCCESS);
                                                SPUtils.put(UiTools.getContext(),Config.USER_ID,result.getString(Config.USER_ID));
                                                break;
                                            case 1:
                                                handler.sendEmptyMessage(Config.REGISTER_CODE_ERROR);
                                                break;
                                        }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Config.GET_CODE_COUNT:
                    if(Config.count >= 0){
                        btn_get_code.setEnabled(false);
                        btn_get_code.setText(Config.count+"s");
                        btn_register.setEnabled(false);
                        startTime();
                    }else {
                        Config.count = 30;
                        btn_get_code.setEnabled(true);
                        btn_register.setEnabled(true);
                        btn_get_code.setText("获取");
                    }
                    break;
                case Config.GET_CODE_SUCCESS:
                    Toast.makeText(UiTools.getContext(),"验证码已发送请注意查收~",Toast.LENGTH_SHORT).show();
                    break;
                case Config.REGISTER_SUCCESS:
                    Toast.makeText(UiTools.getContext(),"注册成功请登录~",Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(login);
                    finish();
                    break;
                case Config.REGISTER_CODE_ERROR:
                    Toast.makeText(UiTools.getContext(),"验证码错误",Toast.LENGTH_SHORT).show();
                    et_auth_code.setText("");
                    break;
                case Config.GET_CODE_PHONE_EXIST:
                    Toast.makeText(UiTools.getContext(),"手机号已注册",Toast.LENGTH_SHORT).show();
                    break;
                case Config.REGISTER_USER_EXIST:
                    Toast.makeText(UiTools.getContext(),"用户名已存在",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }

        }
    };
    public void getCode() {
        startTime();
        // 执行getCode的网络访问服务
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",et_phone_num.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(okClientManager != null)
            okClientManager.postJsonString(Config.REGISTER_URL, jsonObject.toString(), new OKClientManager.LoadJsonObject() {
                @Override
                public void onResponse(JSONObject result) {
                    // 这里根据服务器的状态进行处理
                    Log.d(Config.DEBUG,result.toString());
                    try {
                        if(result != null){

                            switch (result.getInt(Config.CODE)){
                                case 0:
                                    handler.sendEmptyMessage(Config.GET_CODE_SUCCESS);
                                    break;
                                case 2:
                                    handler.sendEmptyMessage(Config.GET_CODE_PHONE_EXIST);
                                    break;
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

    }

    private void startTime() {
        timer = new Timer();
        timeTask = new TimerTask() {
            @Override
            public void run() {
                --Config.count;
                Message message = handler.obtainMessage();
                message.what = Config.GET_CODE_COUNT;
                handler.sendMessage(message);
            }
        };
        // 设置倒计时的间隔
        timer.schedule(timeTask,1000);
    }
}
