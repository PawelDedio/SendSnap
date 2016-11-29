package com.example.pdedio.sendsnap.presenters.fragments;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.ui.activities.BaseFragmentActivity;
import com.example.pdedio.sendsnap.ui.views.BaseImageButton;

import org.androidannotations.annotations.EBean;

/**
 * Created by p.dedio on 28.11.16.
 */
@EBean
public class SelectSnapRecipientPresenter extends BaseFragmentPresenter {

    private PresenterCallback presenterCallback;



    //Lifecycle
    public void init(PresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
    }

    @Override
    public void afterViews() {
        this.presenterCallback.showStatusBar();
        this.setNotificationColor();
        this.configureViews();
    }

    @Override
    public void destroy() {
        this.presenterCallback = null;
    }


    //Private methods
    private void setNotificationColor() {
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            BaseFragmentActivity activity = this.presenterCallback.getBaseFragmentActivity();
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.action_bar_blue_color_dark));
        }
    }

    private void configureViews() {
        this.presenterCallback.getBtnBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popFragment(presenterCallback.getBaseFragmentActivity());
            }
        });
    }



    public interface PresenterCallback {
        void showStatusBar();

        BaseFragmentActivity getBaseFragmentActivity();

        BaseImageButton getBtnBack();

        BaseImageButton getBtnSearch();
    }
}
