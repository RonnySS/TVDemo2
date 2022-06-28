package com.example.mytvdemo.fragments;

import android.content.Intent;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.Metrics;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.mytvdemo.R;

import java.util.HashMap;
import java.util.List;

public class MainFragment extends BrowseSupportFragment implements
        LoaderManager.LoaderCallbacks<HashMap<String, List<Movie>>> {
    BackgroundManager backgroundManager;
    Drawable defaultBackground;
    DisplayMetrics metrics;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loadVideoData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prepareBackgroundManager();
        setupUIElements();

//        setupEventListeners();
    }

    private void prepareBackgroundManager() {
        backgroundManager = BackgroundManager.getInstance(getActivity());
        backgroundManager.attach(getActivity().getWindow());
        defaultBackground = getResources()
                .getDrawable(R.drawable.default_background);
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }





    private void setupUIElements() {
//        setBadgeDrawable(getActivity().getResources()
//                .getDrawable(R.drawable.videos_by_google_banner));
        // Badge, when set, takes precedent over title
        setTitle(getString(R.string.browse_title));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        // set headers background color
        setBrandColor(ContextCompat.getColor(requireContext(), R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(requireContext(), R.color.search_opaque));
    }








    @NonNull
    @Override
    public Loader<HashMap<String, List<Movie>>> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<HashMap<String, List<Movie>>> loader, HashMap<String, List<Movie>> data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<HashMap<String, List<Movie>>> loader) {

    }


//    private void loadVideoData() {
//        VideoProvider.setContext(getActivity());
//        videosUrl = getString(R.string.catalog_url);
//        getLoaderManager().initLoader(0, null, this);
//    }


    /*
    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());

    }*/
}