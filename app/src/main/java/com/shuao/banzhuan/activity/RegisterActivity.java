package com.shuao.banzhuan.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.tools.JSONUtils;
import com.shuao.banzhuan.tools.MatchUtil;
import com.shuao.banzhuan.tools.OKClientManager;
import com.shuao.banzhuan.view.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by flyonthemap on 16/8/4.
 *  用户注册模块
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llStepNavigate;// 页面第一第二步的导航条
    private TextView tvStep1;
    private TextView tvStep2;
    private ViewFlipper vfRegister;// 页面切换的容器
    private int viewIndex = Toast.LENGTH_SHORT;// 初始化当前页排位

    private LoadingDialog loadingDialog ;// 网络访问提示对话框
    private String url;   // 网络访问url
    // step1界面元素
    private EditText etPhoneNum;// 电话号码输入框
    private String phone;
    private Button btGetCode;// 获取验证码按钮;
    private EditText etCheckCode;// 验证码输入框
    private Button btNextStep;// 下一步按钮
    private CheckBox cbAccepted;// 同意复选框
    private TextView tvProtocolOfOrong;// 服务条款标题

    // step2的界面元素
    private EditText etUserName;// 电话或用户名
    private EditText etPassWord;// 登录密码
    private EditText etConfirmPassWord;// 确认密码密码
    private Button btRegister;// 注册
    private TextView tvConfirmPassword;
    private int validTime = 30;
    private Runnable timerRunnable;// 定时器
    private static final int TIMECHANGE = 2000;
    private LoadingDialog registerDialog;
    // 定时结束
    private static final int TIMERCOMPLETE = 3000;
    OKClientManager okClientManager;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIMECHANGE:
                    Bundle data = msg.getData();
                    int time = data.getInt("time");
                    btGetCode.setText(time+"s");
                    break;
                case TIMERCOMPLETE:
                    btGetCode.setClickable(true);
                    setButtonBackground(btGetCode);
                    btGetCode.setText(R.string.get_verification_code);
                    btNextStep.setClickable(false);
                    setButtonBackground(btNextStep);
                    handler.removeCallbacks(timerRunnable);
                    timerRunnable = null;
                    break;
                // 获取验证码成功
                case Config.GET_CODE_SUCCESS:
                    //对话框提示
                    loadingDialog.dismissSuc(getString(R.string.send_message));
                    btGetCode.setClickable(false);
                    setButtonBackground(btGetCode);
                    if (cbAccepted.isChecked()) {
                        btNextStep.setClickable(true);
                        setButtonBackground(btNextStep);
                    }
                    // 定时器
                    if (timerRunnable != null) {
                        handler.removeCallbacks(timerRunnable);
                        timerRunnable = null;
                    }
                    timerRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (validTime > 0) {
                                validTime--;
                                Message msg = handler.obtainMessage();
                                msg.what = TIMECHANGE;
                                Bundle bundle = new Bundle();
                                bundle.putInt("time", validTime);
                                msg.setData(bundle);
                                msg.sendToTarget();
                                handler.postDelayed(timerRunnable, 1000);
                            } else {
                                handler.sendEmptyMessage(TIMERCOMPLETE);
                            }
                        }
                    };
                    handler.post(timerRunnable);
                    break;
                // 手机号已被注册
                case Config.GET_CODE_PHONE_EXIST:
                    loadingDialog.dismissFail(getString(R.string.phone_has_register));
                    break;
                // 加载时间过程
                case Config.LOAD_FAIL_FINISH:
                    Toast.makeText(RegisterActivity.this,getString(R.string.check_netword_setting),Toast.LENGTH_SHORT).show();
                    break;
                // 验证码验证失败
                case Config.CODE_CONFIRM_FAIL:
                    Toast.makeText(RegisterActivity.this,getString(R.string.fail_please_try_later),Toast.LENGTH_SHORT).show();

                    break;
                // 验证码验证成功
                case Config.CODE_CONFIRM_SUCCESS:
                    Toast.makeText(RegisterActivity.this,getString(R.string.confirm_success),Toast.LENGTH_SHORT).show();

                    llStepNavigate.setBackgroundResource(R.drawable.step2);
                    tvStep1.setTextColor(getResources().getColor(R.color.white2));
                    tvStep2.setTextColor(getResources().getColor(R.color.white2));
                    vfRegister.showNext();
                    viewIndex++;
                    break;
                // 注册成功
                case Config.REGISTER_SUCCESS:
                    registerDialog.dismissSuc(getString(R.string.register_success_please_login));
                    // 启动登录界面
                    Intent login = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(login);
                    finish();

                    break;
                // 注册失败
                case Config.REGISTER_FAIL:
                    registerDialog.dismissSuc(getString(R.string.register_fail_please_try_later));
                    break;
                default:
                    loadingDialog.dismiss();
                    break;
            }
        }
    };



    @Override
    protected void initView() {
        setContentView(R.layout.activity_register);
        llStepNavigate = (LinearLayout) this.findViewById(R.id.ll_step_navigate);
        tvStep1 = (TextView) this.findViewById(R.id.tv_step1);
        tvStep2 = (TextView) this.findViewById(R.id.tv_step2);

        vfRegister = (ViewFlipper) this.findViewById(R.id.vfl_register);
        loadingDialog = new LoadingDialog(this);
        registerDialog = new LoadingDialog(this);
        // step1
        etPhoneNum = (EditText) this.findViewById(R.id.et_phonenum);
        btGetCode = (Button) this.findViewById(R.id.bt_get_checkCode);
        btGetCode.setOnClickListener(this);

        etCheckCode = (EditText) this.findViewById(R.id.et_checkcode);
        // 同意并已经输入验证码才能实行下一步
        cbAccepted = (CheckBox) this.findViewById(R.id.cb_accepted);
        cbAccepted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 获取四位验证码数字
                btNextStep.setClickable(isChecked && etCheckCode.getText().toString().trim().length() == 4 && !btGetCode.isClickable());
                setButtonBackground(btNextStep);
            }
        });

        tvProtocolOfOrong = (TextView) this.findViewById(R.id.tv_protocol_of_orong);
        tvProtocolOfOrong.setOnClickListener(this);

        btNextStep = (Button) this.findViewById(R.id.bt_next_step);
        btNextStep.setOnClickListener(this);
        btNextStep.setClickable(false);
        setButtonBackground(btNextStep);

        // step2
        etUserName = (EditText) this.findViewById(R.id.et_nickname);
        etPassWord = (EditText) this.findViewById(R.id.et_login_paw);
        tvConfirmPassword = (TextView) this.findViewById(R.id.tv_password_confirm);
        etConfirmPassWord = (EditText) this.findViewById(R.id.et_confirm_paw);
        etConfirmPassWord.setOnClickListener(this);
        etConfirmPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!etConfirmPassWord.getText().toString().equals(etPassWord.getText().toString())){
                    tvConfirmPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etConfirmPassWord.getText().toString().equals(etPassWord.getText().toString())){
                    tvConfirmPassword.setVisibility(View.INVISIBLE);
                }else {
                    tvConfirmPassword.setVisibility(View.VISIBLE);
                }
            }
        });
        etPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!etConfirmPassWord.getText().toString().equals(etPassWord.getText().toString())){
                    tvConfirmPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etConfirmPassWord.getText().toString().equals(etPassWord.getText().toString())){
                    tvConfirmPassword.setVisibility(View.INVISIBLE);
                }else {
                    tvConfirmPassword.setVisibility(View.VISIBLE);
                }
            }
        });



        btRegister = (Button) this.findViewById(R.id.bt_register);
        btRegister.setOnClickListener(this);

        // 试图 获取本机电话号码并显示到电话号输入框
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String telephone = manager.getLine1Number();
        if (telephone != null) {
            if (telephone.startsWith("+86")) {
                telephone = telephone.substring(3);
            } else if (telephone.startsWith("86")) {
                telephone = telephone.substring(2);
            }
        }
        etPhoneNum.setText(telephone);
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

    // 设置顶部步骤选择按钮的状态
    private void setButtonBackground(Button button) {
        if (button.isClickable()) {
            button.setBackgroundResource(R.drawable.register_step_background_selector);
        } else {
            button.setBackgroundResource(R.drawable.unclickble_right_round);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_checkCode:
                Log.e(Config.TAG,"-->the  check button is clicked.");
                phone = etPhoneNum.getText().toString();
                // 获取验证码并进行校验
                getCaptcha(phone);
                break;
            case R.id.bt_next_step:
                // 点击下一步按钮的时候进行验证码的校验
                Log.e(Config.TAG,"-->the next step is clicked.");
                String captcha = etCheckCode.getText().toString();
                doCheckCaptcha(captcha);
                break;
            case R.id.tv_protocol_of_orong:
//                startActivity(new Intent(this, ProtocolActivity.class));
                break;
            case R.id.bt_register:
                doRegister();
                break;
            default:

                break;
        }
    }
    /**
     * 返回前一页 如果ViewFlipper 的当前页不是第一页退回前一页，否则退出整个页面，
     */
    private void reback() {
        if (viewIndex > Toast.LENGTH_SHORT) {
            llStepNavigate.setBackgroundResource(R.drawable.step1);
            tvStep1.setTextColor(getResources().getColor(R.color.white2));
            tvStep2.setTextColor(getResources().getColor(R.color.textcolor));
            vfRegister.showPrevious();
            viewIndex--;
        } else {
            finish();
        }

    }
    /**
     * 获取验证码
     *
     * @param phone 接收验证码的手机帐号
     */
    private void getCaptcha(String phone) {
        loadingDialog.show();
        if (MatchUtil.isPhoneNum(phone)) {

            // 利用get方法获取验证码
            Map<String,String> params = new HashMap<>();
            params.put(Config.PHONE,phone);
            okClientManager.requestGetBySyn(Config.GET_CODE_URI,params,new OKClientManager.LoadJsonString() {
                @Override
                public void onResponse(String result) {
                    if(result !=  null){
                        JSONObject res = JSONUtils.instanceJsonObject(result);
                        try {
                            switch (res.getInt(Config.CODE)){
                                case 0:
                                    handler.sendEmptyMessage(Config.GET_CODE_SUCCESS);
                                    break;
                                case 1:
                                    handler.sendEmptyMessage(Config.GET_CODE_PHONE_EXIST);
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
        }else {
            if ("".equals(phone)) {
                Toast.makeText(this, R.string.null_phone, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.error_phone, Toast.LENGTH_SHORT).show();
            }
            btNextStep.setClickable(false);
            setButtonBackground(btNextStep);
            return;
        }
    }
    // 进行验证码校验
    private void doCheckCaptcha(String captcha) {
        if (captcha == null || "".equals(captcha)) {
            Toast.makeText(this, R.string.null_captcha, Toast.LENGTH_SHORT).show();
            return;
        } else {
            // 将验证码数据提交到服务器
            Map<String,String> params = new HashMap<>();
            params.put(Config.AUTH_CODE,captcha);
            params.put(Config.PHONE,phone);
            okClientManager.requestPostBySyn(Config.CHECK_AUTH_CODE,params,new OKClientManager.LoadJsonString() {
                @Override
                public void onResponse(String result) {
                    if(result != null){
                        Log.e(Config.TAG,result);
                        JSONObject res = JSONUtils.instanceJsonObject(result);
                        try {
                            switch (res.getInt("code")){
                                case 0:
                                    handler.sendEmptyMessage(Config.CODE_CONFIRM_SUCCESS);
                                    break;
                                case 1:
                                    handler.sendEmptyMessage(Config.CODE_CONFIRM_FAIL);
                                    Log.e(Config.TAG,"--->code confirm fail..");
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

    /**
     * 注册并登录
     */
    private void doRegister() {
        // 验证用户名
        String username = etUserName.getText().toString().trim();
        if (!MatchUtil.isLicitAccount(username)) {
            if ("".equals(username)) {
                Toast.makeText(this, R.string.null_account, Toast.LENGTH_SHORT).show();
            } else if (username.length() < 3) {
                Toast.makeText(this, R.string.short_account, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.error_account, Toast.LENGTH_SHORT).show();
            }
            return;
        }
        // 验证密码
        final String loginPasWord = etPassWord.getText().toString().trim();
        if ("".equals(loginPasWord)) {
            Toast.makeText(this, R.string.null_password, Toast.LENGTH_SHORT).show();
            return;
        } else if (loginPasWord.length() < 6) {
            Toast.makeText(this, R.string.short_password, Toast.LENGTH_SHORT).show();
            return;
        }
        if (MatchUtil.isAllNumber(loginPasWord)) {
            Toast.makeText(this, R.string.number_password, Toast.LENGTH_SHORT).show();
            return;
        } else if (!MatchUtil.isLicitPassword(loginPasWord)) {
            Toast.makeText(this, R.string.error_password, Toast.LENGTH_SHORT).show();

            return;
        }

        // 没有密码不一致提示信息就可以请求注册
        if(tvConfirmPassword.getVisibility() == tvConfirmPassword.INVISIBLE)
            requestRegister(phone, username, loginPasWord);

    }

    /**
     * 注册登录请求
     *
     * @param phone
     *            注册手机
     * @param username
     *            注册帐号
     * @param loginPassword
     *            登录密码
     */
    private void requestRegister(String phone, String username, String loginPassword) {
        Map<String,String> datas = new HashMap<>();
        datas.put(Config.PHONE, phone);
        datas.put(Config.USER_NAME,username);
        datas.put(Config.PASSWORD, loginPassword);
        registerDialog.show();
        okClientManager.requestPostBySyn(Config.REGISTER_URI, datas, new OKClientManager.LoadJsonString() {
            @Override
            public void onResponse(String result) {
                if(result != null){
                    Log.e(Config.TAG,result);
                    JSONObject res = JSONUtils.instanceJsonObject(result);
                    try {
                        switch (res.getInt("code")){
                            case 0:
                                handler.sendEmptyMessage(Config.REGISTER_SUCCESS);
                                break;
                            case 1:
                                handler.sendEmptyMessage(Config.REGISTER_FAIL);
                                Log.e(Config.TAG,"--->code confirm fail..");
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

    @Override
    public void onBackPressed() {
        reback();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Config.TAG,"-->destory");
        if(registerDialog != null)
            registerDialog.dismiss();
    }
}