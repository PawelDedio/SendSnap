package com.example.pdedio.sendsnap;

import android.app.Application;
import android.content.Context;

import com.karumi.dexter.Dexter;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.androidannotations.annotations.EApplication;

/**
 * Created by p.dedio on 26.09.16.
 */
@EApplication
public class SendSnapApplication extends Application {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        Dexter.initialize(this);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        this.refWatcher = LeakCanary.install(this);

        FlowManager.init(new FlowConfig.Builder(this).openDatabasesOnInit(true).build());
    }

    public static RefWatcher getRefWatcher(Context context) {
        SendSnapApplication application = (SendSnapApplication) context.getApplicationContext();
        return application.refWatcher;
    }
}
