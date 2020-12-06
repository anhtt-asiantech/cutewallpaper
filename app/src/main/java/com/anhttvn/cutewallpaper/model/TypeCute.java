package com.anhttvn.cutewallpaper.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TypeCute {
    @SerializedName("cute")
    @Expose
    private ArrayList<TypeCute> listType = new ArrayList<>();
    @SerializedName("position")
    @Expose
    private int position;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("ads")
    @Expose
    private int ads;
    @SerializedName("listCute")
    @Expose
    private ArrayList<Cutewallpaper> listCute = new ArrayList<>();
    public String getTitle() {
        return title;
    }
    public void setAds(int ads) {
        this.ads = ads;
    }
    public int getAds() {
        return ads;
    }
    public String getUrl() {
        return url;
    }
    public ArrayList<Cutewallpaper> getListCute() {
        return listCute;
    }
    public ArrayList<TypeCute> getListType() {
        return listType;
    }
}
