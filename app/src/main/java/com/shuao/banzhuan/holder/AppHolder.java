package com.shuao.banzhuan.holder;

import android.content.Intent;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.activity.BaseActivity;
import com.shuao.banzhuan.activity.DetailActivity;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.manager.DownloadManager;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.model.DownloadInfo;
import com.shuao.banzhuan.tools.PicassoUtils;
import com.shuao.banzhuan.tools.UiTools;
import com.shuao.banzhuan.view.ProgressArc;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by flyonthemap on 2016/8/23.
 *
 */
public class AppHolder extends BaseRecyclerHolder<AppInfo> implements DownloadManager.DownloadObserver {

    @BindView(R.id.tv_item_title) TextView tvName;
    @BindView(R.id.iv_item_icon) ImageView ivIcon;
    @BindView(R.id.tv_item_size) TextView tvSize;
    @BindView(R.id.tv_item_description) TextView tvDesc;
    @BindView(R.id.tv_download) TextView tvDownload;
    @BindView(R.id.fl_action_progress) FrameLayout flDownload;
    @BindView(R.id.item_action) RelativeLayout rlItemAction;
    @BindView(R.id.fl_app_item) FrameLayout rlAppItem;

    private ProgressArc pbProgress;
    private DownloadManager mDM;
    private int mCurrentState;
    private float mProgress;

    public AppHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void initView(View view) {

        ButterKnife.bind(this,view);


        pbProgress = new ProgressArc(UiTools.getContext());
        pbProgress.setArcDiameter(UiTools.dip2px(26));
        pbProgress.setProgressColor(UiTools.getColor(R.color.progress));

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                UiTools.dip2px(27), UiTools.dip2px(27));
        flDownload.addView(pbProgress, params);

        mDM = DownloadManager.getInstance();
        // 注册观察者，同步更新下载状态
        mDM.registerObserver(this);

    }


    @Override
    public void refreshView(AppInfo data) {
        if (data != null) {
            tvName.setText(data.getName());
            tvSize.setText(Formatter.formatFileSize(UiTools.getContext(),
                    data.getSize()));
            tvDesc.setText(data.getDescription());
            PicassoUtils.loadImageWithHolder(Config.BASE_IMAGE_URL+"?name="+data.getIconUrl(),R.drawable.ic_default,ivIcon);

            DownloadInfo downloadInfo = mDM.getDownloadInfo(data);
            // 判断当前应用是否下载过
            if (downloadInfo != null) {
                mCurrentState = downloadInfo.currentState;
                mProgress = downloadInfo.getProgress();
            } else {
                // 没有下载过
                mCurrentState = DownloadManager.STATE_NONE;
                mProgress = 0;
            }

            refreshProgressUI(mCurrentState, mProgress, data.getAppId());
        }
    }
    @OnClick(R.id.fl_app_item)
    void openAppDetail(){
        Intent intent=new Intent(UiTools.getContext(), DetailActivity.class);
        // 将APPInfo对象传入DetailActivity
        intent.putExtra("appInfo", mData);
        // 启动Activity
        if(BaseActivity.activity == null){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            UiTools.getContext().startActivity(intent);
        }else {
            BaseActivity.activity.startActivity(intent);
        }
    }
    @OnClick({R.id.item_action,R.id.tv_download,R.id.fl_action_progress})
    void downloadByState(){
        // 根据当前状态来决定下一步操作
        if (mCurrentState == DownloadManager.STATE_NONE
                || mCurrentState == DownloadManager.STATE_ERROR
                || mCurrentState == DownloadManager.STATE_PAUSE) {
            mDM.download(getData());
        } else if (mCurrentState == DownloadManager.STATE_DOWNLOAD
                || mCurrentState == DownloadManager.STATE_WAITING) {
            mDM.pause(getData());
        } else if (mCurrentState == DownloadManager.STATE_SUCCESS) {
            mDM.install(getData());
        }
    }
    @Override
    public void onDownloadStateChanged(DownloadInfo info) {
        refreshProgressUIOnMainThread(info);
    }

    @Override
    public void onDownloadProgressChanged(DownloadInfo info) {
        refreshProgressUIOnMainThread(info);

    }

    // 更新当前任务的下载状态和进度
    private void refreshProgressUI(int state, float progress, String id) {
        if (isCurrentAppData(id) || state == DownloadManager.STATE_INSTALL){
            mCurrentState = state;
            mProgress = progress;
            switch (state) {
                case DownloadManager.STATE_NONE:
                    // 自定义进度条背景
                    pbProgress.setBackgroundResource(R.drawable.ic_download);
                    // 没有进度
                    pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                    tvDownload.setText("下载");
                    break;
                case DownloadManager.STATE_WAITING:
                    pbProgress.setBackgroundResource(R.drawable.ic_download);
                    // 等待模式
                    pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
                    tvDownload.setText("等待");
                    break;
                case DownloadManager.STATE_DOWNLOAD:
                    pbProgress.setBackgroundResource(R.drawable.ic_pause);
                    // 下载中模式
                    pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                    pbProgress.setProgress(progress, true);
                    tvDownload.setText((int) (progress * 100) + "%");
                    break;
                case DownloadManager.STATE_PAUSE:
                    pbProgress.setBackgroundResource(R.drawable.ic_resume);
                    pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                    break;
                case DownloadManager.STATE_ERROR:
                    pbProgress.setBackgroundResource(R.drawable.ic_redownload);
                    pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                    tvDownload.setText("下载失败");
                    break;
                case DownloadManager.STATE_SUCCESS:
                    pbProgress.setBackgroundResource(R.drawable.ic_install);
                    pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                    tvDownload.setText("安装");
                    break;
                case DownloadManager.STATE_INSTALL:
                    Log.e(Config.TAG,"这个方法被调用了");
                    pbProgress.setBackgroundResource(R.drawable.install);
                    pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                    tvDownload.setText("已安装");
                    rlAppItem.setClickable(false);
                default:
                    break;
            }
        }


    }


    private void refreshProgressUIOnMainThread(final DownloadInfo info) {

        UiTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshProgressUI(info.currentState, info.getProgress(), info.id);
            }
        });
    }



    private boolean isCurrentAppData(String id){
        return id.equals(getData().getAppId());
    }
}

