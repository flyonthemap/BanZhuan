package com.shuao.banzhuan.protocol;

import com.shuao.banzhuan.entity.SubjectInfo;
import com.shuao.banzhuan.entity.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flyonthemap on 2017/4/19.
 */

public class PersonInfoProtocol extends BaseProtocol<UserInfo> {
    private UserInfo info;
    @Override
    public String getKey() {
        return "subject";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public UserInfo parseJson(String result) {
        try {
            JSONObject res = new JSONObject(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
