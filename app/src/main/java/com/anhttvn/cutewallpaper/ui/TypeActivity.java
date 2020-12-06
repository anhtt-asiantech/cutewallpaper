package com.anhttvn.cutewallpaper.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anhttvn.cutewallpaper.R;
import com.anhttvn.cutewallpaper.adapter.TypeAdapter;
import com.anhttvn.cutewallpaper.model.TypeCute;
import com.anhttvn.cutewallpaper.service.PhotoApi;
import com.anhttvn.cutewallpaper.util.ApiUtils;
import com.anhttvn.cutewallpaper.util.BaseActivity;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TypeActivity extends BaseActivity implements TypeAdapter.OnclickType{
    private RecyclerView list_type;
    private TextView tv_name_type,tv_no_data_type;
    private ArrayList<TypeCute> mListCute = new ArrayList<>();

    private TypeAdapter mAdapter;
    private int mPosition = 0;

    @Override
    protected void init() {
        this.initView();
        this.getData();
    }

    private void initView () {
        list_type = findViewById(R.id.list_type);
        tv_name_type = findViewById(R.id.tv_name_type);
        tv_no_data_type = findViewById(R.id.tv_no_data_type);
        tv_no_data_type.setVisibility(View.GONE);
        tv_no_data_type.setTypeface(fontNetMuc());
        tv_name_type.setTypeface(fontOngDo());
    }
    private void getData() {
        showDialogTitle("Loading data ...");
        PhotoApi api = ApiUtils.getPhotoAPi();
        Call<TypeCute> listCute = api.getPhotos();
        listCute.enqueue(new Callback<TypeCute>() {
            @Override
            public void onResponse(Call<TypeCute> call, Response<TypeCute> response) {
                mListCute = response.body().getListType();
                hideDialogTitle();
                initAdapter();
            }

            @Override
            public void onFailure(Call<TypeCute> call, Throwable t) {
                list_type.setVisibility(View.GONE);
                tv_no_data_type.setVisibility(View.VISIBLE);
                hideDialogTitle();
//                tv_no_data_type.setText(getString(R.string.txt_no_data));
            }
        });


    }
    private void initAdapter () {
        if (mListCute.size() > 0 && mListCute != null ) {
            list_type.setVisibility(View.VISIBLE);
            tv_no_data_type.setVisibility(View.GONE);
            mAdapter = new TypeAdapter(this,mListCute,this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            list_type.setLayoutManager(mLayoutManager);
            list_type.setItemAnimator(new DefaultItemAnimator());
            list_type.setAdapter(mAdapter);
            list_type.scrollToPosition(mPosition);
            mAdapter.notifyDataSetChanged();
        } else {
            list_type.setVisibility(View.GONE);
            tv_no_data_type.setVisibility(View.VISIBLE);
        }
    }



    @Override
    protected int getLayoutID() {
        return R.layout.type_activity;
    }

    @Override
    protected AdView getIdAdview() {
        return null;
    }

    public void onClickBack(View view) {
        finish();
    }

    @Override
    public void selectType(int position) {
        mPosition = position;
        Intent intent = new Intent(this, DownloadActivity.class);
        intent.putExtra("title",mListCute.get(position).getTitle());
        intent.putExtra ("listCute", mListCute.get(position).getListCute());
        startActivity(intent);
    }
}
