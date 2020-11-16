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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhttvn.cutewallpaper.R;
import com.anhttvn.cutewallpaper.model.Cutewallpaper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolderDownload>  {
    private List<Cutewallpaper> mlistPhoto;
    private Context mContext;
    public DownloadAdapter(Context context,List<Cutewallpaper> list) {
        super();
        this.mContext = context;
        this.mlistPhoto = list;
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
        String path = this.mlistPhoto.get(position).getUrl();
        Picasso.with(mContext).load(path).into(holder.imgHolder);
        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage(path);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlistPhoto.size();
    }

    public void downloadImage(String pathURL) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage("Please wait Download.....");
        dialog.show();
        Picasso.with(mContext)
                .load(pathURL)
                .into(new Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                              try {
                                  String root = Environment.getExternalStorageDirectory().toString();
                                  File myDir = new File(root + "/CuteWallpaperVN");

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

    public class ViewHolderDownload extends RecyclerView.ViewHolder {
        private ImageView imgHolder;
        private Button btnDownload;
        public ViewHolderDownload(@NonNull View itemView) {
            super(itemView);
            imgHolder = itemView.findViewById(R.id.item_image_download);
            btnDownload = itemView.findViewById(R.id.btnDownloadURL);
        }
    }
}
