package com.example.pdedio.sendsnap.edit_snap;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.databinding.FragmentEditSnapBinding;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.common.views.BaseImageButton;
import com.example.pdedio.sendsnap.common.views.BaseImageView;
import com.example.pdedio.sendsnap.common.views.BaseTextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.io.File;

/**
 * Created by pawel on 19.09.2016.
 */
@EFragment
public class EditSnapFragment extends BaseFragment implements EditSnapContract.EditSnapView {

    @Bean(EditSnapPresenter.class)
    protected EditSnapContract.EditSnapPresenter editSnapPresenter;

    @FragmentArg
    @InstanceState
    protected int snapType;

    @FragmentArg
    @InstanceState
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

    @ViewById(R.id.sipEditSnapFilters)
    protected FiltersView filtersView;

    private FragmentEditSnapBinding editSnapBinding;

    private NumberPickerDialog numberPickerDialog;

    @ColorRes(R.color.edit_snap_default_draw)
    protected int defaultDrawColor;




    // Lifecycle
    @AfterViews
    protected void afterViewsEditSnapFragment() {
        this.editSnapPresenter.init(this);
        this.configureViews();
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


    //Events
    @Click(R.id.btnEditSnapClose)
    protected void onBtnCloseClick() {
        this.editSnapPresenter.onCloseButtonClick();
    }

    @Click(R.id.btnEditSnapTimer)
    protected void onBtnTimerClick() {
        this.editSnapPresenter.onTimerButtonClick();
    }

    @Touch(R.id.sipEditSnapFilters)
    protected boolean onSipFiltersTouch(View view, MotionEvent motionEvent) {
        return editSnapPresenter.onFiltersClick(motionEvent);
    }

    @Click(R.id.btnEditSnapAddText)
    protected void onBtnAddTextClick() {
        this.editSnapPresenter.onAddTextClick();
    }

    @Click(R.id.btnEditSnapDraw)
    protected void onBtnDrawClick() {
        this.editSnapPresenter.onDrawClick();
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
    public int getSnapType() {
        return this.snapType;
    }

    @Override
    public Bitmap getSnapBitmap() {
        return this.snapBitmap;
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
    public FiltersView getFiltersView() {
        return this.filtersView;
    }

    @Override
    public void clearDrawingArea() {
        this.vDraw.clearArea();
    }

    @Override
    public void startDrawing() {
        this.btnAddText.setVisibility(View.GONE);
        this.btnUndoDraw.setVisibility(View.VISIBLE);
        this.vDraw.startDrawing();
        this.btnColorSelector.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopDrawing() {
        this.btnAddText.setVisibility(View.VISIBLE);
        this.btnUndoDraw.setVisibility(View.GONE);
        this.vDraw.stopDrawing();
        this.btnColorSelector.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNumberPicker(int selectedValue, NumberPickerDialog.ResultListener listener) {
        if(this.numberPickerDialog == null) {
            this.numberPickerDialog = new NumberPickerDialog(this.getContext());
            this.numberPickerDialog.setTitle(R.string.edit_snap_number_picker_title);
            this.numberPickerDialog.setMinValue(Consts.MIN_SNAP_DURATION);
            this.numberPickerDialog.setMaxValue(Consts.MAX_SNAP_DURATION);
            this.numberPickerDialog.setWrapSelectorWheel(false);
            this.numberPickerDialog.setResultListener(listener);
        }

        this.numberPickerDialog.setSelectedValue(selectedValue);
        this.numberPickerDialog.show();
    }

    @Override
    public boolean isTextTyping() {
        return this.etText.isTyping();
    }

    @Override
    public void startTypingText(float yPosition) {
        this.etText.startTyping(yPosition);
    }

    @Override
    public void startTypingTextFromCenter() {
        this.etText.startTypingFromCenter();
    }

    @Override
    public void stopTypingText() {
        this.etText.stopTyping();
    }

    @Override
    public String getSnapText() {
        return null;
    }

    @Override
    public int getTextVisibility() {
        return this.etText.getVisibility();
    }

    @Override
    public void clearSnapText() {
        this.etText.setText("");
    }

    @Override
    public boolean isDrawingEnabled() {
        return this.vDraw.isDrawingEnabled();
    }


    //Private methods
    private void configureViews() {
        this.editSnapBinding.setPrefs(this.editSnapPresenter.getSharedPrefManager());

        this.vDraw.setColor(this.defaultDrawColor);
    }
}
