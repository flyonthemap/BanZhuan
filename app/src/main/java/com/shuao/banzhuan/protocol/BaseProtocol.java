package com.shuao.banzhuan.protocol;

import android.os.SystemClock;
import android.util.Log;

import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.http.HttpHelper;
import com.shuao.banzhuan.tools.FileUtils;
import com.shuao.banzhuan.tools.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by flyonthemap on 16/8/8.
 * 网络请求的框架，首先从本地加载，如果本地没有，则从服务器加载数据，并存储
 */
public abstract class  BaseProtocol<T> {
    public T load(int index) {
        SystemClock.sleep(500);
        // 加载本地数据
        String json = loadLocal(index);
        if (json == null) {
            // 请求服务器
            Log.d(Config.DEBUG,"load from server...");
            json = loadServer(index);
            if (json != null) {
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
//        String url = Config.BASE_URL+"/task/list"+"?index="+index;
        String url = Config.BASE_URL+ "/"+getKey()
                + "?index=" + index + getParams();
//        HttpHelper.HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey()
//                + "?index=" + index + getParams());
        HttpHelper.HttpResult httpResult = HttpHelper.get(url);
        Log.d(Config.TAG,url);
        if (httpResult != null) {
            String json = httpResult.getString();
//            Log.d(Config.DEBUG,json);
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

    private void saveJsonLocal(String result, int index) {
        // 获取系统缓存目录
        File cacheDir =FileUtils.getCacheDir();
        // 以网络链接作为文件名称,保证特定接口对应特定数据，这里要注意key值中的/要被替换否则会造成文件替换不成功

        File cacheFile = new File(cacheDir,getKey().replaceAll("/","_") + "?index=" + index
                + getParams());
        FileWriter writer = null;
        try {
            writer = new FileWriter(cacheFile);
//            Log.e(Config.TAG,"--->load form local..2");
            // 缓存有效期限, 截止时间设定为半小时之后
            long validTime = System.currentTimeMillis() + 30 * 60 * 1000;
            writer.write(validTime + "\n");// 将缓存截止时间写入文件第一行
            writer.write(result);
            writer.flush();
        } catch (IOException e) {
//            Log.e(Config.TAG,"--->load form local..6...exception");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(writer);
        }

        Log.d(Config.DEBUG,"save local...");
    }


    private String loadLocal(int index) {
        Log.d(Config.TAG,"load form local..");
        // 以url为文件名, 以json为文件内容,保存在本地
        File cacheDir = FileUtils.getCacheDir();// 本应用的缓存文件夹
        // 生成缓存文件
        File file = new File(cacheDir, getKey().replaceAll("/","_") + "?index=" + index
                + getParams());
        Log.e(Config.TAG,file.toString());
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            long outOfDate = Long.parseLong(br.readLine());
//            Log.d(Config.TAG,"--->load form local.."+outOfDate+"hahahahah");
            // 如果发现文件已经过期了，就不再复用缓存
            if (System.currentTimeMillis() > outOfDate) {
                return null;
            } else {
                String str = null;
                StringWriter sw = new StringWriter();
                while ((str = br.readLine()) != null) {

                    sw.write(str);
                }
                String result = sw.toString();
//                if(result!=null){
//                    Log.d(Config.TAG,"load form local.."+result);
//                }
                return result;
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

