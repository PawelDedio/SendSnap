package com.example.pdedio.sendsnap.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import org.androidannotations.annotations.EView;

/**
 * Created by p.dedio on 03.11.16.
 */
@EView
public class BaseImageButton extends ImageButton {

    public BaseImageButton(Context context) {
        super(context);
    }

    public BaseImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
