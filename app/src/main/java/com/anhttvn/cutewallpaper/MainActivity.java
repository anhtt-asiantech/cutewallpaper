package com.anhttvn.cutewallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anhttvn.cutewallpaper.ui.DownloadActivity;
import com.anhttvn.cutewallpaper.ui.PhotoActivity;
import com.anhttvn.cutewallpaper.util.BaseActivity;
import com.google.android.gms.ads.AdView;

import java.io.File;

public class MainActivity extends BaseActivity {
    private Button btnWallpaper,btnDownload;
    private AdView adViewMain;
    private TextView tv_name_app,tv_version_app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        showAdsBanner();
        createFolder();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected AdView getIdAdview() {
        return adViewMain;
    }

    private  void init() {
        btnWallpaper = findViewById(R.id.btnWallpaper);
        btnWallpaper.setTypeface(fontDefault());
        adViewMain = findViewById(R.id.adViewMain);

        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setTypeface(fontDefault());
        tv_name_app = findViewById(R.id.tv_name_app);
        tv_name_app.setTypeface(fontDefault());
        tv_version_app = findViewById(R.id.tv_version_app);
        tv_version_app.setTypeface(fontDefault());
    }

    public void eventClickPhoto(View view) {
        startActivity(new Intent(this, PhotoActivity.class));

    }

    public void eventClickDownload(View view) {
        if(noInternetConnectAvailble()) {
            startActivity(new Intent(this, DownloadActivity.class));
        } else {
            showAlertDialog("No Internet Connection");
        }

    }

    private  void createFolder() {
        File file = new File(Environment.getExternalStorageDirectory(), "CuteWallpaperVN");
        if (!file.mkdirs()) {
            file.mkdirs();
        }

    }

}