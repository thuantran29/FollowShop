package com.trannguyentanthuan2903.followshop;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/15/2017.
 */

public class ViewAdapterFragment extends FragmentPagerAdapter {

    private ArrayList<Fragment> arrayFragment=new ArrayList<>();
    private ArrayList<String> arrayName=new ArrayList<>();

    public ViewAdapterFragment(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return arrayFragment.get(position);
    }

    @Override
    public int getCount() {
        return arrayFragment.size();
    }

    public void addFragment(Fragment fragment,String title){
        arrayFragment.add(fragment);
        arrayName.add(title);
    }
}
