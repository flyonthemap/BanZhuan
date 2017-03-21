package com.shuao.banzhuan.protocol;

import android.util.Log;

import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.model.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by flyonthemap on 16/8/8.
 * 加载解析app需要
 */
public class AppProtocol extends BaseProtocol<List<AppInfo>>{
    private List<String> pictures;
    @Override
    public List<AppInfo> parseJson(String json) {
        Log.d(Config.DEBUG,"parse json in AppProtocol...");
        Log.d(Config.DEBUG,json);
        List<AppInfo> appInfoList=new ArrayList<AppInfo>();
//        pictures=new ArrayList<String>();
        try {
            JSONObject jsonObject=new JSONObject(json);
//            JSONArray jsonArray2 = jsonObject.getJSONArray("picture");
//            for(int i=0;i<jsonArray2.length();i++){
//                String str=jsonArray2.getString(i);
//                pictures.add(str);
//            }




            JSONArray jsonArray = jsonObject.getJSONArray("task");
            Log.d(Config.DEBUG,jsonArray.toString());
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                AppInfo info=new AppInfo();
                Log.d(Config.TAG,"appID="+jsonObj.getString("appID"));
                info.setAppId(jsonObj.getString("appID"));
                info.setBonus(jsonObj.getInt("reward"));
                info.setName(jsonObj.getString("taskName"));
                info.setTaskType(jsonObj.getString("type"));
                info.setSize(jsonObj.getLong("size"));
                info.setDescription(jsonObj.getString("brief"));
                info.setTaskId(jsonObj.getString("taskID"));
                appInfoList.add(info);

            }
            return appInfoList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<String> getPictures() {
        return pictures;
    }



    @Override
    public String getKey() {
        return "task/list";
    }


}
