package com.example.pdedio.sendsnap.jobs;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.example.pdedio.sendsnap.communication.CommunicationService;
import com.example.pdedio.sendsnap.communication.CommunicationService_;
import com.example.pdedio.sendsnap.database.SnapDB;
import com.example.pdedio.sendsnap.helpers.SessionManager;
import com.example.pdedio.sendsnap.helpers.SessionManager_;
import com.example.pdedio.sendsnap.models.BaseSnapModel;
import com.example.pdedio.sendsnap.models.User;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by pawel on 05.03.2017.
 */

public abstract class BaseSnapJob extends Job {

    protected CommunicationService communicationService;

    protected SessionManager sessionManager;



    protected BaseSnapJob(Params params, Context context) {
        super(params);

        this.communicationService = CommunicationService_.getInstance_(context);
        this.sessionManager = SessionManager_.getInstance_(context);
    }

    protected void dropDbIfNewUser(User user) {
        User oldUser = this.sessionManager.getLoggedUser();

        if(oldUser == null || !user.id.equals(oldUser.id)) {
           this.dropDb();
        }
    }

    protected void dropDb() {
        FlowManager.getDatabase(SnapDB.class).reset(this.getApplicationContext());
    }

    protected void callSuccessOnMainThread(final BaseSnapModel.OperationCallback callback, final BaseSnapModel model) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(callback != null) {
                    callback.onSuccess(model);
                }
            }
        });
    }

    protected void callFailureOnMainThread(final BaseSnapModel.OperationCallback callback, final BaseSnapModel.OperationError error) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(callback != null) {
                    callback.onFailure(error);
                }
            }
        });
    }

    protected void callCanceledOnMainThread(final BaseSnapModel.OperationCallback callback, final int reason, final Throwable throwable) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(callback != null) {
                    callback.onCanceled(reason, throwable);
                }
            }
        });
    }
}
