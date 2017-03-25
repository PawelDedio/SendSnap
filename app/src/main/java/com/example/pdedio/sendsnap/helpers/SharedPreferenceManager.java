package com.example.pdedio.sendsnap.helpers;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.pdedio.sendsnap.BR;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by p.dedio on 17.11.16.
 * This class is for workaround android data binding error.
 * For now we can't set generated classes as a data binding variable.
 */
@EBean
public class SharedPreferenceManager extends BaseObservable {

    @RootContext
    protected Context context;

    @Pref
    public SharedPrefHelper_ sharedPrefHelper;

    @Bindable
    public Integer getSnapDuration() {
        return this.sharedPrefHelper.snapDuration().get();
    }

    public void setSnapDuration(Integer value) {
        this.sharedPrefHelper.snapDuration().put(value);

        this.notifyPropertyChanged(BR.snapDuration);
    }

    public Integer getCameraId() {
        return sharedPrefHelper.cameraId().get();
    }

    public void setCameraId(Integer value) {
        this.sharedPrefHelper.cameraId().put(value);
    }

    @Bindable
    public boolean getNotificationDisplay() {
        return this.sharedPrefHelper.notificationDisplay().get();
    }

    public void setNotificationDisplay(boolean value) {
        this.sharedPrefHelper.notificationDisplay().put(value);

        this.notifyPropertyChanged(BR.notificationDisplay);
    }

    @Bindable
    public boolean getNotificationLed() {
        return this.sharedPrefHelper.notificationLed().get();
    }

    public void setNotificationLed(boolean value) {
        this.sharedPrefHelper.notificationLed().put(value);

        this.notifyPropertyChanged(BR.notificationLed);
    }

    @Bindable
    public boolean getNotificationVibration() {
        return this.sharedPrefHelper.notificationVibration().get();
    }

    public void setNotificationVibration(boolean value) {
        this.sharedPrefHelper.notificationVibration().put(value);

        this.notifyPropertyChanged(BR.notificationVibration);
    }

    @Bindable
    public boolean getNotificationSound() {
        return this.sharedPrefHelper.notificationSound().get();
    }

    public void setNotificationSound(boolean value) {
        this.sharedPrefHelper.notificationSound().put(value);

        this.notifyPropertyChanged(BR.notificationSound);
    }
}
