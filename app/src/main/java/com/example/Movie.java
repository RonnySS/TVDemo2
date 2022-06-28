package com.example.mytvdemo;

import android.net.Uri;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Movie implements Serializable {
    private static final String TAG = Movie.class.getSimpleName();
    static final long serialVersionUID = 727566175075960653L;
    private long id;
    private String title;
    private String studio;
    private String cardImageUri;
    private String vedioURL;



    public Movie() {
    }

    public URL getCardImageUri() {
        try {
            return new URL(cardImageUri);
        } catch ( MalformedURLException e) {
            return null;
        }


    }


    public String getVedioURL() {
        return vedioURL;
    }

    public void setVedioURL(String vedioURL) {
        this.vedioURL = vedioURL;
    }

    public void setCardImageUri(String cardImageUri) {
        this.cardImageUri = cardImageUri;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getStudio() {
        return studio;
    }
    public void setStudio(String studio) {
        this.studio = studio;
    }
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
