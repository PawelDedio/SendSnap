package com.example.pdedio.sendsnap.helpers;

import org.androidannotations.annotations.EBean;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * Created by pawel on 25.02.2017.
 */
@EBean
public class ValidationHelper {

    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public boolean isNotEmpty(@Nullable String value) {

        return value != null && !value.isEmpty();
    }

    public boolean haveCorrectLength(@Nullable String value, int min, int max) {

        return value != null && value.length() >= min && value.length() <= max;
    }

    public boolean areValuesTheSame(@Nullable String value, @Nullable String confirmation) {

        if(value == null || confirmation == null) {
            return false;
        }


        return value.equals(confirmation);
    }

    public boolean isValidEmail(@Nullable String email) {

        if(email == null || email.isEmpty()) {
            return false;
        }

        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
}
