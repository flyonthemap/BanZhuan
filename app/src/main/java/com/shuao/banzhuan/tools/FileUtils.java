package com.shuao.banzhuan.tools;

/**
 * Created by flyonthemap on 16/8/8.
 */

import java.io.File;

import android.os.Environment;

import com.shuao.banzhuan.data.Config;

public class FileUtils {

    /**
    * 获取下载的路径
     */
    public static File getDownloadPath(String name){
        return getDir(Config.DOWNLOAD+File.separator+name+".apk");
    }
    /**
     * 获取图片的缓存的路径
     * @return
     */
    public static File getIconDir(){
        return getDir(Config.ICON);

    }
    /**
     * 获取缓存路径
     * @return
     */
    public static File getCacheDir() {
        return getDir(Config.CACHE);
    }

    public static File getDir(String cache) {
        StringBuilder path = new StringBuilder();
        // 首先判断应用是否可以存在SD卡上
        if (isSDAvailable()) {
            //返回cache目录 /mnt/sdcard/GooglePlay/cache
            path.append(Environment.getExternalStorageDirectory()
                    .getAbsolutePath());
            path.append(File.separator);
            path.append(Config.ROOT);
            path.append(File.separator);
            path.append(cache);

        }else{
            // 返回cache目录/data/data/com.shuao.BanZhuan/cache/cache
            File filesDir = UiTools.getContext().getCacheDir();
            path.append(filesDir.getAbsolutePath());
            path.append(File.separator);
            path.append(cache);
        }
        File file = new File(path.toString());
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();// 创建文件夹
        }
        return file;

    }



    //    判断sd卡是否可用
    private static boolean isSDAvailable() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


}
