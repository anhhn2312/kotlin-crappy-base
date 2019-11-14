package com.dinominator.extensions.permissionUtils;

import java.util.ArrayList;

class Callback {
    public interface SimpleCallback {
        /**
         * @param allPermissionsGranted true if all permissions are granted
         */
        void result(boolean allPermissionsGranted);

    }

    public interface FullCallback {
        /**
         * @param permissionsGranted       list of permission granted
         * @param permissionsDenied        list of permission denied
         * @param permissionsDeniedForever list of permission denied forever
         * @param permissionsAsked         list of permission asked
         */
        void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked);
    }

    public interface SmartCallback {
        /**
         * @param allPermissionsGranted        true if all permissions are granted
         * @param somePermissionsDeniedForever true if one of asked permissions are denied forever
         */
        void result(boolean allPermissionsGranted, boolean somePermissionsDeniedForever);
    }

    public interface AskAgainCallback {
        /**
         * @param response user response
         */
        void showRequestPermission(UserResponse response);

        interface UserResponse {

            /**
             * @param askAgain the response from the user if allow to ask again a permission
             */
            void result(boolean askAgain);

        }
    }
}
