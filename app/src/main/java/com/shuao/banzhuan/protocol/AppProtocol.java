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
    private static List<AppInfo> appInfoList;
    @Override
    public List<AppInfo> parseJson(String json) {
        Log.d(Config.DEBUG,"parse json in AppProtocol...");
         appInfoList=new ArrayList<AppInfo>();
        try {
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("task");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                AppInfo info=new AppInfo();
                info.setAppId(jsonObj.getString("id"));
                info.setBonus(jsonObj.getInt("reward"));
                info.setName(jsonObj.getString("name"));
                info.setPackageName(jsonObj.getString("packageName"));
                info.setIconUrl(jsonObj.getString("iconUrl"));
                info.setDownloadUrl(jsonObj.getString("downloadUrl"));
                info.setSize(jsonObj.getLong("size"));
                info.setDescription(jsonObj.getString("des"));
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
