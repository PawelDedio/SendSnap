package com.example.pdedio.sendsnap.common.views;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import org.androidannotations.annotations.EView;

/**
 * Created by p.dedio on 03.11.16.
 */
@EView
public class BaseEditText extends AppCompatEditText {

    public BaseEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseEditText(Context context) {
        super(context);
    }


    public void setError(@StringRes int stringRes) {
        this.setError(this.getContext().getString(stringRes));
    }
}
