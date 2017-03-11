package com.example.pdedio.sendsnap.jobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.models.BaseSnapModel;
import com.example.pdedio.sendsnap.models.BaseSnapModel.OperationError;
import com.example.pdedio.sendsnap.models.User;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by pawel on 11.03.2017.
 */

public class LogInUserJob extends BaseSnapJob {


    public static final int PRIORITY = Consts.JOB_PRIORITY_MAX;

    private User user;

    private WeakReference<BaseSnapModel.OperationCallback<User>> callback;


    public LogInUserJob(User user, Context context, BaseSnapModel.OperationCallback<User> callback) {
        super(new Params(PRIORITY).requireNetwork().overrideDeadlineToCancelInMs(TimeUnit.SECONDS.toMillis(10)), context);

        this.user = user;
        this.callback = new WeakReference(callback);
    }


    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Call<User> call = this.communicationService.signInUser(this.user);
        Response<User> response = call.execute();

        if(response.isSuccessful()) {
            final User loggedUser = response.body();

            this.dropDbIfNewUser(loggedUser);
            loggedUser.save();

            if(this.callback != null) {
                this.callSuccessOnMainThread(this.callback.get(), loggedUser);
            }
        } else {
            JSONObject object = new JSONObject(response.errorBody().string());
            this.user.mapErrorsFromJson(object, this.getApplicationContext());

            final OperationError<User> error = new OperationError<>(response, this.user);

            if(this.callback != null) {
                this.callFailureOnMainThread(this.callback.get(), error);
            }
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        if(this.callback != null) {
            this.callCanceledOnMainThread(callback.get(), cancelReason, throwable);
        }
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
