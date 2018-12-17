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

public class SearchWallpaperRecyclerViewAdapter extends RecyclerView.Adapter<SearchWallpaperRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<String> myImageUrlList = new ArrayList<String>();
    private ArrayList<String> myImageThumbnailList = new ArrayList<String>();
    private SearchWallpaperActivity context;

    public SearchWallpaperRecyclerViewAdapter(SearchWallpaperActivity context,ArrayList myImageUrlList,ArrayList myImageThumbnailList)
    {
        this.context = context;
        this.myImageUrlList = myImageUrlList;
        this.myImageThumbnailList = myImageThumbnailList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recent_recyclerview,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(context).asBitmap().load(myImageThumbnailList.get(position)).into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),RecentWallpaper.class);
                intent.putExtra("Image Url",myImageUrlList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myImageThumbnailList.size();
    }

    public void swap(ArrayList myImageUrlList , ArrayList myImageThumbnailList)
    {
        this.myImageUrlList = myImageUrlList;
        this.myImageThumbnailList = myImageThumbnailList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
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
