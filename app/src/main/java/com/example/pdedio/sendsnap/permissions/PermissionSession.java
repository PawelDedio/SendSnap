package com.example.pdedio.sendsnap.permissions;

import com.karumi.dexter.PermissionToken;

/**
 * Created by pawel on 16.12.2016.
 */

public class PermissionSession {

    private PermissionToken token;

    public PermissionSession(PermissionToken token) {
        this.token = token;
    }

    public void continuePermissionRequest() {
        if(this.token == null) {
            return;
        }

        this.token.continuePermissionRequest();
    }

    public void cancelPermissionRequest() {
        if(this.token == null) {
            return;
        }

        this.token.cancelPermissionRequest();
    }
}
