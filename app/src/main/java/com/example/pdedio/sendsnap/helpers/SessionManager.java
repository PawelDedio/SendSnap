package com.example.pdedio.sendsnap.helpers;

import com.example.pdedio.sendsnap.models.User;

import org.androidannotations.annotations.EBean;

/**
 * Created by pawel on 09.03.2017.
 */
@EBean
public class SessionManager {

    public User getLoggedUser() {
        return User.getSavedUser();
    }

    public void logOutUser() {
        User.getSavedUser().delete();
    }
}
