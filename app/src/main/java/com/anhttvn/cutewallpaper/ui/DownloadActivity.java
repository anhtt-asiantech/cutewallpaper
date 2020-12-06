package com.anhttvn.cutewallpaper.ui;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.anhttvn.cutewallpaper.R;
import com.anhttvn.cutewallpaper.adapter.DownloadAdapter;
import com.anhttvn.cutewallpaper.model.Cutewallpaper;
import com.anhttvn.cutewallpaper.util.BaseActivity;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DownloadActivity extends BaseActivity implements DownloadAdapter.OnclickDownload {

    private ArrayList<Cutewallpaper> mListCute = new ArrayList<>();
    private DownloadAdapter mAdapter;

    private RecyclerView recyclerView_download;
    private TextView tv_name_download,tv_no_data;
    private AdView ads_download;
    @Override
    protected void init() {
        recyclerView_download = findViewById(R.id.recyclerView_download);
        tv_name_download = findViewById(R.id.tv_name_download);
        tv_name_download.setTypeface(fontOngDo());
        tv_no_data = findViewById(R.id.tv_no_data);
        tv_no_data.setTypeface(fontDefault());
        ads_download = findViewById(R.id.ads_download);
        initData();
    }

    private void initAdapter() {
        if(this.mListCute != null || this.mListCute.size() == 0) {
            recyclerView_download.setVisibility(View.VISIBLE);
            tv_no_data.setVisibility(View.GONE);
            mAdapter = new DownloadAdapter(this,mListCute, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView_download.setLayoutManager(mLayoutManager);
            recyclerView_download.setItemAnimator(new DefaultItemAnimator());
            recyclerView_download.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        } else {
            recyclerView_download.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.VISIBLE);
        }
    }
    private void initData () {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mListCute = (ArrayList) bundle.getSerializable("listCute");
            String title = bundle.getString("title");
            tv_name_download.setText(title);
            initAdapter();
        }
    }
    public void eventBack(View view) {
        finish();
    }
    @Override
    protected int getLayoutID() {
        return R.layout.activity_download;
    }

    @Override
    protected AdView getIdAdview() {
        return ads_download;
    }

    @Override
    public void clickDownload(int position) {
        downloadImage(mListCute.get(position).getUrl());
    }
    public void downloadImage(String pathURL) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait Download.....");
        dialog.show();
        Picasso.with(this)
                .load(pathURL)
                .into(new Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                              try {
                                  String root = Environment.getExternalStorageDirectory().toString();
                                  File myDir = new File(root + "/Android/CuteVN");

                                  if (!myDir.exists()) {
                                      myDir.mkdirs();
                                  }

                                  String name = new Date().toString() + ".jpg";
                                  myDir = new File(myDir, name);
                                  FileOutputStream out = new FileOutputStream(myDir);
                                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                  new android.os.Handler().postDelayed(
                                          new Runnable() {
                                              public void run() {
                                                  dialog.dismiss();
                                                  showToast("Download success");
                                              }
                                          },
                                          1000);

                                  out.flush();
                                  out.close();

                              } catch(Exception e){
                                  // some action
                              }
                          }

                          @Override
                          public void onBitmapFailed(Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                );
    }
}
