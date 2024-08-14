package com.example.geofencing.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.geofencing.R;
import com.example.geofencing.repository.PolygonRepository;
import com.example.geofencing.utils.Contstants;
import com.example.geofencing.viewmodel.PolygonViewModel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;
import java.util.Map;

public class LocationService extends Service {

    private static final String TAG = "LocationService";
    private PolygonRepository repository;
    private MutableLiveData<Map<String, List<LatLng>>> polygonPoints = new MutableLiveData<>();
    private LocationListener locationListener;
    private Boolean lastStatus = null;
    private String currentPolygonName = "";

    public interface LocationListener {
        void onLocationChanged(boolean inside, String name);
    }

    private void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null && locationResult.getLastLocation() != null) {
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                LatLng currentLocation = new LatLng(latitude, longitude);

                boolean insideAnyPolygon = false;
                String polygonName = "";
                Map<String, List<LatLng>> polygons = polygonPoints.getValue();
                if (polygons != null) {
                    for (Map.Entry<String, List<LatLng>> entry : polygons.entrySet()) {
                        List<LatLng> polygon = entry.getValue();
                        polygonName = entry.getKey();
                        if (PolyUtil.containsLocation(currentLocation.latitude, currentLocation.longitude, polygon, true)) {
                            insideAnyPolygon = true;
                            break;
                        }
                    }
                }

                if (lastStatus == null || insideAnyPolygon != lastStatus || !polygonName.equals(currentPolygonName)) {
                    if (locationListener != null) {
                        locationListener.onLocationChanged(insideAnyPolygon, polygonName);
                    }
                    lastStatus = insideAnyPolygon;
                    currentPolygonName = polygonName;
                }

            }

        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        repository = new PolygonRepository();
        repository.fetchPolygonData(polygonPoints);

        setLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(boolean inside, String name) {
                Log.d(TAG, "onLocationChanged: " + inside + " " + name);
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startLocationService() {
        String channelId = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setSmallIcon(R.drawable.mona);
        builder.setContentTitle("Location Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId, "Location Service", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("This channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);

            }

            LocationRequest locationRequest = new LocationRequest();
//            locationRequest.setInterval(10000);
//            locationRequest.setFastestInterval(20000);
            locationRequest.setInterval(4000);
            locationRequest.setFastestInterval(2000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, null);
            startForeground(Contstants.LOCATION_SERVICE_ID, builder.build());
        }
    }

    private void stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(Contstants.ACTION_START_LOCATION_SERVICE)) {
                    startLocationService();
                } else if (action.equals(Contstants.ACTION_STOP_LOCATION_SERVICE)) {
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
