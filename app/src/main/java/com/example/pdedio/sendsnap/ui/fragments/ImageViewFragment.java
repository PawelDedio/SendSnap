package com.example.pdedio.sendsnap.ui.fragments;

import android.graphics.Bitmap;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.ui.views.BaseImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

/**
 * Created by p.dedio on 21.11.16.
 */
@EFragment(R.layout.fragment_image_view)
public class ImageViewFragment extends BaseFragment {


    @ViewById(R.id.ivImageViewMain)
    protected BaseImageView ivMain;

    @FragmentArg
    @InstanceState
    protected Bitmap background;


    //Lifecycle
    @AfterViews
    protected void afterViewsImageViewFragment() {
        if(this.background != null) {
            this.ivMain.setImageBitmap(background);
        }
    }
}
