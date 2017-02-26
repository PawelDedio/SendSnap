package com.example.pdedio.sendsnap.models;

/**
 * Created by pawel on 26.02.2017.
 */

import android.content.Context;

import com.example.pdedio.sendsnap.helpers.ValidationHelper;

import java.io.Serializable;

public abstract class BaseModel implements Serializable {


    protected ValidationHelper validationHelper;


    public BaseModel() {
        this.validationHelper = new ValidationHelper();
    }

    public abstract boolean isValid(Context context);
}
