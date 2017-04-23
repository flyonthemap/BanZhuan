package com.shuao.banzhuan.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.tools.JSONUtils;
import com.shuao.banzhuan.tools.MatchUtil;
import com.shuao.banzhuan.tools.OKClientManager;
import com.shuao.banzhuan.tools.UiTools;
import com.shuao.banzhuan.view.LoadingDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by flyonthemap on 16/8/4.
 *  用户注册模块
 */
public class RegisterActivity extends BaseActivity{

    public static final int GET_CODE_PHONE_EXIST = 1003; // 手机号已经注册
    public static final int CODE_CONFIRM_FAIL = 1004;   //验证码验证失败
    public static final int GET_CODE_SUCCESS = 1005; //获取验证码成功
    public static final int CODE_CONFIRM_SUCCESS = 1006;   //验证码验证失败
    public static final int REGISTER_SUCCESS = 1007;       // 注册成功
    public static final int REGISTER_FAIL = 1008;       // 注册失败
    public static final String AUTH_CODE = "authCode"; // 验证码验证的表单参数
    public static final String PHONE = "phoneNum";
    public static final String CHECK_AUTH_CODE = "checkAuthCode";
    public final static String PASSWORD = "password";
    public static final String USER_NAME = "nickname";
    private static final java.lang.String TIME = "time";
    private static final int GET_PHOTO_STATE = 100;

    // 注册顶部界面
    @BindView(R.id.ll_step_navigate) LinearLayout llStepNavigate;
    @BindView(R.id.tv_step1) TextView tvStep1;
    @BindView(R.id.tv_step2) TextView tvStep2;
    @BindView(R.id.vfl_register) ViewFlipper vfRegister;// 页面切换的容器
    private int viewIndex = Toast.LENGTH_SHORT;// 初始化当前页排位

    private LoadingDialog loadingDialog ;// 网络访问提示对话框
    private LoadingDialog registerDialog;
    // step1界面元素
    @BindView(R.id.et_phonenum) EditText etPhoneNum;// 电话号码输入框
    @BindView(R.id.bt_get_checkCode) Button btGetCode;// 获取验证码按钮;
    @BindView(R.id.et_checkcode) EditText etCheckCode;// 验证码输入框
    @BindView(R.id.bt_next_step) Button btNextStep;// 下一步按钮
    @BindView(R.id.cb_accepted) CheckBox cbAccepted;// 同意复选框
    @BindView(R.id.tv_protocol_of_orong) TextView tvProtocolOfOrong;// 服务条款标题

    private String phone;
    // step2的界面元素
    @BindView(R.id.ll_step2_main) LinearLayout linearLayout;
    @BindView(R.id.et_nickname) EditText etUserName;// 电话或用户名
    @BindView(R.id.et_login_paw) EditText etPassWord;// 登录密码
    @BindView(R.id.et_confirm_paw) EditText etConfirmPassWord;// 确认密码密码
    @BindView(R.id.bt_register) Button btRegister;// 注册
    @BindView(R.id.tv_password_confirm) TextView tvConfirmPassword;
    @BindView(R.id.ci_register_portrait) CircleImageView ciPortrait;


