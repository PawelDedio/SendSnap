package com.example.pdedio.sendsnap.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import org.androidannotations.annotations.EView;

/**
 * Created by p.dedio on 03.11.16.
 */
@EView
public class BaseEditText extends EditText {

    public BaseEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseEditText(Context context) {
        super(context);
    }
}
