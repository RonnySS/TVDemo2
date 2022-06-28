package com.example.mytvdemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import android.util.Log;
import android.view.View;

import com.example.mytvdemo.BackgroundManger.GlideBackgroundManger;
import com.example.mytvdemo.BackgroundManger.SimpleBackgroundManager;
import com.example.mytvdemo.activities.DetailsActvity;
import com.example.mytvdemo.activities.ErrorActivity;
import com.example.mytvdemo.activities.SearchActivity;
import com.example.mytvdemo.activities.WebActivity;

public class TestFragment extends BrowseSupportFragment {
    ArrayObjectAdapter rowsAdapter;
    private static SimpleBackgroundManager simpleBackgroundManager = null;
    private GlideBackgroundManger glideBackgroundManger = null;
    private static String TAG = TestFragment.class.getSimpleName();

    public TestFragment() {
        // Required empty public constructor
    }






    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTitle("TV Demo");
        simpleBackgroundManager = new SimpleBackgroundManager(getActivity());
        glideBackgroundManger = new GlideBackgroundManger(getActivity());
        setHeadersState(HEADERS_HIDDEN);
        setHeadersTransitionOnBackEnabled(true);
        //setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));



        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));
        loadRows();
        setupEventListeners();
    }


    private void setupEventListeners(){
        setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                Log.d(TAG, "onItemSelected: ");
                if(item instanceof String){
                    glideBackgroundManger.updateBackgroundWithDelay("https://fuss10.elemecdn.com/3/28/bbf893f792f03a54408b3b7a7ebf0jpeg.jpeg");
                }else if(item instanceof Movie){
                    glideBackgroundManger.updateBackgroundWithDelay("https://imgconvert.csdnimg.cn/aHR0cHM6Ly9tbWJpei5xcGljLmNuL21tYml6X2pwZy9QbjRTbTBSc0F1aWFwaHBNaWEzaWJZTTZVOGJvZ3RrY1laVjkxVWZzaGdNSklkNW04WlpTUzdoNUp0dVF6THFwOUlmNkpxTzhLalV1SUR0RTdiREp6TGdmZy82NDA?x-oss-process=image/format,png");
                }
            }
        });
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if(item instanceof String){
                    if(((String)item).equals("错误")){
                        Intent intent = new Intent(getActivity(), ErrorActivity.class);
                        startActivity(intent);
                    }

                }
                if(item instanceof String){
                    if(((String)item).equals("网页测试")){

                        Intent intent = new Intent(getActivity(), WebActivity.class);

                        startActivity(intent);
                    }

                }
                if (item instanceof Movie) {
                    Movie movie = (Movie) item;
                    Intent intent = new Intent(getActivity(), DetailsActvity.class);
                    intent.putExtra("Movie", movie);
                    getActivity().startActivity(intent);
                }
            }
        });
        setOnSearchClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));

            }
        });
    }

    private void loadRows(){
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        HeaderItem headerItem = new HeaderItem(0,"a_headitem");
        HeaderItem headerItem2 = new HeaderItem(1,"b_headitem");
        GridItemPresenter gridItemPresenter = new GridItemPresenter();
        CardPresenter cardPresenter = new CardPresenter();
        ArrayObjectAdapter cardRowAdapter = new ArrayObjectAdapter(cardPresenter);
        for (int i = 0; i < 10; i++) {
            Movie movie = new Movie();
            movie.setTitle("Title"+i);
            movie.setCardImageUri("https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg");
//            movie.setVedioURL("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
            movie.setVedioURL("android.resource://"+getActivity().getPackageName()+"/" + R.raw.tutorial_test);
            movie.setStudio("studio"+i);
            cardRowAdapter.add(movie);

        }


        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(gridItemPresenter);
        gridRowAdapter.add("item1");
        gridRowAdapter.add("网页测试");
        gridRowAdapter.add("item3");
        gridRowAdapter.add("错误");

        rowsAdapter.add(new ListRow(headerItem,cardRowAdapter));
        rowsAdapter.add(new ListRow(headerItem2,gridRowAdapter));
        setAdapter(rowsAdapter);

    }
}