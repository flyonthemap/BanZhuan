package com.shuao.banzhuan.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flyonthemap on 16/8/8.
 *
 */
public class AppInfo implements Serializable {
    private static final long serialVersionUID = 2072893447591548402L;


    // 用用条目中需要的信息
    private String taskId; //    任务的Id
    private String appId;//    应用的Id
    private String name;//    应用的名字
    private String iconUrl;//    图标的URL
    private long size;//    app的大小
    private long bonus; //    奖励数
    private String taskType; //    任务类型
    private String packageName; // 应用包名
    public String downloadUrl; //    apk下载地址的URL

    /*应用详情界面中需要的信息*/

    private String description;//    应用描述信息
    private ArrayList<String> steps; //    步骤信息
    public ArrayList<String> screen; // 应用截图信息
    private String version;// 应用的版本号
    public ArrayList<String> getScreen() {
        return screen;
    }

    public void setScreen(ArrayList<String> screen) {
        this.screen = screen;
    }
    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }



    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }




    public String getVersion() {
        return version;
    }

    public List<String> getSteps() {
        return steps;
    }

    public String getDescription() {
        return description;
    }

    public String getTaskId() {
        return taskId;
    }

    public long getBonus() {
        return bonus;
    }

    public long getSize() {
        return size;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getAppId() {
        return appId;
    }

    public String getName() {
        return name;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setBonus(long bonus) {
        this.bonus = bonus;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public void setVersion(String version) {
        this.version = version;
    }


}

