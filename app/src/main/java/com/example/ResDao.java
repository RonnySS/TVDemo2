package com.example.mytvdemo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResDao {

    @SerializedName("exist")
    private boolean exist;
    @SerializedName("resultList")
    private List<Moive> resultList;

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public List<Moive> getResultList() {
        return resultList;
    }

    public void setResultList(List<Moive> resultList) {
        this.resultList = resultList;
    }

    public static class Moive {
        @SerializedName("studio")
        private String studio;
        @SerializedName("CardImageUri")
        private String cardImageUri;
        @SerializedName("title")
        private String title;
        @SerializedName("VedioURL")
        private String vedioURL;

        public String getStudio() {
            return studio;
        }

        public void setStudio(String studio) {
            this.studio = studio;
        }

        public String getCardImageUri() {
            return cardImageUri;
        }

        public void setCardImageUri(String cardImageUri) {
            this.cardImageUri = cardImageUri;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVedioURL() {
            return vedioURL;
        }

        public void setVedioURL(String vedioURL) {
            this.vedioURL = vedioURL;
        }
    }
}
