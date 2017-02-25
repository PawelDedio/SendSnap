package com.example.pdedio.sendsnap.helpers;

import org.androidannotations.annotations.EBean;
import org.jetbrains.annotations.Nullable;

/**
 * Created by pawel on 25.02.2017.
 */
@EBean
public class ValidationHelper {

    public boolean isNotEmpty(@Nullable String value) {
        return true;
    }

    public boolean haveCorrectLength(@Nullable String value, int min, int max) {
        return true;
    }

    public boolean areValuesTheSame(@Nullable String value, @Nullable String confirmation) {
        return true;
    }

    public boolean isValidEmail(@Nullable String email) {
        return true;
    }
}
