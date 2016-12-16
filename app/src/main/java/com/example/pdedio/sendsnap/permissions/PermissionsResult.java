package com.example.pdedio.sendsnap.permissions;

import com.karumi.dexter.MultiplePermissionsReport;

/**
 * Created by pawel on 16.12.2016.
 */

public class PermissionsResult {

    private MultiplePermissionsReport permissionsReport;

    public PermissionsResult(MultiplePermissionsReport report) {
        this.permissionsReport = report;
    }

    public boolean areAllPermissionGranted() {
        return this.permissionsReport.areAllPermissionsGranted();
    }

    public boolean isAnyPermissionPernamentDenied() {
        return this.permissionsReport.isAnyPermissionPermanentlyDenied();
    }
}
