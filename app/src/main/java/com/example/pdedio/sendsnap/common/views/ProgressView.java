package com.example.pdedio.sendsnap.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.pdedio.sendsnap.R;
import com.github.glomadrian.roadrunner.IndeterminateRoadRunner;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by pawel on 11.03.2017.
 */
@EViewGroup(R.layout.view_progress)
public class ProgressView extends RelativeLayout {


    @ViewById(R.id.vProgressView)
    protected IndeterminateRoadRunner progressView;



    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
