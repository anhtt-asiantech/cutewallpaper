package com.anhttvn.cutewallpaper.ui;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.anhttvn.cutewallpaper.R;
import com.anhttvn.cutewallpaper.adapter.PhotoAdapter;
import com.anhttvn.cutewallpaper.service.PhotoApi;
import com.anhttvn.cutewallpaper.util.BaseActivity;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class PhotoActivity extends BaseActivity implements PhotoAdapter.OnclickSelectPhoto {

    private String [] images;
    private ArrayList<String> listImages;
    private PhotoAdapter mAdapterPhoto;
    private boolean mSelectEvent = false;
    private String nameSelect;

    // item view
    private RecyclerView recycler_photo;
    private TextView tv_photo,tv_no_data_photo;
    private Button btnDefault,btnFolder;




    @Override
    protected void init() {
        recycler_photo = findViewById(R.id.recycler_photo);
        tv_photo = findViewById(R.id.tv_photo);
        tv_photo.setTypeface(fontDefault());
        btnDefault = findViewById(R.id.btnDefault);
        btnFolder = findViewById(R.id.btnFolder);
        tv_no_data_photo = findViewById(R.id.tv_no_data_photo);
        tv_no_data_photo.setTypeface(fontDefault());
        this.dataPhoto();
        this.initAdapter();
        nameSelect = this.getString(R.string.txt_photo) +" " + this.getString(R.string.txt_default);
        tv_photo.setText(nameSelect);
        btnDefault.setTextColor(Color.RED);
        btnFolder.setTextColor(Color.WHITE);
        btnDefault.setBackgroundResource(R.drawable.button_gradient_download);
        btnFolder.setBackgroundColor(Color.GRAY);
        configAdsFull();
    }

    /**
     * get data photo for asset
     */
    private  void dataPhoto() {
        showDialogTitle("Please wait Loading.....");
        try {
            images =getAssets().list("image");
            hideDialogTitle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listImages = new ArrayList<String>(Arrays.asList(images));
    }
    public void getFromSdcard() {
        showDialogTitle("Please wait Loading.....");
        this.listImages = new ArrayList<>();
        File[] listFile;
        File file= new File(android.os.Environment.getExternalStorageDirectory(),"/Android/CuteVN");

        if (file.isDirectory())
        {
            listFile = file.listFiles();
            for(int i = 0; i < listFile.length; i++){
                this.listImages.add(listFile[i].getAbsolutePath());
            }
            hideDialogTitle();
        }
    }
    /**
     * init adapter
     */
    private void initAdapter() {
        mAdapterPhoto =new PhotoAdapter(this,listImages,this.mSelectEvent,this);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recycler_photo.setLayoutManager(gridLayoutManager);
        if (this.listImages != null) {
            recycler_photo.setAdapter(mAdapterPhoto);
            if(this.listImages.size() == 0) {
                recycler_photo.setVisibility(View.GONE);
                tv_no_data_photo.setVisibility(View.VISIBLE);
            } else {
                recycler_photo.setVisibility(View.VISIBLE);
                tv_no_data_photo.setVisibility(View.GONE);
            }
        }
    }

    public void eventSelectDefault(View view) {
        dataPhoto();
        btnDefault.setTextColor(Color.RED);
        btnFolder.setTextColor(Color.WHITE);
        btnDefault.setBackgroundResource(R.drawable.button_gradient_download);
        btnFolder.setBackgroundColor(Color.GRAY);
        nameSelect = this.getString(R.string.txt_photo) +" " + this.getString(R.string.txt_default);
        tv_photo.setText(nameSelect);
        this.mSelectEvent = false;
        this.initAdapter();
    }
    public void eventSelectFolder(View view) {
        getFromSdcard();
        btnDefault.setTextColor(Color.WHITE);
        btnFolder.setTextColor(Color.RED);
        btnFolder.setBackgroundResource(R.drawable.button_gradient_download);
        btnDefault.setBackgroundColor(Color.GRAY);
        nameSelect = this.getString(R.string.txt_photo) +" " + this.getString(R.string.txt_folder);
        tv_photo.setText(nameSelect);
        this.mSelectEvent = !this.mSelectEvent;
        this.initAdapter();
    }
    public void eventBack(View view) {
        finish();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_photo;
    }

    @Override
    protected AdView getIdAdview() {
        return null;
    }

    @Override
    public void selectPhoto(int position) {
       this.viewPhotoSelect(position);
    }

    @Override
    public void setWallpaper(int position) {
        mPosition = position;
        dialogSetWallpaper();
    }

    private int mPosition;
    private void viewPhotoSelect(int position) {
        mPosition = position;
        final Dialog dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.activity_view_photo);
        ImageView imgViewPhoto = dialog.findViewById(R.id.img_view_photo);
        this.showImageSelect(imgViewPhoto,position);
        dialog.findViewById(R.id.btnCloseDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // select left
        dialog.findViewById(R.id.imgSelectLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition == 0) {
                    mPosition = listImages.size() -1;
                } else {
                    mPosition -=1;
                }
                showImageSelect(imgViewPhoto,mPosition);
            }
        });
        // select right
        dialog.findViewById(R.id.imgSelectRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition == listImages.size()-1) {
                    mPosition = 0;
                } else {
                    mPosition +=1;
                }
                showImageSelect(imgViewPhoto,mPosition);
            }
        });
        dialog.show();
    }
    private void showImageSelect(ImageView img,int position) {
        if (!this.mSelectEvent) {
            InputStream inputstream= null;
            try {
                inputstream = this.getAssets().open("image/"
                        +this.listImages.get(position));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable drawable = Drawable.createFromStream(inputstream, null);
            img.setImageDrawable(drawable);
        } else {
            File imgFile = new File(this.listImages.get(position));
            if(imgFile.exists())
            {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img.setImageBitmap(myBitmap);
            }
        }
    }
    private void dialogSetWallpaper() {
        final Dialog dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_view_setwallpaper);
        dialog.show();
        dialog.findViewById(R.id.btnHomeScreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSelectEvent) {
                    homeWallpaper(getBitmapFromAsset(getApplicationContext(),listImages.get(mPosition)));
                } else {
                    Bitmap bitmap = BitmapFactory.decodeFile(listImages.get(mPosition));
                    homeWallpaper(bitmap);
                }
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        dialog.findViewById(R.id.btnLockScreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSelectEvent) {
                    lockWallpaper(getBitmapFromAsset(getApplicationContext(),listImages.get(mPosition)));
                } else {
                    Bitmap bitmap = BitmapFactory.decodeFile(listImages.get(mPosition));
                    lockWallpaper(bitmap);
                }
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        dialog.findViewById(R.id.btnHomLockScreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSelectEvent) {
                    homeWallpaper(getBitmapFromAsset(getApplicationContext(),listImages.get(mPosition)));
                    lockWallpaper(getBitmapFromAsset(getApplicationContext(),listImages.get(mPosition)));
                } else {
                    Bitmap bitmap = BitmapFactory.decodeFile(listImages.get(mPosition));
                    homeWallpaper(bitmap);
                    lockWallpaper(bitmap);
                }
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        dialog.findViewById(R.id.btnCloseDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open("image/"+filePath);
            bitmap = BitmapFactory.decodeStream(istr);

        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }
    private void homeWallpaper(Bitmap bm){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        Bitmap bitmap = Bitmap.createScaledBitmap(bm,width,height, true);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        wallpaperManager.setWallpaperOffsetSteps(1, 1);
        wallpaperManager.suggestDesiredDimensions(width, height);
        try {
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    private void lockWallpaper(Bitmap bm){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        Bitmap bitmap = Bitmap.createScaledBitmap(bm,width,height, true);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        wallpaperManager.setWallpaperOffsetSteps(1, 1);
        wallpaperManager.suggestDesiredDimensions(width, height);
        try {
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
