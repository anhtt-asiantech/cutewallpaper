package com.anhttvn.cutewallpaper.util;

import com.anhttvn.cutewallpaper.service.PhotoApi;
import com.anhttvn.cutewallpaper.service.RetrofitClient;

public class ApiUtils {
    public static final String BASE_URL
            = "https://raw.githubusercontent.com/anhtt-asiantech/json-app_tienanhvn/master/";

    public static PhotoApi getPhotoAPi () {
        return RetrofitClient.getClient(BASE_URL).create(PhotoApi.class);
    }
}
