package com.shuao.banzhuan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.fragment.DatePickerFragment;
import com.shuao.banzhuan.tools.BaseApplication;
import com.shuao.banzhuan.tools.OKClientManager;
import com.shuao.banzhuan.tools.PicassoUtils;
import com.shuao.banzhuan.tools.SPUtils;
import com.shuao.banzhuan.tools.UiTools;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by flyonthemap on 16/8/4.
 * 个人信息修改界面的实现
 */
public class PersonalInfoActivity extends BaseActivity {

    @BindView(R.id.tv_date)
    TextView  tvDate;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_women)
    RadioButton rbWomen;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.ci_portrait)
    CircleImageView portrait;
    ImageItem imageItem;

    private final int GET_PHOTO_STATE = 1000;
    private boolean isMan  = false;
    private OKClientManager okClientManager;
    private String phoneNum;
    private static final String GENDER = "gender";
    private static final String NICKNAME = "nickname";
    private static final String BIRTHDAY = "birthday";


    @Override
    protected void init() {
        okClientManager = OKClientManager.getOkManager();
        phoneNum = (String) SPUtils.get(UiTools.getContext(),Config.PHONE,"");
    }


    @Override
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_change_personInfo);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null)
            ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.change_personal_info);
        ButterKnife.bind(this);
        // 监听RatioButton的状态变化


        rbMan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isMan = true;
                }else {
                    isMan = false;
                }
            }
        });
        Map<String,String> params = new HashMap<>();
        params.put(Config.PHONE,phoneNum);
        okClientManager.requestPostBySyn(Config.USER_PERSON_INFO_URI, params, new OKClientManager.LoadJsonString() {
            @Override
            public void onResponse(String result) {
                try {
                    Log.e(Config.TAG,result);
                    JSONObject json = new JSONObject(result);
                    JSONObject jsonObject = json.getJSONObject("personInfo");
                    final int gender = Integer.parseInt(jsonObject.getString("gender"));
                    final String birthday = jsonObject.getString("birthday");
                    final String nickname = jsonObject.getString("nickname");
                    UiTools.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(gender == 0){
                                rbMan.setChecked(true);
                            }else {
                                rbWomen.setChecked(true);
                            }
                            etNickname.setText(nickname);
                            tvDate.setText(birthday);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        PicassoUtils.loadImageWithHolder(Config.USER_PORTRAIT_URL+"?phoneNum="+phoneNum,R.drawable.ic_default,portrait);
    }


    @OnClick(R.id.ll_change_birthday)
    void changeBirthday(){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    // 修改生日
    public void changeBirthday(int year,int month,int day){
        tvDate.setText(year + "." + month +"." +day);
    }










    @OnClick(R.id.ci_portrait)
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
                Picasso.with(getApplicationContext()).load(new File(imageItem.path)).into(portrait);
            } else {
                Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @OnClick(R.id.btn_save)
    void SavePersonInfo(){
        int gender = 0;
        if(!isMan){
            gender = 1;
        }
        String nickname = etNickname.getText().toString();
        String birthday = tvDate.getText().toString();
        Map<String,String> data = new HashMap<>();
        data.put(Config.PHONE, phoneNum);
        data.put(GENDER,gender+"");
        data.put(NICKNAME, nickname);
        data.put(BIRTHDAY,birthday);
        Map<String,String> paths = new HashMap<>();
        if(imageItem != null)
            paths.put(phoneNum,imageItem.path);
        okClientManager.postByMultipart(Config.CHANGE_PERSON_URI, data, paths, new OKClientManager.LoadJsonString() {
             @Override
             public void onResponse(String result) {
                 try {
                     JSONObject res = new JSONObject(result);
                     switch (res.getInt(Config.CODE)){
                         case 0:
                             UiTools.runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     Toast.makeText(UiTools.getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                                 }
                             });
                             break;
                         case 1:
                             UiTools.runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     Toast.makeText(UiTools.getContext(),"修改失败",Toast.LENGTH_SHORT).show();
                                 }
                             });
                             break;
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         });
    }

}
