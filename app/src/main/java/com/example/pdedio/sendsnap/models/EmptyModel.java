package com.example.pdedio.sendsnap.models;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pawel on 09.03.2017.
 */

public class EmptyModel extends BaseSnapModel {



    @Override
    public boolean isValid(Context context) {
        return false;
    }

    @Override
    public void save(Context context, OperationCallback callback) {

    }

    @Override
    public void update(Context context, OperationCallback callback) {

    }

    @Override
    public void mapErrorsFromJson(JSONObject json, Context context) throws JSONException {

    }
}
