package com.example.pdedio.sendsnap.models;

/**
 * Created by pawel on 26.02.2017.
 */

import android.content.Context;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.example.pdedio.sendsnap.BR;
import com.example.pdedio.sendsnap.helpers.ValidationHelper;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import retrofit2.Response;

public abstract class BaseSnapModel<T extends BaseSnapModel> extends BaseModel implements Serializable, Observable {


    protected ValidationHelper validationHelper;

    private transient PropertyChangeRegistry callbacks;


    public BaseSnapModel() {
        this.validationHelper = new ValidationHelper();
    }

    public abstract boolean isValid(Context context);

    public abstract void save(Context context, OperationCallback<T> callback);

    public abstract void update(Context context, OperationCallback<T> callback);

    public abstract void mapErrorsFromJson(JSONObject json, Context context) throws JSONException;


    //Observable methods
    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        if(this.callbacks == null) {
            this.callbacks = new PropertyChangeRegistry();
        }

        this.callbacks.add(onPropertyChangedCallback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        if(this.callbacks != null) {
            this.callbacks.remove(onPropertyChangedCallback);
        }
    }

    public void notifyChange() {
        if(this.callbacks != null) {
            this.callbacks.notifyCallbacks(this, BR._all, null);
        }
    }



    public interface OperationCallback<M extends BaseSnapModel> {
        void onSuccess(M model);

        void onFailure(OperationError<M> error);

        void onCanceled(int canceledReason, Throwable throwable);
    }

    public static class OperationError<M extends BaseSnapModel>{

        public Response<M> response;

        public M model;


        public OperationError(Response<M> response, M model) {
            this.response = response;
            this.model = model;
        }
    }
}
