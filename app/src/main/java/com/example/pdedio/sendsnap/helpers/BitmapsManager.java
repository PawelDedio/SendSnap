package com.example.pdedio.sendsnap.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import org.androidannotations.annotations.EBean;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by pawel on 16.12.2016.
 */
@EBean
public class BitmapsManager {

    public Bitmap getBitmapFromFile(@NotNull File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        return this.createWithMatrix(bitmap, matrix);
    }

    public Bitmap rotateAndScale(Bitmap bitmap, float degrees, float sx, float sy) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        matrix.preScale(sx, sy);

        return this.createWithMatrix(bitmap, matrix);
    }

    public Bitmap createWithMatrix(Bitmap bitmap, Matrix matrix) {
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
