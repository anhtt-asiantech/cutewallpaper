package com.anhttvn.cutewallpaper.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anhttvn.cutewallpaper.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends Activity {

    private ProgressDialog mProgressDialog;
    public static final int MULTIPLE_PERMISSIONS = 10;
    public String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        ButterKnife.bind(this);
        onViewReady(savedInstanceState, getIntent());
        this.init();
        this.configAdsFull();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (checkPermissions())
            createFolder();

    }
    protected abstract void init();
    protected abstract  int getLayoutID();

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    public boolean noInternetConnectAvailble() {
        return Connectivity.isConnectedFast(this);
    }

    public void showProgressDialog(String title, @NonNull String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            if (title != null)
                mProgressDialog.setTitle(title);
            mProgressDialog.setIcon(R.mipmap.ic_launcher);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }


    public void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void showAlertDialog(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(null);
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton(getString(R.string.dialog_ok_btn), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogBuilder.setCancelable(false);
        dialogBuilder.show();
    }

    /**
     * show toast
     * @param mToastMsg
     */
    protected void showToast(String mToastMsg) {
        Toast.makeText(this, mToastMsg, Toast.LENGTH_LONG).show();
    }

    @CallSuper
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        //To be used by child activities
    }

    @Override
    protected void onDestroy() {
        ButterKnife.bind(this);
        super.onDestroy();

    }

    /**
     * show ads banner
     */
    public void showAdsBanner () {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("16F857DC15DB4E25CF35D27FB44DE5D9").build();
        if (noInternetConnectAvailble()) {
            getIdAdview().setVisibility(View.VISIBLE);
            getIdAdview().loadAd(adRequest);

        }else{
            getIdAdview().setVisibility(View.GONE);
        }
    }

    /**
     * get idview ADS
     * @return
     */
    protected abstract AdView getIdAdview();

    private InterstitialAd mInterstitialAd;
    private AdRequest mAdRequest;

    /**
     * show Ads full
     */
    public void configAdsFull() {
        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-3840180634112397~9498484526");

        mAdRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("16F857DC15DB4E25CF35D27FB44DE5D9").build();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(this.getString(R.string.banner_full_screen));
        if(noInternetConnectAvailble()){
            mInterstitialAd.loadAd(mAdRequest);
        }

    }
    public void showAdsFull() {
        if(mInterstitialAd != null && mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
    }


    protected ProgressDialog mDialogProgress;
    public void showDialogTitle(String title) {
        mDialogProgress = new ProgressDialog(this);
        mDialogProgress.setMessage(title);
        mDialogProgress.show();
    }
    public void hideDialogTitle() {
        mDialogProgress.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    createFolder();

                }
                return;
            }
        }
    }

    private void createFolder()
    {
        File folder2 = new File(Environment.getExternalStorageDirectory() + "/Android");
        if (!folder2.exists()) {
            folder2.mkdir();
        }


        File folder = new File(Environment.getExternalStorageDirectory() + "/Android/CuteVN");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
    }

    public  Typeface fontDefault() {
        Typeface typeThuDay = Typeface.createFromAsset(this.getResources().getAssets(), "font/Beyond Wonderland.ttf");
        return typeThuDay;
    }
    public  Typeface fontThuPhap() {
        Typeface typeThuDay = Typeface.createFromAsset(this.getResources().getAssets(), "font/Thuphap3.ttf");
        return typeThuDay;
    }
    public  Typeface fontOngDo() {
        Typeface typeThuDay = Typeface.createFromAsset(this.getResources().getAssets(), "font/OngDo.ttf");
        return typeThuDay;
    }
    public  Typeface fontNetMuc() {
        Typeface typeThuDay = Typeface.createFromAsset(this.getResources().getAssets(), "font/Netmuc.ttf");
        return typeThuDay;
    }
    public  Typeface fontTimenewsRoman() {
        Typeface typeThuDay = Typeface.createFromAsset(this.getResources().getAssets(), "font/Time_New_Roman.TTF");
        return typeThuDay;
    }
    public  Typeface fontValentine() {
        Typeface typeThuDay = Typeface.createFromAsset(this.getResources().getAssets(), "font/Valentine.ttf");
        return typeThuDay;
    }
}
