package com.shuao.banzhuan.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.holder.DetailDownloadHolder;
import com.shuao.banzhuan.holder.DetailInfoHolder;
import com.shuao.banzhuan.holder.DetailScreenHolder;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.protocol.DetailProtocol;
import com.shuao.banzhuan.view.LoadingPage;

/**
 * 任务详情界面
 */
public class TaskActivity extends BaseActivity {

    private LoadingPage mLoadingPage;
    private LinearLayout ll_aty_detail;
    private Toolbar toolbar;
    private String packageName;
    private AppInfo appInfo;
    private HorizontalScrollView detail_screen;
    private DetailInfoHolder detailInfoHolder;
    private DetailScreenHolder screenHolder;
    private FrameLayout bottom_layout,detail_info,detail_safe,detail_des,detail_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 创建加载的界面
        mLoadingPage = new LoadingPage(this) {

            @Override
            public View createSuccessView() {
                return TaskActivity.this.onCreateSuccessView();
            }

            @Override
            public LoadResult load() {
                return TaskActivity.this.onLoad();
            }

        };

        setContentView(mLoadingPage);// 直接将一个view对象设置给activity


        // 开始加载网络数据
        mLoadingPage.changeState();

    }

    @Override
    protected void initView() {




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
        setContentView(R.layout.activity_task);
        ll_aty_detail = (LinearLayout) findViewById(R.id.ll_aty_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_detail);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 初始化成功的布局
//        View view = UiTools.inflate(R.layout.activity_detail);
//        View view = View.inflate(this,R.id.ll_aty_detail,null);



        //  初始化应用信息模块，动态增加布局的内容
        detail_info = (FrameLayout) ll_aty_detail.findViewById(R.id.fl_detail_appinfo);
        // new 会调用initView方法
        detailInfoHolder = new DetailInfoHolder();
        // 设置数据,会调用refreshView方法
        detailInfoHolder.setData(appInfo);
        detail_info.addView(detailInfoHolder.getContentView());

        // 初始化应用信息模块
        FrameLayout flDetailAppInfo = (FrameLayout) ll_aty_detail
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
        HorizontalScrollView hsvPic = (HorizontalScrollView) ll_aty_detail
                .findViewById(R.id.hsv_detail_pics);
        DetailScreenHolder picsHolder = new DetailScreenHolder();
        hsvPic.addView(picsHolder.getContentView());
        picsHolder.setData(appInfo);
//
//        // 初始化描述模块
        FrameLayout flDetailDes = (FrameLayout) ll_aty_detail
                .findViewById(R.id.fl_detail_des);
//        DetailDesHolder desHolder = new DetailDesHolder();
//        flDetailDes.addView(desHolder.getRootView());
//        desHolder.setData(appInfo);
//
//        // getIntent().getSerializableExtra("list");
//
//         初始化下载模块
        FrameLayout flDetailDownload = (FrameLayout) ll_aty_detail
                .findViewById(R.id.fl_detail_download);
        DetailDownloadHolder downloadHolder = new DetailDownloadHolder();
        flDetailDownload.addView(downloadHolder.getContentView());
//        Log.d(Config.DEBUG,data.getAppId());
        if( appInfo != null){

            downloadHolder.setData(appInfo);
        }
//        return view;
        // 这里必须要return null，防止布局之间的重叠
        return null;
    }

    public LoadingPage.LoadResult onLoad() {
        // 请求网络,加载数据
        DetailProtocol protocol = new DetailProtocol(packageName);
        appInfo = protocol.load(0);
        // 根据加载数据的结果
        if (appInfo != null) {
            return  LoadingPage.LoadResult.success;
        } else {
            return  LoadingPage.LoadResult.error;
        }
    }

    @Override
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_detail);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
