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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by flyonthemap on 16/8/18.
 * DetailDownloadHolder
 */
public class DetailDownloadHolder extends BaseHolder<AppInfo> implements
        DownloadManager.DownloadObserver{

    private DownloadManager mDM;
    private int mCurrentState;
    private float mProgress;

    @BindView(R.id.fl_progress) FrameLayout flProgress;
    @BindView(R.id.btn_download) Button btnDownload;
    private ProgressHorizontal pbProgress;

    @Override
    public View initView() {
        View view = UiTools.inflate(R.layout.layout_detail_download);
        ButterKnife.bind(this,view);

        pbProgress = new ProgressHorizontal(UiTools.getContext());
        pbProgress.setProgressBackgroundResource(R.color.dark_gray);// 进度条背景图片
        pbProgress.setProgressResource(R.drawable.progress_normal);// 进度条图片
        pbProgress.setProgressTextColor(Color.WHITE);// 进度文字颜色
        pbProgress.setProgressTextSize(UiTools.dip2px(18));// 进度文字大小

        // 宽高填充父窗体
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        // 给帧布局添加自定义进度条
        flProgress.addView(pbProgress, params);

        mDM = DownloadManager.getInstance();
        mDM.registerObserver(this);// 注册观察者, 监听状态和进度变化

        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        // 判断当前应用是否下载过
        DownloadInfo downloadInfo = mDM.getDownloadInfo(data);
        if (downloadInfo != null) {
            // 之前下载过
            mCurrentState = downloadInfo.currentState;
            mProgress = downloadInfo.getProgress();
        } else {
            // 没有下载过
            mCurrentState = DownloadManager.STATE_NONE;
            mProgress = 0;
        }

        refreshProgress(mCurrentState, mProgress);
    }

    // 根据当前的下载进度和状态来更新界面
    private void refreshProgress(int currentState, float progress) {
        mCurrentState = currentState;
        mProgress = progress;
        switch (currentState) {
            case DownloadManager.STATE_NONE:// 未下载
                flProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("下载");
                break;

            case DownloadManager.STATE_WAITING:// 等待下载
                flProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("等待中..");
                break;

            case DownloadManager.STATE_DOWNLOAD:// 正在下载
                flProgress.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
                pbProgress.setCenterText("");
                pbProgress.setProgress(mProgress);// 设置下载进度
                break;

            case DownloadManager.STATE_PAUSE:// 下载暂停
                flProgress.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
                pbProgress.setCenterText("暂停");
                pbProgress.setProgress(mProgress);
                break;

            case DownloadManager.STATE_ERROR:// 下载失败
                flProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("下载失败");
                break;

            case DownloadManager.STATE_SUCCESS:// 下载成功
                flProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("安装");
                break;

            default:
                break;
        }

    }



    // 状态更新
    @Override
    public void onDownloadStateChanged(DownloadInfo info) {

        refreshUIOnMainThread(info);
    }

    @Override
    public void onDownloadProgressChanged(DownloadInfo info) {

        refreshUIOnMainThread(info);
    }
    private void refreshUIOnMainThread(final DownloadInfo info) {
        if (isCurAppData(info.id)) {
            UiTools.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    refreshProgress(info.currentState, info.getProgress());
                }
            });
        }
    }

    @OnClick({R.id.btn_download,R.id.fl_progress})
    void executeByCurState(){
        if (mCurrentState == DownloadManager.STATE_NONE
                || mCurrentState == DownloadManager.STATE_ERROR
                || mCurrentState == DownloadManager.STATE_PAUSE) {
            mDM.download(getData());// 开始下载
        } else if (mCurrentState == DownloadManager.STATE_DOWNLOAD
                || mCurrentState == DownloadManager.STATE_WAITING) {
            mDM.pause(getData());// 暂停下载
        } else if (mCurrentState == DownloadManager.STATE_SUCCESS) {
            mDM.install(getData());// 开始安装
        }
    }
    private boolean isCurAppData(String id){
        if(getData().getAppId().equals(id)){
            return true;
        }
        return false;
    }


}
