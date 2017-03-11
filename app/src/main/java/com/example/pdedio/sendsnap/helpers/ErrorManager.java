package com.example.pdedio.sendsnap.helpers;

import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.example.pdedio.sendsnap.BaseContract;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.authorization.AuthActivity_;

import org.androidannotations.annotations.EBean;
import org.jetbrains.annotations.Nullable;

import retrofit2.Response;

/**
 * Created by pawel on 11.03.2017.
 */
@EBean
public class ErrorManager {


    public boolean serviceError(BaseContract.BaseView view, @Nullable Response response) {
        if(response == null) {
            view.showSnackbar(R.string.error_network, Snackbar.LENGTH_LONG);
            return true;
        } else if(response.code() == 401) {
            view.openActivity(AuthActivity_.class);
            view.showToast(R.string.error_session_expired, Toast.LENGTH_LONG);
            view.finishCurrentActivity();
            return true;
        } else if(response.code() == 500) {
            view.showSnackbar(R.string.error_server, Snackbar.LENGTH_LONG);
            return true;
        }
        return false;
    }
}
