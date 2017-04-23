package com.shuao.banzhuan.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.manager.DownloadManager;
import com.shuao.banzhuan.manager.TaskManager;
import com.shuao.banzhuan.model.DownloadInfo;
import com.shuao.banzhuan.tools.JSONUtils;
import com.shuao.banzhuan.tools.OKClientManager;
import com.shuao.banzhuan.tools.SPUtils;
import com.shuao.banzhuan.tools.UiTools;
import com.shuao.banzhuan.view.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flyonthemap on 2017/4/11.
 */

public class AppMonitorReceiver extends BroadcastReceiver {
    private static final int INSTALL = 1;
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    private TaskManager taskManager;
    private DownloadManager downloadManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            taskManager = TaskManager.getInstance();
            downloadManager = DownloadManager.getInstance();
            String packageName = getPackageName(intent);
            String phoneNum = (String) SPUtils.get(UiTools.getContext(),Config.PHONE,"");

            int state = INSTALL;

            if(taskManager.hasDownload(packageName)){
                taskManager.updateState(packageName,state);
                Map<String,String> params = new HashMap<>();
                params.put(Config.STR_PACK_NAME,packageName);
                params.put(Config.PHONE,phoneNum);
                params.put(Config.STATE,String.valueOf(state));
                OKClientManager.getOkManager().requestPostBySyn(Config.TASK_URI, params, new OKClientManager.LoadJsonString() {
                    @Override
                    public void onResponse(String res) {
                        JSONObject result = JSONUtils.instanceJsonObject(res);
                        Log.e(Config.TAG,result.toString());
                        try {
                            switch (result.getInt(Config.CODE)){
                                case SUCCESS:
                                    final int reward = result.getInt(Config.STR_REWARD);
                                    Log.e(Config.TAG,reward +"");
                                    UiTools.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(UiTools.getContext(),"获得奖励"+reward+"元",Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    DownloadInfo downloadInfo = new DownloadInfo();
                                    downloadInfo.currentState = DownloadManager.STATE_INSTALL;
                                    downloadManager.notifyDownloadStateChanged(downloadInfo);

                                    break;
                                case FAIL:
                                    UiTools.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(UiTools.getContext(),"已经获取奖励了，不能重复获取",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    DownloadInfo downloadInfo2 = new DownloadInfo();
                                    downloadInfo2.currentState = DownloadManager.STATE_INSTALL;
                                    downloadManager.notifyDownloadStateChanged(downloadInfo2);
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString();
            Log.e(Config.TAG,"卸载了:" + packageName + "包名的程序");

        }
    }
    private String getPackageName(Intent  intent){

        String[] result = intent.getDataString().split(":");
        return result[1];
    }
    private void init(){

    }
}
