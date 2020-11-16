package com.anhttvn.cutewallpaper.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cutewallpaper {
    @SerializedName("cutewallpaper")
    @Expose
    private List<Cutewallpaper> listItem = null;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("url")
    @Expose
    private String url;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Cutewallpaper> getListItem() {
        return listItem;
    }

    public void setListItem(List<Cutewallpaper> listItem) {
        this.listItem = listItem;
    }
}
