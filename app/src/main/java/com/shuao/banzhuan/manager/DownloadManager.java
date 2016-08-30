package com.shuao.banzhuan.manager;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.nostra13.universalimageloader.utils.L;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.http.HttpHelper;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.model.DownloadInfo;
import com.shuao.banzhuan.tools.IOUtils;
import com.shuao.banzhuan.tools.UiTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by flyonthemap on 16/8/17.
 * Download Manager通知所有的观察者
 */
public class DownloadManager {
    public static final int STATE_UNDO = 1000;
    public static final int STATE_WAITING = 1001;
    public static final int STATE_DOWNLOADING = 1002;
    public static final int STATE_PAUSE = 1003;
    public static final int  STATE_ERROR = 1004;
    public static final int  STATE_SUCCESS = 1005;
    // 记录观察者的集合
    private List<DownloadObserver> mObservers = new ArrayList<>();

    // 使用线程安全的hashmap
    private ConcurrentHashMap<String,DownloadInfo> mDownloadInfo = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,DownloadTask> mDownloadTask = new ConcurrentHashMap<>();
    private volatile static DownloadManager downloadManager;
    private DownloadManager(){}
    // DownloadManager使用单例模式
    public static DownloadManager getDownloadManager(){
        if(downloadManager == null){
            synchronized (DownloadManager.class){
                if(downloadManager == null){
                    downloadManager = new DownloadManager();
                }
            }
        }
        return downloadManager;
    }
    // 注册观察者
    public void registerObserver(DownloadObserver observer){
        if(observer != null && !mObservers.contains(observer))
            mObservers.add(observer);
    }
    // 注销观察者
    public void unRegisterObserver(DownloadObserver observer){
        if(observer != null && mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }
    // 状态更新通知
    public void notifyDownloadStateChanged(DownloadInfo downloadInfo){

        for (DownloadObserver observer:mObservers) {
            observer.onDownloadProgressChanged(downloadInfo);
        }

    }
    public void notifyDownloadProgressChanged(DownloadInfo downloadInfo){
        for(DownloadObserver observer:mObservers){
            observer.onDownloadProgressChanged(downloadInfo);
        }
    }
    //声明观察者接口
    public interface DownloadObserver{
        void onDownloadStateChanged(DownloadInfo downloadInfo);
        void onDownloadProgressChanged(DownloadInfo downloadInfo);
    }




    // 下载
    public synchronized void  download(AppInfo info){
        // 首先判断是否下载过
        DownloadInfo  downloadInfo = mDownloadInfo.get(info.getAppId());
        if(downloadInfo == null){
            downloadInfo = DownloadInfo.copy(info);
            // 更新状态
            downloadInfo.setCurrentState(STATE_WAITING);
            //通知观察者状态发生变化
            Log.d(Config.DEBUG,"wait downloading....");
            notifyDownloadProgressChanged(downloadInfo);
        }
        mDownloadInfo.put(downloadInfo.getAppId(),downloadInfo);
        // 初始化下载任务
        DownloadTask downloadTask = new DownloadTask(downloadInfo);
        //执行下载任务，放入线程池中
        ThreadManager.getThreadManager().createLongPool().execute(downloadTask);
        mDownloadTask.put(downloadInfo.getAppId(),downloadTask);
    }

    //暂停
    public synchronized void pause(AppInfo appInfo){
        DownloadInfo downloadInfo = mDownloadInfo.get(appInfo.getAppId());
        if(downloadInfo != null){
            switch (downloadInfo.getCurrentState()){
                // 正在下载或者等待下载的状态则可以取消当前的下载任务
                case STATE_DOWNLOADING:
                case STATE_WAITING:
                    downloadInfo.setCurrentState(STATE_PAUSE);
                    // 通知状态发生变化
                    notifyDownloadProgressChanged(downloadInfo);
                    DownloadTask downloadTask = mDownloadTask.get(downloadInfo.getAppId());
                    if(downloadTask != null){
                        // 若下载任务在排队等待，则在此处进行取消任务；否则在run方法中取消任务
                        ThreadManager.getThreadManager().createLongPool().cancel(downloadTask);
                    }
                    break;
            }
        }
    }

    //安装
    public void install(AppInfo appInfo){
        DownloadInfo downloadInfo = mDownloadInfo.get(appInfo.getAppId());
        if(downloadInfo != null){
            //跳转到安装界面
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://"+downloadInfo.getPath()),"application/vnd.android.package-archive");
            UiTools.getContext().startActivity(intent);
        }
    }

    private class DownloadTask implements Runnable{
        private DownloadInfo downloadInfo;

        public DownloadTask(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            HttpHelper.HttpResult httpResult;
            Log.d(Config.DEBUG,"start downloading....");
            // 这里要考虑断点续传的问题
            //更新状态
            downloadInfo.setCurrentState(STATE_DOWNLOADING);
            notifyDownloadStateChanged(downloadInfo);
            File file = new File(downloadInfo.getPath());
//            Log(Config.DEBUG,downloadInfo.getPath().toString());
            if(!file.exists()||file.length() != downloadInfo.getCurrentPos()
                    ||downloadInfo.getCurrentPos() == 0){
                //从头开始下载,首先删除无效文件
                file.delete();
                downloadInfo.setCurrentPos(0);
                //从下载位置下载
                httpResult = HttpHelper.download(Config.DOWNLOAD_URL);
                Log.d(Config.DEBUG,"downloading.... ");

            } else {
                //用断点续传的URL进行下载
                httpResult = HttpHelper.download(Config.DOWNLOAD_URL);
            }
            if(httpResult != null && httpResult.getInputStream() != null) {
                // 网络正常连接
                Log.d(Config.DEBUG,"httpResult ...");
                InputStream in = null;
                FileOutputStream fo = null;
                BufferedWriter bw = null;
                try {
                    in = httpResult.getInputStream();
//                    Log.d(Config.DEBUG,in.toString());
                    fo = new FileOutputStream(file, true);
                    int len ;
                    byte[] buffer = new byte[1024];
                    // 第二个条件是解决下载过程中中途暂停的问题
                    // 只有状态是正在下载, 才继续轮询. 解决下载过程中中途暂停的问题
                    if((len = in.read(buffer)) != -1){
                        Log.d(Config.DEBUG,"stream is error");
                    }
//                    Log.d(Config.DEBUG,in.read(buffer)+"hahahahahah");
                    while ((len = in.read(buffer)) != -1
                            && downloadInfo.getCurrentState() == STATE_DOWNLOADING) {
                        fo.write(buffer, 0, len);
                        fo.flush();// 把剩余数据刷入本地
                        Log.d(Config.DEBUG,"hahahah.......");
                        // 更新下载进度
                        downloadInfo.setCurrentState(downloadInfo.getCurrentState() + len);
                        notifyDownloadProgressChanged(downloadInfo);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(fo);
                    IOUtils.closeQuietly(bw);
                }
                // 下载完成之后，对下载文件的完整性进行校验
                Log.d(Config.DEBUG,"download complete...");
                Log.d(Config.DEBUG,downloadInfo.getSize()+"=="+file.length());
                if(file.length() == downloadInfo.getSize()){
                    Log.d(Config.DEBUG,"size not equal..");
                    downloadInfo.setCurrentState(STATE_SUCCESS);
                    notifyDownloadStateChanged(downloadInfo);
                }else if(downloadInfo.getCurrentState() == STATE_PAUSE){ //暂停
                    notifyDownloadStateChanged(downloadInfo);
                }else {
                    Log.d(Config.DEBUG,"download error....");
                    file.delete();
                    downloadInfo.setCurrentState(STATE_ERROR);
                    downloadInfo.setCurrentPos(0);
                    notifyDownloadStateChanged(downloadInfo);
                }
            }else {
                // 网络异常的状态
                Log.d(Config.DEBUG,"network error....");
                file.delete();
                downloadInfo.setCurrentState(STATE_ERROR);
                downloadInfo.setCurrentPos(0);
                notifyDownloadStateChanged(downloadInfo);
                notifyDownloadProgressChanged(downloadInfo);
            }
            // 移除最终的下载任务
            mDownloadTask.remove(downloadInfo.getAppId());
        }
    }

    // 根据应用信息，返回下载对象
    public DownloadInfo getDownloadInfo(AppInfo appInfo){

        return mDownloadInfo.get(appInfo.getAppId());
    }
}
