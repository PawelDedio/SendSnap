package com.example.pdedio.sendsnap.edit_snap;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.NumberPicker;

import com.example.pdedio.sendsnap.BaseFragmentActivity;
import com.example.pdedio.sendsnap.BaseFragmentContract;
import com.example.pdedio.sendsnap.common.views.BaseImageButton;
import com.example.pdedio.sendsnap.common.views.BaseImageView;
import com.example.pdedio.sendsnap.common.views.BaseTextView;
import com.example.pdedio.sendsnap.databinding.FragmentEditSnapBinding;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.helpers.SharedPreferenceManager;

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
    }

    public interface EditSnapView extends BaseFragmentContract.BaseFragmentView {
        BaseFragmentActivity getBaseFragmentActivity();

        TextureView getPreviewTextureView();

        BaseImageView getPreviewImageView();

        File getSnapFile();

        int getSnapType();

        Bitmap getSnapBitmap();

        BaseImageButton getSaveImageButton();

        BaseImageButton getSendButton();

        MovableEditText getTextEt();

        DrawingView getDrawingView();

        BaseImageButton getUndoButton();

        View getColorSelectorButton();

        FiltersView getFiltersView();

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
    }
}
