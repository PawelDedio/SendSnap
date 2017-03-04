package com.example.pdedio.sendsnap.permissions;

/**
 * Created by pawel on 16.12.2016.
 */

public class PermissionRequest {

    private com.karumi.dexter.listener.PermissionRequest request;

    public PermissionRequest(com.karumi.dexter.listener.PermissionRequest request) {
        this.request = request;
    }

    public String getName() {
        if(this.request == null) {
            return null;
        }
        return this.request.getName();
    }
}
