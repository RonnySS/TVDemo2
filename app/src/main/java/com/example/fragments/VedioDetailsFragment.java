package com.example.mytvdemo.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.SparseArrayObjectAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytvdemo.BackgroundManger.GlideBackgroundManger;
import com.example.mytvdemo.CardPresenter;
import com.example.mytvdemo.DetailsDescriptionPresenter;
import com.example.mytvdemo.Movie;
import com.example.mytvdemo.R;
import com.example.mytvdemo.activities.PlaybackOverlayActivity;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VedioDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VedioDetailsFragment extends DetailsSupportFragment {
    FullWidthDetailsOverviewRowPresenter fullWidthDetailsOverviewRowPresenter;

    private static final String TAG = VedioDetailsFragment.class.getSimpleName();
    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;
    private static final String MOVIE = "Movie";
    private GlideBackgroundManger mPicassoBackgroundManager;
    private Movie mSelectedMovie;
    private DetailsRowBuilderTask mDetailsRowBuilderTask;



    public VedioDetailsFragment() {
        // Required empty public constructor
    }

    public static VedioDetailsFragment newInstance(String param1, String param2) {
        VedioDetailsFragment fragment = new VedioDetailsFragment();

        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();

        try {
            mPicassoBackgroundManager.updateBackgroundWithDelay(mSelectedMovie.getCardImageUri().toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullWidthDetailsOverviewRowPresenter = new FullWidthDetailsOverviewRowPresenter( new DetailsDescriptionPresenter()  );
        mPicassoBackgroundManager = new GlideBackgroundManger(getActivity());


        mSelectedMovie = (Movie)getActivity().getIntent().getSerializableExtra(MOVIE);
        //Log.d(TAG, "onCreate: fragment加载并解析了"+mSelectedMovie.getStudio());
        mDetailsRowBuilderTask = (DetailsRowBuilderTask) new DetailsRowBuilderTask().execute(mSelectedMovie);

        ;
    }







    private class DetailsRowBuilderTask extends AsyncTask<Movie, Integer, DetailsOverviewRow> {
        @Override
        protected DetailsOverviewRow doInBackground(Movie... params) {
            DetailsOverviewRow row = new DetailsOverviewRow(mSelectedMovie);

            row.setImageBitmap(getActivity(), BitmapFactory.decodeResource(getResources(),R.drawable.badge));
            return row;
        }
        @Override
        protected void onPostExecute(DetailsOverviewRow row) {
            /* 1st row: DetailsOverviewRow */
            SparseArrayObjectAdapter sparseArrayObjectAdapter = new SparseArrayObjectAdapter();
            sparseArrayObjectAdapter.set(0, new Action(0, "Play Video"));
            sparseArrayObjectAdapter.set(1, new Action(1, "Action 2", "label"));
            sparseArrayObjectAdapter.set(2, new Action(2, "Action 3", "label"));
            row.setActionsAdapter(sparseArrayObjectAdapter);
            fullWidthDetailsOverviewRowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
                @Override
                public void onActionClicked(Action action) {
                    if (action.getId() == 0) {
                        Intent intent = new Intent(getActivity(), PlaybackOverlayActivity.class);
                        intent.putExtra("Movie", mSelectedMovie);
                        intent.putExtra("shouldStart", true);
                        startActivity(intent);
                    }
                }
            });





            /* 2nd row: ListRow */
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
            for(int i = 0; i < 10; i++){
                Movie movie = new Movie();
                if(i%3 == 0) {
                    movie.setCardImageUri("https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg");
                } else if (i%3 == 1) {
                    movie.setCardImageUri("https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg");
                } else {
                    movie.setCardImageUri("https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg");
                }
                movie.setTitle("title" + i);
                movie.setStudio("studio" + i);
                listRowAdapter.add(movie);
            }
            HeaderItem headerItem = new HeaderItem(0, "Related Videos！!!");
            ClassPresenterSelector classPresenterSelector = new ClassPresenterSelector();
            fullWidthDetailsOverviewRowPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.STATE_SMALL);

            classPresenterSelector.addClassPresenter(DetailsOverviewRow.class, fullWidthDetailsOverviewRowPresenter);
            classPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
            ArrayObjectAdapter adapter = new ArrayObjectAdapter(classPresenterSelector);
            /* 1st row */
            adapter.add(row);
            /* 2nd row */
            adapter.add(new ListRow(headerItem, listRowAdapter));
            /* 3rd row */
            //adapter.add(new ListRow(headerItem, listRowAdapter));
            setAdapter(adapter);
        }
    }

}