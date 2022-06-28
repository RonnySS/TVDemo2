package com.example.mytvdemo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.mytvdemo.Movie;
import com.example.mytvdemo.R;
import com.example.mytvdemo.Utils;

public class PlaybackOverlayActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_overlay);
        loadViews();
    }

    private static final String TAG = PlaybackOverlayActivity.class.getSimpleName();
    private VideoView mVideoView;
    private LeanbackPlaybackState mPlaybackState = LeanbackPlaybackState.IDLE;
    private int mPosition = 0;
    private long mStartTimeMillis;
    private long mDuration = -1;
    /*
     * List of various states that we can be in
     */
    public enum LeanbackPlaybackState {
        PLAYING, PAUSED, IDLE
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayback();
        mVideoView.suspend();
//        mVideoView.setVideoURI(null);
    }
    private void loadViews() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setFocusable(false);
        mVideoView.setFocusableInTouchMode(false);
        Movie movie = (Movie) getIntent().getSerializableExtra(DetailsActvity.MOVIE);
        setVideoPath(movie.getVedioURL());
    }
    public void setVideoPath(String videoUrl) {
        setPosition(0);
        mVideoView.setVideoPath(videoUrl);
        mStartTimeMillis = 0;
//        mDuration = Utils.getDuration(videoUrl);
        MediaController mediaController = new MediaController(this);

        //VideoView与MediaController建立关联
        mVideoView.setMediaController(mediaController);
        mediaController.setVisibility(View.INVISIBLE);
        mVideoView.requestFocus();
    }
    private void stopPlayback() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }

    private void setPosition(int position) {
        if (position > mDuration) {
            mPosition = (int) mDuration;
        } else if (position < 0) {
            mPosition = 0;
            mStartTimeMillis = System.currentTimeMillis();
        } else {
            mPosition = position;
        }
        mStartTimeMillis = System.currentTimeMillis();
        Log.d(TAG, "position set to " + mPosition);
    }

    public int getPosition() {
        return mPosition;
    }


    public void playPause(boolean doPlay) {
        if (mPlaybackState == LeanbackPlaybackState.IDLE) {
            /* Callbacks for mVideoView */
            //如果是闲置状态
            setupCallbacks();
        }
        if (doPlay && mPlaybackState != LeanbackPlaybackState.PLAYING) {
            //要播放而且视频状态不是播放
            mPlaybackState = LeanbackPlaybackState.PLAYING;
            if (mPosition > 0) {
                mVideoView.seekTo(mPosition);
            }
            mVideoView.start();
            //start和下面这个pause是控制的核心方法
            mStartTimeMillis = System.currentTimeMillis();
        } else {
            mPlaybackState = LeanbackPlaybackState.PAUSED;
            int timeElapsedSinceStart = (int) (System.currentTimeMillis() - mStartTimeMillis);
            setPosition(mPosition + timeElapsedSinceStart);
            mVideoView.pause();
        }
    }

    private void setupCallbacks() {
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mVideoView.stopPlayback();
                mPlaybackState = LeanbackPlaybackState.IDLE;
                return false;
            }
        });
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mPlaybackState == LeanbackPlaybackState.PLAYING) {
                    mVideoView.start();
                }
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlaybackState = LeanbackPlaybackState.IDLE;
            }
        });
    }

    public void fastForward() {
        if (mDuration != -1) {
            // Fast forward 10 seconds.
            setPosition(mVideoView.getCurrentPosition() + (10 * 1000));
            mVideoView.seekTo(mPosition);
        }
    }
    public void rewind() {
        // rewind 10 seconds
        setPosition(mVideoView.getCurrentPosition() - (10 * 1000));
        mVideoView.seekTo(mPosition);
    }




}