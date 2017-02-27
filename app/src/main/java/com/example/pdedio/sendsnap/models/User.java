package com.example.pdedio.sendsnap.models;

import android.content.Context;
import android.support.annotation.StringRes;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by pawel on 26.02.2017.
 */

public class User extends BaseModel {

    @SerializedName("name")
    public String name;

    @SerializedName("display_name")
    public String displayName;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("password_confirmation")
    public String passwordConfirmation;

    @SerializedName("terms_accepted")
    public boolean termsAccepted;

    @SerializedName("role")
    public String role;

    @SerializedName("auth_token")
    public String authToken;

    @SerializedName("token_expire_time")
    public Date tokenExpireTime;

    @SerializedName("created_at")
    public Date createdAt;

    @SerializedName("updated_at")
    public Date updatedAt;

    public String nameError;

    public String displayNameError;

    public String emailError;

    public String passwordError;

    public String passwordConfirmationError;

    public String termsAndConditionError;



    //Validation
    @Override
    public boolean isValid(Context context) {
        boolean isValid = true;

        isValid &= this.validateName(context);
        isValid &= this.validateDisplayName(context);
        isValid &= this.validateEmail(context);
        isValid &= this.validatePassword(context);
        isValid &= this.validatePasswordConfirmation(context);
        isValid &= this.validateTermsAndConditions(context);

        return isValid;
    }

    public boolean validateName(Context context) {
        if(this.validationHelper.isNotEmpty(this.name)) {
            if(!this.validationHelper.haveCorrectLength(this.name, Consts.USER_NAME_MIN_LENGTH, Consts.USER_NAME_MAX_LENGTH)) {
                this.nameError = context.getString(R.string.error_field_wrong_length, context.getString(R.string.user_name), Consts.USER_NAME_MIN_LENGTH, Consts.USER_NAME_MAX_LENGTH);
                return false;
            }
        } else {
            this.nameError = context.getString(R.string.error_field_blank, context.getString(R.string.user_name));
            return false;
        }

        this.nameError = null;
        return true;
    }

    public boolean validateDisplayName(Context context) {
        if(!this.validationHelper.isNotEmpty(this.displayName)) {
            return true;
        }

        if(!this.validationHelper.haveCorrectLength(this.displayName, Consts.USER_DISPLAY_NAME_MIN_LENGTH, Consts.USER_NAME_MAX_LENGTH)) {
            this.displayNameError = context.getString(R.string.error_field_wrong_length,
                    context.getString(R.string.user_name), Consts.USER_DISPLAY_NAME_MIN_LENGTH, Consts.USER_DISPLAY_NAME_MAX_LENGTH);

            return false;
        }

        this.displayNameError = null;
        return true;
    }

    public boolean validateEmail(Context context) {
        if(this.validationHelper.isNotEmpty(this.email)) {
            if(!this.validationHelper.isValidEmail(this.email)) {
                this.emailError = context.getString(R.string.error_field_invalid, context.getString(R.string.user_email));
                return false;
            }
        } else {
            this.emailError = context.getString(R.string.error_field_blank, context.getString(R.string.user_email));
            return false;
        }

        this.emailError = null;
        return true;
    }

    public boolean validatePassword(Context context) {
        if(createdAt != null) {
            return true;
        }

        if(!this.validationHelper.isNotEmpty(this.password)) {
            this.passwordError = context.getString(R.string.error_field_blank, context.getString(R.string.user_password));
            return false;
        }

        this.passwordError = null;
        return true;
    }

    public boolean validatePasswordConfirmation(Context context) {
        if(createdAt != null) {
            return true;
        }

        if(this.validationHelper.isNotEmpty(this.passwordConfirmation)) {
            if(!this.validationHelper.areValuesTheSame(this.password, this.passwordConfirmation)) {
                this.passwordConfirmationError = context.getString(R.string.error_passwords_not_match);
                return false;
            }
        } else {
            this.passwordConfirmationError = context.getString(R.string.error_field_blank, context.getString(R.string.user_password_confirmation));
            return false;
        }

        this.passwordConfirmationError = null;
        return true;
    }

    public boolean validateTermsAndConditions(Context context) {
        if(!this.termsAccepted) {
            this.termsAndConditionError = context.getString(R.string.error_terms_not_accepted);
            return false;
        }

        this.termsAndConditionError = null;
        return true;
    }
}
