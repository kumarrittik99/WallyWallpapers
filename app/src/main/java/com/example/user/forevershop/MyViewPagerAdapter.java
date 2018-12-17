package com.example.user.forevershop;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyViewPagerAdapter extends FragmentStatePagerAdapter
{
    private Context context;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentNameList = new ArrayList();

    public MyViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return mFragmentNameList.get(position);
    }

    public void addFragment(Fragment fragment, String title)
    {
        mFragmentList.add(fragment);
        mFragmentNameList.add(title);
    }

}
