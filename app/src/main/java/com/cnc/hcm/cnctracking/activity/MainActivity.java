package com.cnc.hcm.cnctracking.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.WorkFragmentAdapter;
import com.cnc.hcm.cnctracking.dialog.DialogNotification;
import com.cnc.hcm.cnctracking.fragment.WorkCancelFragment;
import com.cnc.hcm.cnctracking.fragment.WorkCompletedFragment;
import com.cnc.hcm.cnctracking.fragment.WorkDoingFragment;
import com.cnc.hcm.cnctracking.fragment.WorkNewFragment;
import com.cnc.hcm.cnctracking.model.ItemWork;
import com.cnc.hcm.cnctracking.service.GPSService;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import biz.laenger.android.vpbs.BottomSheetUtils;
import biz.laenger.android.vpbs.ViewPagerBottomSheetBehavior;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends FragmentActivity implements View.OnClickListener
        , OnMapReadyCallback {


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_LOCATION_UPDATE = 4234;
    private GoogleMap mMap;

    private ViewPager viewPager;
    private LinearLayout llClickNetwork;
    private LinearLayout llClickGPS;
    private LinearLayout llClickSetting;
    private LinearLayout llClickHelp;
    private LinearLayout bottomSheetLayout;
    private TextView tvStatusNetwork, tvStatusGPS;
    private Button btnLogout;
    private CircleImageView imvAvatar;
    private TextView tvUsername, tvUserMail;
    private ImageView imvMenuDrawer, imvSearch;
    private ImageView imvProfile;
    private DrawerLayout drawer;
    private ViewPagerBottomSheetBehavior bottomSheetBehavior;


    private ArrayList<ItemWork> arrItemWorkNew = new ArrayList<>();
    private boolean isNetworkConnected;

    private WorkNewFragment workNewFragment = new WorkNewFragment();
    private WorkDoingFragment workDoingFragment = new WorkDoingFragment();
    private WorkCompletedFragment workCompletedFragment = new WorkCompletedFragment();
    private WorkCancelFragment workCancelFragment = new WorkCancelFragment();

    private Fragment[] listFrag = {workNewFragment, workDoingFragment, workCompletedFragment, workCancelFragment};

    private GPSService gpsService;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            gpsService = ((GPSService.MyBinder) iBinder).getGPSService();
            gpsService.setMainActivity(MainActivity.this);
            //FIXME this code which is commented could remove if you want
//            if (gpsService.getmSocket() == null || !gpsService.getmSocket().connected()) {
//                String token = UserInfo.getInstance(MainActivity.this).getAccessToken();
//                Log.d("GPSService","Call connect in MainActivity");
//                gpsService.connectSocket(token);
//            }
            Log.d(TAG, "ServiceConnection, onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "ServiceConnection, onServiceDisconnected");
        }
    };

    private void registerBroadcastReciver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Conts.ACTION_NETWORK_CHANGE);
        registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Conts.ACTION_NETWORK_CHANGE:
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetInfor = connectivityManager.getActiveNetworkInfo();
                    isNetworkConnected = activeNetInfor != null && activeNetInfor.isConnectedOrConnecting();
                    if (tvStatusNetwork != null)
                        if (isNetworkConnected) {
                            tvStatusNetwork.setTextColor(getResources().getColor(R.color.color_text_status));
                            tvStatusNetwork.setText(getResources().getString(R.string.On));
                            if (gpsService != null) {
                                updateDistanceTo4TabWork();
                            }

                        } else {
                            tvStatusNetwork.setTextColor(getResources().getColor(android.R.color.darker_gray));
                            tvStatusNetwork.setText(getResources().getString(R.string.Off));
                        }
                    break;
            }

        }
    };

    private void updateDistanceTo4TabWork() {
        if (workNewFragment != null) {
            workNewFragment.updateDistanceNewWork(gpsService.getLatitude(), gpsService.getLongitude());
        }

        if (workDoingFragment != null) {
            workDoingFragment.updateDistanceDoingWork(gpsService.getLatitude(), gpsService.getLongitude());
        }

        if (workCompletedFragment != null) {
            workCompletedFragment.updateDistanceCompleteWork(gpsService.getLatitude(), gpsService.getLongitude());
        }

        if (workCancelFragment != null) {
            workCancelFragment.updateDistanceCancelWork(gpsService.getLatitude(), gpsService.getLongitude());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
        createFileBackup();
        UserInfo.getInstance(this).setMainActivityActive(true);
        registerBroadcastReciver();
        if (!isServiceRunning(GPSService.class)) {
            Intent intent = new Intent(this, GPSService.class);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            startService(intent);
        }
        bindService(new Intent(this, GPSService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        checkUserLoginOnOtherDevice();
        initMap();
    }


    private void initData() {
        arrItemWorkNew.clear();
        arrItemWorkNew.add(new ItemWork(Conts.TYPE_NEW_TASK, "11:30:24 07/11/2017", "Sửa tủ lạnh", 21.172724, 105.910090, "0 Km", "",
                "14:20 - 16h20, 09/11/2017", "Mầu Ngô Giáp", "0974356994", "Unnamed Road, thôn Đông, Thuỵ Lâm, Đông Anh, Hà Nội, Vietnam", "Sửa tủ lạnh",
                "200.000 VND", "Máy vẫn chạy nhưng không còn lạnh, rỉ nước", false, "14:30", "16:30", "2h20'", "", false));
        arrItemWorkNew.add(new ItemWork(Conts.TYPE_NEW_TASK, "11:30:24 07/11/2017", "Sửa điều hoà", 21.025291, 105.793761, "0 Km", "",
                "14:20 - 16h20, 09/11/2017", "Đỗ Văn Tân", "0973545345", "602 Dương Đình Nghệ, Yên Hoà, Cầu Giấy, Hà Nội, Vietnam", "Sửa điều hoà",
                "200.000 VND", "Máy vẫn chạy nhưng không còn lạnh, rỉ nước", false, "14:30", "16:30", "2h20'", "", false));
        arrItemWorkNew.add(new ItemWork(Conts.TYPE_NEW_TASK, "11:30:24 07/11/2017", "Sửa tủ lạnh", 21.021846, 105.786384, "0 Km", "",
                "14:20 - 16h20, 09/11/2017", "Lê Quốc Nam", "0974356995", "Dương Đình Nghệ, Yên Hoà, Cầu Giấy, Hà Nội, Vietnam", "Sửa tủ lạnh",
                "200.000 VND", "Máy vẫn chạy nhưng không còn lạnh, rỉ nước", false, "14:30", "16:30", "2h20'", "", false));
        arrItemWorkNew.add(new ItemWork(Conts.TYPE_NEW_TASK, "11:30:24 07/11/2017", "Sửa tủ lạnh", 21.0273855, 105.7846931, "0 Km", "",
                "14:20 - 16h20, 09/11/2017", "Trần Văn Gạo", "0974356996", "5 Tôn Thất Thuyết, Dịch Vọng Hậu, Cầu Giấy, Hà Nội, Vietnam", "Sửa tủ lạnh",
                "200.000 VND", "Máy vẫn chạy nhưng không còn lạnh, rỉ nước", false, "14:30", "16:30", "2h20'", "", false));
        arrItemWorkNew.add(new ItemWork(Conts.TYPE_NEW_TASK, "11:30:24 07/11/2017", "Sửa máy lạnh", 21.0232644, 105.7843098, "0 Km", "",
                "14:20 - 16h20, 09/11/2017", "Phạm Trung Đoan", "0974356997", "Ngõ 3 Tôn Thất thuyết, Yên Hoà, Cầu Giấy, Hà Nội, Vietnam", "Sửa máy lạnh",
                "200.000 VND", "Máy vẫn chạy nhưng không còn lạnh, rỉ nước", false, "14:30", "16:30", "2h20'", "", false));


        arrItemWorkNew.add(new ItemWork(Conts.TYPE_DOING_TASK, "11:30:24 07/11/2017", "Sửa tủ lạnh", 21.0273855, 105.7846931, "0 Km", "",
                "14:20 - 16h20, 09/11/2017", "Trần Văn Gạo", "0974356996", "5 Tôn Thất Thuyết, Dịch Vọng Hậu, Cầu Giấy, Hà Nội, Vietnam", "Sửa tủ lạnh",
                "200.000 VND", "Máy vẫn chạy nhưng không còn lạnh, rỉ nước", false, "14:30", "16:30", "2h20'", "", false));
        arrItemWorkNew.add(new ItemWork(Conts.TYPE_DOING_TASK, "11:30:24 07/11/2017", "Sửa máy lạnh", 21.0232644, 105.7843098, "0 Km", "",
                "14:20 - 16h20, 09/11/2017", "Phạm Trung Đoan", "0974356997", "Ngõ 3 Tôn Thất thuyết, Yên Hoà, Cầu Giấy, Hà Nội, Vietnam", "Sửa máy lạnh",
                "200.000 VND", "Máy vẫn chạy nhưng không còn lạnh, rỉ nước", false, "14:30", "16:30", "2h20'", "", false));


        arrItemWorkNew.add(new ItemWork(Conts.TYPE_COMPLETE_TASK, "11:30:24 07/11/2017", "Sửa tủ lạnh", 21.021846, 105.786384, "0 Km", "",
                "14:20 - 16h20, 09/11/2017", "Lê Quốc Nam", "0974356995", "Dương Đình Nghệ, Yên Hoà, Cầu Giấy, Hà Nội, Vietnam", "Sửa tủ lạnh",
                "200.000 VND", "Máy vẫn chạy nhưng không còn lạnh, rỉ nước", false, "14:30", "16:30", "2h20'", "", false));


        arrItemWorkNew.add(new ItemWork(Conts.TYPE_CANCEL_TASK, "11:30:24 07/11/2017", "Sửa tủ lạnh", 21.172724, 105.910090, "0 Km", "",
                "14:20 - 16h20, 09/11/2017", "Mầu Ngô Giáp", "0974356994", "Unnamed Road, thôn Đông, Thuỵ Lâm, Đông Anh, Hà Nội, Vietnam", "Sửa tủ lạnh",
                "200.000 VND", "Máy vẫn chạy nhưng không còn lạnh, rỉ nước", false, "14:30", "16:30", "2h20'", "", false));
        arrItemWorkNew.add(new ItemWork(Conts.TYPE_CANCEL_TASK, "11:30:24 07/11/2017", "Sửa điều hoà", 21.025291, 105.793761, "0 Km", "",
                "14:20 - 16h20, 09/11/2017", "Đỗ Văn Tân", "0973545345", "602 Dương Đình Nghệ, Yên Hoà, Cầu Giấy, Hà Nội, Vietnam", "Sửa điều hoà",
                "200.000 VND", "Máy vẫn chạy nhưng không còn lạnh, rỉ nước", false, "14:30", "16:30", "2h20'", "", false));


    }

    private void initMarkerOnMap() {
        for (ItemWork work : arrItemWorkNew) {
            switch (work.getTypeWork()) {
                case Conts.TYPE_NEW_TASK:
                    addMarkerMap(work.getLatitude(), work.getLongitude(), work.getAddress(),
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    break;
                case Conts.TYPE_DOING_TASK:
                    addMarkerMap(work.getLatitude(), work.getLongitude(), work.getAddress(),
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    break;
                case Conts.TYPE_COMPLETE_TASK:
                    addMarkerMap(work.getLatitude(), work.getLongitude(), work.getAddress(),
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    break;
                case Conts.TYPE_CANCEL_TASK:
                    addMarkerMap(work.getLatitude(), work.getLongitude(), work.getAddress(),
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    break;
            }
        }
    }

    private void createFileBackup() {
        try {
            File folderBackup = new File(Environment.getExternalStorageDirectory(), "/CoolBackup");
            if (!folderBackup.exists()) {
                folderBackup.mkdirs();
                Log.w("DEBUG", "Created default directory.");
            }
            File file_network = new File(Environment.getExternalStorageDirectory() + "/CoolBackup", Conts.FILE_LOCATION_NETWORK);
            File file_no_network = new File(Environment.getExternalStorageDirectory() + "/CoolBackup", Conts.FILE_LOCATION_NO_NETWORK);
            File file_size = new File(Environment.getExternalStorageDirectory() + "/CoolBackup", Conts.FILE_LOCATION_UPLOAD_SIZE);

            if (!file_network.exists()) {
                file_network.createNewFile();
            }
            if (!file_no_network.exists()) {
                file_no_network.createNewFile();
            }
            if (!file_size.exists()) {
                file_size.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void checkUserLoginOnOtherDevice() {
        if (UserInfo.getInstance(this).getUserLoginOnOtherDevice()) {
            showMessageRequestLogout();
        }
    }

    private void initViews() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        imvMenuDrawer = (ImageView) findViewById(R.id.imv_menu);
        imvSearch = (ImageView) findViewById(R.id.imv_seach_location);
        imvProfile = (ImageView) findViewById(R.id.imv_profile);
        imvProfile.setOnClickListener(this);
        imvMenuDrawer.setOnClickListener(this);
        imvSearch.setOnClickListener(this);

        llClickNetwork = (LinearLayout) findViewById(R.id.ll_click_network);
        llClickGPS = (LinearLayout) findViewById(R.id.ll_click_gps);
        llClickSetting = (LinearLayout) findViewById(R.id.ll_click_setting);
        llClickHelp = (LinearLayout) findViewById(R.id.ll_click_help);

        llClickNetwork.setOnClickListener(this);
        llClickGPS.setOnClickListener(this);
        llClickSetting.setOnClickListener(this);
        llClickHelp.setOnClickListener(this);

        tvStatusNetwork = (TextView) findViewById(R.id.tv_status_network);
        tvStatusGPS = (TextView) findViewById(R.id.tv_status_gps);

        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);

        imvAvatar = (CircleImageView) findViewById(R.id.imv_avatar_profile);
        imvAvatar.setOnClickListener(this);
        tvUsername = (TextView) findViewById(R.id.tv_username_profile);
        tvUserMail = (TextView) findViewById(R.id.tv_user_email_profile);

        Picasso.with(this).load(Conts.URL_BASE + UserInfo.getInstance(this).getUserUrlImage())
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account).into(imvAvatar);
        tvUsername.setText(UserInfo.getInstance(this).getUsername());
        tvUserMail.setText(UserInfo.getInstance(this).getUserEmail());

        bottomSheetLayout = (LinearLayout) findViewById(R.id.linear_layout_bottom_sheet);
        bottomSheetBehavior = ViewPagerBottomSheetBehavior.from(bottomSheetLayout);


        WorkFragmentAdapter adapter = new WorkFragmentAdapter(getSupportFragmentManager(), listFrag);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                bottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        BottomSheetUtils.setupViewPager(viewPager);

    }

    public ArrayList<ItemWork> getDataByWorkType(int typeWork) {
        ArrayList<ItemWork> arrayList = new ArrayList<>();
        for (int index = 0; index < arrItemWorkNew.size(); index++) {
            ItemWork itemWork = arrItemWorkNew.get(index);
            if (itemWork.getTypeWork() == typeWork) {
                arrayList.add(itemWork);
            }
        }
        return arrayList;
    }

    public void appendText(String str) {
//        tvStatus.append("\n" + str);
    }

    public void disPlayTimeProgress(int time) {
        //tvTimeProgress.setText("Time: " + time);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void setTitleStatusGPS(boolean status) {
        if (status) {
            tvStatusGPS.setTextColor(getResources().getColor(R.color.color_text_status));
            tvStatusGPS.setText(getResources().getString(R.string.On));
        } else {
            tvStatusGPS.setTextColor(getResources().getColor(android.R.color.darker_gray));
            tvStatusGPS.setText(getResources().getString(R.string.Off));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_avatar_profile:
            case R.id.imv_profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.ll_click_network:
                Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intentWifi);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.ll_click_gps:
                Intent intentGps = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intentGps);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.btn_logout:
                actionLogout();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.ll_click_setting:
                Toast.makeText(this, "Tính năng đang hoàn thiện. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.ll_click_help:
                Toast.makeText(this, "Tính năng đang hoàn thiện. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.imv_menu:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.imv_seach_location:
                Toast.makeText(this, "Tính năng đang hoàn thiện. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void actionLogout() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.text_logout));
        progressDialog.show();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserInfo.getInstance(MainActivity.this).setUserInfoLogout();
                if (gpsService != null) {
                    gpsService.disconnectService();
                }
                progressDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserInfo.getInstance(this).setMainActivityActive(false);
        unbindService(serviceConnection);
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (bottomSheetBehavior.getState() == ViewPagerBottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_COLLAPSED);
            return;
        }

        super.onBackPressed();
    }

    public void showMessageRequestLogout() {
        final DialogNotification dialog = new DialogNotification(MainActivity.this);
        dialog.setHiddenBtnExit();
        dialog.setContentMessage("Tài khoản này đã được đăng nhập trên thiết bị khác. Ứng dụng sẽ đăng xuất tài khoản. Vui lòng đăng nhập lại để tiếp tục sử dụng.");
        dialog.setCancelable(true);
        dialog.setOnClickDialogNotificationListener(new DialogNotification.OnClickDialogNotificationListener() {
            @Override
            public void onClickButtonOK() {
                actionLogout();
            }

            @Override
            public void onClickButtonExit() {

            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_UPDATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permision accept", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permision denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String permissions[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            requestPermissions(permissions, MainActivity.REQUEST_CODE_LOCATION_UPDATE);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (gpsService != null) {
            if (gpsService.getLatitude() != 0 && gpsService.getLongitude() != 0) {
                myLocationHere(gpsService.getLatitude(), gpsService.getLongitude(),
                        gpsService.getAccuracy(), gpsService.getAddressName(), gpsService.getCityName());
            }
        }
    }

    public void myLocationHere(double latitude, double longitude, float accuracy, String addName, String cityName) { // TODO did we need accuracy here?
        if (mMap != null) {
            mMap.clear();
            LatLng myLocation = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(myLocation).title(addName).snippet(cityName));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));

            initMarkerOnMap();
        }

        workNewFragment.updateDistanceNewWork(latitude, longitude);
        workDoingFragment.updateDistanceDoingWork(latitude, longitude);
        workCompletedFragment.updateDistanceCompleteWork(latitude, longitude);
        workCancelFragment.updateDistanceCancelWork(latitude, longitude);

    }

    public void addMarkerMap(double latitude, double longitude, String addName, BitmapDescriptor bitmapDescriptor) {
        if (mMap != null) {
            //BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

            LatLng myLocation = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(myLocation).title(addName)).setIcon(bitmapDescriptor);
        }
    }


    public void moveCameraMap(LatLng myLocation) {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        }
    }

    public double getLongtitude() {
        if (gpsService != null) {
            return gpsService.getLongitude();
        }
        return 0;
    }

    public double getLatitude() {
        if (gpsService != null) {
            return gpsService.getLatitude();
        }
        return 0;
    }
}
