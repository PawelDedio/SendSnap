package com.example.pdedio.sendsnap.ui.views;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.SystemService;

/**
 * Created by p.dedio on 08.11.16.
 */
@EView
public class MovableEditText extends BaseEditText {

    private long touchTime;

    private float touchY;

    private int savedInputType;

    private static final int TIME_TO_MOVE = 500;

    private static final int DISTANCE_TO_MOVE = 10;

    @SystemService
    protected InputMethodManager inputMethodManager;


    public MovableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.init(context, null);
    }

    public MovableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.init(context, null);
    }

    public MovableEditText(Context context) {
        super(context);

        this.init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        this.savedInputType = this.getInputType();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN :
                this.touchTime = System.currentTimeMillis();
                this.touchY = event.getRawY();
                if(this.isFocused()) {
                    return super.onTouchEvent(event);
                }
                break;

            case MotionEvent.ACTION_MOVE :
                if(Math.abs(this.touchY - event.getRawY()) > DISTANCE_TO_MOVE) {
                    this.stopTyping();

                    int y = (int) event.getRawY();
                    int windowHeight = this.getContext().getResources().getDisplayMetrics().heightPixels;
                    if(y + this.getHeight() > windowHeight) {
                        y = windowHeight - this.getHeight();
                    }

                    ViewGroup.LayoutParams params = this.getLayoutParams();
                    if(params instanceof RelativeLayout.LayoutParams) {
                        ((RelativeLayout.LayoutParams) params).topMargin = y;
                    }

                    this.setLayoutParams(params);
                }

                break;

            case MotionEvent.ACTION_UP :
                long currentTime = System.currentTimeMillis();
                if(currentTime - touchTime < TIME_TO_MOVE && (Math.abs(this.touchY - event.getRawY()) < DISTANCE_TO_MOVE)) {
                    this.startTyping();
                }

                if(this.isFocused()) {
                    return super.onTouchEvent(event);
                }

                break;
        }

        return true;
    }

    public void setOnCenter() {
        int windowHeight = this.getContext().getResources().getDisplayMetrics().heightPixels;
        int margin = (windowHeight - this.getHeight()) / 2;

        ViewGroup.LayoutParams params = this.getLayoutParams();
        if(params instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) params).topMargin = margin;
        }

        this.setLayoutParams(params);
    }

    public void startTyping() {
        this.requestFocus();
        this.setInputType(this.savedInputType);
        this.inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
    }

    public void stopTyping() {
        this.clearFocus();
        this.setInputType(InputType.TYPE_NULL);
        this.inputMethodManager.hideSoftInputFromWindow(this.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
