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

public class MyRecentRecyclerViewAdapter extends RecyclerView.Adapter<MyRecentRecyclerViewAdapter.MyViewHolder>{

    private RecentFragment context;
    private ArrayList<String> mImageThumbNailList;
    private ArrayList<String> mImageUrlList;

    public MyRecentRecyclerViewAdapter(RecentFragment context,ArrayList mImageThumbNailList, ArrayList mImageUrlList)
    {
        this.context = context;
        this.mImageThumbNailList = mImageThumbNailList;
        this.mImageUrlList = mImageUrlList;
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
                Intent intent = new Intent(context.getContext(),RecentWallpaper.class);
                intent.putExtra("Image Thumbnail",mImageThumbNailList.get(position));
                intent.putExtra("Image Url",mImageUrlList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageThumbNailList == null ? 0 : mImageThumbNailList.size();
    }
    public void swap(ArrayList<String> mImageThumbNailList,ArrayList<String> mImageUrlList)
    {

        this.mImageThumbNailList = mImageThumbNailList;
        this.mImageUrlList = mImageUrlList;
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView ;
        LinearLayout linearLayout;
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}
