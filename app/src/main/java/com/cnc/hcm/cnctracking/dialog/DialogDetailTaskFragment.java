package com.cnc.hcm.cnctracking.dialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.AddProductActivity;
import com.cnc.hcm.cnctracking.adapter.WorkDetailPageAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.fragment.WorkDetailDeviceFragment;
import com.cnc.hcm.cnctracking.fragment.WorkDetailServiceFragment;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.service.GPSService;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import biz.laenger.android.vpbs.BottomSheetUtils;
import biz.laenger.android.vpbs.ViewPagerBottomSheetDialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class DialogDetailTaskFragment extends ViewPagerBottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = DialogDetailTaskFragment.class.getSimpleName();

    // Float action button
    private LinearLayout llViewControl;
    private FrameLayout flBlurView;
    private FloatingActionsMenu fabMenu;
    private TextView tvComplete;
    private FloatingActionButton fabScanQR, fabAddProduct;

    private LinearLayout llImageService, llFindWay;
    private RelativeLayout rlDetail;
    private ImageView imvBack, imvImageService, imvTime, imvDistance;
    private TextView tvDetailTask;
    private ViewPager viewPager;

    private String idTask = Conts.BLANK;
    private String customerId;
    private double latitude;
    private double longitude;

    private WorkDetailPageAdapter mWorkDetailPageAdapter;

    private ProgressDialog mProgressDialog;
    private GetTaskDetailResult getTaskDetailResult;
    private TextView tv_title_item_work, tv_address_item_work, tv_time_item_work, tv_distance_item_work;

    private List<TaskDetailLoadedListener> mTaskDetailLoadedListener = new ArrayList<>();

    public void setTaskDetailLoadedListener(TaskDetailLoadedListener taskDetailLoadedListener) {
        mTaskDetailLoadedListener.add(taskDetailLoadedListener);
        taskDetailLoadedListener.onTaskDetailLoaded(getTaskDetailResult);
    }

    private List<LocationUpdateListener> mLocationUpdateListeners = new ArrayList<>();

    public void setLocationUpdateListeners(LocationUpdateListener locationUpdateListener) {
        mLocationUpdateListeners.add(locationUpdateListener);
        locationUpdateListener.onLocationUpdate(latitude, longitude);
    }

