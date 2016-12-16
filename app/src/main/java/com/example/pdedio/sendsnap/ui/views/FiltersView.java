package com.example.pdedio.sendsnap.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.ui.activities.BaseFragmentActivity;
import com.example.pdedio.sendsnap.ui.adapters.VpFiltersAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by p.dedio on 21.11.16.
 */
@EViewGroup(R.layout.view_sliding_image_preview)
public class FiltersView extends LinearLayout {

    @ViewById(R.id.vpSlidingImagePreview)
    protected BaseViewPager vpMain;



    public FiltersView(Context context) {
        super(context);
    }

    public FiltersView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FiltersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //Lifecycle
    @AfterViews
    protected void afterViewsSlidingImagePreview() {
        this.showFilters();
    }


    //Public methods
    public void showFilters() {
        Context context = this.getContext();
        if(context instanceof BaseFragmentActivity) {
            BaseFragmentActivity activity = (BaseFragmentActivity) context;
            ArrayList<Bitmap> bitmaps = this.prepareBitmapsList(context);

            VpFiltersAdapter adapter = new VpFiltersAdapter(activity.getSupportFragmentManager());
            adapter.setBitmaps(bitmaps);

            this.vpMain.setAdapter(adapter);
            this.vpMain.setAlpha(0.1f);
            this.vpMain.setScrolling(true);
        } else {
            throw new IllegalStateException("Context must be from BaseFragmentActivity");
        }
    }

    public BaseViewPager getViewPager() {
        return this.vpMain;
    }


    //Private methods
    private ArrayList<Bitmap> prepareBitmapsList(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);

        Bitmap bitmap = Bitmap.createBitmap(metrics.widthPixels/10, metrics.heightPixels/10, null);

        Bitmap aweStruckVibeBitmap = Bitmap.createBitmap(bitmap);
        aweStruckVibeBitmap.eraseColor(Color.parseColor("#FF530D"));

        Bitmap blueMessBitmap = Bitmap.createBitmap(bitmap);
        blueMessBitmap.eraseColor(Color.parseColor("#00A3FF"));

        Bitmap pinkBitmap = Bitmap.createBitmap(bitmap);
        pinkBitmap.eraseColor(Color.parseColor("#FF66F7"));

        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(bitmap);
        bitmaps.add(aweStruckVibeBitmap);
        bitmaps.add(blueMessBitmap);
        bitmaps.add(pinkBitmap);

        return bitmaps;
    }
}
