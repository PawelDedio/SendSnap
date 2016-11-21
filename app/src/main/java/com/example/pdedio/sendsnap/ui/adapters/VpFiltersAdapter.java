package com.example.pdedio.sendsnap.ui.adapters;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.pdedio.sendsnap.ui.fragments.ImageViewFragment_;

import java.util.List;

/**
 * Created by p.dedio on 21.11.16.
 */

public class VpFiltersAdapter extends FragmentStatePagerAdapter {


    private List<Bitmap> bitmaps;

    public VpFiltersAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ImageViewFragment_.builder().background(this.bitmaps.get(position)).build();
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }
}
