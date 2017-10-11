package com.cnc.hcm.cnctracking.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.MainActivity;
import com.cnc.hcm.cnctracking.api.APIService;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.model.ItemTrackLocation;
import com.cnc.hcm.cnctracking.model.TrackLocation;
import com.cnc.hcm.cnctracking.model.UpdateLocationResponseStatus;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;
import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnGeofencingTransitionListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geofencing.utils.TransitionGeofence;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by giapmn on 9/14/17.
 */

public class GPSService extends Service implements OnLocationUpdatedListener, OnActivityUpdatedListener, OnGeofencingTransitionListener {
    public static final String ACTION_SHOW_APP = "ACTION_SHOW_APP";
    private static final int NOTIFI_ID = 111;
    private static final String TAGG = "GPSService";
    public static final String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final float MIN_DISTANCE_GET_GPS = 30.0f;
    private static final long MIN_TIME_GET_GPS = 15000L;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews remoteViews;
    private Notification mNotification;

    private boolean isPause;

    private ArrayList<TrackLocation> arrTrackLocation = new ArrayList<>();
    private double longitude;
    private double latitude;
    private float accuracy;
    private boolean isNetworkConnected;
    private int batteryLevel;

    private final Gson gson = new Gson();
    private APIService mService;

    private MainActivity mainActivity;
    private String body = "";
    private LocationGooglePlayServicesProvider provider;

