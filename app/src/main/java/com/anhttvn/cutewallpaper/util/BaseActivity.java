package com.anhttvn.cutewallpaper.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anhttvn.cutewallpaper.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import butterknife.ButterKnife;

public abstract class BaseActivity extends Activity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        ButterKnife.bind(this);
        onViewReady(savedInstanceState, getIntent());
    }
    protected abstract  int getLayoutID();

    protected void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null)
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }
    public boolean noInternetConnectAvailble() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
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
                "ca-app-pub-3840180634112397~9187759690");

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

    public  Typeface fontDefault() {
        Typeface typeThuDay = Typeface.createFromAsset(this.getResources().getAssets(), "font/Beyond Wonderland.ttf");
        return typeThuDay;
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
}
