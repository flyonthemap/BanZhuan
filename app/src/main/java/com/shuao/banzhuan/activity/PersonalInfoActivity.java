package com.shuao.banzhuan.activity;

import android.content.Context;
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
import com.shuao.banzhuan.tools.UiTools;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by flyonthemap on 16/8/4.
 * 个人信息修改界面的实现
 */
public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView  tv_date;
    private RadioButton rb_man,rb_women;
    private SharedPreferences sharedPreferences;
    private Button btn_save;
    private EditText et_nickname;
    private CircleImageView portrait;
    private SharedPreferences.Editor editor;

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private GalleryFinal.OnHanlderResultCallback mOnHandlerResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                PhotoInfo photoInfo = resultList.get(0);
                Picasso.with(getApplicationContext()).load(new File(photoInfo.getPhotoPath())).into(portrait);
                //将图片保存至本地
                String[] paths = photoInfo.getPhotoPath().split("/");
                Config.PORTRAIT_PATH = UiTools.getContext().getCacheDir() + File.separator + paths[paths.length-1];
                PicassoUtils.saveLocalByPath(photoInfo.getPhotoPath(),Config.PORTRAIT_PATH);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(PersonalInfoActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        sharedPreferences = getSharedPreferences("banzhuan",MODE_PRIVATE);
        editor = sharedPreferences.edit();

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
        setContentView(R.layout.change_personal_info);
        tv_date = (TextView) findViewById(R.id.tv_date);
        btn_save = (Button) findViewById(R.id.btn_save);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rb_women = (RadioButton) findViewById(R.id.rb_women);
        portrait = (CircleImageView) findViewById(R.id.ci_portrait);

        // 首先加载之前存储过的本地信息
        getDefaultInfo();

        portrait.setOnClickListener(this);
        // 监听RatioButton的状态变化

        rb_man.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean(Config.STR_IS_MAN,true);
                }else {
                    editor.putBoolean(Config.STR_IS_MAN,false);
                }
            }
        });

        // 修改生日
        View ll_change_birthday = findViewById(R.id.ll_change_birthday);
        if(ll_change_birthday != null)
            ll_change_birthday.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    // 修改生日
    public void changeBirthday(int year,int month,int day){
        tv_date.setText(year + "." + month +"." +day);
    }





    // 从sharedPreferences中加载默认的信息
    private void getDefaultInfo() {
        Config.isMan = sharedPreferences.getBoolean(Config.STR_IS_MAN,true);
        Config.nickName = sharedPreferences.getString(Config.STR_NICKNAME,"听说有昵称的孩子更帅哦~");
        final Calendar c = Calendar.getInstance();
        // 如果没有设置则使用当前的日期
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        final String birthday = year+"."+(month+1)+"."+day;
        Config.birthday = sharedPreferences.getString(Config.STR_BIRTHDAY,birthday);
        tv_date.setText(Config.birthday);
        et_nickname.setText(Config.nickName);
        if(Config.isMan){
            rb_man.setChecked(true);
        }else {
            rb_women.setChecked(true);
        }
        // 为了防止程序崩溃，要在这种存在空指针异常的地方，进行空指针异常的检查
        Config.PORTRAIT_PATH = sharedPreferences.getString(Config.STR_PORTRAIT_PATH,null);
        if(Config.PORTRAIT_PATH != null){
            Picasso.with(getApplicationContext()).load(new File(Config.PORTRAIT_PATH)).into(portrait);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                // 首先将信息提交到服务器，然后将信息保存至本地

                saveLocal();
                Toast.makeText(PersonalInfoActivity.this,"修改成功~",Toast.LENGTH_SHORT).show();
             
                OKClientManager.getOkManager().uploadImagineByURL(Config.UPLOAD_IMAGE_URL, Config.PORTRAIT_PATH, new OKClientManager.LoadJsonString() {
                    @Override
                    public void onResponse(String result) {
                        if(result != null){
                            Log.d(Config.DEBUG,result.toString());
                        }
                    }
                });
                // 向服务器提交数据
                break;
            case R.id.ci_portrait:
                changePortrait();
                break;
            case R.id.ll_change_birthday:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
        }
    }
    // 点击保存按钮之后才会修改生效
    private void saveLocal() {
        editor.putString(Config.STR_NICKNAME,et_nickname.getText().toString());
        editor.putString(Config.STR_BIRTHDAY,tv_date.getText().toString());
        editor.putString(Config.STR_PORTRAIT_PATH,Config.PORTRAIT_PATH);
        editor.commit();
    }
    // 修改头像
    private void changePortrait() {
        final FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .setMutiSelectMaxSize(4)
                .build();


        ActionSheet.createBuilder(PersonalInfoActivity.this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHandlerResultCallback);
                                break;
                            case 1:
                                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHandlerResultCallback);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
        initImageLoader(this);
    }
    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }


}
