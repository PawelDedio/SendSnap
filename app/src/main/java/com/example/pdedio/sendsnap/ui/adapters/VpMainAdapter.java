package com.example.pdedio.sendsnap.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.example.pdedio.sendsnap.ui.fragments.BaseFragment;

import java.util.List;

/**
 * Created by p.dedio on 31.08.16.
 */
public class VpMainAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragments;

    public VpMainAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    public void setFragments(List<BaseFragment> fragments) {
        this.fragments = fragments;
    }
}
