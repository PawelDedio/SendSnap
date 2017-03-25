package com.example.pdedio.sendsnap.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.StyleRes;

/**
 * Created by pawel on 25.03.2017.
 */

public class TextInputDialog extends AlertDialog {
    protected TextInputDialog(Context context) {
        super(context);
    }

    protected TextInputDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected TextInputDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }
}
