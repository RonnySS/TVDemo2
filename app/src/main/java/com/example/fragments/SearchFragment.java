package com.example.mytvdemo.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.leanback.app.SearchSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;

import com.example.mytvdemo.CardPresenter;
import com.example.mytvdemo.Movie;
import com.example.mytvdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends SearchSupportFragment implements SearchSupportFragment.SearchResultProvider {

    private ArrayObjectAdapter mRowsAdapter;

    String mQuery;
    ArrayList<Movie> mItems;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRowsAdapter = new ArrayObjectAdapter();
        ArrayList<Movie> mItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Movie movie = new Movie();
            movie.setTitle("Title"+i);
            movie.setCardImageUri("https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg");
//            movie.setVedioURL("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
            movie.setVedioURL("android.resource://"+getActivity().getPackageName()+"/" + R.raw.tutorial_test);
            movie.setStudio("studio"+i);
            mItems.add(movie);

        }
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        listRowAdapter.addAll(0, mItems);
        HeaderItem header = new HeaderItem("Search results");
        mRowsAdapter.add(new ListRow(header, listRowAdapter));
        setSearchQuery("Title3",true);
    }

    @Override
    public Intent getRecognizerIntent() {
        return super.getRecognizerIntent();
    }


    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        Log.i("TAG", String.format("Search Query Text Change %s", newQuery));
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mQuery = query;
        loadRows();
        Log.i("TAG", String.format("Search Query Text Submit %s", query));
        return false;
    }

    private void loadRows() {
        // offload processing from the UI thread
            }

}