//    private GPSService gpsService;
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder iBinder) {
//            Log.e(TAG, "serviceConnection at DialogDetailTaskFragment is Connected");
//            gpsService = ((GPSService.MyBinder) iBinder).getGPSService();
//            if (gpsService != null) {
//                gpsService.setDialogDetailTaskFragment(DialogDetailTaskFragment.this);
//            } else {
//                Log.e(TAG, "serviceConnection at DialogDetailTaskFragment is null");
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.e(TAG, "serviceConnection at DialogDetailTaskFragment is Disconnected");
//        }
//    };

    public DialogDetailTaskFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("VIM", "onCreateView(), idTask: " + idTask);
        View view = inflater.inflate(R.layout.dialog_bottom_sheet, container);
        initViews(view);
        mTaskDetailLoadedListener.clear();
        mLocationUpdateListeners.clear();

        return view;
    }

    private void initViews(View view) {
        llImageService = (LinearLayout) view.findViewById(R.id.ll_image_service);
        imvBack = (ImageView) view.findViewById(R.id.imv_back_detail_task_dialog_bottom);
        imvBack.setOnClickListener(this);
        imvImageService = (ImageView) view.findViewById(R.id.imv_image_service);
        imvTime = (ImageView) view.findViewById(R.id.imv_time);
        imvDistance = (ImageView) view.findViewById(R.id.imv_distance);
        rlDetail = (RelativeLayout) view.findViewById(R.id.rl_detail_task);

        tvDetailTask = (TextView) view.findViewById(R.id.tv_detail_task);
        tvDetailTask.setOnClickListener(this);
        llFindWay = (LinearLayout) view.findViewById(R.id.ll_find_way);
        llFindWay.setOnClickListener(this);

        // Float action button
        llViewControl = (LinearLayout) view.findViewById(R.id.view_control);
        flBlurView = (FrameLayout) view.findViewById(R.id.blurView);
        fabMenu = (FloatingActionsMenu) view.findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                llViewControl.setVisibility(View.VISIBLE);
                flBlurView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                llViewControl.setVisibility(View.GONE);
                flBlurView.setVisibility(View.GONE);
            }
        });

        tvComplete = (TextView) view.findViewById(R.id.tv_complete_work);
        tvComplete.setOnClickListener(this);

        fabAddProduct = (FloatingActionButton) view.findViewById(R.id.fab_add_product);
        fabAddProduct.setOnClickListener(this);
        fabScanQR = (FloatingActionButton) view.findViewById(R.id.fab_scan_qr);
        fabScanQR.setOnClickListener(this);

        tv_title_item_work = view.findViewById(R.id.tv_title_item_work);
        tv_address_item_work = view.findViewById(R.id.tv_address_item_work);
        tv_time_item_work = view.findViewById(R.id.tv_time_item_work);
        tv_distance_item_work = view.findViewById(R.id.tv_distance_item_work);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mWorkDetailPageAdapter = new WorkDetailPageAdapter(getChildFragmentManager());
        mWorkDetailPageAdapter.addFragment(new WorkDetailServiceFragment());
        mWorkDetailPageAdapter.addFragment(new WorkDetailDeviceFragment());
        viewPager.setAdapter(mWorkDetailPageAdapter);
        BottomSheetUtils.setupViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("VIM", "onResume(), idTask: " + idTask);
        tryGetTaskDetail(UserInfo.getInstance(getActivity().getApplicationContext()).getAccessToken(), idTask);

    }

    private void tryGetTaskDetail(String accessToken, String idTask) {
        showDialogLoadding();
        Log.e(TAG, "tryGetTaskDetail(), accessToken: " + accessToken + ", idTask: " + idTask);
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        ApiUtils.getAPIService(arrHeads).getTaskDetails(idTask).enqueue(new Callback<GetTaskDetailResult>() {
            @Override
            public void onResponse(Call<GetTaskDetailResult> call, Response<GetTaskDetailResult> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    getTaskDetailResult = response.body();
                    Log.e(TAG, "tryGetTaskDetail.onResponse(), statusCode: " + statusCode + ", getTaskDetailResult: " + getTaskDetailResult);
                    onTaskInfoLoaded(getTaskDetailResult);
                    dismisDialogLoading();
                }
            }

            @Override
            public void onFailure(Call<GetTaskDetailResult> call, Throwable t) {
                dismisDialogLoading();
                Log.e(TAG, "tryGetTaskDetail.onFailure() --> " + t);
            }
        });
    }

    private void onTaskInfoLoaded(GetTaskDetailResult getTaskDetailResult) {
        try {
            if (getTaskDetailResult != null && getTaskDetailResult.result != null) {
                tv_title_item_work.setText(getTaskDetailResult.result.title + "");
                String date = getTaskDetailResult.result.createdDate;
                if (!TextUtils.isEmpty(date)) {
                    tv_time_item_work.setText(date.substring(date.indexOf("T") + 1, date.indexOf("T") + 6));
                }
                if (getTaskDetailResult.result.address != null) {
                    tv_address_item_work.setText(getTaskDetailResult.result.address.street + "");
                } else if (getTaskDetailResult.result.customer.address != null) {
                    tv_address_item_work.setText(getTaskDetailResult.result.customer.address.street + "");
                }
                tv_distance_item_work.setText("0 km");    //TODO update distance later
                if (getTaskDetailResult.result.customer != null) {
                    customerId = getTaskDetailResult.result.customer._id;
                }
                for (TaskDetailLoadedListener taskDetailLoadedListener : mTaskDetailLoadedListener) {
                    if (taskDetailLoadedListener != null) {
                        taskDetailLoadedListener.onTaskDetailLoaded(getTaskDetailResult);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onTaskInfoLoaded() --> Exception occurs.", e);
        }
    }

    private void showDialogLoadding() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loadding));
        mProgressDialog.show();
    }

    private void dismisDialogLoading() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    //TODO edit them at here
    @Override
    public int getTheme() {
        return super.getTheme();
    }

    @Override
    public void onSlideBottomSheet(@NonNull View bottomSheet, float slideOffset) {
        if (slideOffset > 0) {
            llImageService.setVisibility(View.GONE);
            rlDetail.setVisibility(View.GONE);
            imvTime.setVisibility(View.GONE);
            imvDistance.setVisibility(View.GONE);
        } else {
            llImageService.setVisibility(View.VISIBLE);
            rlDetail.setVisibility(View.VISIBLE);
            imvTime.setVisibility(View.VISIBLE);
            imvDistance.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStateChangedBottomSheet(@NonNull View bottomSheet, int newState) {
    }

    @Override
    public void setVisibilityView() {
        llImageService.setVisibility(View.GONE);
        rlDetail.setVisibility(View.GONE);
        imvTime.setVisibility(View.GONE);
        imvDistance.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back_detail_task_dialog_bottom:
                dismiss();
                break;
            case R.id.tv_detail_task:
                setExpaned(true);
                showExpaned();
                setExpaned(false);
                break;
            case R.id.ll_find_way:
                CommonMethod.actionFindWayInMapApp(getContext(), 0, 0, 0, 0);
                break;
            case R.id.tv_complete_work:
                handleCompleteWordAction();
                break;
            case R.id.fab_add_product:
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                intent.putExtra(Conts.KEY_CUSTOMER_ID, customerId);
                intent.putExtra(Conts.KEY_ID_TASK, idTask);
                intent.putExtra(Conts.KEY_WORK_NAME,tv_title_item_work.getText().toString());
                intent.putExtra(Conts.KEY_WORK_LOCATION,tv_address_item_work.getText().toString());
                intent.putExtra(Conts.KEY_WORK_TIME,tv_time_item_work.getText().toString());
                intent.putExtra(Conts.KEY_WORK_DISTANCE,tv_distance_item_work.getText().toString());
                startActivity(intent);
                fabMenu.collapse();
                break;
            case R.id.fab_scan_qr:
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.initiateScan();
                fabMenu.collapse();
                break;
        }
    }

    private void handleCompleteWordAction() {
        boolean isComplete = true;
        try {
            GetTaskDetailResult.Result.Process[] process = getTaskDetailResult.result.process;
            for (int i = 0; i < process.length; i++) {
                if(process[i].status._id < 3) {
                    isComplete = false;
                    break;
                }
            }
        } catch (Exception e) {
        }
        CommonMethod.makeToast(getActivity(), isComplete ? "Completed Work" : "Some devices are not completed");
        fabMenu.collapse();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
            } else {
                String content = result.getContents();
                String format = result.getFormatName();
                CommonMethod.makeToast(getActivity(), content + ", " + format);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public void myLocationHere(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        for (LocationUpdateListener locationUpdateListener : mLocationUpdateListeners) {
            if (locationUpdateListener != null) {
                locationUpdateListener.onLocationUpdate(latitude, longitude);
            }
        }
    }

    public interface TaskDetailLoadedListener {
        void onTaskDetailLoaded(GetTaskDetailResult getTaskDetailResult);
    }

    public interface LocationUpdateListener {
        void onLocationUpdate(double latitude, double longitude);
    }
}
