package com.cnc.hcm.cnctracking.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.MainActivity;
import com.cnc.hcm.cnctracking.activity.WorkDetailActivity;
import com.cnc.hcm.cnctracking.api.APIService;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.model.ItemTrackLocation;
import com.cnc.hcm.cnctracking.model.LocationBackupFile;
import com.cnc.hcm.cnctracking.model.LocationResponseUpload;
import com.cnc.hcm.cnctracking.model.LocationUploadSize;
import com.cnc.hcm.cnctracking.model.TrackLocation;
import com.cnc.hcm.cnctracking.model.UpdateLocationResponseStatus;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;
//import com.github.nkzawa.socketio.client.Ack;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.internal.bo;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by giapmn on 9/14/17.
 */

public class GPSService extends Service implements OnLocationUpdatedListener {
    private static final String ACTION_SHOW_APP = "ACTION_SHOW_APP";
    private static final String TAGG = "GPSService";
    private static final String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final int NOTIFI_ID = 111;
    private static final float MIN_DISTANCE_GET_GPS = 20f;
    private static final long MIN_TIME_GET_GPS = 1000L;
    private static final int MAXIMUM_PACKAGE = 100;
    private static final int MINXIMUM_PACKAGE = 5;


    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews remoteViews;
    private Notification mNotification;

    private ArrayList<TrackLocation> arrTrackLocation = new ArrayList<>();

    private Gson gson = new Gson();
    private APIService mService;
    private MainActivity mainActivity;
    private WorkDetailActivity workDetailActivity;
    //private LocationGooglePlayServicesProvider provider;

    private double longitude;
    private double latitude;
    private float accuracy;
    private int batteryLevel;
    private String body = "";

    private boolean isNetworkConnected;
    private Socket mSocket;
    private String addressName;
    private String cityName;
    private Thread mThreadUpload;
    private boolean isUploading;

