package com.example.mytvdemo.glideTarget;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class SearchTarget extends SimpleTarget<GlideDrawable> {
    ImageView imageView;


    public  SearchTarget(ImageView imageView){
        this.imageView = imageView;

    }
    @Override
    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {

        imageView.setBackground(resource.getCurrent());
    }
}
