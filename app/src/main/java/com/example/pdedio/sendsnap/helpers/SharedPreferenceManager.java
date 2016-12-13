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

        this.notifyPropertyChanged(BR._all);
    }


}
