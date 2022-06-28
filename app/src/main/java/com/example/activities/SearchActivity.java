package com.example.mytvdemo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.mytvdemo.CustomVedioView;
import com.example.mytvdemo.Movie;
import com.example.mytvdemo.R;
import com.example.mytvdemo.ResDao;
import com.example.mytvdemo.SpacesItemDecoration;
import com.example.mytvdemo.adapters.SearchAdapter;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import com.example.mytvdemo.interfaces.retrofitInterface;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends FragmentActivity {
    static final String TAG = SearchActivity.class.getSimpleName();
    ArrayList<Movie> mItems = new ArrayList<>();
    public Movie currentMovie;
    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    Button backward,fastfoward,pause,fullScreen;
    EditText editText;
    CustomVedioView videoView;
    SeekBar seekBar;
    public TextView nowTime,maxTime;
    LinearLayoutManager layoutManager;
    int num = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fullScreen = findViewById(R.id.fullscreen);
        backward = findViewById(R.id.rollback);
        fastfoward = findViewById(R.id.fastforward);
        pause = findViewById(R.id.pause);
        videoView = findViewById(R.id.search_videoView);
        editText = findViewById(R.id.search_edittext);
        seekBar = findViewById(R.id.search_seekBar);
        nowTime = findViewById(R.id.nowTime);
        maxTime = findViewById(R.id.maxTime);
        initData();
        recyclerView = findViewById(R.id.research_recycleView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        searchAdapter = new SearchAdapter(mItems,this,videoView,seekBar,this);
        recyclerView.setAdapter(searchAdapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);

        recyclerView.setItemAnimator(defaultItemAnimator);

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                videoView.seekTo(Math.max(0,videoView.getCurrentPosition()-15000));
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying()){
                    videoView.pause();
                }else {
                    videoView.start();
                }
            }
        });

        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, PlaybackOverlayActivity.class);
                intent.putExtra("Movie", currentMovie);
                intent.putExtra("shouldStart", true);
                startActivity(intent);
            }
        });

        fastfoward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(Math.min(videoView.getDuration(),videoView.getCurrentPosition()+15000));
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        if(videoView.isPlaying()){
                            int current = videoView.getCurrentPosition()/1000;
                            Log.d("TAG", "run: "+current);
                            String curTime = current/60 + ":" + current%60;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    seekBar.setProgress(current);
                                    nowTime.setText(curTime);
                                }
                            });

                        }

                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editText.setBackgroundResource(R.drawable.focus_rounded_rectangle);
                }else {
                    editText.setBackgroundResource(R.drawable.rounded_rectangle);
                }

            }
        });
        RxTextView.textChanges(editText)
                .debounce(1000, TimeUnit.MILLISECONDS).skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CharSequence value) {
                        Log.d("TAG", "onNext: "+value.toString());
                        HttpUrl httpUrl = HttpUrl.parse("http://124.221.72.44:8080/findMovie?")
                                .newBuilder()
                                .addQueryParameter("MapStr",value.toString()).build();

                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request request1 = new Request.Builder().url(httpUrl).get().build();
                        okHttpClient.newCall(request1).enqueue(new okhttp3.Callback() {
                            @Override
                            public void onFailure(okhttp3.Call call, IOException e) {
                                Log.d("TAG", "onFailure: OKHTTP失败了");
                            }

                            @Override
                            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                                Gson gson = new Gson();
                                ResDao resDao = gson.fromJson(response.body().string(),ResDao.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        searchAdapter.clearData();
                                    }
                                });


                                Observable.create(new ObservableOnSubscribe<Movie>() {

                                    @Override
                                    public void subscribe(ObservableEmitter<Movie> e) throws Exception {
                                        ////////////////

                                        Log.d("TAG", "这里也执行了onNext: "+value.toString());
                                        //e.onNext(null);
                                        for (ResDao.Moive moive:resDao.getResultList()){
                                            Movie movie = new Movie();
                                            movie.setTitle(moive.getTitle());
                                            movie.setCardImageUri(moive.getCardImageUri());
                                            if(moive.getVedioURL().equals("1")){
                                                movie.setVedioURL("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
                                            }else {
                                                movie.setVedioURL("android.resource://"+getPackageName()+"/" + R.raw.tutorial_test);
                                            }

                                            Log.d(TAG, "subscribe:视频资源的实际地址 "+"android.resource://"+getPackageName()+"/" + R.raw.tutorial_test);
                                            e.onNext(movie);
                                            Thread.sleep(200);
                                        }

                                        e.onComplete();
                                    }
                                })
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<Movie>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onNext(Movie value) {
//                                                if (value!=null){
                                                    searchAdapter.addData(0,value);
//                                                }else {
//
//                                                }

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {


                                            }
                                        });

                            }
                        });





                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });





    }

    void initData(){
        HttpUrl httpUrl = HttpUrl.parse("http://124.221.72.44:8080/findMovie?")
                .newBuilder()
                .addQueryParameter("MapStr","").build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request1 = new Request.Builder().url(httpUrl).get().build();
        okHttpClient.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: 初始化网络请求失败了");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ResDao resDao = gson.fromJson(response.body().string(),ResDao.class);
                Observable.create(new ObservableOnSubscribe<Movie>() {
                    @Override
                    public void subscribe(ObservableEmitter<Movie> e) throws Exception {
                        for (ResDao.Moive moive:resDao.getResultList()){
                            Movie movie = new Movie();
                            movie.setTitle(moive.getTitle());
                            movie.setCardImageUri(moive.getCardImageUri());
                            movie.setStudio(moive.getStudio());
                            if(moive.getVedioURL().equals("1")){
                                movie.setVedioURL("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
                            }else {
                                movie.setVedioURL("android.resource://"+getPackageName()+"/" + R.raw.tutorial_test);

                                //movie.setVedioURL("android.resource://"+getPackageName()+"/" + R.raw.yewen);
                            }
                            e.onNext(movie);
                        }
                        Thread.sleep(300);
                        e.onComplete();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Movie>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Movie value) {
                                searchAdapter.addData(0,value);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                //!!!!!!
//                                recyclerView.smoothScrollToPosition(0);
//                                recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                                    @Override
//                                    public void onGlobalLayout() {
//                                        Log.d(TAG, "onGlobalLayout: recycleView绘制完成了一次");
//                                        if (layoutManager.findViewByPosition(0)!=null){
//                                            Log.d(TAG, "onGlobalLayout: 里面执行了一次");
//                                            layoutManager.findViewByPosition(0).requestFocus();
//                                            recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                                        }
//                                    }
//                                });
                                layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition()).requestFocus();


                            }
                        });



            }
        });


//        for (int i = 0; i < 10; i++) {
//            Movie movie = new Movie();
//            movie.setTitle("Title"+i);
//            movie.setCardImageUri("https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg");
////            movie.setVedioURL("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
//            movie.setVedioURL("android.resource://"+getPackageName()+"/" + R.raw.tutorial_test);
//            movie.setStudio("studio"+i);
//            mItems.add(movie);
//
//        }
    }

}