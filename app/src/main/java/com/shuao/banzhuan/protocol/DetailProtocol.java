package com.shuao.banzhuan.protocol;

import android.util.Log;

import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.model.AppInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flyonthemap on 16/8/17.
 */
public class DetailProtocol extends BaseProtocol<AppInfo> {
    private String taskID;
    public DetailProtocol(String taskID) {
        this.taskID = taskID;
    }

    @Override
    public AppInfo parseJson(String json) {
        AppInfo appInfo = new AppInfo();
        try {
            JSONObject jsonObject = null;
            if(json != null)
                jsonObject = new JSONObject(json);
            Log.d(Config.DEBUG,json);
            if(jsonObject != null){
                appInfo.setTaskId(jsonObject.getString("taskID"));
                appInfo.setAppId(jsonObject.getString("appID"));
                appInfo.setName(jsonObject.getString("taskName"));
                appInfo.setBonus(jsonObject.getInt("reward"));
                appInfo.setSize(jsonObject.getLong("size"));
                appInfo.setDescription(jsonObject.getString("description"));
                appInfo.setVersion(jsonObject.getString("version"));
                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("stepDesc"));
                int stepCount = jsonObject.getInt("stepNum");
                List<String> steps = new ArrayList<>();
                for(int i=1;i <= stepCount;++i){
                    steps.add(jsonObject1.getString("step"+i));
                    Log.d(Config.DEBUG,jsonObject1.getString("step"+i));
                }
                appInfo.setSteps(steps);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return appInfo;
    }

    @Override
    public String getKey() {
        return "task/detail";
    }

    @Override
    protected String getParams() {
        return "&taskID=" + taskID;
    }
}
