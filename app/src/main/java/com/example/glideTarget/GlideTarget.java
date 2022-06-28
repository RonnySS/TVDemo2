package com.example.mytvdemo.glideTarget;

import androidx.leanback.app.BackgroundManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class GlideTarget extends SimpleTarget<GlideDrawable> {
    BackgroundManager backgroundManager = null;
    public GlideTarget(BackgroundManager backgroundManager){
        this.backgroundManager = backgroundManager;
    }

    @Override
    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
        backgroundManager.setDrawable(resource.getCurrent());
    }
}