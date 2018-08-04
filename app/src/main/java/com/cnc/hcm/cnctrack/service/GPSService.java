package com.cnc.hcm.cnctrack.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.Toast;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.activity.AddProductActivity;
import com.cnc.hcm.cnctrack.activity.MainActivity;
import com.cnc.hcm.cnctrack.activity.ProductDetailActivity;
import com.cnc.hcm.cnctrack.activity.SplashActivity;
import com.cnc.hcm.cnctrack.api.APIService;
import com.cnc.hcm.cnctrack.api.ApiUtils;
import com.cnc.hcm.cnctrack.api.MHead;
import com.cnc.hcm.cnctrack.fragment.DialogDetailTaskFragment;
import com.cnc.hcm.cnctrack.model.ItemCancelTask;
import com.cnc.hcm.cnctrack.model.ItemTask;
import com.cnc.hcm.cnctrack.model.ItemTrackLocation;
import com.cnc.hcm.cnctrack.model.LocationBackupFile;
import com.cnc.hcm.cnctrack.model.LocationResponseUpload;
import com.cnc.hcm.cnctrack.model.TrackLocation;
import com.cnc.hcm.cnctrack.model.UpdateLocationResponseStatus;
import com.cnc.hcm.cnctrack.model.common.RecommendedServices;
import com.cnc.hcm.cnctrack.model.common.TaskListResult;
import com.cnc.hcm.cnctrack.util.CommonMethod;
import com.cnc.hcm.cnctrack.util.Conts;
import com.cnc.hcm.cnctrack.util.UserInfo;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.github.nkzawa.socketio.client.Ack;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;


/**
 * Created by giapmn on 9/14/17.
 */

public class GPSService extends Service implements OnLocationUpdatedListener {
    private static final String ACTION_SHOW_APP = "ACTION_SHOW_APP";
    private static final String ACTION_DETAIL_TASK = "ACTION_DETAIL_TASK";
    private static final String KEY_ID_OPEND_DETAIL_TASK = "KEY_ID_OPEND_DETAIL_TASK";

    private static final String TAGG = "GPSService";
    private static final String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final int NOTIFI_ID = 111;
    private static final float MIN_DISTANCE_GET_GPS = 20f;
    private static final int MAXIMUM_PACKAGE = 100;
    private static final int MINXIMUM_PACKAGE = 5;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews remoteViews;
    private Notification mNotification;

    private ArrayList<TrackLocation> arrTrackLocation;

    private Gson gson = new Gson();
    private APIService mService;
    private MainActivity mainActivity;
    private DialogDetailTaskFragment dialogDetailTaskFragment;
    private AddProductActivity addProductActivity;
    private ProductDetailActivity productDetailActivity;
    //private LocationGooglePlayServicesProvider provider;

    private double longitude;
    private double latitude;
    private float accuracy;
    private int batteryLevel;
    private String body = "";

    private boolean isNetworkConnected;
    private Socket mSocket;

