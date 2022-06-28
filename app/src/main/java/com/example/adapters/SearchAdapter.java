package com.example.mytvdemo.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mytvdemo.CustomVedioView;
import com.example.mytvdemo.Movie;
import com.example.mytvdemo.R;
import com.example.mytvdemo.activities.DetailsActvity;
import com.example.mytvdemo.activities.SearchActivity;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchAdapter extends RecyclerView.Adapter{
    ArrayList<Movie> movieList;
    Context context;
    CustomVedioView videoView;
    MediaController mediaController;
    SeekBar seekBar;
    SearchActivity searchActivity;






    public void addData(int position,Movie movie) {
        showList(movieList);
        movieList.add(position, movie);
        notifyItemInserted(position);//注意这里
        showList(movieList);
    }

    public void removeData(int position) {
        movieList.remove(position);
        notifyItemRemoved(position);//注意这里
    }

    public void clearData(){
        movieList.clear();
        notifyDataSetChanged();
    }



    public SearchAdapter(ArrayList<Movie> movieList, Context context, CustomVedioView videoView, SeekBar seekBar, SearchActivity searchActivity){
        this.searchActivity = searchActivity;
        this.seekBar = seekBar;
        this.context = context;
        this.movieList = movieList;
        this.videoView = videoView;
    }

    static class VH extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        FrameLayout frameLayout;
        public VH(@NonNull View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.item_wrap);
            textView = itemView.findViewById(R.id.search_item_text);
            imageView = itemView.findViewById(R.id.search_item_image);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mediaController = new MediaController(context);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        mediaController.setVisibility(View.INVISIBLE);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_adpter_item,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int positionValue = position;
        VH vh = (VH)holder;
        vh.textView.setText(movieList.get(position).getTitle());
        //final int originHeight = textView.getHeight();!!!!这时候是不是textView还没初始化？？？？？
        FrameLayout frameLayout = vh.frameLayout;
        Glide.with(context).load(movieList.get(position).getCardImageUri()).into(vh.imageView);
        frameLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){//被聚焦的item

                    searchActivity.currentMovie = movieList.get(holder.getAdapterPosition());
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 1.1F);
                    valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            frameLayout.setScaleX((float)animation.getAnimatedValue());
                            frameLayout.setScaleY((float)animation.getAnimatedValue());
                            frameLayout.requestLayout();
                        }
                    });
                    frameLayout.setBackgroundResource(R.drawable.shape_orange_square_bg);
                    valueAnimator.start();

                    videoView.setVideoPath(movieList.get(holder.getAdapterPosition()).getVedioURL());





                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            if (mp.isPlaying()) {
                                mp.stop();
                                mp.release();
                                mp = new MediaPlayer();
                            }
                            int max = videoView.getDuration()/1000;
                            Log.d("TAG", "onFocusChange:最大值是 "+max);
                            String maxTime = max/60 + ":" + max%60;
                            seekBar.setMax(max);
                            searchActivity.maxTime.setText(maxTime);
                            mp.setVolume(0,0);
                            mp.setLooping(true);
                            //mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                            mp.start();
                        }
                    });

                }else{//失去焦点
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.1F,1);
                    valueAnimator.setDuration(500);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            frameLayout.setScaleX((float) animation.getAnimatedValue());
                            frameLayout.setScaleY((float) animation.getAnimatedValue());
                            frameLayout.requestLayout();
                        }
                    });
                    valueAnimator.start();
                    frameLayout.setBackgroundResource(R.drawable.shape_gray_square_bg);


                }


            }
        });
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActvity.class);
                intent.putExtra("Movie",movieList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    void showList(ArrayList<Movie> movieList){
        for(Movie movie : movieList){
            Log.d("TAG", "showList: "+movie.getTitle());
        }


    }
}
