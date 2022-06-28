package com.example.mytvdemo;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;

public class CustomVedioView extends VideoView {
    public CustomVedioView(Context context) {
        super(context);
    }

    public CustomVedioView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVedioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }


}
