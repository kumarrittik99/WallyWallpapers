package com.example.user.forevershop;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.networkutil.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class WallpaperInsideCategory extends AppCompatActivity {


    String categoryName;
    RecyclerView recyclerView;
    GridLayoutManager manager;
    ProgressBar progressBar;
    Boolean isScrolling = false;
    int currentItem,totalItem,scrolledItem;
    int pageCount = 1;
    String data = null;
    MyInsideCategoryRecyclerViewAdapter myInsideCategoryRecyclerViewAdapter;
    ArrayList<String> mImageUrlList  = new ArrayList<>();
    ArrayList<String> mImageThumbnailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_inside_category);

        progressBar = findViewById(R.id.progressBar);

        Bundle bundle = getIntent().getExtras();
        categoryName = bundle.getString("Category Name");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(categoryName);

        recyclerView = findViewById(R.id.recyclerView);
        myInsideCategoryRecyclerViewAdapter = new MyInsideCategoryRecyclerViewAdapter(this,mImageUrlList,mImageThumbnailList);
        recyclerView.setAdapter(myInsideCategoryRecyclerViewAdapter);
        manager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(manager);

        new FetchImage().execute();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = manager.getChildCount();
                totalItem = manager.getItemCount();
                scrolledItem = manager.findFirstVisibleItemPosition();
                if(isScrolling && (currentItem + scrolledItem == totalItem))
                {
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
            String url = "https://pixabay.com/api/?key=" + apiKey.getKey() + "&q=" + categoryName + "&image_type=photo&pretty=true&per_page=10&page="+ String.valueOf(pageCount++);
            data = NetworkUtil.makeServiceCall(url);

            return null;

        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(data != null)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("hits");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject mJsonObject = jsonArray.getJSONObject(i);
                        String imageUrl = mJsonObject.getString("largeImageURL");
                        mImageUrlList.add(imageUrl);
                        String imageThumbnail = mJsonObject.getString("webformatURL");
                        mImageThumbnailList.add(imageThumbnail);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                myInsideCategoryRecyclerViewAdapter.swap(mImageUrlList,mImageThumbnailList);
            }
            progressBar.setVisibility(View.GONE);
        }

    }

}
