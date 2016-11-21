package com.example.pdedio.sendsnap.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import org.androidannotations.annotations.EView;

/**
 * Created by p.dedio on 18.11.16.
 */
@EView
public class BaseViewPager extends ViewPager {

    private boolean isScrollingEnabled = true;


    public BaseViewPager(Context context) {
        super(context);
    }

    public BaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //Public methods
    public void setScrolling(boolean isScrollEnabled) {
        this.isScrollingEnabled = isScrollEnabled;
    }


    //Events
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isScrollingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        return this.isScrollingEnabled && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean executeKeyEvent(KeyEvent event) {
        return this.isScrollingEnabled && super.executeKeyEvent(event);
    }


    //Private methods
    private void init(Context context) {

    }
}
