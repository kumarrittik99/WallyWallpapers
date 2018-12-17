package com.example.user.forevershop;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RecentWallpaper extends AppCompatActivity {

    ImageView imageView;
    Button button;
    ProgressBar progressBar;
    private int fileCount = 0;
    String imageUrl,imageThumbnail;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_wallpaper);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        imageThumbnail = bundle.getString("Image Thumbnail");
        imageUrl = bundle.getString("Image Url");
        loadImage();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                Glide.with(getApplicationContext()).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            WallpaperManager.getInstance(getApplicationContext()).setBitmap(resource);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RecentWallpaper.this, "Wallpaper is set", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });

    }

    private void loadImage()
    {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

}
