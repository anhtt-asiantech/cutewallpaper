package com.anhttvn.cutewallpaper.service;

import com.anhttvn.cutewallpaper.model.Cutewallpaper;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PhotoApi {
    @GET("cute_wallpaper.json")
    Call<Cutewallpaper> getPhotos();
}
