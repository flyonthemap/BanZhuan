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

    public static final int STATE_NONE = 0;// 下载未开始
    public static final int STATE_WAITING = 1;// 等待下载
    public static final int STATE_DOWNLOAD = 2;// 正在下载
    public static final int STATE_PAUSE = 3;// 下载暂停
    public static final int STATE_ERROR = 4;// 下载失败
    public static final int STATE_SUCCESS = 5;// 下载成功
    public static final int STATE_INSTALL = 6;

    // 所有观察者的集合
    private ArrayList<DownloadObserver> mObservers = new ArrayList<DownloadObserver>();

    // 下载对象的集合, ConcurrentHashMap是线程安全的HashMap
    private ConcurrentHashMap<String, DownloadInfo> mDownloadInfoMap = new ConcurrentHashMap<String, DownloadInfo>();
    // 下载任务集合, ConcurrentHashMap是线程安全的HashMap
    private ConcurrentHashMap<String, DownloadTask> mDownloadTaskMap = new ConcurrentHashMap<String, DownloadTask>();

    private static DownloadManager sInstance = new DownloadManager();

    private DownloadManager() {}

    public static DownloadManager getInstance() {
        return sInstance;
    }

    // 2. 注册观察者
    public synchronized void registerObserver(DownloadObserver observer) {
        if (observer != null && !mObservers.contains(observer))
        {
            mObservers.add(observer);
        }
    }

    // 3. 注销观察者
    public synchronized void unregisterObserver(DownloadObserver observer) {
        if (observer != null && mObservers.contains(observer))
        {
            mObservers.remove(observer);
        }
    }

    // 4. 通知下载状态变化
    public synchronized void notifyDownloadStateChanged(DownloadInfo info) {
        for (DownloadObserver observer : mObservers) {
            observer.onDownloadStateChanged(info);
        }
    }

    // 5. 通知下载进度变化
    public synchronized void notifyDownloadProgressChanged(DownloadInfo info) {
        for (DownloadObserver observer : mObservers) {
            observer.onDownloadProgressChanged(info);
        }
    }

    // 开始下载
    public synchronized void download(AppInfo appInfo) {
        if (appInfo != null) {
            DownloadInfo downloadInfo = mDownloadInfoMap.get(appInfo.getAppId());
            // 如果downloadInfo不为空,表示之前下载过, 就无需创建新的对象, 要接着原来的下载位置继续下载,也就是断点续传
            if (downloadInfo == null) {// 如果为空,表示第一次下载, 需要创建下载对象, 从头开始下载
                downloadInfo = DownloadInfo.copy(appInfo);
            }
            downloadInfo.currentState = STATE_WAITING;
            // 通知状态发生变化,各观察者根据此通知更新主界面
            notifyDownloadStateChanged(downloadInfo);

            // 将下载对象保存在集合中
            mDownloadInfoMap.put(appInfo.getAppId(), downloadInfo);

            // 初始化下载任务
            DownloadTask task = new DownloadTask(downloadInfo);
            // 启动下载任务
            ThreadManager.getThreadManager().createLongPool().execute(task);
            // 将下载任务对象维护在集合当中
            mDownloadTaskMap.put(appInfo.getAppId(), task);
        }
    }

    public interface DownloadObserver {
        // 下载状态发生变化
        void onDownloadStateChanged(DownloadInfo info);

        // 下载进度发生变化
        void onDownloadProgressChanged(DownloadInfo info);
    }

    // 下载任务
    class DownloadTask implements Runnable {

        private DownloadInfo downloadInfo;
        private HttpHelper.HttpResult httpResult;

        public DownloadTask(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            // 更新下载状态
            downloadInfo.currentState = STATE_DOWNLOAD;
            notifyDownloadStateChanged(downloadInfo);
            File file = new File(downloadInfo.path);
            Log.e(Config.TAG,downloadInfo.path);
            String downloadURL = HttpHelper.URL
                    + "/download?name=" + downloadInfo.downloadUrl;
            if (!file.exists() || file.length() != downloadInfo.currentPos
                    || downloadInfo.currentPos == 0) {// 文件不存在, 或者文件长度和对象的长度不一致,
                // 或者对象当前下载位置是0
                file.delete();// 移除无效文件
                downloadInfo.currentPos = 0;// 文件当前位置归零
                httpResult = HttpHelper.download(downloadURL);// 从头开始下载文件

//                Log.d(Config.TAG,downloadURL);
            } else {
                // 需要断点续传
                httpResult = HttpHelper.download(downloadURL
                        + "&range=" + file.length());
            }

            InputStream in;
            FileOutputStream out = null;
            if (httpResult != null
                    && (in = httpResult.getInputStream()) != null) {
                try {
                    out = new FileOutputStream(file, true);//在源文件基础上追加

                    int len = 0;
                    byte[] buffer = new byte[1024];
                    while ((len = in.read(buffer)) != -1
                            && downloadInfo.currentState == STATE_DOWNLOAD) {// 只有在下载的状态才读取文件,如果状态变化,就立即停止读写文件
                        out.write(buffer, 0, len);
                        out.flush();

                        downloadInfo.currentPos += len;// 更新当前文件下载位置
                        notifyDownloadProgressChanged(downloadInfo);// 通知进度更新
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }

                // 下载结束, 判断文件是否完整
                if (file.length() == downloadInfo.size) {
                    // 下载完毕
                    downloadInfo.currentState = STATE_SUCCESS;

                    notifyDownloadStateChanged(downloadInfo);
                } else if (downloadInfo.currentState == STATE_PAUSE) {
                    // 中途暂停
                    notifyDownloadStateChanged(downloadInfo);
                } else {
                    // 下载失败
                    downloadInfo.currentState = STATE_ERROR;
                    downloadInfo.currentPos = 0;
                    notifyDownloadStateChanged(downloadInfo);
                    // 删除无效文件
                    file.delete();
                }

            } else {
                // 下载失败
                downloadInfo.currentState = STATE_ERROR;
                downloadInfo.currentPos = 0;
                notifyDownloadStateChanged(downloadInfo);
                // 删除无效文件
                file.delete();
            }

            // 不管下载成功,失败还是暂停, 下载任务已经结束,都需要从当前任务集合中移除
            mDownloadTaskMap.remove(downloadInfo.id);
        }

    }

    /**
     * 下载暂停
     */
    public synchronized void pause(AppInfo appInfo) {
        if (appInfo != null) {
            DownloadInfo downloadInfo = mDownloadInfoMap.get(appInfo.getAppId());
            if (downloadInfo != null) {
                int state = downloadInfo.currentState;
                // 如果当前状态是等待下载或者正在下载, 需要暂停当前任务
                if (state == STATE_WAITING || state == STATE_DOWNLOAD) {
                    // 停止当前的下载任务
                    DownloadTask downloadTask = mDownloadTaskMap
                            .get(appInfo.getAppId());
                    if (downloadTask != null) {
                        // 若下载任务在排队等待，则在此处进行取消任务；否则在run方法中取消任务
                        ThreadManager.getThreadManager().createLongPool().cancel(downloadTask);
                    }

                    // 更新下载状态为暂停
                    downloadInfo.currentState = STATE_PAUSE;
                    notifyDownloadStateChanged(downloadInfo);
                }
            }
        }
    }
    // 安装apk
    public synchronized void install(AppInfo appInfo) {
        DownloadInfo downloadInfo = mDownloadInfoMap.get(appInfo.getAppId());
        if (downloadInfo != null) {
            // 跳到系统的安装页面进行安装
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + downloadInfo.path),
                    "application/vnd.android.package-archive");
            UiTools.getContext().startActivity(intent);
            Log.e(Config.TAG,appInfo.getPackageName() + "正在被安装");
            TaskManager.getInstance().recordApp(downloadInfo.packageName,Config.DOWNLOAD_SUCCESS);

        }
    }

    // 根据应用对象,获取对应的下载对象
    public DownloadInfo getDownloadInfo(AppInfo data) {
        if (data != null) {
            return mDownloadInfoMap.get(data.getAppId());
        }

        return null;
    }
}
