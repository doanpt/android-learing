package com.cnc.hcm.cnctracking.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.AddProductActivity;
import com.cnc.hcm.cnctracking.activity.ChangeTimeActivity;
import com.cnc.hcm.cnctracking.activity.MainActivity;
import com.cnc.hcm.cnctracking.activity.ProductDetailActivity;
import com.cnc.hcm.cnctracking.adapter.WorkDetailPageAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.fragment.WorkDetailDeviceFragment;
import com.cnc.hcm.cnctracking.fragment.WorkDetailServiceFragment;
import com.cnc.hcm.cnctracking.model.AddContainProductResult;
import com.cnc.hcm.cnctracking.model.CheckContainProductResult;
import com.cnc.hcm.cnctracking.model.CompleteTicketResponse;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
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
public class DialogDetailTaskFragment extends ViewPagerBottomSheetDialogFragment
        implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = DialogDetailTaskFragment.class.getSimpleName();

    // Float action button
    private LinearLayout llViewControl;
    private LinearLayout llTaskTime;
    private FrameLayout flBlurView;
    private FloatingActionsMenu fabMenu;
    private TextView tvComplete;
    private FloatingActionButton fabScanQR, fabAddProduct;

    private LinearLayout llImageService, llFindWay;
    private RelativeLayout rlDetail;
    private ImageView imvBack, imvImageService, imvTime;
    private TextView tvDetailTask;
    private ViewPager viewPager;

    private String idTask = Conts.BLANK;
    private String customerId;
    private double latitude;
    private double longitude;

    private WorkDetailPageAdapter mWorkDetailPageAdapter;

    private MainActivity mainActivity;

    private ProgressDialog mProgressDialog;
    private GetTaskDetailResult getTaskDetailResult;
    private TextView tv_title_item_work, tv_address_item_work, tv_time_item_work;

    private List<TaskDetailLoadedListener> mTaskDetailLoadedListener = new ArrayList<>();
    private TextView tv_completed_ticket;
    private Handler mHandler;

    public void setTaskDetailLoadedListener(TaskDetailLoadedListener taskDetailLoadedListener) {
        mTaskDetailLoadedListener.add(taskDetailLoadedListener);
        if (getTaskDetailResult != null) {
            taskDetailLoadedListener.onTaskDetailLoaded(getTaskDetailResult);
        }
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public GetTaskDetailResult getGetTaskDetailResult() {
        return getTaskDetailResult;
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
        mHandler = new Handler();
        mTaskDetailLoadedListener.clear();

        return view;
    }

    public void showDialogUnAssignedTask() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                final AlertDialog dialog = builder.create();

                View view = getLayoutInflater().inflate(R.layout.dialog_cancel, null);
                dialog.setView(view);
                TextView tv_message = view.findViewById(R.id.tv_message);
                tv_message.setText(getContext().getResources().getString(R.string.you_are_remove_from_ticket));
                view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        DialogDetailTaskFragment.this.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    public void showDialogCancelTicket(String message) {
        if (TextUtils.isEmpty(message)) {
            message = "Đơn hàng bị hủy.";
        }

        final String finalMessage = message;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                final AlertDialog dialog = builder.create();

                View view = getLayoutInflater().inflate(R.layout.dialog_cancel, null);
                dialog.setView(view);
                TextView tv_message = view.findViewById(R.id.tv_message);
                tv_message.setText(finalMessage);
                view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        DialogDetailTaskFragment.this.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    private void initViews(View view) {
        llImageService = (LinearLayout) view.findViewById(R.id.ll_image_service);
        imvBack = (ImageView) view.findViewById(R.id.imv_back_detail_task_dialog_bottom);
        imvBack.setOnClickListener(this);
        imvImageService = (ImageView) view.findViewById(R.id.imv_image_service);
        imvTime = (ImageView) view.findViewById(R.id.imv_time);
//        imvDistance = (ImageView) view.findViewById(R.id.imv_distance);
        rlDetail = (RelativeLayout) view.findViewById(R.id.rl_detail_task);

        tvDetailTask = (TextView) view.findViewById(R.id.tv_detail_task);
        tvDetailTask.setOnClickListener(this);
        llFindWay = (LinearLayout) view.findViewById(R.id.ll_find_way);
        llFindWay.setOnClickListener(this);

        tv_completed_ticket = view.findViewById(R.id.tv_completed_ticket);

        // Float action button
        llViewControl = (LinearLayout) view.findViewById(R.id.view_control);
        llTaskTime = (LinearLayout) view.findViewById(R.id.ll_task_time);
        llTaskTime.setOnClickListener(this);
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

        tvComplete = (TextView) view.findViewById(R.id.tv_complete_work_float_button);
        tvComplete.setOnClickListener(this);

        fabAddProduct = (FloatingActionButton) view.findViewById(R.id.fab_add_product);
        fabAddProduct.setOnClickListener(this);
        fabScanQR = (FloatingActionButton) view.findViewById(R.id.fab_scan_qr);
        fabScanQR.setOnClickListener(this);

        tv_title_item_work = view.findViewById(R.id.tv_title_item_work);
        tv_address_item_work = view.findViewById(R.id.tv_address_item_work);
        tv_time_item_work = view.findViewById(R.id.tv_time_item_work);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mWorkDetailPageAdapter = new WorkDetailPageAdapter(getChildFragmentManager());
        WorkDetailServiceFragment workDetailServiceFragment = new WorkDetailServiceFragment();
//        workDetailServiceFragment.setOnPayCompletedListener(new WorkDetailServiceFragment.OnPayCompletedListener() {
//            @Override
//            public void onPayCompleted() {
//                if (fabMenu != null) {
//                    fabMenu.setVisibility(View.GONE);
//                }
//            }
//        });
        mWorkDetailPageAdapter.addFragment(workDetailServiceFragment);
        mWorkDetailPageAdapter.addFragment(new WorkDetailDeviceFragment());
        viewPager.setAdapter(mWorkDetailPageAdapter);
        BottomSheetUtils.setupViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        setExpaned(true);
        Log.e("VIM", "onResume(), idTask: " + idTask);
        loadInforTask();
    }

    public void loadInforTask() {
        viewPager.setCurrentItem(0);
        tryGetTaskDetail(UserInfo.getInstance(getActivity().getApplicationContext()).getAccessToken(), idTask);
        if (fabMenu != null && fabMenu.isExpanded()) {
            fabMenu.collapse();
        }
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
                t.printStackTrace();
                CommonMethod.makeToast(getContext(), t.getMessage() != null ? t.getMessage() : "onFailure");
            }
        });
    }

    private void onTaskInfoLoaded(GetTaskDetailResult getTaskDetailResult) {
        try {
            if (getTaskDetailResult != null && getTaskDetailResult.result != null) {
                tv_title_item_work.setText(getTaskDetailResult.result.title + "");
                String date = getTaskDetailResult.result.appointmentDate;
                if (!TextUtils.isEmpty(date)) {
                    String time = CommonMethod.formatTimeFromServerToString(date);
                    tv_time_item_work.setText(time);
                }
                if (getTaskDetailResult.result.address != null) {
                    tv_address_item_work.setText(getTaskDetailResult.result.address.getStreet() + "");
                } else if (getTaskDetailResult.result.customer.address != null) {
                    tv_address_item_work.setText(getTaskDetailResult.result.customer.address.street + "");
                }
                if (getTaskDetailResult.result.customer != null) {
                    customerId = getTaskDetailResult.result.customer._id;
                }

                try {
                    fabMenu.setVisibility(getTaskDetailResult.result.status._id == Conts.TYPE_COMPLETE_TASK ? View.GONE : View.VISIBLE);
                    tv_completed_ticket.setVisibility(getTaskDetailResult.result.status._id == Conts.TYPE_COMPLETE_TASK ? View.VISIBLE : View.GONE);
                } catch (Exception e) {
                    Log.e(TAG, "onTaskInfoLoaded() --> Exception occurs.", e);
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
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismisDialogLoading();
        setExpaned(false);
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
//            imvDistance.setVisibility(View.GONE);
        } else {
            llImageService.setVisibility(View.VISIBLE);
            rlDetail.setVisibility(View.VISIBLE);
            imvTime.setVisibility(View.VISIBLE);
//            imvDistance.setVisibility(View.VISIBLE);
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
//        imvDistance.setVisibility(View.GONE);
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
                break;
            case R.id.ll_find_way:
                handleFindWayAction();
                break;
            case R.id.ll_task_time:
                PopupMenu popup = new PopupMenu(getMainActivity(), view);
                popup.getMenuInflater().inflate(R.menu.task_time_popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(this);
                popup.show();
                break;
            case R.id.tv_complete_work_float_button:
                handleCompleteWordAction();
                break;
            case R.id.fab_add_product:
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                intent.putExtra(Conts.KEY_CUSTOMER_ID, customerId);
                intent.putExtra(Conts.KEY_ID_TASK, idTask);
                intent.putExtra(Conts.KEY_WORK_NAME, tv_title_item_work.getText().toString());
                intent.putExtra(Conts.KEY_WORK_LOCATION, tv_address_item_work.getText().toString());
                intent.putExtra(Conts.KEY_WORK_TIME, tv_time_item_work.getText().toString());
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

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_task_change_time:
                handleTaskChangeTimeAction();
                break;
        }
        return true;
    }


    private void handleTaskChangeTimeAction() {
        Intent intentNote = new Intent(getMainActivity(), ChangeTimeActivity.class);
        try {
            intentNote.putExtra(Conts.KEY_ID_TASK, idTask);
            intentNote.putExtra(Conts.KEY_WORK_TIME, getTaskDetailResult.result.appointmentDate);
        } catch (Exception e) {
            Log.e(TAG, "handleTaskChangeTimeAction(), Exception: " + e);
        }
        if (mainActivity != null) {
            mainActivity.startActivityForResult(intentNote, ChangeTimeActivity.CODE_CHANGE_TIME);
        }
    }

    private void handleFindWayAction() {
        GetTaskDetailResult.Result result = getTaskDetailResult.result;
        if (mainActivity != null && result != null) {
            if (result.address != null) {
                if (result.address.getLocation() != null) {
                    CommonMethod.actionFindWayInMapApp(getContext(), mainActivity.getLatitude(),
                            mainActivity.getLongtitude(), result.address.getLocation().latitude, result.address.getLocation().longitude);
                } else {
                    String locationName = result.address.getStreet();
                    Address address = CommonMethod.getLocationFromLocationName(getContext(), locationName);
                    if (address != null) {
                        CommonMethod.actionFindWayInMapApp(getContext(), mainActivity.getLatitude(),
                                mainActivity.getLongtitude(), address.getLatitude(), address.getLongitude());
                    }
                }
            } else {
                if (result.customer != null && result.customer.address != null && result.customer.address.location != null) {

                    CommonMethod.actionFindWayInMapApp(getContext(), mainActivity.getLatitude(),
                            mainActivity.getLongtitude(), result.customer.address.location.latitude, result.customer.address.location.longitude);
                }
            }
        }
    }

    private void handleCompleteWordAction() {
        boolean isComplete = true;
        String message = "Đang xử lý hoàn thành ticket";
        try {
            Log.d(TAG, "onTaskDetailLoaded, invoice.status._id: " + getTaskDetailResult.result.invoice.status._id);
            if (getTaskDetailResult.result.invoice.status._id <= 1) {
                isComplete = false;
                message = "Khách hàng phải thanh toán trước khi đóng ticket";
            }
        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded(), Exception1: " + e);
            message = "Lỗi khi check trạng thái thanh toán";
        }

        try {
            GetTaskDetailResult.Result.Process[] process = getTaskDetailResult.result.process;
            if (process == null || process.length < 1) {
                isComplete = false;
                message = "Chưa có thiết bị nào được sửa chữa";
            } else {
                for (int i = 0; i < process.length; i++) {
                    if (process[i].status._id < 3) {
                        isComplete = false;
                        message = "Thiết bị thứ " + (i + 1) + " chưa được sửa xong";
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded(), Exception2: " + e);
            message = "Lỗi khi check trạng thái từng thiết bị";
        }

        if (isComplete) {
            finishTicket();
        }

        CommonMethod.makeToast(getActivity(), message);
        fabMenu.collapse();
    }

    private void finishTicket() {
        if (getTaskDetailResult != null && getTaskDetailResult.result != null) {
            String idTask = getTaskDetailResult.result._id;
            List<MHead> arrHeads = new ArrayList<>();
            arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, UserInfo.getInstance(getActivity()).getAccessToken()));
            ApiUtils.getAPIService(arrHeads).completeTicket(idTask).enqueue(new Callback<CompleteTicketResponse>() {
                @Override
                public void onResponse(Call<CompleteTicketResponse> call, Response<CompleteTicketResponse> response) {
                    if (response.isSuccessful()) {
                        CompleteTicketResponse completeTicketResponse = response.body();
                        if (completeTicketResponse.getStatusCode() == Conts.RESPONSE_STATUS_OK) {
                            CommonMethod.makeToast(getActivity(), "Xử lý hoàn thành ticket thành công");
                            fabMenu.setVisibility(View.GONE);
                            tv_completed_ticket.setVisibility(View.VISIBLE);
                        } else {
                            CommonMethod.makeToast(getActivity(), "Bạn chưa hoàn thành dịch vụ");
                        }
                    }
                }

                @Override
                public void onFailure(Call<CompleteTicketResponse> call, Throwable t) {
                    CommonMethod.makeToast(getActivity(), "Xử lý hoàn thành ticket thất bại");
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                final String content = result.getContents();
                try {
                    GetTaskDetailResult.Result.Process[] processes = getTaskDetailResult.result.process;
                    for (int i = 0; i < processes.length; i++) {
                        if (TextUtils.equals(content, processes[i].device._id)) {
                            showDialogDeviceExisted();
                            return;
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onActivityResult()", e);
                }

                List<MHead> arrHeads = new ArrayList<>();
                arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, UserInfo.getInstance(getActivity()).getAccessToken()));
                arrHeads.add(new MHead(Conts.KEY_CUSTOMER_ID, customerId));

                ApiUtils.getAPIService(arrHeads).getProductById(content).enqueue(new Callback<CheckContainProductResult>() {
                    @Override
                    public void onResponse(Call<CheckContainProductResult> call, Response<CheckContainProductResult> response) {
                        Long status = response.body().getStatusCode();
                        if (status == Conts.RESPONSE_STATUS_OK) {
                            addDeviceToTask(content);
                        } else {
                            showDialogAddDevice(content);
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckContainProductResult> call, Throwable t) {
                        CommonMethod.makeToast(getActivity(), "getProductById.onFailure()");
                    }
                });
            }
        }
    }

    private void addDeviceToTask(final String deviceId) {
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, UserInfo.getInstance(getActivity()).getAccessToken()));
        arrHeads.add(new MHead(Conts.KEY_DEVICE_ID, deviceId));
        ApiUtils.getAPIService(arrHeads).addProductContain(idTask).enqueue(new Callback<AddContainProductResult>() {
            @Override
            public void onResponse(Call<AddContainProductResult> call, Response<AddContainProductResult> response) {
                int status = response.code();
                if (status == Conts.RESPONSE_STATUS_OK) {
                    CommonMethod.makeToast(getActivity(), "Đã thêm thiết bị vào đơn hàng thành công.");
                    showProductDetailActivity(deviceId);
                } else {
                    CommonMethod.makeToast(getActivity(), "Add thiết bị vào đơn hàng không thành công");
                }
            }

            @Override
            public void onFailure(Call<AddContainProductResult> call, Throwable t) {
                String cause = "";
                cause = t.getCause().toString();
                CommonMethod.makeToast(getActivity(), "addProductContain() -> onFailure:" + cause);
            }
        });
    }

    private void showDialogDeviceExisted() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Thiết bị đã có sẵn trong đơn hàng.");
        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showProductDetailActivity(String deviceId) {
        Intent productDetail = new Intent(getMainActivity(), ProductDetailActivity.class);
        productDetail.putExtra(Conts.KEY_PRODUCT_ID, deviceId);
        productDetail.putExtra(Conts.KEY_ACCESS_TOKEN, UserInfo.getInstance(getActivity()).getAccessToken());
        productDetail.putExtra(Conts.KEY_ID_TASK, idTask);
        String workName = Conts.BLANK;
        String workLocation = Conts.BLANK;
        String workTime = Conts.BLANK;

        try {
            if (getTaskDetailResult != null && getTaskDetailResult.result != null) {
                workName += getTaskDetailResult.result.title;
                String date = getTaskDetailResult.result.appointmentDate;
                if (!TextUtils.isEmpty(date)) {
                    workTime += CommonMethod.formatTimeFromServerToString(date);
                }
                if (getTaskDetailResult.result.address != null) {
                    workLocation += getTaskDetailResult.result.address.getStreet();
                } else if (getTaskDetailResult.result.customer.address != null) {
                    workLocation += getTaskDetailResult.result.customer.address.street;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "showProductDetailActivity() --> Exception occurs.", e);
        }
        productDetail.putExtra(Conts.KEY_WORK_NAME, workName);
        productDetail.putExtra(Conts.KEY_WORK_LOCATION, workLocation);
        productDetail.putExtra(Conts.KEY_WORK_TIME, workTime);
        startActivity(productDetail);
    }

    private void showDialogAddDevice(final String deviceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Thiết bị không tồn tại. Thêm mới thiết bị vào hệ thống?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                handleConfirmAddNewDeviceAction(deviceId);
            }
        });
        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void handleConfirmAddNewDeviceAction(String deviceId) {
        Intent intent = new Intent(getActivity(), AddProductActivity.class);
        intent.putExtra(Conts.KEY_CUSTOMER_ID, customerId);
        intent.putExtra(Conts.KEY_ID_TASK, idTask);
        intent.putExtra(Conts.KEY_WORK_NAME, tv_title_item_work.getText().toString());
        intent.putExtra(Conts.KEY_WORK_LOCATION, tv_address_item_work.getText().toString());
        intent.putExtra(Conts.KEY_WORK_TIME, tv_time_item_work.getText().toString());
        intent.putExtra(Conts.KEY_DEVICE_ID, deviceId);
        startActivity(intent);
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public void myLocationHere(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public interface TaskDetailLoadedListener {
        void onTaskDetailLoaded(GetTaskDetailResult getTaskDetailResult);
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
