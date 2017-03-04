package com.example.pdedio.sendsnap.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pawel on 16.12.2016.
 */
@EBean
public class PermissionManager {

    public boolean isHavePermission(Context context, String... permissions) {
        for(String permission : permissions) {
            int permissionResult = ContextCompat.checkSelfPermission(context, permission);
            if(permissionResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }

        return true;
    }

    public void requestForPermission(final PermissionCallback callback, String... permissions) {
        Dexter.checkPermissions(new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if(callback != null) {
                    PermissionsResult result = new PermissionsResult(report);
                    callback.onPermissionChecked(result);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                if(callback != null) {
                    List<PermissionRequest> requests = new ArrayList<>();
                    for(com.karumi.dexter.listener.PermissionRequest request : permissions) {
                        PermissionRequest newRequest = new PermissionRequest(request);
                        requests.add(newRequest);
                    }
                    PermissionSession session = new PermissionSession(token);

                    callback.onPermissionRationaleShouldBeShown(requests, session);
                }
            }
        }, permissions);
    }

    public interface PermissionCallback {
        void onPermissionChecked(PermissionsResult result);

        void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionSession session);
    }
}
