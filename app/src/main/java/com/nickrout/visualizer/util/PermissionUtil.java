package com.nickrout.visualizer.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import static android.os.Build.VERSION_CODES.M;

public class PermissionUtil {

    public static final int PERMISSION_RESULT_RECORD_AUDIO = 1;

    private PermissionUtil() {
    }

    public static boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < M) {
            return true;
        }
        if (context == null || TextUtils.isEmpty(permission)) {
            return false;
        }
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity, String permission, int resultCode) {
        if (Build.VERSION.SDK_INT < M) {
            return;
        }
        if (activity == null || TextUtils.isEmpty(permission)) {
            return;
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission}, resultCode);
    }
}
