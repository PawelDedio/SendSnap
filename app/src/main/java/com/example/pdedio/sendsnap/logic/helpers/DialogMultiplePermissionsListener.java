package com.example.pdedio.sendsnap.logic.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.EmptyMultiplePermissionsListener;

/**
 * Created by p.dedio on 26.09.16.
 */

public class DialogMultiplePermissionsListener extends EmptyMultiplePermissionsListener {

    private final Context context;
    private final String title;
    private final String message;
    private final String positiveButtonText;
    private final Drawable icon;
    private DialogInterface.OnClickListener buttonListener;

    private DialogMultiplePermissionsListener(Context context, String title,
                                              String message, String positiveButtonText, Drawable icon,
                                              DialogInterface.OnClickListener buttonListener) {
        this.context = context;
        this.title = title;
        this.message = message;
        this.positiveButtonText = positiveButtonText;
        this.icon = icon;
        this.buttonListener = buttonListener;
    }

    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
        super.onPermissionsChecked(report);

        if (!report.areAllPermissionsGranted()) {
            showDialog();
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, this.buttonListener)
                .setIcon(icon)
                .show();
    }


    public static class Builder {
        private final Context context;
        private String title;
        private String message;
        private String buttonText;
        private Drawable icon;
        private DialogInterface.OnClickListener buttonListener;

        private Builder(Context context) {
            this.context = context;
        }

        public static DialogMultiplePermissionsListener.Builder withContext(Context context) {
            return new DialogMultiplePermissionsListener.Builder(context);
        }

        public DialogMultiplePermissionsListener.Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public DialogMultiplePermissionsListener.Builder withTitle(@StringRes int resId) {
            this.title = context.getString(resId);
            return this;
        }

        public DialogMultiplePermissionsListener.Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public DialogMultiplePermissionsListener.Builder withMessage(@StringRes int resId) {
            this.message = context.getString(resId);
            return this;
        }

        public DialogMultiplePermissionsListener.Builder withButtonText(String buttonText) {
            this.buttonText = buttonText;
            return this;
        }

        public DialogMultiplePermissionsListener.Builder withButtonText(@StringRes int resId) {
            this.buttonText = context.getString(resId);
            return this;
        }

        public DialogMultiplePermissionsListener.Builder withIcon(Drawable icon) {
            this.icon = icon;
            return this;
        }

        public DialogMultiplePermissionsListener.Builder withIcon(@DrawableRes int resId) {
            this.icon = context.getResources().getDrawable(resId);
            return this;
        }

        public DialogMultiplePermissionsListener.Builder withButtonListener(DialogInterface.OnClickListener listener) {
            this.buttonListener = listener;
            return this;
        }

        public DialogMultiplePermissionsListener build() {
            String title = this.title == null ? "" : this.title;
            String message = this.message == null ? "" : this.message;
            String buttonText = this.buttonText == null ? "" : this.buttonText;
            return new DialogMultiplePermissionsListener(context, title, message, buttonText, icon, buttonListener);
        }
    }
}
