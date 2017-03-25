package com.example.pdedio.sendsnap.edit_snap;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseImageView;
import com.example.pdedio.sendsnap.helpers.BitmapsManager;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.helpers.FilesManager;
import com.example.pdedio.sendsnap.helpers.SharedPreferenceManager;
import com.thebluealliance.spectrum.SpectrumDialog;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyFloat;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by p.dedio on 21.12.16.
 */

public class EditSnapPresenterTest {


    @Mock
    private EditSnapContract.EditSnapView mockedView;

    @Mock
    private SharedPreferenceManager mockedPrefManager;

    @Mock
    private BitmapsManager mockedBitmapsManager;

    @Mock
    private FilesManager mockedFilesManager;

    @Mock
    private File mockedFile;



    private EditSnapPresenter configureAndInitPresenter() {
        EditSnapPresenter presenter = this.configurePresenter();
        presenter.init(mockedView);

        return presenter;
    }

    private EditSnapPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);
        EditSnapPresenter presenter = new EditSnapPresenter();
        presenter.sharedPreferenceManager = mockedPrefManager;
        presenter.bitmapsManager = mockedBitmapsManager;
        presenter.filesManager = mockedFilesManager;

        return presenter;
    }

    private MotionEvent prepareMockedEvent(float x, float y, int action) {
        MotionEvent event = mock(MotionEvent.class);

        when(event.getRawX()).thenReturn(x);
        when(event.getRawY()).thenReturn(y);
        when(event.getAction()).thenReturn(action);

        return event;
    }

    private BaseImageView prepareMockedImageView(int width, int height) {
        BaseImageView mockedImageView = mock(BaseImageView.class);
        when(mockedImageView.getWidth()).thenReturn(width);
        when(mockedImageView.getHeight()).thenReturn(height);

        return mockedImageView;
    }


    //init()
    @Test
    public void shouldHideStatusBar() {
        EditSnapPresenter presenter = this.configurePresenter();

        presenter.init(mockedView);

        verify(mockedView).hideStatusBar();
    }

    @Test
    public void shouldShowPhotoForPhotoSnap() {
        EditSnapPresenter presenter = this.configurePresenter();

        when(mockedView.getSnapType()).thenReturn(Consts.SNAP_TYPE_PHOTO);

        presenter.init(mockedView);

        verify(mockedView).showPhoto();
    }

    @Test
    public void shouldShowVideoForVideoSnap() {
        EditSnapPresenter presenter = this.configurePresenter();

        when(mockedView.getSnapType()).thenReturn(Consts.SNAP_TYPE_VIDEO);

        presenter.init(mockedView);

        verify(mockedView).showVideo();
    }


    //getSharedPrefManager()
    @Test
    public void shouldReturnValidManager() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        Assert.assertEquals(presenter.getSharedPrefManager(), mockedPrefManager);
    }


    //onCloseButtonClick()
    @Test
    public void shouldClearDrawingAreaWhenUserIsDrawing() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();
        presenter.isDrawing = true;

        presenter.onCloseButtonClick();

        verify(mockedView).clearDrawingArea();
    }

    @Test
    public void shouldStopDrawingWhenUserIsDrawing() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();
        presenter.isDrawing = true;

        presenter.onCloseButtonClick();

        verify(mockedView).stopDrawing();
    }

    @Test
    public void shouldPopFragmentWhenUserIsNotDrawing() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();
        presenter.isDrawing = false;

        presenter.onCloseButtonClick();

        verify(mockedView).popFragment();
    }


    //onTimerButtonClick()
    @Test
    public void shouldShowNumberPickerWithValueFromSharedPrefs() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();
        int duration = 7;

        when(mockedPrefManager.getSnapDuration()).thenReturn(duration);

        presenter.onTimerButtonClick();

        verify(mockedView).showNumberPicker(eq(duration), any(NumberPickerDialog.ResultListener.class));
    }

    @Test
    public void shouldUpdateSnapDurationAfterChoosingTime() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();
        final int duration = 7;

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                NumberPickerDialog.ResultListener listener = (NumberPickerDialog.ResultListener) invocation.getArguments()[1];
                listener.onValueSet(duration);

                return null;
            }
        }).when(mockedView).showNumberPicker(anyInt(), any(NumberPickerDialog.ResultListener.class));

        presenter.onTimerButtonClick();

        verify(mockedPrefManager).setSnapDuration(duration);
    }


    //onFiltersClick()
    @Test
    public void shouldNotStartOrStopTypingWhenMoveDestinationIsTooLarge() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        MotionEvent down = this.prepareMockedEvent(100, 100, MotionEvent.ACTION_DOWN);
        MotionEvent up = this.prepareMockedEvent(101 + EditSnapPresenter.MAX_DISTANCE_FOR_CLICK,
                101 + EditSnapPresenter.MAX_DISTANCE_FOR_CLICK, MotionEvent.ACTION_UP);

        presenter.onFiltersClick(down);
        presenter.onFiltersClick(up);

        verify(mockedView, never()).stopTypingText();
        verify(mockedView, never()).startTypingText(anyFloat());
    }

    @Test
    public void shouldStopTypingTextWhenIsTyping() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        MotionEvent down = this.prepareMockedEvent(100, 100, MotionEvent.ACTION_DOWN);
        MotionEvent up = this.prepareMockedEvent(99 + EditSnapPresenter.MAX_DISTANCE_FOR_CLICK,
                99 + EditSnapPresenter.MAX_DISTANCE_FOR_CLICK, MotionEvent.ACTION_UP);

        when(mockedView.isTextTyping()).thenReturn(true);

        presenter.onFiltersClick(down);
        presenter.onFiltersClick(up);

        verify(mockedView).stopTypingText();
    }

    @Test
    public void shouldStartTypingFromRequiredPositionWhenTextIsBlank() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        MotionEvent down = this.prepareMockedEvent(100, 100, MotionEvent.ACTION_DOWN);
        MotionEvent up = this.prepareMockedEvent(99 + EditSnapPresenter.MAX_DISTANCE_FOR_CLICK,
                99 + EditSnapPresenter.MAX_DISTANCE_FOR_CLICK, MotionEvent.ACTION_UP);

        when(mockedView.isTextTyping()).thenReturn(false);
        when(mockedView.getSnapText()).thenReturn("");

        presenter.onFiltersClick(down);
        presenter.onFiltersClick(up);

        verify(mockedView).startTypingText(100);
    }

    @Test
    public void shouldNotStartTypingFromRequiredPositionWhenTextIsNotBlank() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        MotionEvent down = this.prepareMockedEvent(100, 100, MotionEvent.ACTION_DOWN);
        MotionEvent up = this.prepareMockedEvent(99 + EditSnapPresenter.MAX_DISTANCE_FOR_CLICK,
                99 + EditSnapPresenter.MAX_DISTANCE_FOR_CLICK, MotionEvent.ACTION_UP);

        when(mockedView.isTextTyping()).thenReturn(false);
        when(mockedView.getSnapText()).thenReturn("not empty text");

        presenter.onFiltersClick(down);
        presenter.onFiltersClick(up);

        verify(mockedView, never()).startTypingText(anyInt());
    }


    //onAddTextClick()
    @Test
    public void shouldClearTextWhenTextIsVisible() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        when(mockedView.getTextVisibility()).thenReturn(View.VISIBLE);

        presenter.onAddTextClick();

        verify(mockedView).clearSnapText();
    }

    @Test
    public void shouldStopTypingTextWhenTextIsVisible() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        when(mockedView.getTextVisibility()).thenReturn(View.VISIBLE);

        presenter.onAddTextClick();

        verify(mockedView).stopTypingText();
    }

    @Test
    public void shouldStartTypingTextFromCenterWhenTextIsNotVisible() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        when(mockedView.getTextVisibility()).thenReturn(View.INVISIBLE);

        presenter.onAddTextClick();

        verify(mockedView).startTypingTextFromCenter();
    }


    //onDrawClick()
    @Test
    public void shouldStopDrawingWhenDrawingIsEnabled() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        when(mockedView.isDrawingEnabled()).thenReturn(true);

        presenter.onDrawClick();

        verify(mockedView).stopDrawing();
    }

    @Test
    public void shouldStartDrawingWhenDrawingIsNotEnabled() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        when(mockedView.isDrawingEnabled()).thenReturn(false);

        presenter.onDrawClick();

        verify(mockedView).startDrawing();
    }


    //onUndoClick()
    @Test
    public void shouldUndoLastDrawChange() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        presenter.onUndoClick();

        verify(mockedView).undoLastDrawChange();
    }


    //onColorSelectorClick()
    @Test
    public void shouldShowColorSelector() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        presenter.onColorSelectorClick();

        verify(mockedView).showColorSelector(any(SpectrumDialog.OnColorSelectedListener.class));
    }

    @Test
    public void shouldUpdateDrawingColorAfterSuccessfullyChoosingByUser() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        final int color = -7617718;

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                SpectrumDialog.OnColorSelectedListener listener = (SpectrumDialog.OnColorSelectedListener) invocation.getArguments()[0];
                listener.onColorSelected(true, color);

                return null;
            }
        }).when(mockedView).showColorSelector(any(SpectrumDialog.OnColorSelectedListener.class));

        presenter.onColorSelectorClick();

        verify(mockedView).setDrawingColor(color);
    }

    @Test
    public void shouldNotUpdateDrawingColorAfterNotSuccessfullyChoosingByUser() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        final int color = -7617718;

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                SpectrumDialog.OnColorSelectedListener listener = (SpectrumDialog.OnColorSelectedListener) invocation.getArguments()[0];
                listener.onColorSelected(false, color);

                return null;
            }
        }).when(mockedView).showColorSelector(any(SpectrumDialog.OnColorSelectedListener.class));

        presenter.onColorSelectorClick();

        verify(mockedView, never()).setDrawingColor(color);
    }


    //onSaveClick()
    @Test
    public void shouldShowToastAfterSavingSnap() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        when(mockedView.getStringFromRes(R.string.snap_directory_name)).thenReturn("/Send Snap");
        when(mockedView.getStringFromRes(eq(R.string.snap_saved_file_name), Matchers.anyVararg()))
                .thenReturn("SnapName");
        when(mockedView.getSnapPath()).thenReturn(mockedFile.getAbsolutePath());

        presenter.onSaveClick();

        verify(mockedView).showToast(R.string.edit_snap_snap_saved_message, Toast.LENGTH_SHORT);
    }

    @Test
    public void shouldScanImageFilesAfterSnapSaving() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        when(mockedView.getStringFromRes(R.string.snap_directory_name)).thenReturn("/Send Snap");
        when(mockedView.getStringFromRes(eq(R.string.snap_saved_file_name), Matchers.anyVararg()))
                .thenReturn("SnapName");
        when(mockedView.getSnapPath()).thenReturn(mockedFile.getAbsolutePath());

        presenter.onSaveClick();

        verify(mockedView).scanImageFiles(anyString());
    }

    @Test
    public void shouldCloneOriginalFile() throws Exception {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        when(mockedView.getStringFromRes(R.string.snap_directory_name)).thenReturn("/Send Snap");
        when(mockedView.getStringFromRes(eq(R.string.snap_saved_file_name), Matchers.anyVararg()))
                .thenReturn("SnapName");
        when(mockedView.getSnapPath()).thenReturn(mockedFile.getAbsolutePath());

        presenter.onSaveClick();

        verify(mockedFilesManager).copyFile(any(File.class), any(File.class));
    }

    @Test
    public void shouldSaveBitmapToFile() throws Exception {
        EditSnapPresenter presenter = this.configureAndInitPresenter();
        BaseImageView mockedImageView = this.prepareMockedImageView(100, 100);

        when(mockedView.getStringFromRes(R.string.snap_directory_name)).thenReturn("/Send Snap");
        when(mockedView.getStringFromRes(eq(R.string.snap_saved_file_name), Matchers.anyVararg()))
                .thenReturn("SnapName");
        when(mockedView.getSnapPath()).thenReturn(mockedFile.getAbsolutePath());
        when(mockedView.getSnapType()).thenReturn(Consts.SNAP_TYPE_PHOTO);
        when(mockedView.getPreviewImageView()).thenReturn(mockedImageView);

        presenter.onSaveClick();

        verify(mockedBitmapsManager).saveBitmapToFile(any(Bitmap.class), any(File.class));
    }


    //onSendClick()
    @Test
    public void shouldGetOriginalSnapFile() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        when(mockedView.getStringFromRes(R.string.snap_directory_name)).thenReturn("/Send Snap");
        when(mockedView.getStringFromRes(eq(R.string.snap_saved_file_name), Matchers.anyVararg()))
                .thenReturn("SnapName");
        when(mockedView.getSnapPath()).thenReturn(mockedFile.getAbsolutePath());

        presenter.onSendClick();

        verify(mockedView, atLeastOnce()).getSnapPath();
    }

    @Test
    public void shouldOpenFragment() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        when(mockedView.getStringFromRes(R.string.snap_directory_name)).thenReturn("/Send Snap");
        when(mockedView.getStringFromRes(eq(R.string.snap_saved_file_name), Matchers.anyVararg()))
                .thenReturn("SnapName");
        when(mockedView.getSnapPath()).thenReturn(mockedFile.getAbsolutePath());

        presenter.onSendClick();

        verify(mockedView).showFragment(any(BaseFragment.class));
    }


    //destroy()
    @Test
    public void shouldSetViewReferenceToNull() {
        EditSnapPresenter presenter = this.configureAndInitPresenter();

        presenter.destroy();

        Assert.assertNull(presenter.editSnapView);
    }
}
