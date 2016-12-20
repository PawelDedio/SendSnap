package com.example.pdedio.sendsnap.edit_snap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.helpers.BitmapsManager;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.helpers.SharedPreferenceManager;
import com.example.pdedio.sendsnap.select_recipient.SelectSnapRecipientFragment;
import com.example.pdedio.sendsnap.select_recipient.SelectSnapRecipientFragment_;
import com.thebluealliance.spectrum.SpectrumDialog;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by pawel on 19.09.2016.
 */
@EBean
public class EditSnapPresenter extends BaseFragmentPresenter implements EditSnapContract.EditSnapPresenter {


    private EditSnapContract.EditSnapView editSnapView;

    @Bean
    protected SharedPreferenceManager sharedPreferenceManager;

    private boolean isDrawing;

    float touchY;

    float touchX;

    private static final int MAX_DISTANCE_FOR_CLICK = 15;

    @Bean
    protected BitmapsManager bitmapsManager;



    //Lifecycle
    public void init(EditSnapContract.EditSnapView editSnapView) {
        this.editSnapView = editSnapView;
        this.editSnapView.hideStatusBar();
        this.configureViews();
    }

    @Override
    public void destroy() {
        this.editSnapView = null;
    }

    @Override
    public void onResume() {
        if(this.editSnapView != null && this.editSnapView.getSnapType() == Consts.SNAP_TYPE_VIDEO) {
            this.editSnapView.showVideo();
        }
    }


    //EditSnapPresenter methods
    @Override
    public SharedPreferenceManager getSharedPrefManager() {
        return null;
    }

    @Override
    public void onCloseButtonClick() {
        if(isDrawing) {
            editSnapView.clearDrawingArea();
            stopDrawing();
        } else {
            //popFragment(mainView.getBaseFragmentActivity());
        }
    }

    @Override
    public void onTimerButtonClick() {
        int selectedValue = this.sharedPreferenceManager.getSnapDuration();
        this.editSnapView.showNumberPicker(selectedValue, new NumberPickerDialog.ResultListener() {

            @Override
            public void onValueSet(int value) {
                updateSnapDuration(value);
            }
        });
    }

    @Override
    public boolean onFiltersClick(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchY = event.getRawY();
                touchX = event.getRawX();
                break;

            case MotionEvent.ACTION_UP:
                float currentY = event.getRawY();
                float currentX = event.getRawX();

                if(Math.abs(touchY - currentY) <= MAX_DISTANCE_FOR_CLICK && Math.abs(touchX - currentX) <= MAX_DISTANCE_FOR_CLICK) {

                    if(this.editSnapView.isTextTyping()) {
                        this.editSnapView.stopTypingText();
                    } else if(this.editSnapView.getSnapText().length() == 0) {
                        this.editSnapView.startTypingText(touchY);
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onAddTextClick() {
        if(this.editSnapView.getTextVisibility() == View.VISIBLE) {
            this.editSnapView.clearSnapText();
            this.editSnapView.stopTypingText();
        } else {
            this.editSnapView.startTypingTextFromCenter();
        }
    }

    @Override
    public void onDrawClick() {
        if(this.editSnapView.isDrawingEnabled()) {
            stopDrawing();
        } else {
            startDrawing();
        }
    }

    @Override
    public void onUndoClick() {
        this.editSnapView.undoLastDrawChange();
    }

    @Override
    public void onColorSelectorCLick() {
        this.showColorPicker();
    }

    @Override
    public void onSaveClick() {
        this.saveSnap();
    }

    @Override
    public void onSendClick() {
        this.sendSnap();
    }


    //Private methods
    private void configureViews() {
        switch(this.editSnapView.getSnapType()) {
            case Consts.SNAP_TYPE_PHOTO :
                this.editSnapView.showPhoto();
                break;
            case Consts.SNAP_TYPE_VIDEO :
                this.editSnapView.showVideo();
                break;
        }
    }

    private void startDrawing() {
        this.isDrawing = true;

        this.editSnapView.startDrawing();
    }

    private void stopDrawing() {
        this.isDrawing = false;

        this.editSnapView.stopDrawing();
    }

    private void showColorPicker() {
        this.editSnapView.showColorSelector(new SpectrumDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                if(positiveResult) {
                    editSnapView.setDrawingColor(color);
                } else {
                    Log.e("onColorSelected", "false");
                }
            }
        });
    }

    private void updateSnapDuration(int value) {
        this.sharedPreferenceManager.setSnapDuration(value);
    }

    private void saveSnap() {
        String directory = this.editSnapView.getStringFromRes(R.string.snap_directory_name);
        Long timeStamp = System.currentTimeMillis() / 1000;
        String name = this.editSnapView.getStringFromRes(R.string.snap_saved_file_name, timeStamp);

        File snap = this.generateSnapFile(directory, name);

        this.editSnapView.showToast(R.string.edit_snap_snap_saved_message, Toast.LENGTH_SHORT);

        this.editSnapView.scanImageFiles(snap.getAbsolutePath());
    }

    private void sendSnap() {
        File snap = this.generateSnapFile();

        this.openFragment(snap);
    }

    private File generateSnapFile() {
        return this.generateSnapFile(null, null);
    }

    private File generateSnapFile(String directory, String fileName) {

        File snap;

        if(directory == null || fileName == null) {
            snap = this.editSnapView.getSnapFile();
        } else {
            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + directory);

            if(!path.exists()) {
                path.mkdir();
            }

            String format = this.editSnapView.getSnapType() == Consts.SNAP_TYPE_PHOTO ? ".jpg" : ".mp4";
            snap = new File(path, fileName + format);
        }

        if(this.editSnapView.getSnapType() == Consts.SNAP_TYPE_PHOTO) {
            Bitmap bitmap = this.makeViewsScreenshot();
            this.saveBitmapToFile(bitmap, snap);
        } else if(snap.length() == 0) {
            File takenSNap  = this.editSnapView.getSnapFile();
            this.copyFile(takenSNap, snap);
            takenSNap.delete();
        }

        return snap;
    }

    private Bitmap makeViewsScreenshot() {
        int width = this.editSnapView.getPreviewImageView().getWidth();
        int height = this.editSnapView.getPreviewImageView().getHeight();

        Bitmap bitmap = this.bitmapsManager.createEmptyBitmap(width, height);
        final Canvas canvas = new Canvas(bitmap);
        this.editSnapView.drawViewsOnCanvas(canvas);

        return bitmap;
    }

    private void saveBitmapToFile(Bitmap bitmap, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyFile(File source, File destination) {
        try {
            FileInputStream in = new FileInputStream(source);
            FileOutputStream out = new FileOutputStream(destination);
            FileChannel inChannel = in.getChannel();
            FileChannel outChannel = out.getChannel();

            inChannel.transferTo(0, inChannel.size(), outChannel);
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openFragment(File file) {
        SelectSnapRecipientFragment fragment = SelectSnapRecipientFragment_.builder().snapFile(file).build();
        this.editSnapView.showFragment(fragment);
    }
}
