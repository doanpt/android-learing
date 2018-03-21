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
import com.cnc.hcm.cnctracking.activity.AddProductActivity;
import com.cnc.hcm.cnctracking.activity.MainActivity;
import com.cnc.hcm.cnctracking.activity.ProductDetailActivity;
import com.cnc.hcm.cnctracking.api.APIService;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.dialog.DialogDetailTaskFragment;
import com.cnc.hcm.cnctracking.model.GetTaskListResult;
import com.cnc.hcm.cnctracking.model.ItemCancelTask;
import com.cnc.hcm.cnctracking.model.ItemTask;
import com.cnc.hcm.cnctracking.model.ItemTrackLocation;
import com.cnc.hcm.cnctracking.model.LocationBackupFile;
import com.cnc.hcm.cnctracking.model.LocationResponseUpload;
import com.cnc.hcm.cnctracking.model.TrackLocation;
import com.cnc.hcm.cnctracking.model.UpdateLocationResponseStatus;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;
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
                            String idTask = intent.getStringExtra(KEY_ID_OPEND_DETAIL_TASK);
                            if (mainActivity != null && mainActivity.isMainActive()) {
                                mainActivity.showTaskDetail(idTask);
                            } else {
                                Intent intentShowDetailTask = new Intent(this, MainActivity.class);
                                intentShowDetailTask.putExtra(Conts.KEY_ID_TASK_TO_SHOW_DETAIL, idTask);
                                intentShowDetailTask.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intentShowDetailTask);
                            }
                            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancel(0);
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
                        connectSocket(UserInfo.getInstance(GPSService.this).getAccessToken());
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
                mainActivity.handleNetworkSetting(isNetworkConnected);
                mainActivity.handleGPSSetting(statusGPS);
            }
            if (addProductActivity != null) {
                addProductActivity.handleNetworkSetting(isNetworkConnected);
                addProductActivity.handleGPSSetting(statusGPS);
            }

            if (productDetailActivity != null) {
                productDetailActivity.handleNetworkSetting(isNetworkConnected);
                productDetailActivity.handleGPSSetting(statusGPS);
            }

            if (timeMinute >= 60) {
                for (final ItemTask item : listTaskToDay) {
                    String appointmentDate = item.getTaskResult().appointmentDate.substring(0, item.getTaskResult().appointmentDate.lastIndexOf(".")) + "Z";
                    String timeItemTask = CommonMethod.formatTimeAppointmentDateBeforThirtyMinute(appointmentDate);
                    Log.d(TAGG, "runnableUpdateUI, Time before 30': " + timeItemTask);
                    String currentTime = CommonMethod.formatDateToString(CommonMethod.getInstanceCalendar().getTimeInMillis());

                    if (timeItemTask.equals(currentTime)) {
                        if (mainActivity != null && mainActivity.isMainActive()) {
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mainActivity.showDialogAppointmentTask(item);
                                }
                            });
                        }


                        GetTaskListResult.Result.RecommendedServices recommendedServices = getRecommendedServiceDefault(item.getTaskResult().recommendedServices);
                        if (recommendedServices != null && recommendedServices.getService() != null) {
                            addNotification(item.getTaskResult()._id, item.getTaskResult().title, recommendedServices.getService().name);
                        } else {
                            addNotification(item.getTaskResult()._id, item.getTaskResult().title, "Dịch vụ");
                        }
                        addNotification(item.getTaskResult()._id,
                                "Ticket " + item.getTaskResult().title + " " + "còn 30 phút nữa đến lịch hẹn.",
                                "Đã đến lịch hẹn");
                    }
                }
                timeMinute = 0;
                if (mSocket != null && !mSocket.connected()) {
                    CommonMethod.makeToast(getApplicationContext(), "No connect to Socket server");
                    Log.d(TAGG, "No connect to Socket server");
                }
            }
            handler.postDelayed(runnableUpdateUI, 1000);
        }
    };

    public Socket getmSocket() {
        return mSocket;
    }

    private void connectSocket(String token) {
        try {
            String url = Conts.URL_BASE;
            IO.Options options = new IO.Options();
            options.forceNew = true;
            options.query = "token=" + token;
            options.timeout = 30000L;
            mSocket = IO.socket(url, options);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            CommonMethod.makeToast(getApplicationContext(), "connectSocket, " + e.getMessage());
        }
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
            mSocket = null;
        }
    }

    private Emitter.Listener eventDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAGG, "Socket disconnect");
            if (isNetworkConnected){
                connectSocket(UserInfo.getInstance(GPSService.this).getAccessToken());
                Log.d(TAGG, "Socket reconnect");
            }
        }
    };

    private Emitter.Listener eventError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAGG, "ConnectSocket eventError");
            Log.d(TAGG, "ConnectSocket eventError");
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
            final GetTaskListResult.Result result = gson.fromJson(args[0].toString(), GetTaskListResult.Result.class);
            if (result != null) {
                if (CommonMethod.checkCurrentDay(result.appointmentDate) && listTaskToDay != null) {
                    listTaskToDay.add(new ItemTask(result));
                }
                GetTaskListResult.Result.RecommendedServices recommendedServices = getRecommendedServiceDefault(result.recommendedServices);
                if (recommendedServices != null && recommendedServices.getService() != null) {
                    addNotification(result._id, result.title, recommendedServices.getService().name);
                } else {
                    addNotification(result._id, result.title, "Dịch vụ");
                }
                if (mainActivity != null) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.receiverNewTask(result);
                        }
                    });
                }

                Log.d(TAGG, "eventNewTask: " + args[0].toString());
                Log.d(TAGG, "eventNewTask");
            }
        }
    };

    private GetTaskListResult.Result.RecommendedServices getRecommendedServiceDefault(GetTaskListResult.Result.RecommendedServices[] recommendedServices) {
        if (recommendedServices != null && recommendedServices.length > 0) {
            for (int i = 0; i < recommendedServices.length; i++) {
                if (recommendedServices[i].getIsDefault()) {
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

    private void updateNotification(String content) {
        int api = Build.VERSION.SDK_INT;

        remoteViews.setTextViewText(R.id.notif_content, content);

        if (api < Build.VERSION_CODES.HONEYCOMB) {
            notificationManager.notify(NOTIFI_ID, mNotification);
        } else if (api >= Build.VERSION_CODES.HONEYCOMB) {
            notificationManager.notify(NOTIFI_ID, mBuilder.build());
        }

    }

    private void addNotification(String idTask, String titleTask, String serviceName) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(titleTask)
                        .setContentText(serviceName);

        Intent intentOpen = new Intent(ACTION_DETAIL_TASK);
        intentOpen.setClass(this, GPSService.class);
        intentOpen.putExtra(KEY_ID_OPEND_DETAIL_TASK, idTask);
        PendingIntent piOpen = PendingIntent.getService(this, 0, intentOpen, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(piOpen);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
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
