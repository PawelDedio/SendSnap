package com.example.pdedio.sendsnap.select_recipient;

import android.support.annotation.ColorRes;

import com.example.pdedio.sendsnap.BaseFragmentContract;

/**
 * Created by p.dedio on 21.12.16.
 */

public class SelectSnapRecipientContract {

    public interface SelectSnapRecipientPresenter extends BaseFragmentContract.BaseFragmentPresenter {

        void init(SelectSnapRecipientView view);

        void onBackClick();
    }

    public interface SelectSnapRecipientView extends BaseFragmentContract.BaseFragmentView {

        void setNotificationColor(@ColorRes int colorId);
    }
}
