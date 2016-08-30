package com.shuao.banzhuan.tools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Created by flyonthemap on 16/8/12.
 */
public class PicassoImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private Bitmap.Config mConfig;

    public PicassoImageLoader() {
        this(Bitmap.Config.RGB_565);
    }

    public PicassoImageLoader(Bitmap.Config config) {
        this.mConfig = config;
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Picasso.with(activity)
                .load(new File(path))
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .config(mConfig)
                .resize(width, height)
                .centerInside()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}

