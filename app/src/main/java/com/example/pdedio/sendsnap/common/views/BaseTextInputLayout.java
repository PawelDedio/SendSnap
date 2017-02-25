package com.example.pdedio.sendsnap.common.views;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

import org.androidannotations.annotations.EViewGroup;

/**
 * Created by pawel on 24.02.2017.
 */
@EViewGroup
public class BaseTextInputLayout extends TextInputLayout {



    public BaseTextInputLayout(Context context) {
        super(context);
    }

    public BaseTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setError(@StringRes int stringRes) {
        this.setError(this.getContext().getString(stringRes));
    }

    public void setError(@StringRes int stringRes, Object... formatArgs) {
        this.setError(this.getContext().getString(stringRes, formatArgs));
    }
}
