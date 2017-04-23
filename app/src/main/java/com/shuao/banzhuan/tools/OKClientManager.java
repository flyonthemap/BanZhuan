package com.shuao.banzhuan.tools;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.manager.ThreadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.shuao.banzhuan.data.Config.BASE_URL;

/**
 * Created by flyonthemap on 16/8/8.
 * okhttp的工具类，封装了图片单张多张图片上传，异步get请求和post请求
 */

public class OKClientManager {
    // 上传JSON格式的数据
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    // 指定提交正文的类型，否则无法获取post请求体中的内容
    private static final MediaType MEDIA_TYPE_FORM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
    // 上传图片
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_MULTIPART = MediaType.parse("multipart/form-data;charset=utf-8");
    private static final String PORTRAIT = "portrait";
    private OkHttpClient okHttpClient;
    private volatile static OKClientManager okManager;
    private OKClientManager(){
        okHttpClient = new OkHttpClient();
    }

    // 双重校验单例模式
    public static  OKClientManager getOkManager(){
        if(okManager == null){
            synchronized (OKClientManager.class){
                if(okManager == null){
                    okManager = new OKClientManager();
                }
            }
        }
        return okManager;
    }



    /**
     * get异步请求
     *
     * @param actionURI 请求的URI
     * @param paramsMap 请求参数的map
     * @param callback 回调接口
     */
    public  void requestGetBySyn(String actionURI, Map<String, String> paramsMap,final LoadJsonString callback) {
        try {

            //生成完成的请求地址
            String params = getParamString(paramsMap);
            String requestUrl = getRequestUrl(actionURI+"?"+params);
            Log.d(Config.TAG,requestUrl);
            //创建一个请求
            final Request request = new Request.Builder().url(requestUrl).build();
            // 异步get调用
            ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            onSuccessJsonString(response.body().string(),callback);
                        }
                    });

                }
            });

        } catch (Exception e) {
            Log.e(Config.TAG, e.toString());
        }
    }
    /**
     * post异步请求
     *
     * @param actionUri 请求的URI
     * @param paramsMap 请求参数的map
     * @param callback 回调接口
     */
    public  void requestPostBySyn(String actionUri, Map<String, String> paramsMap,final LoadJsonString callback) {
        try {
            String params = getParamString(paramsMap);
            String requestUrl = getRequestUrl(actionUri);
            Log.d(Config.TAG,requestUrl);
            //生成参数
            Log.d(Config.TAG,params);
            //创建一个请求实体对象 RequestBody
            RequestBody body = RequestBody.create(MEDIA_TYPE_FORM, params);
            //创建一个请求
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            //异步调用
            ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            onSuccessJsonString(response.body().string(),callback);
                        }
                    });

                }
            });

        } catch (Exception e) {
            Log.e(Config.TAG, e.toString());
        }
    }


    // 进行多段式表单提交
    public void postByMultipart(String actionURI, final Map<String,String> paramsMap,final Map<String,String> paths, final LoadJsonString callback){
        final String requestUrl = getRequestUrl(actionURI);
        ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                MultipartBody.Builder builder =  new MultipartBody.Builder().setType(MultipartBody.FORM);
                Log.e(Config.TAG,requestUrl);
                if(paths.size() != 0){
                    for (String path : paths.keySet()) {
                        builder.addFormDataPart(PORTRAIT, path, RequestBody.create(MEDIA_TYPE_PNG, new File(paths.get(path))));
                    }
                }
                if(paramsMap.size() != 0){
                    for (String key : paramsMap.keySet()) {
                        builder.addFormDataPart(key, paramsMap.get(key));
                    }
                }
                RequestBody requestBody = builder.build();
                Request request = new Request.Builder()
                        .url(requestUrl)
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        onSuccessJsonString(response.body().string(),callback);
                    }
                });
            }
        });
    }

    //从特定的URL上获取JSONString
    public void asyncByURL(String url, final LoadJsonString callback){
        final Request request = new Request.Builder().url(url).build();
        // 在线程池中进行网络访问请求
        ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response != null){
                            if(response.isSuccessful()){
                                onSuccessJsonString(response.body().string(),callback);
                            }
                        }
                    }
                });
            }
        });


    }


    //从特定的URL获取特定的json对象
    public void asyncByURL(final String url, final LoadJsonObject callback){
        ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                final Request request = new Request.Builder().url(url).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response != null){
                            if(response.isSuccessful()){
                                onSuccessJsonObject(response.body().string(),callback);
                            }
                        }
                    }
                });
            }
        });

    }
    // 从特定的URL中获取JSON数组
    public void asyncByURL(final String url, final LoadByteArray callback){
        ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                final Request request = new Request.Builder().url(url).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response != null){
                            if(response.isSuccessful()){
                                onSuccessByte(response.body().bytes(),callback);
                            }
                        }
                    }
                });
            }
        });


    }

    // 从特定的URL中获取下载图片
    public void asyncByURL(final String url, final LoadBitmap callback){
        ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                final Request request = new Request.Builder().url(url).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response != null){
                            if(response.isSuccessful()){
                                byte[] data = response.body().bytes();

                                Bitmap bitmap = new PicassoUtils.CropSquareTransformation().transform(BitmapFactory.decodeByteArray(data,0,data.length));
                                onSuccessBitmap(bitmap,callback);
                                callback.onResponse(bitmap);
                            }
                        }
                    }
                });
            }
        });



    }



    public void cacheResponse(File cacheDir,final String url,int cacheSize) throws Exception {
        Cache cache = new Cache(cacheDir, cacheSize);

        okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        final Request request = new Request.Builder().url(url)
                .build();
        ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(request).execute();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void onSuccessJsonString(final String jsonValue, final LoadJsonString callback){
        if(callback != null){
            try {
                callback.onResponse(jsonValue);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    private void onSuccessBitmap(final Bitmap jsonValue, final LoadBitmap callback){
        if(callback != null){
            try {
                callback.onResponse(jsonValue);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    private void onSuccessJsonObject(final String jsonValue, final LoadJsonObject callback){
        if(callback != null){
            try {
                callback.onResponse(new JSONObject(jsonValue));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    private void onSuccessByte(final byte[] jsonValue, final LoadByteArray callback){
        if(callback != null){
            try {
                callback.onResponse(jsonValue);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private String getParamString(Map<String,String> paramsMap){
        StringBuilder tempParams = new StringBuilder();
        int pos = 0;
        for (String key : paramsMap.keySet()) {
            if (pos > 0) {
                tempParams.append("&");
            }
            try {
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            pos++;
        }
        return tempParams.toString();
    }
    private String getRequestUrl(String actionUri) {
        return String.format("%s/%s", BASE_URL, actionUri);
    }
    public interface LoadJsonString{
        void onResponse(String result);
    }
    public interface LoadByteArray{
        void onResponse(byte[] result);
    }
    public interface LoadBitmap{
        void onResponse(Bitmap result);
    }
    public interface LoadJsonObject{
        void onResponse(JSONObject result) throws JSONException;
    }
}
