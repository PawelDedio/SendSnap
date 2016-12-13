package com.example.pdedio.sendsnap.edit_snap;

import android.animation.Animator;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.inputmethod.InputMethodManager;

import com.example.pdedio.sendsnap.common.views.BaseEditText;

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

    private boolean isTyping;


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

    //Events
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
                    this.isTyping = false;

                    float y = this.correctIfOverScreen(event.getRawY());

                    this.setTranslationY(y);
                }

                break;

            case MotionEvent.ACTION_UP :
                long currentTime = System.currentTimeMillis();
                if(currentTime - touchTime < TIME_TO_MOVE && (Math.abs(this.touchY - event.getRawY()) < DISTANCE_TO_MOVE) && !this.isTyping) {
                    this.startTyping(this.getTranslationY());
                }

                if(this.isFocused()) {
                    return super.onTouchEvent(event);
                }

                break;
        }

        return true;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if(this.isTyping) {
                this.stopTyping();
                return true;
            }
        }

        return false;
    }


    //Public methods
    public void startTypingFromCenter() {
        this.startTyping(this.getCenterPosition());
    }
    public void startTyping(float position) {
        if(this.getVisibility() == View.INVISIBLE) {
            this.setVisibility(View.VISIBLE);
        }
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
                setSelection(getText().length());
                isTyping = true;
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
        this.isTyping = false;
        if(getText().length() == 0) {
            clearFocusAndHideKeyboard();
            setVisibility(INVISIBLE);
        } else {
            this.smoothMoveToPosition(this.savedPosition);
            this.smoothMoveToPosition(this.savedPosition).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    clearFocusAndHideKeyboard();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    clearFocusAndHideKeyboard();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    public boolean isTyping() {
        return this.isTyping;
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
        float position = this.getCenterPosition();

        ViewPropertyAnimator animator = this.animate();
        return animator.translationY(position);
    }

    private ViewPropertyAnimator smoothMoveToPosition(float position) {
        ViewPropertyAnimator animator = this.animate();
        return animator.translationY(position);
    }

    private float getCenterPosition() {
        float windowHeight = this. getContext().getResources().getDisplayMetrics().heightPixels;
        float position = (windowHeight - this.getHeight()) / 2;

        return position;
    }

    private float correctIfOverScreen(float requiredPosition) {
        int windowHeight = this.getContext().getResources().getDisplayMetrics().heightPixels;
        if(requiredPosition + this.getHeight() > windowHeight) {
            requiredPosition = windowHeight - this.getHeight();
        }

        return requiredPosition;
    }
}
