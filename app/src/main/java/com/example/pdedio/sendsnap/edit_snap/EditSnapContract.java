package com.example.pdedio.sendsnap.edit_snap;

import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.view.MotionEvent;

import com.example.pdedio.sendsnap.BaseFragmentContract;
import com.example.pdedio.sendsnap.common.views.BaseImageView;
import com.example.pdedio.sendsnap.helpers.SharedPreferenceManager;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.io.File;

/**
 * Created by p.dedio on 20.12.16.
 */

public class EditSnapContract {

    public interface EditSnapPresenter extends BaseFragmentContract.BaseFragmentPresenter {

        void init(EditSnapView editSnapView);

        SharedPreferenceManager getSharedPrefManager();

        void onCloseButtonClick();

        void onTimerButtonClick();

        boolean onFiltersClick(MotionEvent event);

        void onAddTextClick();

        void onDrawClick();

        void onUndoClick();

        void onColorSelectorClick();

        void onSaveClick();

        void onSendClick();
    }

    public interface EditSnapView extends BaseFragmentContract.BaseFragmentView {
        BaseImageView getPreviewImageView();

        File getSnapFile();

        int getSnapType();

        void hideStatusBar();

        void clearDrawingArea();

        void startDrawing();

        void stopDrawing();

        void showNumberPicker(int selectedValue, NumberPickerDialog.ResultListener listener);

        boolean isTextTyping();

        void startTypingText(float yPosition);

        void startTypingTextFromCenter();

        void stopTypingText();

        String getSnapText();

        int getTextVisibility();

        void clearSnapText();

        boolean isDrawingEnabled();

        void undoLastDrawChange();

        void showColorSelector(SpectrumDialog.OnColorSelectedListener listener);

        void setDrawingColor(@ColorInt int color);

        void showPhoto();

        void showVideo();

        String getStringFromRes(@StringRes int string);

        String getStringFromRes(int stringId, Object... args);

        void scanImageFiles(String path);

        void drawViewsOnCanvas(Canvas canvas);
    }
}