package com.example.pdedio.sendsnap.models;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.SendSnapApplication;
import com.example.pdedio.sendsnap.database.SnapDB;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.helpers.ErrorStringMapper;
import com.example.pdedio.sendsnap.jobs.user.CreateUserJob;
import com.example.pdedio.sendsnap.jobs.user.LogInUserJob;
import com.example.pdedio.sendsnap.jobs.user.LogOutUserJob;
import com.example.pdedio.sendsnap.jobs.user.UpdateUserJob;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by pawel on 26.02.2017.
 */
@Table(database = SnapDB.class)
public class User extends BaseSnapModel<User> {

    @PrimaryKey
    @SerializedName("id")
    public String id;

    @Column
    @SerializedName("name")
    public String name;

    @Column
    @SerializedName("display_name")
    public String displayName;

    @Column
    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("password_confirmation")
    public String passwordConfirmation;

    @Column
    @SerializedName("terms_accepted")
    public boolean termsAccepted;

    @Column
    @SerializedName("role")
    public String role;

    @SerializedName("auth_token")
    public String authToken;

    @SerializedName("token_expire_time")
    public Date tokenExpireTime;

    @Column
    @SerializedName("created_at")
    public Date createdAt;

    @Column
    @SerializedName("updated_at")
    public Date updatedAt;

    public String nameError;

    public String displayNameError;

    public String emailError;

    public String passwordError;

    public String passwordConfirmationError;

    public String termsAndConditionError;



    public User() {

    }

    public User(String name, String displayName, String email, String password, String passwordConfirmation,
                boolean termsAccepted) {
        this.name = name;
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.termsAccepted = termsAccepted;
    }


    //BaseSnapModel methods
    @Override
    public void save(Context context, OperationCallback<User> callback) {
        JobManager manager = SendSnapApplication.getJobManager(context);

        manager.addJobInBackground(new CreateUserJob(this, context, callback));
    }

    @Override
    public void update(Context context, OperationCallback<User> callback) {
        JobManager manager = SendSnapApplication.getJobManager(context);

        manager.addJobInBackground(new UpdateUserJob(this, context, callback));
    }

    @Override
    public void mapErrorsFromJson(JSONObject json, Context context) throws JSONException {
        ErrorStringMapper errorStringMapper = new ErrorStringMapper();

        if(json.has("name")) {
            String error = json.getJSONArray("name").getString(0);
            this.nameError = errorStringMapper.mapCorrectString(error, context, R.string.user_name);
        }

        if(json.has("email")) {
            String error = json.getJSONArray("email").getString(0);
            this.emailError = errorStringMapper.mapCorrectString(error, context, R.string.user_email);
        }

        if(json.has("password")) {
            String error = json.getJSONArray("password").getString(0);
            this.passwordError = errorStringMapper.mapCorrectString(error, context, R.string.user_password);
        }

        if(json.has("password_confirmation")) {
            String error = json.getJSONArray("password_confirmation").getString(0);
            this.passwordConfirmationError = errorStringMapper.mapCorrectString(error, context, R.string.user_password_confirmation);
        }
    }


    //Public methods
    public void logIn(Context context, OperationCallback<User> callback) {
        JobManager manager = SendSnapApplication.getJobManager(context);

        manager.addJobInBackground(new LogInUserJob(this, context, callback));
    }

    public void logOut(Context context) {
        JobManager manager = SendSnapApplication.getJobManager(context);

        manager.addJobInBackground(new LogOutUserJob(context));
    }


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


    //Setters
    public void setName(String name) {
        this.name = name;
        this.notifyChange();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        this.notifyChange();
    }

    public void setEmail(String email) {
        this.email = email;
        this.notifyChange();
    }

    public void setPassword(String password) {
        this.password = password;
        this.notifyChange();
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
        this.notifyChange();
    }

    public void setTermsAccepted(boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
        this.notifyChange();
    }

    public void setRole(String role) {
        this.role = role;
        this.notifyChange();
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        this.notifyChange();
    }

    public void setTokenExpireTime(Date tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
        this.notifyChange();
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        this.notifyChange();
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        this.notifyChange();
    }
}
