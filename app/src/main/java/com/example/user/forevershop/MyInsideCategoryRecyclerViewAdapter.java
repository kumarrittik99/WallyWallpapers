package com.example.user.forevershop;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyInsideCategoryRecyclerViewAdapter extends RecyclerView.Adapter<MyInsideCategoryRecyclerViewAdapter.MyViewHolder>
{

    private WallpaperInsideCategory context;
    private ArrayList<String> mImageUrlList;
    private ArrayList<String> mImageThumbNailList;

    public MyInsideCategoryRecyclerViewAdapter(WallpaperInsideCategory context,ArrayList mImageUrlList,ArrayList mImageThumbNailList)
    {
        this.context = context;
        this.mImageUrlList = mImageUrlList;
        this.mImageThumbNailList = mImageThumbNailList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recent_recyclerview,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Glide.with(context).asBitmap().load(mImageThumbNailList.get(position)).into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),RecentWallpaper.class);
                intent.putExtra("Image Url",mImageUrlList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrlList.size();
    }

    public void swap(ArrayList mImageUrlList,ArrayList mImageThumbNailList)
    {
        this.mImageUrlList = mImageUrlList;
        this.mImageThumbNailList = mImageThumbNailList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        LinearLayout linearLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            imageView = itemView.findViewById(R.id.imageView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
