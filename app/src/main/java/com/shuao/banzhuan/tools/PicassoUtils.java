package com.shuao.banzhuan.tools;

/**
 * Created by flyonthemap on 16/8/8.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.manager.ThreadManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class PicassoUtils {
    // 注意这里不能将Target对象声明为局部变量，可以避免Target的创建和销毁而产生的额外代价
    private static Target target;


    //从path中加载文件，放入ImagineView

    public static void loadImageWithSize(String path, int width, int height, ImageView imageView) {

        Picasso.with(BaseApplication.getApplication()).load(path).
                placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).resize(width, height).centerCrop().
                into(imageView);
    }

    public static void loadImageWithHolder(String path, int resID, ImageView imageView) {
        Picasso.with(BaseApplication.getApplication()).load(path).fit().placeholder(resID).into(imageView);
    }

    public static void loadImageWithCrop(String path, ImageView imageView) {
        Picasso.with(BaseApplication.getApplication()).load(path).transform(new CropSquareTransformation()).
                into(imageView);
    }

    /**
     * 实现对图片的自定义裁剪
     */
    public static class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != null){
                source.recycle();;
            }
            return result;
        }


        @Override
        public String key() {
            return "square()";
        }
    }
    //    将图片缓存至本地
    public static void saveLocalByPath(String url, final String fileName){

        target = new Target() {

            @Override
            public void onPrepareLoad(Drawable drawable) {

            }

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {


                // 确定图片的缓存路径
                final File finalFile = new File(fileName);
                ThreadManager.getThreadManager().createLongPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(finalFile);

                            BufferedOutputStream bs = new BufferedOutputStream(outputStream);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bs);
                            outputStream.close();
                            bs.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
                return;
            }
        };
        //  将本地图片加载到缓存目录
        Picasso.with(BaseApplication.getApplication()).load(new File(url)).into(target);

    }
    public static void cacheLocalByUrl(String url,final String fileName){
        target = new Target() {

            @Override
            public void onPrepareLoad(Drawable drawable) {

                return;
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                try {
                    File file = null;
                    // 确定图片的缓存路径
                    String path = UiTools.getContext().getCacheDir() + File.separator + fileName;
                    Config.PORTRAIT_PATH = path;
                    SharedPreferences sharedPreferences = UiTools.getContext().
                            getSharedPreferences("banzhuan",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Config.STR_PORTRAIT_PATH,Config.PORTRAIT_PATH).apply();
                    file = new File(path);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    BufferedOutputStream bs = new BufferedOutputStream(outputStream);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bs);
                    outputStream.close();
                    bs.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
                return;
            }
        };
        //  将网络图片加载到缓存目录
        Picasso.with(BaseApplication.getApplication()).load(url).into(target);
    }
}
