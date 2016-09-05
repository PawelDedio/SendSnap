package com.example.pdedio.sendsnap.ui.fragments;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Button;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.presenters.fragments.CameraPresenter;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by p.dedio on 31.08.16.
 */
@EFragment(R.layout.fragment_camera)
public class CameraFragment extends BaseFragment implements CameraPresenter.PresenterCallback {

    public static final String TAG = CameraFragment.class.getSimpleName();

    @Bean
    protected CameraPresenter cameraPresenter;

    @ViewById(R.id.pbCameraRecordProgress)
    protected DonutProgress pbRecordProgress;

    @ViewById(R.id.btnCameraRecord)
    protected Button btnCameraRecord;

    @ViewById(R.id.svCameraPreview)
    protected SurfaceView svCameraPreview;


    //Lifecycle
    @AfterInject
    protected void afterInjectCameraFragment() {
        this.cameraPresenter.init(this);
    }

    @AfterViews
    protected void afterViewsCameraFragment() {
        Log.e(TAG, "afterViewsCameraFragment: ");
        this.cameraPresenter.afterViews();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("CameraFragment", "onAttach");
    }


    //PresenterCallback methods
    @Override
    public DonutProgress getCameraProgressBar() {
        return this.pbRecordProgress;
    }

    public Button getCameraButton() {
        return this.btnCameraRecord;
    }
}
