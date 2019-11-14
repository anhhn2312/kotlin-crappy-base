package com.dinominator.extensions.permissionUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.ContextCompat;

public class PermissionUtils {

    /**
     * @param context    current context
     * @param permission permission to check
     * @return if permission is granted return true
     */
    public static boolean isGranted(Context context, PermissionEnum permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(context, permission.toString()) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @param context    current context
     * @param permission all permission you need to check
     * @return if one of permission is not granted return false
     */
    public static boolean isGranted(Context context, PermissionEnum... permission) {
        for (PermissionEnum permissionEnum : permission) {
            if (!isGranted(context, permissionEnum)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param packageName package name of your app
     * @return an intent to start for open settings app
     */
    public static Intent openApplicationSettings(String packageName) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        return intent;
    }

    /**
     * @param context     current context
     * @param packageName package name of your app
     */
    public static void openApplicationSettings(Context context, String packageName) {
        context.startActivity(openApplicationSettings(packageName));
    }
}
