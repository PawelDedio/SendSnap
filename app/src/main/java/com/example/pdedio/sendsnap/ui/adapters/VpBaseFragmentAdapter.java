package com.example.pdedio.sendsnap.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.pdedio.sendsnap.ui.fragments.BaseFragment;

import java.util.List;

/**
 * Created by p.dedio on 31.08.16.
 */
public class VpBaseFragmentAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> fragments;

    public VpBaseFragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    public void setFragments(List<BaseFragment> fragments) {
        this.fragments = fragments;
    }
}
