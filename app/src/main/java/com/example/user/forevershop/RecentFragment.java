package com.example.user.forevershop;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.networkutil.NetworkUtil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class RecentFragment extends Fragment {

    ArrayList<String> mImageUrlList;
    ArrayList<String> mImageThumbnailList;
    Boolean isScrolling = false;
    int currentItem, totalItem, scrolledOutItem;
    GridLayoutManager manager;
    ProgressBar progressBar;


    RecyclerView recyclerView;
    MyRecentRecyclerViewAdapter myRecentRecyclerViewAdapter;

    private String data = null;
    private int pageCount = 1;

    public RecentFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recent, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        recyclerView = view.findViewById(R.id.recyclerViewRecent);
        manager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(manager);

        myRecentRecyclerViewAdapter = new MyRecentRecyclerViewAdapter(this, mImageThumbnailList, mImageUrlList);
        recyclerView.setAdapter(myRecentRecyclerViewAdapter);

        mImageUrlList = new ArrayList<>();
        mImageThumbnailList = new ArrayList<>();

        new FetchImage().execute();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItem = manager.getChildCount();
                totalItem = manager.getItemCount();
                scrolledOutItem = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItem + scrolledOutItem == totalItem)) {
                    new FetchImage().execute();
                }
            }
        });
        return view;
    }

    class FetchImage extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            APIKey apiKey = new APIKey();
            String mRecentWallpaperUrl = "https://pixabay.com/api/?key=" + apiKey.getKey() + "&q=backgrounds&image_type=photo&pretty=true&per_page=10&page=" + String.valueOf(pageCount++);
            data = NetworkUtil.makeServiceCall(mRecentWallpaperUrl);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("hits");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject mJsonObject = jsonArray.getJSONObject(i);
                        String imageUrl = mJsonObject.getString("largeImageURL");
                        mImageUrlList.add(imageUrl);
                        String imageThumbnail = mJsonObject.getString("webformatURL");
                        mImageThumbnailList.add(imageThumbnail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                myRecentRecyclerViewAdapter.swap(mImageThumbnailList, mImageUrlList);

            }
            progressBar.setVisibility(View.GONE);
        }
    }
}
