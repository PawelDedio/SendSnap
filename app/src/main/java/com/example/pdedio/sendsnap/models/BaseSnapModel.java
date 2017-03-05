package com.example.pdedio.sendsnap.models;

/**
 * Created by pawel on 26.02.2017.
 */

import android.content.Context;

import com.example.pdedio.sendsnap.helpers.ValidationHelper;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

public abstract class BaseSnapModel extends BaseModel implements Serializable {


    protected ValidationHelper validationHelper;


    public BaseSnapModel() {
        this.validationHelper = new ValidationHelper();
    }

    public abstract boolean isValid(Context context);

    public abstract void save(Context context, OperationCallback<BaseSnapModel> callback);


    public interface OperationCallback<T extends BaseSnapModel> {
        void onSuccess(T model);

        void onFailure(OperationError error);
    }

    public static class OperationError extends Throwable {

    }
}
