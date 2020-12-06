package com.anhttvn.cutewallpaper.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhttvn.cutewallpaper.R;
import com.anhttvn.cutewallpaper.model.Cutewallpaper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolderDownload> implements
        View.OnClickListener {
    private ArrayList<Cutewallpaper> mlistPhoto;
    private Context mContext;
    private OnclickDownload mOnclickDownload;
    public DownloadAdapter(Context context,ArrayList<Cutewallpaper> list, OnclickDownload click) {
        super();
        this.mContext = context;
        this.mlistPhoto = list;
        mOnclickDownload = click;
    }
    @NonNull
    @Override
    public ViewHolderDownload onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View viewLayout = layoutInflater.inflate(R.layout.item_view_download,parent,false);
        ViewHolderDownload viewHolderDownload = new ViewHolderDownload(viewLayout);
        return viewHolderDownload;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDownload holder, int position) {
        Cutewallpaper cute = mlistPhoto.get(position);
        if (cute.getAds() == 1) {
            holder.rl_download.setVisibility(View.GONE);
            holder.ads.setVisibility(View.VISIBLE);
            showAdsBanner(holder.ads);
        } else {
            holder.rl_download.setVisibility(View.VISIBLE);
            holder.ads.setVisibility(View.GONE);

            Picasso.with(mContext).load(cute.getUrl()).into(holder.imgHolder);
            holder.btnDownload.setOnClickListener(this);
            holder.btnDownload.setTag(position);

        }

    }

    @Override
    public int getItemCount() {
        return mlistPhoto.size();
    }



    public void showAdsBanner (AdView ads) {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("16F857DC15DB4E25CF35D27FB44DE5D9").build();
        ads.loadAd(adRequest);
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag()+"");
        switch (v.getId()){
            case R.id.btnDownloadURL:
                mOnclickDownload.clickDownload(position);
                break;

        }
    }

    public class ViewHolderDownload extends RecyclerView.ViewHolder {
        private ImageView imgHolder;
        private Button btnDownload;
        private AdView ads;
        private RelativeLayout rl_download;
        public ViewHolderDownload(@NonNull View itemView) {
            super(itemView);
            imgHolder = itemView.findViewById(R.id.item_image_download);
            btnDownload = itemView.findViewById(R.id.btnDownloadURL);
            rl_download = itemView.findViewById(R.id.rl_download_item);
            ads = itemView.findViewById(R.id.ads_download);
        }
    }
    public  interface OnclickDownload {
        void clickDownload(int position);
    }
}
