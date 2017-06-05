package com.example.pdedio.sendsnap.jobs.user;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.jobs.BaseSnapJob;
import com.example.pdedio.sendsnap.models.BaseSnapModel;
import com.example.pdedio.sendsnap.models.User;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by pawel on 02.06.2017.
 */

public class UpdateUserJob extends BaseSnapJob {

    public static final int PRIORITY = Consts.JOB_PRIORITY_MAX;

    private User user;

    private WeakReference<BaseSnapModel.OperationCallback<User>> callback;


    public UpdateUserJob(User user, Context context, BaseSnapModel.OperationCallback<User> callback) {
        super(new Params(PRIORITY).requireNetwork().overrideDeadlineToCancelInMs(TimeUnit.SECONDS.toMillis(10)), context);

        this.user = user;
        this.callback = new WeakReference(callback);
    }


    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Call<User> call = this.communicationService.updateUser(this.user);
        Response<User> response = call.execute();

        if(response.isSuccessful()) {
            final User savedUser = response.body();

            savedUser.update();

            if(this.callback != null) {
                this.callSuccessOnMainThread(this.callback.get(), savedUser);
            }
        } else {
            if(response.errorBody() != null && response.errorBody().string() != null && !response.errorBody().string().isEmpty()) {
                JSONObject object = new JSONObject(response.errorBody().string());
                this.user.mapErrorsFromJson(object, this.getApplicationContext());
            }

            final BaseSnapModel.OperationError<User> error = new BaseSnapModel.OperationError(response, this.user);

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
