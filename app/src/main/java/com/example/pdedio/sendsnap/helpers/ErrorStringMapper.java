package com.example.pdedio.sendsnap.helpers;

import android.content.Context;
import android.support.annotation.StringRes;

import com.example.pdedio.sendsnap.R;

import org.androidannotations.annotations.EBean;

/**
 * Created by pawel on 09.03.2017.
 */
@EBean
public class ErrorStringMapper {

    public String mapCorrectString(String apiError, Context context, @StringRes int fieldNameId) {
        switch (apiError) {
            case "has already been taken" :
                return context.getString(R.string.error_field_taken, context.getString(fieldNameId));
            default:
                return null;
        }
    }
}
