package com.shuao.banzhuan.model;

import com.shuao.banzhuan.manager.DownloadManager;
import com.shuao.banzhuan.tools.FileUtils;

import java.io.File;

/**
 * Created by flyonthemap on 16/8/17.
 * 下载信息的封装类
 *
 */
public class DownloadInfo {
    //应用id
    private String id;
    //应用的名字
    private String name;
    //下载的链接
    private String downloadUrl;
    //安装包的大小
    private long size;
    //下载的包名
    private String packName;
    //当前的下载位置
    private long currentPos;
    //当前的状态
    private int currentState;

    private String path;

    //获取下载进度0-1
    public float getProgress(){
        if(size == 0){
            return 0;
        }
        return currentPos/(float)size;
    }

    public static DownloadInfo copy(AppInfo appInfo){
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.id = appInfo.getAppId();
        downloadInfo.size = appInfo.getSize();
//        downloadInfo.downloadUrl = appInfo.getDownloadUrl();
        downloadInfo.name = appInfo.getName();
//        downloadInfo.packName = appInfo.getPackageName();
        downloadInfo.currentPos = 0;
        // 默认的状态未下载
        downloadInfo.currentPos = DownloadManager.STATE_UNDO;
        downloadInfo.path = FileUtils.getDownloadPath(appInfo.getName()).getName();
        return  downloadInfo;
    }

    public void setAppId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public void setCurrentPos(long currentPos) {
        this.currentPos = currentPos;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAppId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public long getSize() {
        return size;
    }

    public String getPackName() {
        return packName;
    }

    public long getCurrentPos() {
        return currentPos;
    }

    public int getCurrentState() {
        return currentState;
    }

    public String getPath() {
        return path;
    }
}
