package com.example.pdedio.sendsnap.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseButton;
import com.example.pdedio.sendsnap.common.views.BaseTextInputLayout;
import com.example.pdedio.sendsnap.common.views.BaseTextView;
import com.example.pdedio.sendsnap.helpers.ValidationHelper;
import com.example.pdedio.sendsnap.helpers.ValidationHelper_;

/**
 * Created by pawel on 25.03.2017.
 */

public class TextInputDialog extends AlertDialog {

    private BaseTextInputLayout tilText;

    private BaseTextView txvTitle;

    private BaseButton btnPositive;

    private BaseButton btnNegative;

    @StringRes
    private int fieldName;

    private int minLength = 0;

    private int maxLength = Integer.MAX_VALUE;

    private ResultListener resultListener;

    private ValidationHelper validationHelper;



    protected TextInputDialog(Context context) {
        super(context);
        this.init(context);
    }

    protected TextInputDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.init(context);
    }

    protected TextInputDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.init(context);
    }


    //Setters
    public void setInitialText(String text) {
        this.tilText.getEditText().setText(text);
    }

    public void setTitle(@StringRes int titleRes) {
        this.txvTitle.setText(titleRes);
    }

    public void setFieldName(@StringRes int fieldName) {
        this.fieldName = fieldName;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }


    //Private methods
    private void init(Context context) {
        this.validationHelper = ValidationHelper_.getInstance_(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_text_input, null);

        this.txvTitle = (BaseTextView) view.findViewById(R.id.txvTextInputTitle);
        this.tilText = (BaseTextInputLayout) view.findViewById(R.id.tilTextInput);

        this.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()) {
                    if(resultListener != null) {
                        resultListener.onValueSet(tilText.getEditText().getText().toString());
                    }

                    dismiss();
                } else {
                    showError();
                }
            }
        });

        this.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        this.setView(view);
    }

    private boolean validateInput() {
        int textLength = this.tilText.getEditText().getText().length();

        return textLength >= this.minLength && textLength <= this.maxLength;
    }

    private void showError() {
        String fieldName = this.getContext().getString(this.fieldName);
        String error = this.getContext().getString(R.string.error_field_wrong_length, fieldName,
                this.minLength, this.maxLength);

        this.tilText.setError(error);
    }


    //Interfaces
    public interface ResultListener {
        void onValueSet(String value);
    }
}