    {
        try {
            String url = Conts.URL_BASE + "?token=" + UserInfo.getInstance(GPSService.this).getAccessToken();
            mSocket = IO.socket(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            CommonMethod.reportCrashToFirebase(e.getMessage());
        }
    }

    private String addressName;
    private String cityName;
    private boolean isUploading;

    private Handler handler = new Handler();
    private ArrayList<ItemTask> listTaskToDay = new ArrayList<>();

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
//                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        Log.i(TAGG, "onStartCommand, ACTION_SHOW_APP");
                    } else {
                        stopSelf();
                        stopForeground(true);
                        return START_NOT_STICKY;
                    }
                    break;
                case ACTION_DETAIL_TASK:
                    if (isUserLogin) {
                        if (intent != null) {
                            int idTask = intent.getIntExtra(KEY_ID_OPEND_DETAIL_TASK, -1);
                            if (mainActivity != null && mainActivity.isMainActive()) {
                                mainActivity.showTaskDetail(idTask + Conts.BLANK);
                            } else {
                                Intent intentShowDetailTask = new Intent(this, MainActivity.class);
                                intentShowDetailTask.putExtra(Conts.KEY_ID_TASK_TO_SHOW_DETAIL, idTask);
                                intentShowDetailTask.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intentShowDetailTask);
                            }
                        }
                    }
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initObject();
        registerBroadcast();
        startThread();
        setupNotification();
        startLocation();
    }

    private void initObject() {
        arrTrackLocation = new ArrayList<>();
    }

    private void startLocation() {
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
        stopSmartLocation();
    }

    private void stopSmartLocation() {
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
        if (dialogDetailTaskFragment != null) {
            dialogDetailTaskFragment.myLocationHere(latitude, longitude);
        }

        if (UserInfo.getInstance(GPSService.this).getIsLogin() && longitude != 0.0f && latitude != 0.0f) {

            if (isNetworkConnected) {
                saveLocationToFile(Environment.getExternalStorageDirectory() + "/CoolBackup/" + Conts.FILE_LOCATION_NETWORK, getItemBackupValue(location, true));

                boolean isPushServerFirstTime = UserInfo.getInstance(GPSService.this).getIsUploadFirstTime();
                if (isPushServerFirstTime) {
                    arrTrackLocation.clear();
                    for (int i = 0; i < Conts.DEFAULT_VALUE_INT_5; i++) {
                        arrTrackLocation.add(new TrackLocation(latitude, longitude, System.currentTimeMillis(), 10.0f));
                    }
                    UserInfo.getInstance(GPSService.this).setUploadFirstTime(false);
                }
                int size = arrTrackLocation.size();
                if (size >= MINXIMUM_PACKAGE && !isUploading) {
                    arrTrackLocation.add(new TrackLocation(latitude, longitude, System.currentTimeMillis(), accuracy));
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

    private void updateLocation(final ItemTrackLocation location) {
        isUploading = true;
        mService.updateLocation(location).enqueue(new Callback<UpdateLocationResponseStatus>() {
            @Override
            public void onResponse(Call<UpdateLocationResponseStatus> call, Response<UpdateLocationResponseStatus> response) {
                int statusCode = response.code();
                Log.d(TAGG, "onLocationUpdated.onResponse(), statusCode: " + statusCode);
                if (response.isSuccessful()) {
                    LocationResponseUpload locationBackupFile = new LocationResponseUpload(statusCode, "in isSuccessful: and message=" + response.body().getMessage() + "\nFirst location in package:" + location.getTrackLocation().get(0).toString() + " \n size Total Array:" + arrTrackLocation.size() + " -- size arr push:" + location.getTrackLocation().size(), System.currentTimeMillis());
                    saveLocationToFile(Environment.getExternalStorageDirectory() + "/CoolBackup/" + Conts.FILE_RESPONSE, locationBackupFile);
                    UpdateLocationResponseStatus updateLocation = response.body();
                    if (updateLocation != null) {
                        int updateLocationCode = updateLocation.getStatusCode();
                        String updateLocationMSG = updateLocation.getMessage();
                        if (updateLocationCode == Conts.RESPONSE_STATUS_OK && updateLocationMSG.equals("Success")) {
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
                            isUploading = false;
                            UserInfo.getInstance(GPSService.this).setUserLoginOnOtherDevice(true);
                            updateNotification(getString(R.string.account_login_on_other_device));
                            if (mainActivity != null && UserInfo.getInstance(GPSService.this).getMainActivityActive()) {
                                mainActivity.showMessageRequestLogout();
                            }
                        } else {
                            isUploading = false;
                        }
                    } else {
                        isUploading = false;
                        LocationResponseUpload locationBackupFileA = new LocationResponseUpload(statusCode, "updateLocation==null in isSuccessful " + response.body().getMessage() + " \n size Total Array:" + arrTrackLocation.size() + " -- size arr push:" + location.getTrackLocation().size(), System.currentTimeMillis());
                        saveLocationToFile(Environment.getExternalStorageDirectory() + "/CoolBackup/" + Conts.FILE_RESPONSE, locationBackupFileA);
                    }
                    Log.d(TAGG, "onLocationUpdated.onResponse().isSuccessful(), body: " + body + "\nSize:= " + location.getTrackLocation().size());
                } else {
                    body = gson.toJson(response.body());
                    LocationResponseUpload locationBackupFile2 = new LocationResponseUpload(statusCode, "onLocationUpdated.onResponse().isFail(), body: " + body + "\n" + response.body().getMessage() + " \n size Total Array:" + arrTrackLocation.size() + " -- size arr push:" + location.getTrackLocation().size(), System.currentTimeMillis());
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
                LocationResponseUpload locationBackupFile = null;
                Log.e(TAGG, "updateLocation.onFailure()");
                if (t != null) {
                    try {
                        locationBackupFile = new LocationResponseUpload(4004, "Upload fail:\n" + t.getCause() + "\n---" + t.getMessage() + "\n---" + t.getStackTrace().toString() + " \n size Total Array:" + arrTrackLocation.size() + " -- size arr push:" + location.getTrackLocation().size(), System.currentTimeMillis());
//                        t.printStackTrace();
                    } catch (Exception e) {
                        locationBackupFile = new LocationResponseUpload(4004, "Upload fail:\n" + t.getCause() + "\n---" + t.getMessage() + "\n---" + t.getStackTrace().toString() + " \n size Total Array:" + arrTrackLocation.size() + " -- size arr push:" + location.getTrackLocation().size(), System.currentTimeMillis());
                    } finally {
                        saveLocationToFile(Environment.getExternalStorageDirectory() + "/CoolBackup/" + Conts.FILE_RESPONSE, locationBackupFile);
                        if (isNetworkConnected) {
                            pushDataGPSToServer();
                        } else {
                            isUploading = false;
                        }
                    }
                } else {
                    locationBackupFile = new LocationResponseUpload(4004, "Upload fail:\n size Total Array:" + arrTrackLocation.size() + " -- size arr push:" + location.getTrackLocation().size(), System.currentTimeMillis());
                    saveLocationToFile(Environment.getExternalStorageDirectory() + "/CoolBackup/" + Conts.FILE_RESPONSE, locationBackupFile);
                    if (isNetworkConnected) {
                        pushDataGPSToServer();
                    } else {
                        isUploading = false;
                    }
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
                        connectSocket();
                        int size = arrTrackLocation.size();
                        if (size >= MINXIMUM_PACKAGE && !isUploading) {
                            pushDataGPSToServer();
                        }
                    } else {
                        disconnectSocket();
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


    private void startThread() {
        String token = UserInfo.getInstance(this).getAccessToken();
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, token));
        mService = ApiUtils.getAPIService(arrHeads);
        Log.d(TAGG, "Call connect in Service");
        handler.post(runnableUpdateUI);
    }

    private Runnable runnableUpdateUI = new Runnable() {
        int timeMinute = 0;

        @Override
        public void run() {

            timeMinute++;
            boolean statusGPS = SmartLocation.with(GPSService.this).location().state().isAnyProviderAvailable();

            //Check network connection
            if (mainActivity != null) {
                mainActivity.handleGPSSetting(statusGPS);
            }
            if (addProductActivity != null) {
                addProductActivity.handleGPSSetting(statusGPS);
            }

            if (productDetailActivity != null) {
                productDetailActivity.handleGPSSetting(statusGPS);
            }

            if (timeMinute >= 60) {
                for (final ItemTask item : listTaskToDay) {
                    String appointmentDate = item.getTaskResult().appointmentDate;
                    String timeItemTask = CommonMethod.formatTimeAppointmentDateBeforThirtyMinute(appointmentDate);
                    Log.d(TAGG, "runnableUpdateUI, Time before 30': " + timeItemTask);
                    String currentTime = CommonMethod.formatTimeDateHourToString(CommonMethod.getInstanceCalendar().getTimeInMillis());

                    if (timeItemTask.equals(currentTime)) {
                        if (mainActivity != null && mainActivity.isMainActive()) {
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mainActivity.showDialogAppointmentTask(item);
                                }
                            });
                        }


                        RecommendedServices recommendedServices = getRecommendedServiceDefault(item.getTaskResult().recommendedServices);
                        if (recommendedServices != null && recommendedServices.getService() != null) {
                            addNotification(Conts.APPOINTMENT_TASK_CHANNEL_ID, item.getTaskResult()._id, item.getTaskResult().title, recommendedServices.getService().name, Conts.GROUP_ID_NOTI_APPOINTMENT);
                        } else {
                            addNotification(Conts.APPOINTMENT_TASK_CHANNEL_ID, item.getTaskResult()._id, item.getTaskResult().title, "Dịch vụ", Conts.GROUP_ID_NOTI_APPOINTMENT);
                        }
                        addNotification(Conts.APPOINTMENT_TASK_CHANNEL_ID, item.getTaskResult()._id, "Đã đến lịch hẹn",
                                "Ticket " + item.getTaskResult().title + " " + "còn 30 phút nữa đến lịch hẹn.", Conts.GROUP_ID_NOTI_APPOINTMENT);
                    }
                }
                timeMinute = 0;
                if (mSocket != null && !mSocket.connected()) {
                    Log.d(TAGG, "No connect to Socket server");
                }
            }
            if (UserInfo.getInstance(getApplicationContext()).getIsLogin()) {
                handler.postDelayed(runnableUpdateUI, 1000);
            }
        }
    };

    public Socket getmSocket() {
        return mSocket;
    }

    private void connectSocket() {
        if (mSocket != null && !mSocket.connected()) {
            mSocket.on(Conts.SOCKET_EVENT_NEW_TASK, eventNewTask)
                    .on(Conts.SOCKET_EVENT_LOGIN_OTHER_DEVICE, eventLoginOtherDevice)
                    .on(Conts.SOCKET_EVENT_ERROR, eventError)
                    .on(Conts.SOCKET_EVENT_DISCONNECT, eventDisconnect)
                    .on(Conts.SOCKET_EVENT_CANCEL_TASK, eventCancelTask)
                    .on(Conts.SOCKET_EVENT_UNASSIGNED_TASK, eventUnassignedTask);
            mSocket.connect();
        }

    }

    private void disconnectSocket() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off(Conts.SOCKET_EVENT_NEW_TASK, eventNewTask)
                    .off(Conts.SOCKET_EVENT_LOGIN_OTHER_DEVICE, eventLoginOtherDevice)
                    .off(Conts.SOCKET_EVENT_ERROR, eventError)
                    .off(Conts.SOCKET_EVENT_DISCONNECT, eventDisconnect)
                    .off(Conts.SOCKET_EVENT_CANCEL_TASK, eventCancelTask)
                    .off(Conts.SOCKET_EVENT_UNASSIGNED_TASK, eventUnassignedTask);
        }
    }

    private Emitter.Listener eventDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String message = "Socket disconnect, " + args.toString();
            Log.d(TAGG, message);
            CommonMethod.reportCrashToFirebase(message);
//            if (isNetworkConnected) {
//                connectSocket();
//                Log.d(TAGG, "Socket reconnect");
//                CommonMethod.reportCrashToFirebase(message);
//            }
        }
    };

    private Emitter.Listener eventError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String message = "ConnectSocket eventError, " + args.toString();
            CommonMethod.reportCrashToFirebase(message);
            Log.d(TAGG, message);
        }
    };

    private Emitter.Listener eventLoginOtherDevice = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            UserInfo.getInstance(GPSService.this).setUserLoginOnOtherDevice(true);
            updateNotification(getString(R.string.account_login_on_other_device));
            stopSmartLocation();
            if (mainActivity != null && UserInfo.getInstance(GPSService.this).getMainActivityActive()) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.showMessageRequestLogout();
                    }
                });
            }
        }
    };

    private Emitter.Listener eventNewTask = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Gson gson = new Gson();
            Log.d(TAGG, "eventNewTask: " + args[0].toString());
            Log.d(TAGG, "eventNewTask");
            try {
                final TaskListResult result = gson.fromJson(args[0].toString(), TaskListResult.class);
                if (result != null) {
                    if (CommonMethod.checkCurrentDay(result.appointmentDate) && listTaskToDay != null) {
                        listTaskToDay.add(new ItemTask(result));
                    }
                    RecommendedServices recommendedServices = getRecommendedServiceDefault(result.recommendedServices);
                    if (recommendedServices != null && recommendedServices.getService() != null) {
                        addNotification(Conts.NEW_TASK_CHANNEL_ID, result._id, result.title, recommendedServices.getService().name, Conts.GROUP_ID_NOTI_NEW_TASK);
                    } else {
                        addNotification(Conts.NEW_TASK_CHANNEL_ID, result._id, result.title, "Dịch vụ", Conts.GROUP_ID_NOTI_NEW_TASK);
                    }
                    if (mainActivity != null) {
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.receiverNewTask(result);
                            }
                        });
                    }
                }
            } catch (final Exception ex) {
                ex.printStackTrace();
                if (mainActivity != null) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GPSService.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }

    };

    private RecommendedServices getRecommendedServiceDefault(RecommendedServices[] recommendedServices) {
        if (recommendedServices != null && recommendedServices.length > 0) {
            for (int i = 0; i < recommendedServices.length; i++) {
                if (recommendedServices[i].isDefault) {
                    return recommendedServices[i];
                }
            }
        }
        return null;
    }

    private Emitter.Listener eventCancelTask = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                Log.d(TAGG, "eventCancelTask -> data: " + args[0]);
                final ItemCancelTask itemCancelTask = new Gson().fromJson(args[0].toString(), ItemCancelTask.class);
                if (mainActivity != null) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.onCancelTicket(itemCancelTask);
                        }
                    });
                }
            } catch (Exception e) {
                Log.e(TAGG, "eventCancelTask -> e: " + e);
            }
        }
    };

    private Emitter.Listener eventUnassignedTask = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                Log.d(TAGG, "eventUnassignedTask -> data: " + args[0]);
                final ItemCancelTask itemCancelTask = new Gson().fromJson(args[0].toString(), ItemCancelTask.class);
                if (mainActivity != null) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.onUnAssignedTask(itemCancelTask);
                        }
                    });
                }
            } catch (Exception e) {
                Log.e(TAGG, "eventUnassignedTask -> e: " + e);
            }
        }
    };

    public void disconnectService() {
        stopSelf();
        stopForeground(true);
    }

    public NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    private void setupNotification() {
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Intent intent = new Intent(this, SplashActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = Conts.FOREGROUND_CHANNEL_ID;
            String channelName = getString(R.string.channel_foreground);
            int importanceNoti = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelID, channelName, importanceNoti);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getNotificationManager().createNotificationChannel(channel);
        }

        mBuilder = new NotificationCompat.Builder(this, Conts.FOREGROUND_CHANNEL_ID)
                .setAutoCancel(false)
                .setContentTitle("Tracking")
                .setContentText("" + latitude + ", " + longitude + ", " + accuracy)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setWhen(System.currentTimeMillis());

        startForeground(NOTIFI_ID, mBuilder.build());
    }


    private void updateNotification(String content) {
        if (mBuilder != null) {
            mBuilder.setContentText(content);
            getNotificationManager().notify(NOTIFI_ID, mBuilder.build());
        }
    }

    private void addNotification(String channelID, String idTask, String titleTask, String serviceName, String groupID) {
        int idNotifi = 0;
        try {
            idNotifi = Integer.parseInt(idTask);
        } catch (Exception e) {
        }
        Intent intentOpen = new Intent(ACTION_DETAIL_TASK);
        intentOpen.setClass(this, GPSService.class);
        intentOpen.putExtra(KEY_ID_OPEND_DETAIL_TASK, idNotifi);
        PendingIntent piOpen = PendingIntent.getService(this, idNotifi, intentOpen, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = Conts.BLANK;
            String description = Conts.BLANK;
            switch (channelID) {
                case Conts.NEW_TASK_CHANNEL_ID:
                    channelName = getString(R.string.channel_new_task);
                    description = getString(R.string.description_channel_new_task);
                    break;
                case Conts.APPOINTMENT_TASK_CHANNEL_ID:
                    channelName = getString(R.string.channel_appointment_task);
                    description = getString(R.string.description_appointment_task);
                    break;
            }
            int importanceNoti = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelID, channelName, importanceNoti);
            channel.setDescription(description);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getNotificationManager().createNotificationChannel(channel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(CommonMethod.getSmallIcon())
                .setContentTitle(titleTask)
                .setContentText(serviceName)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(piOpen)
//                .setGroup(groupID)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(serviceName));

        // Add as notification
        getNotificationManager().notify(idNotifi, builder.build());
    }


    private void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private void setLatitude(double latitude) {
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

    private void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setDialogDetailTaskFragment(DialogDetailTaskFragment dialogDetailTaskFragment) {
        this.dialogDetailTaskFragment = dialogDetailTaskFragment;
        dialogDetailTaskFragment.myLocationHere(latitude, longitude);
    }

    public void setAddProductActivity(AddProductActivity addProductActivity) {
        this.addProductActivity = addProductActivity;
    }

    public void setProductDetailActivity(ProductDetailActivity productDetailActivity) {
        this.productDetailActivity = productDetailActivity;
    }

    public void setListTaskToDay(ArrayList<ItemTask> listTaskToDay) {
        this.listTaskToDay.clear();
        this.listTaskToDay.addAll(listTaskToDay);
    }
}
