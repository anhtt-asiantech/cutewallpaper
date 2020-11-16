package com.anhttvn.cutewallpaper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhttvn.cutewallpaper.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class PhotoAdapter  extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<String> listPhoto;
    private boolean selectEvent;
    private OnclickSelectPhoto mOnclickSelect;
    public PhotoAdapter(Context context,List<String> list,boolean select,OnclickSelectPhoto clickSelect) {
        super();
        this.mContext = context;
        this.listPhoto = list;
        this.selectEvent = select;
        this.mOnclickSelect = clickSelect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View viewPhoto = layoutInflater.inflate(R.layout.item_photo_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(viewPhoto);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!this.selectEvent) {
            InputStream inputstream= null;
            try {
                inputstream = mContext.getAssets().open("image/"
                        +listPhoto.get(position));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable drawable = Drawable.createFromStream(inputstream, null);
            holder.imgPhoto.setImageDrawable(drawable);
        } else {
            File imgFile = new File(this.listPhoto.get(position));
            if(imgFile.exists())
            {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imgPhoto.setImageBitmap(myBitmap);
            }
        }
        holder.imgPhoto.setOnClickListener(this);
        holder.imgPhoto.setTag(position);
        holder.btnSetWallpaper.setOnClickListener(this);
        holder.btnSetWallpaper.setTag(position);
    }

    @Override
    public int getItemCount() {
        return listPhoto.size();
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag()+"");
        switch (v.getId()){
            case R.id.item_image_adapter:
                notifyDataSetChanged();
                mOnclickSelect.selectPhoto(position);
                break;
            case R.id.btnSetWallpaper:
                mOnclickSelect.setWallpaper(position);
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhoto;
        private Button btnSetWallpaper;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.item_image_adapter);
            btnSetWallpaper = itemView.findViewById(R.id.btnSetWallpaper);
        }
    }
    public interface OnclickSelectPhoto {
         void selectPhoto(int position);
         void setWallpaper(int position);
    }
}
