package com.example.pdedio.sendsnap.ui.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.databinding.FragmentEditSnapBinding;
import com.example.pdedio.sendsnap.logic.helpers.Consts;
import com.example.pdedio.sendsnap.presenters.fragments.EditSnapPresenter;
import com.example.pdedio.sendsnap.ui.views.BaseImageButton;
import com.example.pdedio.sendsnap.ui.views.BaseImageView;
import com.example.pdedio.sendsnap.ui.views.BaseTextView;
import com.example.pdedio.sendsnap.ui.views.BaseViewPager;
import com.example.pdedio.sendsnap.ui.views.DrawingView;
import com.example.pdedio.sendsnap.ui.views.MovableEditText;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.File;

/**
 * Created by pawel on 19.09.2016.
 */
@EFragment
public class EditSnapFragment extends BaseFragment implements EditSnapPresenter.PresenterCallback {

    @Bean
    protected EditSnapPresenter editSnapPresenter;

    @FragmentArg
    protected Consts.SnapType snapType;

    @FragmentArg
    protected File snapFile;

    @FragmentArg
    protected Bitmap snapBitmap;

    @ViewById(R.id.ivEditSnapPhoto)
    protected BaseImageView ivPhoto;

    @ViewById(R.id.tvEditSnapVideo)
    protected TextureView tvVideo;

    @ViewById(R.id.btnEditSnapClose)
    protected BaseImageButton btnClose;

    @ViewById(R.id.btnEditSnapDraw)
    protected BaseImageButton btnDraw;

    @ViewById(R.id.btnEditSnapAddText)
    protected BaseImageButton btnAddText;

    @ViewById(R.id.btnEditSnapTimer)
    protected BaseTextView btnTimer;

    @ViewById(R.id.btnEditSnapSaveImage)
    protected BaseImageButton btnSaveImage;

    @ViewById(R.id.btnEditSnapSend)
    protected BaseImageButton btnSend;

    @ViewById(R.id.etEditSnapText)
    protected MovableEditText etText;

    @ViewById(R.id.vEditSnapDraw)
    protected DrawingView vDraw;

    @ViewById(R.id.btnEditSnapUndoDraw)
    protected BaseImageButton btnUndoDraw;

    @ViewById(R.id.btnEditSnapColorSelector)
    protected View btnColorSelector;

    @ViewById(R.id.vpEditSnapFilters)
    protected BaseViewPager vpFilters;

    private FragmentEditSnapBinding editSnapBinding;




    // Lifecycle
    @AfterInject
    protected void afterInjectEditSnapFragment() {
        this.editSnapPresenter.init(this);
    }

    @AfterViews
    protected void afterViewsEditSnapFragment() {
        this.editSnapPresenter.afterViews();
    }

    @Override
    public void onDestroy() {
        this.editSnapPresenter.destroy();
        this.editSnapPresenter = null;
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.editSnapBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_snap, container, false);
        View rootView = this.editSnapBinding.getRoot();
        return rootView;
    }


    //PresenterCallback methods
    @Override
    public TextureView getPreviewTextureView() {
        return this.tvVideo;
    }

    @Override
    public BaseImageView getPreviewImageView() {
        return this.ivPhoto;
    }

    @Override
    public File getSnapFile() {
        return this.snapFile;
    }

    @Override
    public Consts.SnapType getSnapType() {
        return this.snapType;
    }

    @Override
    public Bitmap getSnapBitmap() {
        return this.snapBitmap;
    }

    @Override
    public BaseImageButton getCloseButton() {
        return this.btnClose;
    }

    @Override
    public BaseImageButton getDrawButton() {
        return this.btnDraw;
    }

    @Override
    public BaseImageButton getAddTextButton() {
        return btnAddText;
    }

    @Override
    public BaseTextView getTimerButton() {
        return btnTimer;
    }

    @Override
    public BaseImageButton getSaveImageButton() {
        return btnSaveImage;
    }

    @Override
    public BaseImageButton getSendButton() {
        return btnSend;
    }

    @Override
    public MovableEditText getTextEt() {
        return this.etText;
    }

    @Override
    public RelativeLayout getMainLayout() {
        return (RelativeLayout) this.getView();
    }

    @Override
    public DrawingView getDrawingView() {
        return this.vDraw;
    }

    @Override
    public BaseImageButton getUndoButton() {
        return this.btnUndoDraw;
    }

    @Override
    public View getColorSelectorButton() {
        return this.btnColorSelector;
    }

    @Override
    public FragmentEditSnapBinding getBinding() {
        return this.editSnapBinding;
    }

    @Override
    public BaseViewPager getFiltersVP() {
        return this.vpFilters;
    }
}
