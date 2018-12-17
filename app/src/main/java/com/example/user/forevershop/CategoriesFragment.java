package com.example.user.forevershop;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment {

    private ArrayList<String> mCategoryImageUrlList;
    private ArrayList<String> mCategoryNameList;
    RecyclerView recyclerView;
    MyCategoryRecyclerViewAdapter myCategoryRecyclerViewAdapter;
    DatabaseReference firebaseDatabase;

    public CategoriesFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        mCategoryImageUrlList = new ArrayList<>();
        mCategoryNameList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerViewCategories);
        myCategoryRecyclerViewAdapter = new MyCategoryRecyclerViewAdapter(this,mCategoryImageUrlList,mCategoryNameList);
        recyclerView.setAdapter(myCategoryRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Categories");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot mDataSnapShot: dataSnapshot.getChildren())
                {
                    String categoryName = mDataSnapShot.child("name").getValue().toString();
                    String categoryImageUrl = mDataSnapShot.child("url").getValue().toString();
                    mCategoryNameList.add(categoryName);
                    mCategoryImageUrlList.add(categoryImageUrl);
                }

                myCategoryRecyclerViewAdapter.swap(mCategoryImageUrlList,mCategoryNameList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

}
