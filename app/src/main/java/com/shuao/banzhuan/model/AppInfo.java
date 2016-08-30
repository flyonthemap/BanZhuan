package com.shuao.banzhuan.model;

import java.util.List;

/**
 * Created by flyonthemap on 16/8/8.
 *
 */
public class AppInfo {
    //    任务的Id
    private String taskId;
    //    应用的Id
    private String appId;
    //    应用的名字
    private String name;
    //    图标的URL
    private String iconUrl;
    //    app的大小
    private long size;
    //    奖励数
    private long bonus;
    //    任务类型
    private String taskType;

    /*应用详情界面中需要的信息*/
    //    应用描述信息
    private String description;
    //    步骤信息
    private List<String> steps;
    // 应用的版本号
    private String version;

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

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public void setVersion(String version) {
        this.version = version;
    }


}

