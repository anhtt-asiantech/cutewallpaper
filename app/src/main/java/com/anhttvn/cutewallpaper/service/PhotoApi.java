package com.anhttvn.cutewallpaper.service;

import com.anhttvn.cutewallpaper.model.Cutewallpaper;
import com.anhttvn.cutewallpaper.model.TypeCute;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PhotoApi {
    @GET("cute_wallpaper.json")
    Call<TypeCute> getPhotos();
    @GET("typeCute.json")
    Call<TypeCute> getType();
}
