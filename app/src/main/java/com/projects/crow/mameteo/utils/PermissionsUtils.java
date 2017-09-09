package com.projects.crow.mameteo.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venom on 08/09/2017.
 */

public class PermissionsUtils {

    private static final String TAG = "PermissionsUtils";

    private static Context mContext;
    private static EnhancedSharedPreferences mPreferences;

    public PermissionsUtils(Context context, EnhancedSharedPreferences preferences) {
        mContext = context;
        mPreferences = preferences;
    }

    private boolean canMakeSmore() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmore()) {
            return (mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    private boolean shouldWeAsk(String permission) {
        return (mPreferences.getBoolean(permission, true));
    }

    public void markAsAsked(String permission) {
        mPreferences.edit().putBoolean(permission, false).apply();
    }

    public ArrayList<String> findUnAskedPermissions(List<String> wanted) {
        ArrayList<String> results = new ArrayList<>();

        for (int i = 0; i < wanted.size(); i++) {
            String permission = wanted.get(i);
            if (!hasPermission(permission) && shouldWeAsk(permission)) {
                results.add(permission);
            }
        }
        return results;
    }

    public ArrayList<String> findUnacceptedPermissions(List<String> wanted) {
        ArrayList<String> results = new ArrayList<>();

        for (int i = 0; i < wanted.size(); i++) {
            String permission = wanted.get(i);
            if (!hasPermission(permission)) {
                results.add(permission);
            }
        }
        return results;
    }
}
