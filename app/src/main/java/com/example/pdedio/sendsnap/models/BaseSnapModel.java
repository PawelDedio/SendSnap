package com.example.pdedio.sendsnap.models;

/**
 * Created by pawel on 26.02.2017.
 */

import android.content.Context;

import com.example.pdedio.sendsnap.helpers.ValidationHelper;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import retrofit2.Response;

public abstract class BaseSnapModel<T extends BaseSnapModel> extends BaseModel implements Serializable {


    protected ValidationHelper validationHelper;


    public BaseSnapModel() {
        this.validationHelper = new ValidationHelper();
    }

    public abstract boolean isValid(Context context);

    public abstract void save(Context context, OperationCallback<T> callback);

    public abstract void mapErrorsFromJson(JSONObject json, Context context) throws JSONException;



    public interface OperationCallback<M extends BaseSnapModel> {
        void onSuccess(M model);

        void onFailure(OperationError<M> error);
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
