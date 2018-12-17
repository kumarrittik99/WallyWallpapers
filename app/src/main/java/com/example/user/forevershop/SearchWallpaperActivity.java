package com.example.user.forevershop;

import android.media.Image;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.networkutil.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchWallpaperActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchWallpaperRecyclerViewAdapter searchWallpaperRecyclerViewAdapter;
    String data,myQuery;
    int pageCount = 1;
    int currentItem,totalItem,scrolledOutItem;
    boolean isScrolling = false;
    ArrayList<String> mImageUrlList = new ArrayList<String>();
    ArrayList<String> mImageThumbnailList = new ArrayList<String>();
    GridLayoutManager manager;
    ProgressBar progressBar;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_wallpaper);


        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);

        if(imageView.getVisibility() == View.VISIBLE)
        {
            imageView.setVisibility(View.GONE);
        }

        Bundle bundle = getIntent().getExtras();
        String query = bundle.getString("Query");
        myQuery = query;
        Log.i("Query",query);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(query);

        recyclerView = findViewById(R.id.recyclerViewSearchWallpaper);
        searchWallpaperRecyclerViewAdapter = new SearchWallpaperRecyclerViewAdapter(this,mImageUrlList,mImageThumbnailList);
        recyclerView.setAdapter(searchWallpaperRecyclerViewAdapter);
        manager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(manager);

        FetchImage fetchImage = new FetchImage();
        fetchImage.execute();


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
    }

    public class FetchImage extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            APIKey apiKey = new APIKey();
            String myQueryUrl = "https://pixabay.com/api/?key=" + apiKey.getKey() + "&q=" + myQuery + "&image_type=photo&pretty=true&per_page=10&page="+ String.valueOf(pageCount++);
            data = NetworkUtil.makeServiceCall(myQueryUrl);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (data!=null)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(data);
                    String totalHits = jsonObject.getString("totalHits");
                    int total_Hits = Integer.parseInt(totalHits);
                    if(total_Hits == 0)
                    {
                        if(imageView.getVisibility() == View.GONE)
                        {
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageResource(R.mipmap.no_image_available);
                            Snackbar.make(findViewById(R.id.relativeLayout),"Sorry no match found! Please check your keyword and try again",Snackbar.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("hits");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject mJsonObject = jsonArray.getJSONObject(i);
                            String imageUrl = mJsonObject.getString("largeImageURL");
                            mImageUrlList.add(imageUrl);
                            String imageThumbnail = mJsonObject.getString("webformatURL");
                            mImageThumbnailList.add(imageThumbnail);
                        }
                    }
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                progressBar.setVisibility(View.GONE);
                searchWallpaperRecyclerViewAdapter.swap(mImageUrlList,mImageThumbnailList);
            }
        }
}
