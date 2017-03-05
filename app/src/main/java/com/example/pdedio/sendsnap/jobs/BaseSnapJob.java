package com.example.pdedio.sendsnap.jobs;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;

/**
 * Created by pawel on 05.03.2017.
 */

public abstract class BaseSnapJob extends Job {


    protected BaseSnapJob(Params params) {
        super(params);
    }
}
