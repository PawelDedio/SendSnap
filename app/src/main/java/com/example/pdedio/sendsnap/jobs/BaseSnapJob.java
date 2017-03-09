package com.example.pdedio.sendsnap.jobs;

import android.content.Context;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.example.pdedio.sendsnap.communication.CommunicationService;
import com.example.pdedio.sendsnap.communication.CommunicationService_;
import com.example.pdedio.sendsnap.database.SnapDB;
import com.example.pdedio.sendsnap.models.User;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by pawel on 05.03.2017.
 */

public abstract class BaseSnapJob extends Job {

    protected CommunicationService communicationService;


    protected BaseSnapJob(Params params, Context context) {
        super(params);

        this.communicationService = CommunicationService_.getInstance_(context);
    }

    protected void dropDbIfNewUser(User user) {
        User oldUser = User.getSavedUser();

        if(oldUser != null && user.id.equals(oldUser.id)) {
           this.dropDb();
        }
    }

    protected void dropDb() {
        FlowManager.getDatabase(SnapDB.class).reset(this.getApplicationContext());
    }
}
