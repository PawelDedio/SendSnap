package com.example.pdedio.sendsnap.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import org.androidannotations.annotations.EView;

/**
 * Created by p.dedio on 03.11.16.
 */
@EView
public class BaseTextView extends TextView {

    public BaseTextView(Context context) {
        super(context);
    }

    public BaseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