    String addressName;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public GPSService getGPSService() {
            return GPSService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_STICKY;
        }
        boolean isUserLogin = UserInfo.getInstance(this).getIsLogin();
        String action = intent.getAction();
        if (action != null && !action.isEmpty()) {
            switch (action) {
                case ACTION_SHOW_APP:
                    if (isUserLogin) {
                        requestLocationUpdate();
                        Intent intent1 = new Intent(this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        Log.i(TAGG, "onStartCommand, ACTION_SHOW_APP");
                    } else {
                        stopSelf();
                        stopForeground(true);
                        return START_NOT_STICKY;
                    }
                    break;
            }
        }
        if (isUserLogin) {
            startThread();
            setupNotification();
            requestLocationUpdate();
        }
        Log.i(TAGG, "onStartCommand, UserLogin = " + isUserLogin);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcast);
        sendBroadcast(new Intent(Conts.ACTION_RESTART_SERVICE));

    }

    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {

    }

    @Override
    public void onGeofenceTransition(TransitionGeofence transitionGeofence) {

    }

    @Override
    public void onLocationUpdated(Location location) {
        if (UserInfo.getInstance(GPSService.this).getIsLogin())
            updateNotification(location.getLatitude() + ", " + location.getLongitude() + ", " + location.getAccuracy());

        // We are going to get the address for the current position
        SmartLocation.with(this).geocoding().reverse(location, new OnReverseGeocodingListener() {
            @Override
            public void onAddressResolved(Location original, List<Address> results) {
                if (results.size() > 0) {
                    Address result = results.get(0);
                    StringBuilder builder = new StringBuilder();
                    List<String> addressElements = new ArrayList<>();
                    for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                        addressElements.add(result.getAddressLine(i));
                    }
                    builder.append(TextUtils.join(", ", addressElements));
                    addressName = builder.toString();
                    if (mainActivity != null) {
                        mainActivity.myLocation(original.getLatitude(), original.getLongitude(), original.getAccuracy(), addressName);
                    }
                }
            }
        });

        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());
        setAccuracy(location.getAccuracy());

        if (!isPause && UserInfo.getInstance(GPSService.this).getIsLogin() && longitude != 0 && latitude != 0) {
            isNetworkConnected = getNetworkConntected();
            if (isNetworkConnected) {
                Log.d(TAGG, "onLocationUpdated " + "ADD_GPS: " + latitude + ", " + longitude + ", " + accuracy);

                TrackLocation trackLocation = new TrackLocation(latitude, longitude, System.currentTimeMillis(), accuracy);
                arrTrackLocation.add(trackLocation);

                int size = arrTrackLocation.size();
                if (size >= 5) {
                    Log.d(TAGG, "onLocationUpdated " + "POST_GPS_TO_SERVER");
                    ItemTrackLocation itemTrackLocation = new ItemTrackLocation(arrTrackLocation, batteryLevel);
                    updateLocation(itemTrackLocation);
                }

            } else {
                Log.d(TAGG, "onLocationUpdated " + "BACK_UP_GPS");
                TrackLocation trackLocation = new TrackLocation(latitude, longitude, System.currentTimeMillis(), accuracy);
                arrTrackLocation.add(trackLocation);
            }

        } else {
            updateNotification("GPS not found");
            Log.d(TAGG, "onLocationUpdated " + "GPS_NOT_FOUND");
        }

    }


    public void requestLocationUpdate() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String permissions[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                if (mainActivity != null) {
                    mainActivity.requestPermissions(permissions, MainActivity.REQUEST_CODE_LOCATION_UPDATE);
                }
            }
        } else {
            startLocation();
        }
    }

    private void checkGPSOnOff() {
        if (mainActivity != null) {
            boolean statusGPS = SmartLocation.with(mainActivity).location().state().isAnyProviderAvailable();
            mainActivity.setTitleStatusGPS(statusGPS);
        }
    }

    private void startLocation() {
        LocationParams params = new LocationParams.Builder().setAccuracy(LocationAccuracy.HIGH).setDistance(MIN_DISTANCE_GET_GPS).setInterval(MIN_TIME_GET_GPS).build();
        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();
        smartLocation.location(provider).config(params).start(this);
    }


    public void updateLocation(ItemTrackLocation location) {
        isPause = true;
        Log.d(TAGG, "onLocationUpdated, body: " + location);
        mService.updateLocation(location).enqueue(new Callback<UpdateLocationResponseStatus>() {
            @Override
            public void onResponse(Call<UpdateLocationResponseStatus> call, Response<UpdateLocationResponseStatus> response) {
                int statusCode = response.code();
                Log.d(TAGG, "onLocationUpdated.onResponse(), statusCode: " + statusCode);
                if (response.isSuccessful()) {
                    UpdateLocationResponseStatus updateLocation = response.body();
                    if (updateLocation != null) {
                        int updateLocationCode = updateLocation.getStatusCode();
                        if (updateLocationCode == Conts.RESPONSE_STATUS_OK) {
                            arrTrackLocation.clear();
                            isPause = false;
                            body = gson.toJson(response.body());
                        } else if (updateLocationCode == Conts.RESPONSE_STATUS_TOKEN_WRONG) {
                            UserInfo.getInstance(GPSService.this).setUserLoginOnOtherDevice(true);
                            updateNotification("Tài khoản này đã được đăng nhập trên thiết bị khác");
                            if (mainActivity != null && UserInfo.getInstance(GPSService.this).getMainActivityActive()) {
                                mainActivity.showMessageRequestLogout();
                            }
                        }
                    }

                    Log.d(TAGG, "onLocationUpdated.onResponse().isSuccessful(), body: " + body);
                } else {
                    body = gson.toJson(response.body());
                    Log.d(TAGG, "onLocationUpdated.onResponse().isFail(), body: " + body);
                }
            }

            @Override
            public void onFailure(Call<UpdateLocationResponseStatus> call, Throwable t) {
                Log.e(TAGG, "updateLocation.onFailure()");
                isPause = false;
            }
        });
    }

    private BroadcastReceiver mBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_BATTERY_CHANGED:
                    batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                    break;
                case ACTION_NETWORK_CHANGE:
                    isNetworkConnected = getNetworkConntected();
                    break;
            }

        }
    };

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(ACTION_NETWORK_CHANGE);
        registerReceiver(mBroadcast, intentFilter);

    }

    private boolean getNetworkConntected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfor = connectivityManager.getActiveNetworkInfo();
        return (activeNetInfor != null && activeNetInfor.isConnectedOrConnecting());
    }


    public void startThread() {
        registerBroadcast();
        String token = UserInfo.getInstance(this).getAccessToken();
        mService = ApiUtils.getAPIService(token);
    }


    private void setupNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intentOpen = new Intent(ACTION_SHOW_APP);
        intentOpen.setClass(this, GPSService.class);
        PendingIntent piOpen = PendingIntent.getService(this, 0, intentOpen, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews = new RemoteViews(getPackageName(), R.layout.item_3button_small);
        remoteViews.setTextViewText(R.id.notif_content, "" + latitude + ", " + longitude + ", " + accuracy);

        remoteViews.setOnClickPendingIntent(R.id.notif_icon, piOpen);

        mBuilder = new NotificationCompat.Builder(this);

        CharSequence ticker = "Tracking location";
        int apiVersion = Build.VERSION.SDK_INT;

        if (apiVersion < Build.VERSION_CODES.HONEYCOMB) {

            mNotification = new Notification(R.mipmap.ic_launcher, ticker, System.currentTimeMillis());
            mNotification.contentView = remoteViews;
            mNotification.contentIntent = piOpen;
            mNotification.flags |= Notification.FLAG_NO_CLEAR;
            mNotification.defaults |= Notification.DEFAULT_LIGHTS;
            startForeground(NOTIFI_ID, mNotification);

        } else if (apiVersion >= Build.VERSION_CODES.HONEYCOMB) {

            mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentIntent(piOpen)
                    .setContent(remoteViews)
                    .setTicker(ticker);
            startForeground(NOTIFI_ID, mBuilder.build());
        }

    }

    public void updateNotification(String content) {
        int api = Build.VERSION.SDK_INT;

        remoteViews.setTextViewText(R.id.notif_content, content);

        if (api < Build.VERSION_CODES.HONEYCOMB) {
            notificationManager.notify(NOTIFI_ID, mNotification);
        } else if (api >= Build.VERSION_CODES.HONEYCOMB) {
            notificationManager.notify(NOTIFI_ID, mBuilder.build());
        }

    }


    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
}
