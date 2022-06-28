package com.example.mytvdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.net.URI;
import java.net.URL;

public class CardPresenter extends Presenter {
    private static final String TAG = CardPresenter.class.getSimpleName();
    static Context mContext;
    private static int CARD_WIDTH = 313;
    private static int CARD_HEIGHT = 176;

    static class ViewHolder extends Presenter.ViewHolder{
        private Movie movie;
        private ImageCardView mCardView;
        private Drawable mDefaultCardImage;
        public ViewHolder(View view) {
            super(view);
            mCardView = (ImageCardView) view;
            mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.movie);
        }

        public void setMovie(Movie movie) {
            this.movie = movie;
        }

        public Drawable getmDefaultCardImage() {
            return mDefaultCardImage;
        }

        public Movie getMovie() {
            return movie;
        }

        public ImageCardView getmCardView() {
            return mCardView;
        }

        protected void updateCardViewImage(URL uri){
            MySimpleTarget mySimpleTarget = new MySimpleTarget(CARD_WIDTH,CARD_HEIGHT);
            Glide.with(mContext).load(movie.getCardImageUri())
                    .into(mySimpleTarget);


        }
        class MySimpleTarget extends SimpleTarget<GlideDrawable>{

            public MySimpleTarget(int width,int height){
                super(width,height);
            }

            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                mCardView.setMainImage(resource.getCurrent());

            }
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        mContext = parent.getContext();
        ImageCardView imageCardView = new ImageCardView(mContext);
        imageCardView.setFocusable(true);
        imageCardView.setFocusableInTouchMode(true);
        imageCardView.setBackgroundColor(Color.BLACK);
        return new ViewHolder(imageCardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Movie movie = (Movie) item;
        ((ViewHolder)viewHolder).setMovie(movie);
        ((ViewHolder)viewHolder).mCardView.setTitleText(movie.getTitle());
        ((ViewHolder)viewHolder).mCardView.setContentText(movie.getStudio());
//        ((ViewHolder)viewHolder).mCardView.setMainImage(((ViewHolder)viewHolder).getmDefaultCardImage());
        ((ViewHolder)viewHolder).mCardView.setMainImageDimensions(CARD_WIDTH,CARD_HEIGHT);
        ((ViewHolder)viewHolder).updateCardViewImage(movie.getCardImageUri());

    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

}
