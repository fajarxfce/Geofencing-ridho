package com.mona.geofencing.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

public class FakeGpsDetector {
    private static final String TAG = "FakeGpsDetector";

    public static boolean isMockLocationEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
    }

    public static boolean isFromMockProvider(Location location) {
        return location != null && location.isFromMockProvider();
    }

    public static boolean hasSuspiciousApps(Context context) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo : packages) {
            if (applicationInfo.packageName.contains("fakegps") || applicationInfo.packageName.contains("locationfaker")) {
                Log.d(TAG, "Suspicious app detected: " + applicationInfo.packageName);
                return true;
            }
        }
        return false;
    }

    public static boolean isLocationAccuracySuspicious(Location location) {
        return location != null && location.getAccuracy() > 100; // Example threshold
    }

    public static boolean isLocationSpeedSuspicious(Location location) {
        return location != null && location.getSpeed() > 100; // Example threshold
    }

    public static boolean isLocationProviderSuspicious(Location location) {
        return location != null && !location.getProvider().equals("gps");
    }

    public static boolean isFakeGps(Context context, Location location) {
        Log.d(TAG, "isFakeGps: isMockLocationEnabled: " + isMockLocationEnabled(context));
        Log.d(TAG, "isFakeGps: isFromMockProvider: " + isFromMockProvider(location));
        Log.d(TAG, "isFakeGps: hasSuspiciousApps: " + hasSuspiciousApps(context));
        Log.d(TAG, "isFakeGps: isLocationAccuracySuspicious: " + isLocationAccuracySuspicious(location));
        Log.d(TAG, "isFakeGps: isLocationSpeedSuspicious: " + isLocationSpeedSuspicious(location));
        Log.d(TAG, "isFakeGps: isLocationProviderSuspicious: " + isLocationProviderSuspicious(location));

        return isMockLocationEnabled(context) ||
                isFromMockProvider(location) ||
                hasSuspiciousApps(context);
    }
}
