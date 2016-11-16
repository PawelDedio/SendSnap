package com.example.pdedio.sendsnap.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.pdedio.sendsnap.R;

import org.androidannotations.annotations.EView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by p.dedio on 10.11.16.
 */
@EView
public class DrawingView extends View {

    private Path drawPath;

    private Paint drawPaint;

    private Paint canvasPaint;

    private int paintColor;

    private Canvas drawCanvas;

    private Bitmap canvasBitmap;

    private boolean isDrawingEnabled;

    private List<Path> paths = new ArrayList<>();

    private List<Paint> paints = new ArrayList<>();

    private int strokeWidth;





    public DrawingView(Context context) {
        super(context);
        this.init(context, null);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }


    //Events
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.canvasBitmap = Bitmap.createBitmap(w ,h, Bitmap.Config.ARGB_8888);
        this.drawCanvas = new Canvas(this.canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.canvasBitmap, 0, 0, this.canvasPaint);

        for(int i = 0; i < this.paths.size(); i++) {
            Path path = this.paths.get(i);
            Paint paint = this.paints.get(i);

            canvas.drawPath(path, paint);
        }

        canvas.drawPath(this.drawPath, this.drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(this.isDrawingEnabled) {
            float touchX = event.getX();
            float touchY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    this.drawPath.moveTo(touchX, touchY);
                    this.drawPaint = this.buildPaint();
                    this.paints.add(this.drawPaint);
                    break;
                case MotionEvent.ACTION_MOVE:
                    this.drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    //this.drawCanvas.drawPath(this.drawPath, this.drawPaint);
                    this.paths.add(this.drawPath);
                    this.drawPath = new Path();
                    break;
                default:
                    return false;
            }

            invalidate();
            return true;
        }

        return false;
    }


    //Public methods
    public void startDrawing() {
        this.isDrawingEnabled = true;
    }

    public void stopDrawing() {
        this.isDrawingEnabled = false;
    }

    public void setColor(int color) {
        this.paintColor = color;
    }

    public boolean isDrawingEnabled() {
        return this.isDrawingEnabled;
    }

    public void undoLastChange() {
        if(this.paths.size() > 0) {
            this.paths.remove(this.paths.size() - 1);
            this.paints.remove(this.paints.size() - 1);
            this.invalidate();
        }
    }

    public void clearArea() {
        this.paths.clear();
        this.invalidate();
    }

    public int getCurrentColor() {
        return this.paintColor;
    }


    //Private methods
    private void init(Context context, AttributeSet attrs) {
        this.strokeWidth = context.getResources().getDimensionPixelSize(R.dimen.edit_snap_paint_stroke);

        this.drawPath = new Path();

        this.canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    private Paint buildPaint() {
        Paint paint = new Paint();
        paint.setStrokeWidth(this.strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(this.paintColor);

        return paint;
    }
}
