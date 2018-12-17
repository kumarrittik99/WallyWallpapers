package com.example.user.forevershop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.networkutil.NetworkUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<String> mImageUrlList = new ArrayList<>();
    ArrayList<String> mImageThumbnailList = new ArrayList<>();
    GridLayoutManager manager;
    RecyclerView recyclerView;
    TabLayout tabLayout;
    ViewPager viewPager;
    MyViewPagerAdapter myViewPagerAdapter;
    RelativeLayout relativeLayout,imageViewRelativeLayout;
    ImageView noInternetImageView;
    CircleImageView imageView;
    TextView nameTextView, memailTextView;
    Button tryAgainButton;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount googleSignInAccount;
    MaterialSearchView searchView;
    String data;
    String myQuery;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = findViewById(R.id.search_view);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        relativeLayout = findViewById(R.id.relativeLayout);
        imageViewRelativeLayout = findViewById(R.id.imageViewRelativeLayout);
        noInternetImageView = findViewById(R.id.noInternetImageView);
        tryAgainButton = findViewById(R.id.tryAgainButton);


        if(imageViewRelativeLayout.getVisibility() == View.VISIBLE)
        {
            imageViewRelativeLayout.setVisibility(View.VISIBLE);
        }

        if(isConnected()) {
            mAuth = FirebaseAuth.getInstance();
            firebaseUser = mAuth.getCurrentUser();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

            myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), getApplicationContext());
            myViewPagerAdapter.addFragment(new RecentFragment(), "Popular");
            myViewPagerAdapter.addFragment(new CategoriesFragment(), "Categories");

            viewPager.setAdapter(myViewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);

            searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    myQuery = query;
                    Intent intent = new Intent(getApplicationContext(), SearchWallpaperActivity.class);
                    intent.putExtra("Query", query);
                    startActivity(intent);
                    mImageUrlList.clear();
                    mImageThumbnailList.clear();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        }
        else
        {
            if(imageViewRelativeLayout.getVisibility()== View.GONE)
            {
                imageViewRelativeLayout.setVisibility(View.VISIBLE);
                tryAgainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent = getIntent();
                        startActivity(intent);
                    }
                });
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        imageView = header.findViewById(R.id.imageView);
        nameTextView = header.findViewById(R.id.nameTextView);
        memailTextView =header.findViewById(R.id.emailTextView);
        if (googleSignInAccount != null)
        {
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail().toString();
            Log.i("Email",""+email);
            if(name!=null || email != null)
            {
                nameTextView.setText(name);
                memailTextView.setText(email);
            }
            else
            {
                nameTextView.setText("Wally Wallpapers");
                memailTextView.setText("wally@com");
            }
            Uri profilePic = googleSignInAccount.getPhotoUrl();
            if(profilePic!=null)
            {
                Glide.with(getApplicationContext()).asBitmap().load(profilePic).into(imageView);
            }
        }

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        MenuItem item = menu.findItem(R.id.search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home)
        {

        }
        else if(id == R.id.share)
        {
            try
            {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Wally Wallpapers");
                String sAux = "\nTry this application.\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=the.package.id \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(id == R.id.signOut)
        {
           mAuth.signOut();
           mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   finish();
                   Intent intent = new Intent(getApplicationContext(),SplashScreenActivity.class);
                   startActivity(intent);
               }
           });

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isConnected())
        {
            if(firebaseUser == null)
            {
                Intent intent = new Intent(HomePageActivity.this,SplashScreenActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else
        {
            if(imageViewRelativeLayout.getVisibility()== View.GONE)
            {
                imageViewRelativeLayout.setVisibility(View.VISIBLE);
                tryAgainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent = getIntent();
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
