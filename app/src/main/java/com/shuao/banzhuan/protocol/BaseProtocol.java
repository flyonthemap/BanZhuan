package com.shuao.banzhuan.protocol;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.http.HttpHelper;
import com.shuao.banzhuan.tools.FileUtils;
import com.shuao.banzhuan.tools.IOUtils;
import com.shuao.banzhuan.tools.OKClientManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;

/**
 * Created by flyonthemap on 16/8/8.
 * 网络加载的框架
 */
public abstract class BaseProtocol<T> {
    public T load(int index) {
        SystemClock.sleep(1000);
        // 加载本地数据
        String json = loadLocal(index);
        if (json == null) {
            // 请求服务器
            Log.d(Config.DEBUG,"load from server...");
            json = loadServer(index);
            if (json != null) {
                Log.d(Config.DEBUG,"save local...");
                saveJsonLocal(json, index);
            }
        }
        
        if (json != null) {
            Log.d(Config.DEBUG,"parse json..");
            return parseJson(json);
        } else {
            return null;
        }
    }
    private String loadServer(int index) {
        // 当服务的URL不需要index的值得时候，会自动过滤掉
        HttpHelper.HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey()
                + "?index=" + index + getParams());
        if (httpResult != null) {
            String json = httpResult.getString();
            Log.d(Config.DEBUG,json);
            return json;
        } else {
            return null;
        }
    }

    /**
     * 额外带的参数
     * @return
     */
    protected String getParams() {
        return "";
    }

    private void saveJsonLocal(String json, int index) {

        BufferedWriter bw = null;
        try {
            File dir = FileUtils.getCacheDir();
            // 创建缓存的目录/mnt/sdcard/googlePlay/cache/home_0
            File file = new File(dir, getKey() + "_" + index+getParams());
            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            // 在第一行写一个过期时间，设置为100秒
            bw.write(System.currentTimeMillis() + 1000 * 100 + "");
            bw.newLine();
            bw.write(json);
            bw.flush();
            // 关流处理
            IOUtils.closeQuietly(fw);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(bw);
        }
    }

    private String loadLocal(int index) {

        File dir = FileUtils.getCacheDir();// 获取缓存所在的文件夹
        File file = new File(dir, getKey() + "_" + index+getParams());
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            long outOfDate = Long.parseLong(br.readLine());
            // 如果发现文件已经过期了，就不再复用缓存
            if (System.currentTimeMillis() > outOfDate) {
                return null;
            } else {
                String str = null;
                StringWriter sw = new StringWriter();
                while ((str = br.readLine()) != null) {

                    sw.write(str);
                }
                return sw.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析json,
     *
     * @param json
     * @return
     */
    public abstract T parseJson(String json);

    /**
     * 说明了关键字
     *
     * @return
     */
    public abstract String getKey();
}

