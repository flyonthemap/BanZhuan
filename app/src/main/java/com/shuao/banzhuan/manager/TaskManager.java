package com.shuao.banzhuan.manager;

import android.util.Log;

import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.view.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flyonthemap on 2017/4/12.
 */

public class TaskManager {
    private Map<String,Integer> appRecord= new HashMap<>();
    private static TaskManager taskManager = new TaskManager();
    private TaskManager() {}
    public static TaskManager getInstance() {
        return taskManager;
    }
    public  void recordApp(String packageName,int state) {
        Log.e(Config.TAG,packageName+"已经被记录");
       appRecord.put(packageName,state);
    }
    public  boolean hasDownload(String packageName) {
       return appRecord.containsKey(packageName);
    }
    public void updateState(String packageName,int state){
        appRecord.put(packageName,state);
    }

    public  Map<String,Integer> getAppRecord(){
        return appRecord;
    }
}
