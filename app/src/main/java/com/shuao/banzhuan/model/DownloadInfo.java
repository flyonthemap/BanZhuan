package com.shuao.banzhuan.model;

import android.os.Environment;

import com.shuao.banzhuan.manager.DownloadManager;
import com.shuao.banzhuan.model.AppInfo;

import java.io.File;

public class DownloadInfo {

    public String id;
    public String name;
    public String downloadUrl;
    public long size;
    public String packageName;

    public long currentPos;// 当前下载位置
    public int currentState;// 当前下载状态
    public String path;// 下载到本地文件的路径

    public static final String BANZHUAN = "banzhuan";// sdcard根目录文件夹名称
    public static final String DOWNLOAD = "download";// 子文件夹名称, 存放下载的文件

    // 获取下载进度(0-1)
    public float getProgress() {
        if (size == 0) {
            return 0;
        }

        float progress = currentPos / (float) size;
        return progress;
    }

    // 拷贝对象, 从AppInfo中拷贝出一个DownloadInfo
    public static DownloadInfo copy(AppInfo info) {
        DownloadInfo downloadInfo = new DownloadInfo();

        downloadInfo.id = info.getAppId();
        downloadInfo.name = info.getName();
        downloadInfo.downloadUrl = info.downloadUrl;
        downloadInfo.packageName = info.getPackageName();
        downloadInfo.size = info.getSize();

        downloadInfo.currentPos = 0;
        downloadInfo.currentState = DownloadManager.STATE_NONE;// 默认状态是未下载
        downloadInfo.path = downloadInfo.getFilePath();

        return downloadInfo;
    }

    // 获取文件下载路径
    public String getFilePath() {
        StringBuffer sb = new StringBuffer();
        String sdcard = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        sb.append(sdcard);
        sb.append(File.separator);
        sb.append(BANZHUAN);
        sb.append(File.separator);
        sb.append(DOWNLOAD);

        if (createDir(sb.toString())) {
            // 文件夹存在或者已经创建完成
            return sb.toString() + File.separator + name + ".apk";// 返回文件路径
        }

        return null;
    }

    private boolean createDir(String dir) {
        File dirFile = new File(dir);

        // 文件夹不存在或者不是一个文件夹
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return dirFile.mkdirs();
        }

        return true;// 文件夹存在
    }

}
