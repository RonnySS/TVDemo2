package com.example.mytvdemo.BackgroundManger;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.leanback.app.BackgroundManager;

import com.bumptech.glide.Glide;
import com.example.mytvdemo.R;
import com.example.mytvdemo.glideTarget.GlideTarget;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

public class GlideBackgroundManger {
    private static final String TAG = GlideBackgroundManger.class.getSimpleName();
    private static int BACKGROUND_UPDATE_DELAY = 500;
    private final int DEFAULT_BACKGROUND_RES_ID = R.drawable.default_background;
    private static Drawable mDefaultBackground;
    // Handler attached with main thread
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Activity mActivity;
    private BackgroundManager mBackgroundManager = null;
    private DisplayMetrics mMetrics;
    private URI mBackgroundURI;
//    private PicassoBackgroundManagerTarget mBackgroundTarget;
    Timer mBackgroundTimer; // null when no UpdateBackgroundTask is running.
    public GlideBackgroundManger (Activity activity) {
        mActivity = activity;
        mDefaultBackground = activity.getDrawable(DEFAULT_BACKGROUND_RES_ID);
        mBackgroundManager = BackgroundManager.getInstance(activity);
        mBackgroundManager.attach(activity.getWindow());
        //mBackgroundTarget = new PicassoBackgroundManagerTarget(mBackgroundManager);
        mMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void startBackgroundTimer(){
        if (mBackgroundTimer != null){
            mBackgroundTimer.cancel();
        }

        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(),BACKGROUND_UPDATE_DELAY);
    }

    private class UpdateBackgroundTask extends TimerTask{

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateBackground(mBackgroundURI);

                }
            });
        }
    }


    public void updateBackgroundWithDelay(String url) {
        try {
            URI uri = new URI(url);
            updateBackgroundWithDelay(uri);
        } catch (URISyntaxException e) {
            /* skip updating background */
            Log.e(TAG, e.toString());
        }
    }
    /**
     * updateBackground with delay
     * delay time is measured in other Timer task thread.
     * @param uri
     */
    public void updateBackgroundWithDelay(URI uri) {
        mBackgroundURI = uri;
        startBackgroundTimer();
    }


    private void updateBackground(URI uri) {
        try {
            Glide.with(mActivity)
                    .load(uri.toString())
                    .centerCrop()
                    .error(mDefaultBackground)
                    .into(new GlideTarget(mBackgroundManager));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }




}
