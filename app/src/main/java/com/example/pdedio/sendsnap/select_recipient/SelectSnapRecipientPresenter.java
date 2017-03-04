package com.example.pdedio.sendsnap.select_recipient;

import android.os.Build;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.helpers.AndroidManager;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by p.dedio on 28.11.16.
 */
@EBean
public class SelectSnapRecipientPresenter extends BaseFragmentPresenter implements SelectSnapRecipientContract.SelectSnapRecipientPresenter {

    private SelectSnapRecipientContract.SelectSnapRecipientView selectRecipientView;

    @Bean
    protected AndroidManager androidManager;



    //Lifecycle
    @Override
    public void init(SelectSnapRecipientContract.SelectSnapRecipientView selectRecipientView) {
        this.selectRecipientView = selectRecipientView;
        this.configureViews();
    }

    @Override
    public void destroy() {
        this.selectRecipientView = null;
    }


    //SelectSnapRecipient methods
    @Override
    public void onBackClick() {
        this.selectRecipientView.popFragment();
    }


    //Private methods
    private void configureViews() {
        this.selectRecipientView.showStatusBar();
        this.setNotificationColor();
    }


    private void setNotificationColor() {
        if(this.androidManager.getSdkCode() >= Build.VERSION_CODES.LOLLIPOP){
            this.selectRecipientView.setNotificationColor(R.color.action_bar_blue_color_dark);
        }
    }
}
