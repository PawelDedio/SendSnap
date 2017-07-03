package com.example.pdedio.sendsnap.jobs.user;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.jobs.BaseSnapJob;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;

/**
 * Created by pawel on 05.06.2017.
 */

public class LogOutUserJob extends BaseSnapJob {

    public static final int PRIORITY = Consts.JOB_PRIORITY_MAX;


    public LogOutUserJob(Context context) {
        super(new Params(PRIORITY).requireNetwork().overrideDeadlineToCancelInMs(TimeUnit.SECONDS.toMillis(10)), context);
    }


    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        String token = null;
        if(this.sessionManager.getLoggedUser() != null) {
            token = this.sessionManager.getLoggedUser().authToken;
        }

        this.sessionManager.logOutUser();
        this.dropDb();
        Call<Void> call = this.communicationService.signOutUser(token);
        call.execute();
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
