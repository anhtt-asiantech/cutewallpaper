package com.anhttvn.cutewallpaper.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Cutewallpaper implements Serializable {
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("ads")
    @Expose
    private int ads;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAds(int ads) {
        this.ads = ads;
    }
    public int getAds() {
        return ads;
    }
}
