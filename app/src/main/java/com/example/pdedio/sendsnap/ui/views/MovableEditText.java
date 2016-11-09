package com.example.pdedio.sendsnap.ui.views;

import android.animation.Animator;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewPropertyAnimator;
import android.view.inputmethod.InputMethodManager;

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

    private float savedPosition;

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

    //TODO: Logic for moving view to destination after back click

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
                    this.clearFocusAndHideKeyboard();

                    float y = this.correctIfOverScreen(event.getRawY());

                    this.setTranslationY(y);
                }

                break;

            case MotionEvent.ACTION_UP :
                long currentTime = System.currentTimeMillis();
                if(currentTime - touchTime < TIME_TO_MOVE && (Math.abs(this.touchY - event.getRawY()) < DISTANCE_TO_MOVE)) {
                    this.startTyping(touchY);
                }

                if(this.isFocused()) {
                    return super.onTouchEvent(event);
                }

                break;
        }

        return true;
    }


    //Public methods
    public void startTyping(float position) {
        position = this.correctIfOverScreen(position);
        this.savedPosition = position;
        this.smoothMoveToCenter().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                requestFocus();
                setInputType(savedInputType);
                inputMethodManager.showSoftInput(MovableEditText.this, InputMethodManager.SHOW_IMPLICIT);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void stopTyping() {
        this.clearFocusAndHideKeyboard();
        this.smoothMoveToPosition(this.savedPosition);
    }


    //Private methods
    private void init(Context context, AttributeSet attrs) {
        this.savedInputType = this.getInputType();
    }

    private void clearFocusAndHideKeyboard() {
        this.clearFocus();
        this.setInputType(InputType.TYPE_NULL);
        this.inputMethodManager.hideSoftInputFromWindow(this.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private ViewPropertyAnimator smoothMoveToCenter() {
        int windowHeight = this. getContext().getResources().getDisplayMetrics().heightPixels;
        int position = (windowHeight - this.getHeight()) / 2;

        ViewPropertyAnimator animator = this.animate();
        return animator.translationY(position);
    }

    private void smoothMoveToPosition(float position) {
        ViewPropertyAnimator animator = this.animate();
        animator.translationY(position);
    }

    private float correctIfOverScreen(float requiredPosition) {
        int windowHeight = this.getContext().getResources().getDisplayMetrics().heightPixels;
        if(requiredPosition + this.getHeight() > windowHeight) {
            requiredPosition = windowHeight - this.getHeight();
        }

        return requiredPosition;
    }
}
