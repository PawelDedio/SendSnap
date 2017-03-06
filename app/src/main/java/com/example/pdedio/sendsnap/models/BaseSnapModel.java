package com.example.pdedio.sendsnap.models;

/**
 * Created by pawel on 26.02.2017.
 */

import android.content.Context;

import com.example.pdedio.sendsnap.helpers.ValidationHelper;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

public abstract class BaseSnapModel<T extends BaseSnapModel> extends BaseModel implements Serializable {


    protected ValidationHelper validationHelper;


    public BaseSnapModel() {
        this.validationHelper = new ValidationHelper();
    }

    public abstract boolean isValid(Context context);

    public abstract void save(Context context, OperationCallback<T> callback);


    public interface OperationCallback<M extends BaseSnapModel> {
        void onSuccess(M model);

        void onFailure(OperationError error);
    }

    public static class OperationError extends Throwable {

    }
}
