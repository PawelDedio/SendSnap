package com.example.pdedio.sendsnap.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.models.BaseSnapModel;
import com.example.pdedio.sendsnap.models.User;

/**
 * Created by pawel on 05.03.2017.
 */

public class CreateUserJob extends BaseSnapJob {


    public static final int PRIORITY = Consts.JOB_PRIORITY_MAX;

    private User user;

    private BaseSnapModel.OperationCallback<User> callback;


    public CreateUserJob(User user, BaseSnapModel.OperationCallback<User> callback) {
        super(new Params(PRIORITY).requireNetwork());

        this.user = user;
        this.callback = callback;
    }


    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        //TODO: Implementation of making request
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
