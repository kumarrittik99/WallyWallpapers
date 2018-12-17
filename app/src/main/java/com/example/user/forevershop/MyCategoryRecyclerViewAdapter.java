package com.example.user.forevershop;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyCategoryRecyclerViewAdapter extends RecyclerView.Adapter<MyCategoryRecyclerViewAdapter.MyViewHolder>
{
    private CategoriesFragment context;
    private ArrayList<String> mCategoryImageUrlList = new ArrayList<>();
    private ArrayList<String> mCategoryNameList = new ArrayList<>();
    public MyCategoryRecyclerViewAdapter(CategoriesFragment context,ArrayList mCategoryImageList, ArrayList mCategoryNameList)
    {
        this.context = context;
        this.mCategoryImageUrlList = mCategoryImageList;
        this.mCategoryNameList = mCategoryNameList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_categories_recyclerview,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Glide.with(context).asBitmap().load(mCategoryImageUrlList.get(position)).into(holder.imageView);
        holder.textView.setText(mCategoryNameList.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getContext(),WallpaperInsideCategory.class);
                intent.putExtra("Category Name",mCategoryNameList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryImageUrlList.size();
    }

    public void swap(ArrayList mCategoryImageUrlList,ArrayList mCategoryNameList)
    {
        this.mCategoryImageUrlList = mCategoryImageUrlList;
        this.mCategoryNameList = mCategoryNameList;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        TextView textView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