    private int validTime = 30;
    private Runnable timerRunnable;// 定时器
    private static final int TIME_CHANGE = 2000;
    private static final int TIMER_COMPLETE = 3000;
    OKClientManager okClientManager;
    private ImageItem imageItem;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIME_CHANGE:
                    Bundle data = msg.getData();
                    int time = data.getInt(TIME);
                    btGetCode.setText(getString(R.string.remaining_time,time));
                    break;
                case TIMER_COMPLETE:
                    btGetCode.setClickable(true);
                    setButtonBackground(btGetCode);
                    btGetCode.setText(R.string.get_verification_code);
                    btNextStep.setClickable(false);
                    setButtonBackground(btNextStep);
                    handler.removeCallbacks(timerRunnable);
                    timerRunnable = null;
                    break;
                // 获取验证码成功
                case GET_CODE_SUCCESS:
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
                                msg.what = TIME_CHANGE;
                                Bundle bundle = new Bundle();
                                bundle.putInt("time", validTime);
                                msg.setData(bundle);
                                msg.sendToTarget();
                                handler.postDelayed(timerRunnable, 1000);
                            } else {
                                handler.sendEmptyMessage(TIMER_COMPLETE);
                            }
                        }
                    };
                    handler.post(timerRunnable);
                    break;
                // 手机号已被注册
                case GET_CODE_PHONE_EXIST:
                    loadingDialog.dismissFail(getString(R.string.phone_has_register));
                    break;
                // 加载时间过程
                case Config.LOAD_FAIL_FINISH:
                    Toast.makeText(RegisterActivity.this,getString(R.string.check_netword_setting),Toast.LENGTH_SHORT).show();
                    break;
                // 验证码验证失败
                case CODE_CONFIRM_FAIL:
                    Toast.makeText(RegisterActivity.this,getString(R.string.fail_please_try_later),Toast.LENGTH_SHORT).show();

                    break;
                // 验证码验证成功
                case CODE_CONFIRM_SUCCESS:
                    Toast.makeText(RegisterActivity.this,getString(R.string.confirm_success),Toast.LENGTH_SHORT).show();

                    llStepNavigate.setBackgroundResource(R.drawable.step2);
                    tvStep1.setTextColor(UiTools.getColor(R.color.white2));
                    tvStep2.setTextColor(UiTools.getColor(R.color.white2));
                    vfRegister.showNext();
                    viewIndex++;
                    break;
                // 注册成功
                case REGISTER_SUCCESS:
                    registerDialog.dismissSuc(getString(R.string.register_success_please_login));
                    // 启动登录界面
                    Intent login = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(login);
                    finish();

                    break;
                // 注册失败
                case REGISTER_FAIL:
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
        ButterKnife.bind(this);

        loadingDialog = new LoadingDialog(this);
        registerDialog = new LoadingDialog(this);
        // 同意并已经输入验证码才能实行下一步
        cbAccepted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 获取四位验证码数字
                btNextStep.setClickable(isChecked && etCheckCode.getText().toString().trim().length() == 4 && !btGetCode.isClickable());
                setButtonBackground(btNextStep);
            }
        });
        btNextStep.setClickable(false);
        setButtonBackground(btNextStep);
        confirmPwdConsistency();

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
        UiTools.addLayoutListener(linearLayout,btRegister);

    }


    private void confirmPwdConsistency() {
        etConfirmPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
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
    }

    @Override
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_register);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

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

    @OnClick(R.id.bt_next_step)
    void goNextStep(){
        String captcha = etCheckCode.getText().toString();
        if ("".equals(captcha)) {
            Toast.makeText(this, R.string.null_captcha, Toast.LENGTH_SHORT).show();
            return;
        } else {
            // 将验证码数据提交到服务器
            Map<String,String> params = new HashMap<>();
            params.put(AUTH_CODE,captcha);
            params.put(PHONE,phone);
            okClientManager.requestPostBySyn(CHECK_AUTH_CODE,params,new OKClientManager.LoadJsonString() {
                @Override
                public void onResponse(String result) {
                    if(result != null){
                        Log.e(Config.TAG,result);
                        JSONObject res = JSONUtils.instanceJsonObject(result);
                        try {
                            switch (res.getInt("code")){
                                case 0:
                                    handler.sendEmptyMessage(CODE_CONFIRM_SUCCESS);
                                    break;
                                case 1:
                                    handler.sendEmptyMessage(CODE_CONFIRM_FAIL);
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
    @OnClick(R.id.bt_register)
    void register() {
        String username = etUserName.getText().toString().trim();
        // 验证用户名
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
        if (tvConfirmPassword.getVisibility() == tvConfirmPassword.INVISIBLE) {
            if (imageItem == null) {
                Toast.makeText(this, R.string.please_upload_portrait, Toast.LENGTH_SHORT).show();
            }
            else{
                requestRegister(phone, username, loginPasWord);
            }

        }
    }

    @OnClick(R.id.bt_get_checkCode)
    void getCheckCode(){
        phone = etPhoneNum.getText().toString();
        // 获取验证码并进行校验
        if (MatchUtil.isPhoneNum(phone)) {
            loadingDialog.show();
            // 利用get方法获取验证码
            Map<String,String> params = new HashMap<>();
            params.put(PHONE,phone);
            okClientManager.requestGetBySyn(Config.GET_CODE_URI,params,new OKClientManager.LoadJsonString() {
                @Override
                public void onResponse(String result) {
                    if(result !=  null){
                        JSONObject res = JSONUtils.instanceJsonObject(result);
                        try {
                            switch (res.getInt(Config.CODE)){
                                case 0:
                                    handler.sendEmptyMessage(GET_CODE_SUCCESS);
                                    break;
                                case 1:
                                    handler.sendEmptyMessage(GET_CODE_PHONE_EXIST);
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
    /**
     * 返回前一页 如果ViewFlipper 的当前页不是第一页退回前一页，否则退出整个页面，
     */
    private void reback() {
        if (viewIndex > Toast.LENGTH_SHORT) {
            llStepNavigate.setBackgroundResource(R.drawable.step1);
            tvStep1.setTextColor(UiTools.getColor(R.color.white2));
            tvStep2.setTextColor(UiTools.getColor(R.color.textcolor));
            vfRegister.showPrevious();
            viewIndex--;
        } else {
            finish();
        }

    }

    private void requestRegister(String phone, String username, String loginPassword) {
        Map<String,String> data = new HashMap<>();
        data.put(PHONE, phone);
        data.put(USER_NAME,username);
        data.put(PASSWORD, loginPassword);
        registerDialog.show();
        Map<String,String> paths = new HashMap<>();
        paths.put(phone,imageItem.path);
        okClientManager.postByMultipart(Config.REGISTER_URI, data,paths, new OKClientManager.LoadJsonString() {
            @Override
            public void onResponse(String result) {
                if(result != null){
                    Log.e(Config.TAG,result);
                    JSONObject res = JSONUtils.instanceJsonObject(result);
                    try {
                        switch (res.getInt("code")){
                            case 0:
                                handler.sendEmptyMessage(REGISTER_SUCCESS);
                                break;
                            case 1:
                                handler.sendEmptyMessage(REGISTER_FAIL);
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
        if(registerDialog != null)
            registerDialog.dismiss();
    }
    @OnClick(R.id.ci_register_portrait)
    void selectPortrait(){
        Intent intent = new Intent(getApplicationContext(), ImageGridActivity.class);
        startActivityForResult(intent, GET_PHOTO_STATE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == GET_PHOTO_STATE) {
                ArrayList<ImageItem> photoInfo = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                imageItem = photoInfo.get(0);
                Picasso.with(getApplicationContext()).load(new File(imageItem.path)).into(ciPortrait);
            } else {
                Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
}