package com.shuao.banzhuan.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.manager.DownloadManager;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.model.DownloadInfo;
import com.shuao.banzhuan.tools.UiTools;
import com.shuao.banzhuan.view.ProgressHorizontal;

/**
 * Created by flyonthemap on 16/8/18.
 * DetailDownloadHolder
 */
public class DetailDownloadHolder extends BaseHolder<AppInfo>
        implements DownloadManager.DownloadObserver,View.OnClickListener{

    private DownloadManager downloadManager;
    private int mCurrentState;
    private float mProgress;
    private Button button;
    private FrameLayout flProgress;
    private ProgressHorizontal pbProgress;
    @Override
    public View initView() {
        View view = UiTools.inflate(R.layout.layout_detail_download);
        downloadManager = DownloadManager.getDownloadManager();
        // 初始化自定义的进度条
        flProgress = (FrameLayout) view.findViewById(R.id.fl_progress);
        button = (Button) view.findViewById(R.id.btn_download);
        button.setOnClickListener(this);
        pbProgress = new ProgressHorizontal(UiTools.getContext());
        pbProgress.setOnClickListener(this);
        pbProgress.setProgressBackgroundResource(R.drawable.progress_bg);
        pbProgress.setProgressResource(R.drawable.progress_normal);
        pbProgress.setProgressTextColor(Color.WHITE);
        pbProgress.setProgressTextSize(UiTools.dip2px(18));

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        // 给帧布局添加进度条
        flProgress.addView(pbProgress,layoutParams);
        // 注册观察者，监听状态和进度变化
        downloadManager.registerObserver(this);
        return view;
    }

    @Override
    public void refreshView(AppInfo appInfo) {
        // 判断当前应用是否下载过
        DownloadInfo downloadInfo = downloadManager.getDownloadInfo(appInfo);
        if(downloadInfo != null){
            //根据当前的状态来进行视图显示
            mCurrentState = downloadInfo.getCurrentState();
            mProgress = downloadInfo.getProgress();
        }else {
            mCurrentState = DownloadManager.STATE_UNDO;
            mProgress = 0;
        }
        refreshUI(mCurrentState,mProgress);
    }

    // 根据当前的下载进度和状态来更新界面
    private void refreshUI (int currentState, float progress) {
        mCurrentState = currentState;
        mProgress = progress;
        switch (currentState){
            // 未下载
            case DownloadManager.STATE_UNDO:
                pbProgress.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                button.setText("下载");
                break;
            case DownloadManager.STATE_WAITING:
                pbProgress.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                button.setText("等待中...");
                break;
            case DownloadManager.STATE_DOWNLOADING:
                pbProgress.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                // 设置下载状态
                pbProgress.setCenterText("");
                pbProgress.setProgress(progress);
                break;
            case DownloadManager.STATE_PAUSE:
                pbProgress.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                pbProgress.setCenterText("暂停");
                pbProgress.setProgress(progress);
                break;
            case DownloadManager.STATE_ERROR:
                pbProgress.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                button.setText("下载失败");
                break;
            case DownloadManager.STATE_SUCCESS:
                pbProgress.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                button.setText("安装");
                break;
            default:
                break;
        }
    }




    private void refreshUIOnMainThread(final DownloadInfo downloadInfo){
        UiTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshUI(downloadInfo.getCurrentState(),downloadInfo.getProgress());
            }
        });

    }


    @Override
    public void onDownloadStateChanged(DownloadInfo downloadInfo) {
        //对下载对象进行过滤，在MainThread中更新界面
        AppInfo appInfo = getData();
        if(appInfo.getAppId().equals(downloadInfo.getAppId())){
            refreshUIOnMainThread(downloadInfo);
        }
    }

    // 进度的更新，在子线程中调用
    @Override
    public void onDownloadProgressChanged(DownloadInfo downloadInfo) {
        AppInfo appInfo = getData();
        if(appInfo.getAppId().equals(downloadInfo.getAppId())){
            refreshUIOnMainThread(downloadInfo);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_download:
            case R.id.fl_progress:
                // 根据当前的状态来进行下载操作
                if(mCurrentState == DownloadManager.STATE_UNDO
                        || mCurrentState == DownloadManager.STATE_WAITING
                        || mCurrentState == DownloadManager.STATE_ERROR){
                    downloadManager.download(getData());
                }else if(mCurrentState == DownloadManager.STATE_DOWNLOADING){
                    downloadManager.pause(getData());
                }else {
                    downloadManager.install(getData());
                }
                break;
        }
    }
}
