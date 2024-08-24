package com.mona.geofencing.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;

import com.mona.geofencing.R;
import com.mona.geofencing.helper.StringHelper;
import com.mona.geofencing.model.SendNotification;
import com.mona.geofencing.repository.ChildRepository;
import com.mona.geofencing.repository.PolygonRepository;
import com.mona.geofencing.utils.Contstants;
import com.mona.geofencing.utils.FakeGpsDetector;
import com.mona.geofencing.utils.TokenUtil;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.android.PolyUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LocationService extends Service {

    private static final String TAG = "LocationService";
    private PolygonRepository repository;
    private ChildRepository childRepository;
    private MutableLiveData<List<String>> parentFcmTokensLiveData = new MutableLiveData<>();
    private MutableLiveData<Map<String, List<LatLng>>> polygonPoints = new MutableLiveData<>();
    private LocationListener locationListener;
    private Boolean lastStatus = null;
    private String currentPolygonName = "";
    private FirebaseAuth mAuth;
    private List<String> fcmTokens = new ArrayList<>();

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
                Location location = locationResult.getLastLocation();
                if (FakeGpsDetector.isFakeGps(getApplicationContext(), location)) {
                    Log.d(TAG, "onLocationResult: Fake GPS detected");
                    Toast.makeText(getApplicationContext(), "Fake GPS detected", Toast.LENGTH_SHORT)
                            .show();
                    parentFcmTokensLiveData.observeForever(parentFcmTokens -> {
                        if (parentFcmTokens != null) {
                            fcmTokens = parentFcmTokens;
                            String email = mAuth.getCurrentUser().getEmail();
                            String childName = StringHelper.usernameFromEmail(email);
                            String body =
                                    "[ " + new Date() + " ]" + " : Anak anda " + childName + " menggunakan Fake GPS";
                            for (String parentFcmToken : parentFcmTokens) {
                                new SendNotificationTask(parentFcmToken, "Fake GPS Detected",
                                        body
                                ).execute();
                            }
                        }
                        stopLocationService();
                        stopSelf();
//                        System.exit(0);

                    });
                    return;
                }

                Log.d(TAG, "onLocationResult: " + latitude + " " + longitude);

                repository.saveCoordinates(mAuth.getUid(), latitude, longitude);

                boolean insideAnyPolygon = false;
                String polygonName = "";
                Map<String, List<LatLng>> polygons = polygonPoints.getValue();
                if (polygons != null) {
                    for (Map.Entry<String, List<LatLng>> entry : polygons.entrySet()) {
                        List<LatLng> polygon = entry.getValue();
                        polygonName = entry.getKey();
                        if (PolyUtil.containsLocation(
                                currentLocation.latitude,
                                currentLocation.longitude,
                                polygon,
                                true
                        )) {
                            insideAnyPolygon = true;
                            break;
                        }
                    }
                }

                if (lastStatus == null || insideAnyPolygon != lastStatus || !polygonName.equals(
                        currentPolygonName)) {
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
        childRepository = new ChildRepository();
        repository.fetchPolygonData(polygonPoints);
        mAuth = FirebaseAuth.getInstance();
        childRepository.fetchParentFcmTokens(mAuth.getUid(), parentFcmTokensLiveData);
        parentFcmTokensLiveData.observeForever(parentFcmTokens -> {
            if (parentFcmTokens != null) {
                fcmTokens = parentFcmTokens;
                for (String parentFcmToken : parentFcmTokens) {
                    Log.d(TAG, "parent-fcm-token: " + parentFcmToken);
                }
            }
        });

        setLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(boolean inside, String name) {
                Log.d(TAG, "onLocationChanged: " + inside + " " + name);

                String email = mAuth.getCurrentUser().getEmail();
                Date now = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timestamp = formatter.format(now);

                String childName = StringHelper.usernameFromEmail(email);
                String body = generateNotificationBody(inside, timestamp, childName, name);
                String title = "Location Service";
                repository.saveLocationHistory(body);

                for (String parentFcmToken : fcmTokens) {
                    new SendNotificationTask(parentFcmToken, title, body).execute();
                }

            }
        });
    }

    private String generateNotificationBody(
            boolean inside,
            String timestamp,
            String childName,
            String name
    ) {
        if (inside) {
            return "[ " + timestamp + " ]" + " : Anak anda " + childName + " berada di dalam " + name;
        } else {
            return "[ " + timestamp + " ]" + " : Anak anda " + childName + " keluar dari area";
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startLocationService() {
        String channelId = "location_notification_channel";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setSmallIcon(R.drawable.mona);
        builder.setContentTitle("Location Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);

            }

            LocationRequest locationRequest = new LocationRequest();
//            locationRequest.setInterval(10000);
//            locationRequest.setFastestInterval(20000);
            locationRequest.setInterval(4000);
            locationRequest.setFastestInterval(2000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(this)
                    .requestLocationUpdates(locationRequest, locationCallback, null);
            startForeground(Contstants.LOCATION_SERVICE_ID, builder.build());
        }
    }

    private void stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
        new Thread(() -> {
            try {
                Thread.sleep(4000);
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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

    private class SendNotificationTask extends AsyncTask<Void, Void, String> {

        private String userFcmToken;
        private String title;
        private String body;

        public SendNotificationTask(String userFcmToken, String title, String body) {
            this.userFcmToken = userFcmToken;
            this.title = title;
            this.body = body;

        }

        @Override
        protected String doInBackground(Void... voids) {
            return TokenUtil.getAccessToken();
        }

        @Override
        protected void onPostExecute(String accessToken) {
            if (accessToken != null) {
                String userFcmToken = this.userFcmToken; // Replace with actual parent FCM token
                String title = this.title;
                String body = this.body;
                Log.d(TAG, "onPostExecute: " + userFcmToken);

                SendNotification sendNotification =
                        new SendNotification(accessToken, userFcmToken, title, body);
                sendNotification.sendNotification();
            } else {
                Log.e(TAG, "Failed to get access token");
            }
        }
    }
}
