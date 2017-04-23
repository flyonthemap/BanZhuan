package com.shuao.banzhuan.protocol;

import android.util.Log;

import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.model.TaskInfo;
import com.shuao.banzhuan.tools.JSONUtils;
import com.shuao.banzhuan.tools.SPUtils;
import com.shuao.banzhuan.tools.UiTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flyonthemap on 2017/4/18.
 */

public class TaskRecordProtocol extends BaseProtocol<List<TaskInfo>> {
    private List<TaskInfo> taskInfoList;
    @Override
    public List<TaskInfo> parseJson(String json) {
        taskInfoList=new ArrayList<TaskInfo>();
        try {
            Log.d(Config.TAG,json);
            JSONObject jsonObject = null;
            String packageName;
            String iconURL ;
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                TaskInfo info=new TaskInfo();
                info.setTaskState(jsonObject.getInt("state"));
                info.setTaskName(jsonObject.getString("name"));
                packageName = jsonObject.getString("packageName");
                iconURL = "app/" + packageName + "/icon.jpg";
                info.setTaskIconURL(iconURL);
                info.setTaskTime(jsonObject.getString("time"));


                taskInfoList.add(info);

            }
            return taskInfoList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getKey() {
        return "task/record";
    }
    protected String getParams() {
        String phoneNum = (String) SPUtils.get(UiTools.getContext(),Config.PHONE,"");
        return "&phoneNum=" +phoneNum;
    }
}
