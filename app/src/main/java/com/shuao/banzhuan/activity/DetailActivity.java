package com.shuao.banzhuan.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.holder.DetailDownloadHolder;
import com.shuao.banzhuan.holder.DetailInfoHolder;
import com.shuao.banzhuan.holder.DetailScreenHolder;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.protocol.DetailProtocol;
import com.shuao.banzhuan.tools.UiTools;
import com.shuao.banzhuan.view.LoadingPage;


/**
 * Created by flyonthemap on 16/8/16.
 */
public class DetailActivity extends BaseActivity {

    private LoadingPage mLoadingPage;
    private Toolbar toolbar;
    private String packageName;
    private AppInfo data;
    private HorizontalScrollView detail_screen;
    private DetailInfoHolder detailInfoHolder;
    private DetailScreenHolder screenHolder;
    private FrameLayout bottom_layout,detail_info,detail_safe,detail_des,detail_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoadingPage = new LoadingPage(this) {

            @Override
            public View createSuccessView() {
                return DetailActivity.this.onCreateSuccessView();
            }

            @Override
            public LoadResult load() {
                return DetailActivity.this.onLoad();
            }

        };

        setContentView(mLoadingPage);// 直接将一个view对象设置给activity

        // 获取从HomeFragment传递过来的包名
        packageName = getIntent().getStringExtra("taskID");

        // 开始加载网络数据
        mLoadingPage.changeState();

    }

    @Override
    protected void initView() {
//        setContentView(R.layout.activity_detail);
//        toolbar = (Toolbar) findViewById(R.id.tl_detail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public View onCreateSuccessView() {
        // 初始化成功的布局
        View view = UiTools.inflate(R.layout.activity_detail);

//        ViewUtils.removeParent(view);
//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_aty_detail);


        //  初始化应用信息模块，动态增加布局的内容
        detail_info = (FrameLayout) view.findViewById(R.id.fl_detail_appinfo);
        // new 会调用initView方法
        detailInfoHolder = new DetailInfoHolder();
        // 设置数据,会调用refreshView方法
        detailInfoHolder.setData(data);
        detail_info.addView(detailInfoHolder.getContentView());

        // 初始化应用信息模块
        FrameLayout flDetailAppInfo = (FrameLayout) view
                .findViewById(R.id.fl_detail_appinfo);
        // 动态给帧布局填充页面
//        DetailAppInfoHolder appInfoHolder = new DetailAppInfoHolder();
//        flDetailAppInfo.addView(appInfoHolder.getRootView());
//        appInfoHolder.setData(data);

//        // 初始化安全描述模块
//        FrameLayout flDetailSafe = (FrameLayout) view
//                .findViewById(R.id.fl_detail_safe);
//        DetailSafeHolder safeHolder = new DetailSafeHolder();
//        flDetailSafe.addView(safeHolder.getRootView());
//        safeHolder.setData(data);
//
        // 初始化截图模块
        HorizontalScrollView hsvPic = (HorizontalScrollView) view
                .findViewById(R.id.hsv_detail_pics);
        DetailScreenHolder picsHolder = new DetailScreenHolder();
        hsvPic.addView(picsHolder.getContentView());
        picsHolder.setData(data);
//
//        // 初始化描述模块
        FrameLayout flDetailDes = (FrameLayout) view
                .findViewById(R.id.fl_detail_des);
//        DetailDesHolder desHolder = new DetailDesHolder();
//        flDetailDes.addView(desHolder.getRootView());
//        desHolder.setData(data);
//
//        // getIntent().getSerializableExtra("list");
//
//         初始化下载模块
        FrameLayout flDetailDownload = (FrameLayout) view
                .findViewById(R.id.fl_detail_download);
        DetailDownloadHolder downloadHolder = new DetailDownloadHolder();
        flDetailDownload.addView(downloadHolder.getContentView());
//        Log.d(Config.DEBUG,data.getAppId());
        if( data != null){

            downloadHolder.setData(data);
        }
        return view;
    }

    public LoadingPage.LoadResult onLoad() {
        // 请求网络,加载数据
        DetailProtocol protocol = new DetailProtocol(packageName);
        data = protocol.load(0);
        if (data != null) {
            return  LoadingPage.LoadResult.success;
        } else {
            return  LoadingPage.LoadResult.error;
        }
    }

    @Override
    protected void initToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_detail);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
    }
}
