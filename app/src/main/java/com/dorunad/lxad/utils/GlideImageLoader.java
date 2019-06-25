package com.dorunad.lxad.utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dorunad.lxad.R;
import com.dorunad.lxad.base.App;

import java.io.File;

public class GlideImageLoader {

    public static void displayImageByPath(ImageView imageView, String path) {
        Glide.with(imageView.getContext())                             //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .apply(new RequestOptions()
                        .centerCrop()
                        .error(R.drawable.ic_img_error)
                        .placeholder(R.drawable.ic_img_loading)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                        .dontAnimate()
                )
                .into(imageView);

    }


    /**
     * url方式加载
     *
     * @param v   view
     * @param url url
     */
    public static void displayImageByUrl(ImageView v, String url) {
        Glide.with(v.getContext())
                .load(url)
                .apply(new RequestOptions()
                        .fitCenter()
                        .error(R.drawable.ic_img_error)
                        .placeholder(R.drawable.ic_img_loading)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                        .dontAnimate()
                )
                .into(v);
    }

    /**
     * url方式加载
     *
     * @param v   view
     * @param url url
     */
    public static void displayImageByUrlWithThum(ImageView v, String url) {
        Glide.with(v.getContext())
                .load(url)
                .thumbnail(0.1f)
                .apply(new RequestOptions()
                        .fitCenter()
                        .error(R.drawable.ic_img_error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                        .dontAnimate()
                )
                .into(v);
    }

    /**
     * url方式加载
     *
     * @param v   view
     * @param url url
     */
    public static void displayImageByUrlWithoutPlace(ImageView v, String url) {
        Glide.with(v.getContext())
                .load(url)
                .apply(new RequestOptions()
                        .fitCenter()
                        .error(R.drawable.ic_img_error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                        .dontAnimate()
                )
                .into(v);
    }

    /**
     * 加载本地图片
     *
     * @param v  view
     * @param id 资源id
     */
    public static void displayImageByResId(ImageView v, @DrawableRes int id) {
        Glide.with(v.getContext())
                .load(id)
                .apply(new RequestOptions()
                        .fitCenter()
                        .error(R.drawable.ic_img_error)
                        .placeholder(R.drawable.ic_img_loading)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                        .dontAnimate()
                )
                .into(v);
    }

    /**
     * 通过url获取图片的bitmap
     *
     * @param url
     * @param bitmapSimpleTarget
     */
    public static void getBitmapByUrl(String url, SimpleTarget<Bitmap> bitmapSimpleTarget) {
        Glide.with(App.me())
                .asBitmap()
                .load(url)
                .apply(new RequestOptions())
                .into(bitmapSimpleTarget);
    }
}
