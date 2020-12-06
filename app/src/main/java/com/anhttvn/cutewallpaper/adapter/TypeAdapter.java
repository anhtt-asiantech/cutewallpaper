package com.anhttvn.cutewallpaper.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhttvn.cutewallpaper.R;
import com.anhttvn.cutewallpaper.model.TypeCute;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewTypeHoder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList<TypeCute> mListCute;
    public OnclickType mOnclickType;
    public TypeAdapter(Context context,ArrayList<TypeCute> type,OnclickType click) {
        super();
        mContext = context;
        mListCute = type;
        mOnclickType = click;
    }
    @NonNull
    @Override
    public ViewTypeHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_type_adapter,parent,false);
        ViewTypeHoder viewTypeHoder = new ViewTypeHoder(view);
        return viewTypeHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewTypeHoder holder, int position) {
        TypeCute typeCute = mListCute.get(position);
        if (typeCute.getAds() == 1) {
            holder.rl_type.setVisibility(View.GONE);
            holder.ads_type.setVisibility(View.VISIBLE);
            showAdsBanner(holder.ads_type);
        } else {
            holder.rl_type.setVisibility(View.VISIBLE);
            holder.ads_type.setVisibility(View.GONE);
            Picasso.with(mContext).load(typeCute.getUrl()).into(holder.img_type);
            holder.tv_title.setText(typeCute.getTitle());
            holder.tv_title.setTypeface(fontOngDo());
            holder.rl_type.setTag(position);
            holder.rl_type.setOnClickListener(this);
        }
    }
    public void showAdsBanner (AdView ads) {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("16F857DC15DB4E25CF35D27FB44DE5D9").build();
        ads.loadAd(adRequest);
    }
    public Typeface fontOngDo() {
        Typeface typeThuDay = Typeface.createFromAsset(mContext.getResources().getAssets(), "font/OngDo.ttf");
        return typeThuDay;
    }
    @Override
    public int getItemCount() {
        return mListCute.size();
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag()+"");
        switch (v.getId()){
            case R.id.rl_adapter_type:
                notifyDataSetChanged();
                mOnclickType.selectType(position);
                break;

        }
    }

    public class ViewTypeHoder extends RecyclerView.ViewHolder {

        private RelativeLayout rl_type;
        private ImageView img_type;
        private TextView tv_title;
        private AdView ads_type;
        public ViewTypeHoder(@NonNull View itemView) {
            super(itemView);
            rl_type = itemView.findViewById(R.id.rl_adapter_type);
            img_type = itemView.findViewById(R.id.img_item_type);
            tv_title = itemView.findViewById(R.id.tv_title_type);
            ads_type = itemView.findViewById(R.id.ads_type);

        }
    }
    public interface OnclickType {
        void selectType(int position);
    }
}
