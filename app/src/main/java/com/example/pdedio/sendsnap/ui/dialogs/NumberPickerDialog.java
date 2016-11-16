package com.example.pdedio.sendsnap.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import com.example.pdedio.sendsnap.R;

/**
 * Created by p.dedio on 16.11.16.
 */

public class NumberPickerDialog extends AlertDialog {


    public NumberPickerDialog(Context context) {
        super(context);
        this.init(context);
    }

    public NumberPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.init(context);
    }

    protected NumberPickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.init(context);
    }


    //Private methods
    private void init(Context context) {

    }

    public void show() {
        super.show();
        this.setContentView(R.layout.dialog_number_picker);
    }
}