    {
        try {
            mSocket = IO.socket(Conts.BASE_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

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
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerBroadcast();
        startThread();
        setupNotification();
        startLocation();
    }

    public void startLocation() {
        LocationParams params = new LocationParams.Builder().setAccuracy(LocationAccuracy.HIGH).setDistance(MIN_DISTANCE_GET_GPS).build();
//        provider = new LocationGooglePlayServicesProvider();
//        provider.setCheckLocationSettings(true);
//
//        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();
//        smartLocation.location(provider).config(params).start(this);
        SmartLocation.with(GPSService.this).location().config(params).start(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAGG, "Service on Destroy");
        disconnectSocket();
        unregisterReceiver(mBroadcast);
        SmartLocation.with(GPSService.this).location().stop();
        SmartLocation.with(this).geocoding().stop();
    }

    private static boolean saveLocationToFile(String fileName, Object item) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(fileName), true);
            BufferedWriter oos = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("UTF-8")));
            oos.write(item.toString());
            oos.newLine();
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private LocationBackupFile getItemBackupValue(Location location, boolean isNet) {
        LocationBackupFile itemBackup = new LocationBackupFile();
        itemBackup.setLatitude(location.getLatitude());
        itemBackup.setAccuracy(location.getAccuracy());
        itemBackup.setLongitude(location.getLongitude());
        itemBackup.setTimestamp(System.currentTimeMillis());
        itemBackup.setNetwork(isNet);
        return itemBackup;
    }

    @Override
    public void onLocationUpdated(Location location) {
        if (UserInfo.getInstance(GPSService.this).getIsLogin())
            updateNotification(location.getLatitude() + ", " + location.getLongitude() + ", " + location.getAccuracy());

        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());
        setAccuracy(location.getAccuracy());

        // We are going to get the address for the current position
        SmartLocation.with(GPSService.this).geocoding().reverse(location, new OnReverseGeocodingListener() {
            @Override
            public void onAddressResolved(Location original, List<Address> results) {
                if (results.size() > 0) {
                    Address result = results.get(0);
                    StringBuilder builderAddress = new StringBuilder();
                    List<String> addressElements = new ArrayList<>();
                    for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                        addressElements.add(result.getAddressLine(i));
                    }
                    builderAddress.append(TextUtils.join(", ", addressElements));

                    addressName = builderAddress.toString();
                    cityName = result.getCountryName() + ", " + result.getAdminArea();
                }
            }
        });

        if (mainActivity != null) {
            mainActivity.myLocationHere(latitude, longitude, accuracy, addressName, cityName);
        }
        if (workDetailActivity != null) {
            workDetailActivity.myLocationHere(latitude, longitude);
        }

        if (UserInfo.getInstance(GPSService.this).getIsLogin() && longitude != 0.0f && latitude != 0.0f) {

            if (isNetworkConnected) {
                int size = arrTrackLocation.size();
                saveLocationToFile(Environment.getExternalStorageDirectory() + "/CoolBackup/" + Conts.FILE_LOCATION_NETWORK, getItemBackupValue(location, true));
                if (size >= MINXIMUM_PACKAGE && !isUploading) {
                    pushDataGPSToServer();
                } else {
                    Log.d(TAGG, "onLocationUpdated " + "ADD_GPS: " + latitude + ", " + longitude + ", " + accuracy);
                    arrTrackLocation.add(new TrackLocation(latitude, longitude, System.currentTimeMillis(), accuracy));
                }
            } else {
                arrTrackLocation.add(new TrackLocation(latitude, longitude, System.currentTimeMillis(), accuracy));
                saveLocationToFile(Environment.getExternalStorageDirectory() + "/CoolBackup/" + Conts.FILE_LOCATION_NO_NETWORK, getItemBackupValue(location, false));
                Log.d(TAGG, "onLocationUpdated " + "BACK_UP_GPS: " + latitude + ", " + longitude + ", " + accuracy);
            }
        } else {
            updateNotification("GPS not found");
            Log.d(TAGG, "onLocationUpdated " + "GPS_NOT_FOUND, " + longitude + ", " + latitude);
        }
        if (mainActivity != null) {
            mainActivity.appendText("");
        }
    }

    private void removeGPSAfterUpload(int size) {
        for (int i = 0; i < size; i++) {
            arrTrackLocation.remove(0);
        }
    }

    private void checkGPSOnOff() {
        if (mainActivity != null) {
            boolean statusGPS = SmartLocation.with(mainActivity).location().state().isAnyProviderAvailable();
            mainActivity.setTitleStatusGPS(statusGPS);
        }
    }


    private synchronized void pushDataGPSToServer() {
        int sizeUpload = arrTrackLocation.size() / MAXIMUM_PACKAGE;
        int phanDu = arrTrackLocation.size() % MAXIMUM_PACKAGE;
        int isDoneSizeUpload = 0;
        if (sizeUpload > 0) {
            ArrayList<TrackLocation> arrUpload = new ArrayList<>();
            for (int j = 0; j < MAXIMUM_PACKAGE; j++) {
                arrUpload.add(arrTrackLocation.get(j));
            }
            updateLocation(new ItemTrackLocation(arrUpload, batteryLevel));
            isDoneSizeUpload++;
        } else {
            if (isDoneSizeUpload == sizeUpload && phanDu > 0) {
                ArrayList<TrackLocation> arrUpload = new ArrayList<>();
                for (int j = 0; j < phanDu; j++) {
                    arrUpload.add(arrTrackLocation.get(j));
                }
                updateLocation(new ItemTrackLocation(arrUpload, batteryLevel));
            }
        }
    }

    public void updateLocation(final ItemTrackLocation location) {
        isUploading = true;
        mService.updateLocation(location).enqueue(new Callback<UpdateLocationResponseStatus>() {
            @Override
            public void onResponse(Call<UpdateLocationResponseStatus> call, Response<UpdateLocationResponseStatus> response) {
                int statusCode = response.code();
                Log.d(TAGG, "onLocationUpdated.onResponse(), statusCode: " + statusCode);
                LocationResponseUpload locationBackupFile = new LocationResponseUpload(statusCode, response.body().getMessage(), System.currentTimeMillis());
                saveLocationToFile(Environment.getExternalStorageDirectory() + "/CoolBackup/" + Conts.FILE_RESPONSE, locationBackupFile);
                if (response.isSuccessful()) {
                    UpdateLocationResponseStatus updateLocation = response.body();
                    if (updateLocation != null) {
                        int updateLocationCode = updateLocation.getStatusCode();
                        if (updateLocationCode == Conts.RESPONSE_STATUS_OK) {
                            int sizeUpload = location.getTrackLocation().size();
                            removeGPSAfterUpload(sizeUpload);
                            body = gson.toJson(response.body());
                            int size = arrTrackLocation.size();
                            if (size > MINXIMUM_PACKAGE && isNetworkConnected) {
                                pushDataGPSToServer();
                            } else {
                                isUploading = false;
                            }
                        } else if (updateLocationCode == Conts.RESPONSE_STATUS_TOKEN_WRONG) {
                            UserInfo.getInstance(GPSService.this).setUserLoginOnOtherDevice(true);
                            updateNotification("Tài khoản này đã được đăng nhập trên thiết bị khác");
                            if (mainActivity != null && UserInfo.getInstance(GPSService.this).getMainActivityActive()) {
                                mainActivity.showMessageRequestLogout();
                            }
                        }
                    }
                    Log.d(TAGG, "onLocationUpdated.onResponse().isSuccessful(), body: " + body + "\nSize:= " + location.getTrackLocation().size());
                } else {
                    body = gson.toJson(response.body());
                    LocationResponseUpload locationBackupFile2 = new LocationResponseUpload(response.body().getStatusCode(), "onLocationUpdated.onResponse().isFail(), body: " + body, System.currentTimeMillis());
                    Log.d(TAGG, "onLocationUpdated.onResponse().isFail(), body: " + body);
                    saveLocationToFile(Environment.getExternalStorageDirectory() + "/CoolBackup/" + Conts.FILE_RESPONSE, locationBackupFile2);
                    if (isNetworkConnected) {
                        pushDataGPSToServer();
                    } else {
                        isUploading = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateLocationResponseStatus> call, Throwable t) {
                Log.e(TAGG, "updateLocation.onFailure()");
                LocationResponseUpload locationBackupFile = new LocationResponseUpload(4004, "Upload fail:+\n" + t.getCause() + t.getStackTrace().toString(), System.currentTimeMillis());
                saveLocationToFile(Environment.getExternalStorageDirectory() + "/CoolBackup/" + Conts.FILE_RESPONSE, locationBackupFile);
                t.printStackTrace();
                if (isNetworkConnected) {
                    pushDataGPSToServer();
                } else {
                    isUploading = false;
                }
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
                    if (isNetworkConnected) {
                        //connectSocket(UserInfo.getInstance(GPSService.this).getAccessToken());
                        int size = arrTrackLocation.size();
                        if (size >= MINXIMUM_PACKAGE && !isUploading) {
                            pushDataGPSToServer();
                        }
                    } else {
                        //disconnectSocket();
                    }
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
        String token = UserInfo.getInstance(this).getAccessToken();
        mService = ApiUtils.getAPIService(token);
        Log.d(TAGG, "Call connect in Service");
        connectSocket(token);
    }

    public Socket getmSocket() {
        return mSocket;
    }

    public void connectSocket(String token) {
        //FIXME Please don't remove code in below comment. It will use later
//            mSocket.on("join_res", onNewMessage);
        mSocket.emit("join", token, new Ack() {
            @Override
            public void call(Object... args) {
                Log.d(TAGG, "Tao nhan dc roi1");
                Log.d(TAGG, "startThread1" + args.toString());
            }
        });
        mSocket.connect();
    }
    //FIXME Please don't remove code in below comment. It will use later
//    private Emitter.Listener onNewMessage = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            Log.d(TAGG, "Tao nhan dc roi2");
//            Log.d(TAGG, "startThread2" + args.toString());
//        }
//    };

    private void disconnectSocket() {
        if (mSocket != null && mSocket.connected()) {
            mSocket.disconnect();
            mSocket = null;
        }
    }

    public void disconnectService() {
        stopSelf();
        stopForeground(true);
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

    public String getAddressName() {
        return addressName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setWorkDetailActivity(WorkDetailActivity workDetailActivity) {
        this.workDetailActivity = workDetailActivity;
    }
}
