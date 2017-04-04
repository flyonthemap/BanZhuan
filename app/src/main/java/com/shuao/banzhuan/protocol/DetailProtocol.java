package com.shuao.banzhuan.protocol;

import android.util.Log;

import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.model.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flyonthemap on 16/8/17.
 */
public class DetailProtocol extends BaseProtocol<AppInfo> {
    private String packName;
    public DetailProtocol(String packName) {
        this.packName = packName;
    }

    @Override
    public AppInfo parseJson(String result) {
        try {
            JSONObject jo = new JSONObject(result);

            AppInfo info = new AppInfo();
            info.setDescription(jo.getString(Config.STR_DESCRIPTION));
            info.setDownloadUrl(jo.getString(Config.STR_DOWNLOAD_URL));
            info.setIconUrl(jo.getString(Config.STR_ICON_URL));
            info.setAppId(jo.getString(Config.STR_ID));
            info.setName(jo.getString(Config.STR_NAME));
            info.setPackageName(jo.getString(Config.STR_PACK_NAME));
            info.setSize(jo.getLong(Config.STR_SIZE));





            // 解析截图信息
            JSONArray ja1 = jo.getJSONArray(Config.STR_SCREEN);
            ArrayList<String> screen = new ArrayList<String>();
            for (int i = 0; i < ja1.length(); i++) {
                String pic = ja1.getString(i);
                screen.add(pic);
            }
            info.setScreen(screen);
            return info;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getKey() {
        return "app/detail";
    }

    @Override
    protected String getParams() {
        return "&packName=" + packName;
    }
}
