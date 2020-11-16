package com.anhttvn.cutewallpaper.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.anhttvn.cutewallpaper.R;
import com.anhttvn.cutewallpaper.adapter.DownloadAdapter;
import com.anhttvn.cutewallpaper.model.Cutewallpaper;
import com.anhttvn.cutewallpaper.service.PhotoApi;
import com.anhttvn.cutewallpaper.util.ApiUtils;
import com.anhttvn.cutewallpaper.util.BaseActivity;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadActivity extends BaseActivity {

    private List<Cutewallpaper> mListPhoto = new ArrayList<>();
    private DownloadAdapter mAdapter;

    private RecyclerView recyclerView_download;
    private TextView tv_name_download,tv_no_data;
    private AdView ads_download;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        init();
        showAdsBanner();
    }

    private  void init() {
        recyclerView_download = findViewById(R.id.recyclerView_download);
        tv_name_download = findViewById(R.id.tv_name_download);
        tv_name_download.setTypeface(fontDefault());
        tv_no_data = findViewById(R.id.tv_no_data);
        tv_no_data.setTypeface(fontDefault());
        ads_download = findViewById(R.id.ads_download);
    }
    private void initAdapter() {
        if(this.mListPhoto != null || this.mListPhoto.size() == 0) {
            recyclerView_download.setVisibility(View.VISIBLE);
            tv_no_data.setVisibility(View.GONE);
            mAdapter = new DownloadAdapter(this,this.mListPhoto);
            StaggeredGridLayoutManager gridLayoutManager =
                    new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView_download.setLayoutManager(gridLayoutManager);
            recyclerView_download.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        } else {
            recyclerView_download.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.VISIBLE);
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
    public void loadData () {
        PhotoApi photoApi = ApiUtils.getPhotoAPi();
        Call<Cutewallpaper> listCuteWallpaper = photoApi.getPhotos();
        listCuteWallpaper.enqueue(new Callback<Cutewallpaper>() {
            @Override
            public void onResponse(Call<Cutewallpaper> call, Response<Cutewallpaper> response) {
//                int statusCode = response.code();
//                if (statusCode == 200)
                mListPhoto = response.body().getListItem();
                initAdapter();
            }

            @Override
            public void onFailure(Call<Cutewallpaper> call, Throwable t) {
                // Log error here since request failed
            }
        });
    }
}
