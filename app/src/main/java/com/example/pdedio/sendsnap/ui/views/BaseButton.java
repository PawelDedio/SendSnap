package com.example.pdedio.sendsnap.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import org.androidannotations.annotations.EView;

/**
 * Created by p.dedio on 03.11.16.
 */
@EView
public class BaseButton extends Button {


    public BaseButton(Context context) {
        super(context);
    }

    public BaseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
