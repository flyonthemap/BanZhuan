package com.shuao.banzhuan.model;

import java.sql.Timestamp;

/**
 * Created by flyonthemap on 2017/3/24.
 */

public class TaskInfo {
    private String taskTime;
    private String taskName;
    private int taskState;
    private String taskIconURL;

    public String getTaskIconURL() {
        return taskIconURL;
    }

    public void setTaskIconURL(String taskIconURL) {
        this.taskIconURL = taskIconURL;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }
}
