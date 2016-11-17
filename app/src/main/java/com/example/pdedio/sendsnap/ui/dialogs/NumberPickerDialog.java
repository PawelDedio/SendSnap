package com.example.pdedio.sendsnap.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.ui.views.BaseButton;
import com.example.pdedio.sendsnap.ui.views.BaseTextView;

/**
 * Created by p.dedio on 16.11.16.
 */

public class NumberPickerDialog extends AlertDialog {

    private NumberPicker numberPicker;

    private BaseTextView txvTitle;

    private BaseButton btnAccept;

    private View view;

    private ResultListener resultListener;


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


    //Public methods
    public void show() {
        super.show();
    }

    public void setTitle(int resId) {
        this.txvTitle.setText(resId);
    }

    public void setMinValue(int min) {
        this.numberPicker.setMinValue(min);
    }

    public void setMaxValue(int max) {
        this.numberPicker.setMaxValue(max);
    }

    public void setWrapSelectorWheel(boolean value) {
        this.numberPicker.setWrapSelectorWheel(value);
    }

    public NumberPicker getNumberPicker() {
        return this.numberPicker;
    }

    public void setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void setSelectedValue(int value) {
        this.numberPicker.setValue(value);
    }


    //Private methods
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = inflater.inflate(R.layout.dialog_number_picker, null);

        this.numberPicker = (NumberPicker) this.view.findViewById(R.id.npNumberPicker);
        this.txvTitle = (BaseTextView) this.view.findViewById(R.id.txvNumberPickerTitle);
        this.btnAccept = (BaseButton) this.view.findViewById(R.id.btnNumberPickerDialogPositive);

        this.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resultListener != null) {
                    int value = numberPicker.getValue();

                    resultListener.onValueSet(value);
                    dismiss();
                }
            }
        });

        this.setView(view);
    }


    //Interfaces
    public interface ResultListener {
        void onValueSet(int value) ;
    }
}
