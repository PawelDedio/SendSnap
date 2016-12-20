package com.example.pdedio.sendsnap.edit_snap;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.databinding.FragmentEditSnapBinding;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.helpers.SharedPreferenceManager;
import com.example.pdedio.sendsnap.select_recipient.SelectSnapRecipientFragment;
import com.example.pdedio.sendsnap.select_recipient.SelectSnapRecipientFragment_;
import com.thebluealliance.spectrum.SpectrumDialog;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.ColorRes;

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

    private MediaPlayer mediaPlayer;

    @Bean
    protected SharedPreferenceManager sharedPreferenceManager;

    private boolean isDrawing;

    float touchY;

    float touchX;

    private static final int MAX_DISTANCE_FOR_CLICK = 15;



    // Lifecycle
    public void init(EditSnapContract.EditSnapView editSnapView) {
        this.editSnapView = editSnapView;
        this.editSnapView.hideStatusBar();
    }

    @Override
    public void destroy() {
        this.editSnapView = null;
    }

    @Override
    public void onResume() {
        if(this.editSnapView != null && this.editSnapView.getSnapType() == Consts.SNAP_TYPE_VIDEO) {
            this.showVideo();
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


    //Private methods
    private void configureViews() {
        switch(this.editSnapView.getSnapType()) {
            case Consts.SNAP_TYPE_PHOTO :
                this.showPhoto();
                break;
            case Consts.SNAP_TYPE_VIDEO :
                this.showVideo();
                break;
        }

        this.editSnapView.getUndoButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSnapView.getDrawingView().undoLastChange();
            }
        });
        this.editSnapView.getColorSelectorButton().setBackgroundColor(this.defaultDrawColor);
        this.editSnapView.getColorSelectorButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPicker();
            }
        });

        this.editSnapView.getSaveImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSnap();
            }
        });

        this.editSnapView.getSendButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSnap();
            }
        });
    }

    private void showPhoto() {
        Bitmap bitmap = this.editSnapView.getSnapBitmap();
        this.editSnapView.getPreviewImageView().setImageBitmap(bitmap);
        this.editSnapView.getPreviewImageView().setVisibility(View.VISIBLE);
        this.editSnapView.getPreviewTextureView().setVisibility(View.GONE);
    }

    private void showVideo() {
        this.editSnapView.getPreviewImageView().setVisibility(View.GONE);
        this.editSnapView.getPreviewTextureView().setVisibility(View.VISIBLE);
        this.editSnapView.getPreviewTextureView().setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                try {
                    Surface surface = new Surface(surfaceTexture);
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(editSnapView.getSnapFile().getAbsolutePath());
                    mediaPlayer.setSurface(surface);
                    mediaPlayer.prepare();
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                ActivityManager.isUserAMonkey();
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                ActivityManager.isUserAMonkey();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                ActivityManager.isUserAMonkey();
            }
        });
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
        SpectrumDialog.Builder builder = new SpectrumDialog.Builder(this.editSnapView.getBaseFragmentActivity());
        builder.setColors(R.array.edit_snap_draw_colors)
                .setDismissOnColorSelected(true)
                .setOutlineWidth(2)
                .setSelectedColor(this.editSnapView.getDrawingView().getCurrentColor())
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if(positiveResult) {
                            editSnapView.getColorSelectorButton().setBackgroundColor(color);
                            editSnapView.getDrawingView().setColor(color);
                        } else {
                            Log.e("onColorSelected", "false");
                        }
                    }
                });

        builder.build().show(this.editSnapView.getBaseFragmentActivity().getSupportFragmentManager(), "Dialog");
    }

    private void updateSnapDuration(int value) {
        this.sharedPreferenceManager.setSnapDuration(value);
    }

    private void saveSnap() {
        String directory = this.editSnapView.getBaseFragmentActivity().getString(R.string.snap_directory_name);
        Long timeStamp = System.currentTimeMillis() / 1000;
        String name = this.editSnapView.getBaseFragmentActivity().getString(R.string.snap_saved_file_name, timeStamp);

        File snap = this.generateSnapFile(directory, name);

        Toast.makeText(this.editSnapView.getBaseFragmentActivity(), R.string.edit_snap_snap_saved_message, Toast.LENGTH_SHORT).show();
        MediaScannerConnection.scanFile(this.editSnapView.getBaseFragmentActivity(), new String[] { snap.getAbsolutePath()}, null, null);
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

            String format = this.editSnapView.getSnapType() == Consts.SnapType.PHOTO ? ".jpg" : ".mp4";
            snap = new File(path, fileName + format);
        }

        if(this.editSnapView.getSnapType() == Consts.SnapType.PHOTO) {
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

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        this.editSnapView.getPreviewImageView().draw(canvas);
        this.editSnapView.getFiltersView().draw(canvas);
        this.editSnapView.getDrawingView().draw(canvas);
        MovableEditText etText = this.editSnapView.getTextEt();
        etText.setDrawingCacheEnabled(true);
        Bitmap text = etText.getDrawingCache();
        canvas.drawBitmap(text, 0, etText.getTranslationY(), null);

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
        //this.openFragment(this.mainView.getBaseFragmentActivity(), fragment);
    }
}
