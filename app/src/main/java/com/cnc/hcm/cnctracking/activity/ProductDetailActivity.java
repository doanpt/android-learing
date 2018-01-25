package com.cnc.hcm.cnctracking.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.ProductDetailAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.dialog.DialogGPSSetting;
import com.cnc.hcm.cnctracking.dialog.DialogNetworkSetting;
import com.cnc.hcm.cnctracking.model.GetProductDetailResult;
import com.cnc.hcm.cnctracking.model.SubmitProcessParam;
import com.cnc.hcm.cnctracking.model.UpdateProcessResult;
import com.cnc.hcm.cnctracking.model.UploadImageResult;
import com.cnc.hcm.cnctracking.service.GPSService;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAGG = ProductDetailActivity.class.getSimpleName();
    private static final int KEY_STEP_ONE = 1;
    private static final int KEY_STEP_TWO = 2;
    private static final int KEY_STEP_THREE = 3;
    private LinearLayout llViewControl;
    private FrameLayout flBlurView;
    private FloatingActionsMenu fabMenu;
    private TextView tvCompleteWork;
    private TextView tvName, tvLocation, tvHour, tvDistance, tvProductID;
    private FloatingActionButton fabNote, fabProduct, fabStep1, fabStep2, fabStep3, fabFinish;
    private DialogNetworkSetting dialogNetworkSetting;
    private DialogGPSSetting dialogGPSSetting;
    private GPSService gpsService;
    private ArrayList<String> arrInit;
    private ArrayList<String> arrProcess;
    private ArrayList<String> arrFinish;
    private ProductDetailAdapter initAdapter;
    private ProductDetailAdapter processAdapter;
    private ProductDetailAdapter finishAdapter;
    private RecyclerView initRecycler;
    private RecyclerView processRecycler;
    private RecyclerView finishRecycler;
    private String accessToken, deviceID, idTask, workName, address, distanceWork, timeWork;
    private LinearLayout llComplete;
    private TextView tvStartDate, tvEndDate, tvTotalTime;
    private ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        //11/01/2017 ADD by HoangIT START
        bindService(new Intent(this, GPSService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        initObject();
        //11/01/2017 ADD by HoangIT END
        initViews();
        getData();
    }

    private void getData() {
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        arrHeads.add(new MHead(Conts.KEY_DEVICE_ID, deviceID));
        ApiUtils.getAPIService(arrHeads).getDetailProduct(idTask).enqueue(new Callback<GetProductDetailResult>() {
            @Override
            public void onResponse(Call<GetProductDetailResult> call, Response<GetProductDetailResult> response) {
                Long code = response.body().getStatusCode();
                Log.d(TAGG, "getData.onResponse, code: " + code);
                if (code == Conts.RESPONSE_STATUS_OK) {
                    displayDetailWork(response.body());
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Get Detail error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetProductDetailResult> call, Throwable t) {
                Log.e(TAGG, "getData.onResponse", t);
                Toast.makeText(ProductDetailActivity.this, "Get Detail failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void visiableRecycler() {
        if (arrInit.size() == 0) {
            initRecycler.setVisibility(View.GONE);
        } else {
            initRecycler.setVisibility(View.VISIBLE);
        }
        if (arrProcess.size() == 0) {
            processRecycler.setVisibility(View.GONE);
        } else {
            processRecycler.setVisibility(View.VISIBLE);
        }
        if (arrFinish.size() == 0) {
            finishRecycler.setVisibility(View.GONE);
        } else {
            finishRecycler.setVisibility(View.VISIBLE);
        }
    }

    private void displayDetailWork(GetProductDetailResult body) {
        tvName.setText(workName);
        tvLocation.setText(address);
        tvDistance.setText(distanceWork);
        tvHour.setText(timeWork);
        arrInit.clear();
        arrProcess.clear();
        arrFinish.clear();
        arrInit.addAll(body.getResult().getBefore().getPhotos());
        arrProcess.addAll(body.getResult().getProcess().getPhotos());
        arrFinish.addAll(body.getResult().getAfter().getPhotos());
        if (body.getResult().getStatus().getId() == 3){
            llComplete.setVisibility(View.VISIBLE);
        }
        initAdapter.notifyDataSetChanged();
        processAdapter.notifyDataSetChanged();
        finishAdapter.notifyDataSetChanged();
        visiableRecycler();
    }

    //11/01/2017 ADD by HoangIT START
    private void initObject() {
        Intent passData = getIntent();
        accessToken = passData.getStringExtra(Conts.KEY_ACCESS_TOKEN);
        deviceID = passData.getStringExtra(Conts.KEY_PRODUCT_ID);
        idTask = passData.getStringExtra(Conts.KEY_ID_TASK);
        Log.d("ABC", accessToken + " - " + deviceID + " " + idTask);
        timeWork = passData.getStringExtra(Conts.KEY_WORK_TIME);
        address = passData.getStringExtra(Conts.KEY_WORK_LOCATION);
        workName = passData.getStringExtra(Conts.KEY_WORK_NAME);
        distanceWork = passData.getStringExtra(Conts.KEY_WORK_DISTANCE);
        Log.d("doan.pt", accessToken + " " + deviceID + " " + idTask + " detail-getextra");
        dialogGPSSetting = new DialogGPSSetting(this);
        dialogNetworkSetting = new DialogNetworkSetting(this);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            gpsService = ((GPSService.MyBinder) iBinder).getGPSService();
            if (gpsService != null) {
                gpsService.setProductDetailActivity(ProductDetailActivity.this);
            }

            Log.d(TAGG, "ServiceConnection at ProductDetailActivity, gpsService:= " + gpsService);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAGG, "onServiceDisconnected at ProductDetailActivity");
        }
    };
    //11/01/2017 ADD by HoangIT END

    private void initViews() {
        llViewControl = (LinearLayout) findViewById(R.id.view_control);
        flBlurView = (FrameLayout) findViewById(R.id.blurView);
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
//                llViewControl.setVisibility(View.VISIBLE);
                flBlurView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
//                llViewControl.setVisibility(View.GONE);
                flBlurView.setVisibility(View.GONE);
            }
        });
        imgBack=(ImageView) findViewById(R.id.img_back_work_detail);
        llComplete = (LinearLayout) findViewById(R.id.ll_complete_task);
        tvStartDate = findViewById(R.id.tv_start_date);
        tvEndDate = findViewById(R.id.tv_end_date);
        tvTotalTime = findViewById(R.id.tv_total_time);
        fabNote = (FloatingActionButton) findViewById(R.id.fab_note);
        fabProduct = (FloatingActionButton) findViewById(R.id.fab_add_product);
        fabStep1 = (FloatingActionButton) findViewById(R.id.fab_step_one);
        fabStep2 = (FloatingActionButton) findViewById(R.id.fab_step_two);
        fabStep3 = (FloatingActionButton) findViewById(R.id.fab_step_three);
        fabFinish = (FloatingActionButton) findViewById(R.id.fab_complete);
        tvName = findViewById(R.id.tv_work_name_work_detail);
        tvLocation = findViewById(R.id.tv_location_work_detail);
        tvHour = findViewById(R.id.tv_hour_work_detail);
        tvDistance = findViewById(R.id.tv_distance_work_detail);

        arrInit = new ArrayList<>();
        arrProcess = new ArrayList<>();
        arrFinish = new ArrayList<>();
        initAdapter = new ProductDetailAdapter(this, arrInit);
        processAdapter = new ProductDetailAdapter(this, arrProcess);
        finishAdapter = new ProductDetailAdapter(this, arrFinish);
        initRecycler = findViewById(R.id.rv_initial_condition);
        processRecycler = findViewById(R.id.rv_process_condition);
        finishRecycler = findViewById(R.id.rv_finish_condition);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.scrollToPosition(0);
        initRecycler.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager1.scrollToPosition(0);
        processRecycler.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager2.scrollToPosition(0);
        finishRecycler.setLayoutManager(layoutManager2);

        initRecycler.setAdapter(initAdapter);
        processRecycler.setAdapter(processAdapter);
        finishRecycler.setAdapter(finishAdapter);
        imgBack.setOnClickListener(this);
        fabNote.setOnClickListener(this);
        fabProduct.setOnClickListener(this);
        fabStep1.setOnClickListener(this);
        fabStep2.setOnClickListener(this);
        fabStep3.setOnClickListener(this);
        fabFinish.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (fabMenu.isExpanded()) {
            fabMenu.collapse();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_complete:
                completeWork();
                closeFabMenu();
                break;
            case R.id.fab_note:
                closeFabMenu();
                break;
            case R.id.fab_add_product:
                //TODO add product
                closeFabMenu();
                break;
            case R.id.fab_step_one:
                takePicture(KEY_STEP_ONE);
                closeFabMenu();
                break;
            case R.id.fab_step_two:
                takePicture(KEY_STEP_TWO);
                closeFabMenu();
                break;
            case R.id.fab_step_three:
                takePicture(KEY_STEP_THREE);
                closeFabMenu();
                break;
            case R.id.img_back_work_detail:
                onBackPressed();
                break;

        }
    }

    private void completeWork() {
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        arrHeads.add(new MHead(Conts.KEY_DEVICE_ID, deviceID));
        ApiUtils.getAPIService(arrHeads).completeProcess(idTask).enqueue(new Callback<UpdateProcessResult>() {
            @Override
            public void onResponse(Call<UpdateProcessResult> call, Response<UpdateProcessResult> response) {
                Long status = response.body().getStatusCode();
                Log.d(TAGG, "completeWork.onResponse, status: " + status);
                if (status == Conts.RESPONSE_STATUS_OK) {
                    CommonMethod.makeToast(ProductDetailActivity.this, "Complete OK!!!");
                    llComplete.setVisibility(View.VISIBLE);
                } else {
                    CommonMethod.makeToast(ProductDetailActivity.this, "Complete error!!!");
                }
            }

            @Override
            public void onFailure(Call<UpdateProcessResult> call, Throwable t) {
                CommonMethod.makeToast(ProductDetailActivity.this, "Upload status onFailure");
            }
        });
    }

    private void takePicture(int keyResult) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, keyResult);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo;
        File file = null;
        if (data != null) {
            photo = (Bitmap) data.getExtras().get("data");
            file = saveBitmapFile(photo, System.currentTimeMillis() + "");
        }
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            List<MHead> arrHeads = new ArrayList<>();
            arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));

            ApiUtils.getAPIService(arrHeads).uploadPhoto(body, idTask).enqueue(new Callback<UploadImageResult>() {
                @Override
                public void onResponse(Call<UploadImageResult> call, Response<UploadImageResult> response) {
                    Long code = response.body().getStatusCode();

                    Log.d(TAGG, "uploadPhoto.onResponse, code: " + code);
                    if (code == Conts.RESPONSE_STATUS_OK) {
                        final String url = response.body().getResult().getImageURL();
                        List<MHead> arrNewHeads = new ArrayList<>();
                        arrNewHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
                        arrNewHeads.add(new MHead(Conts.KEY_DEVICE_ID, deviceID));
                        Log.d("ABC", accessToken + " " + deviceID);
                        ArrayList<String> arrImage = new ArrayList();
                        final SubmitProcessParam param = new SubmitProcessParam();
                        if (requestCode == KEY_STEP_ONE) {
                            arrImage.addAll(arrInit);
                            arrImage.add(url);
                            param.setBefore(new SubmitProcessParam.Before());
                            param.getBefore().setPhotos(arrImage);
                        } else if (requestCode == KEY_STEP_TWO) {
                            arrImage.addAll(arrProcess);
                            arrImage.add(url);
                            param.setProcess(new SubmitProcessParam.Process());
                            param.getProcess().setPhotos(arrImage);
                        } else if (requestCode == KEY_STEP_THREE) {
                            arrImage.addAll(arrFinish);
                            arrImage.add(url);
                            param.setAfter(new SubmitProcessParam.After());
                            param.getAfter().setPhotos(arrImage);
                        }
                        Log.d("ABC", idTask + " " + param.getProcess());
                        ApiUtils.getAPIService(arrNewHeads).updateProcess(idTask, param).enqueue(new Callback<UpdateProcessResult>() {
                            @Override
                            public void onResponse(Call<UpdateProcessResult> call, Response<UpdateProcessResult> response) {
                                Long status = response.body().getStatusCode();
                                Log.d(TAGG, "updateProcess.onResponse, status: " + status);
                                if (status == Conts.RESPONSE_STATUS_OK) {
                                    if (requestCode == KEY_STEP_ONE) {
                                        arrInit.add(url);
                                        initAdapter.notifyDataSetChanged();
                                    } else if (requestCode == KEY_STEP_TWO) {
                                        arrProcess.add(url);
                                        processAdapter.notifyDataSetChanged();
                                    } else if (requestCode == KEY_STEP_THREE) {
                                        arrFinish.add(url);
                                        finishAdapter.notifyDataSetChanged();
                                    }
                                    visiableRecycler();
                                    CommonMethod.makeToast(ProductDetailActivity.this, "Update Step OK!!!");
                                } else {
                                    CommonMethod.makeToast(ProductDetailActivity.this, "Update Process Error");
                                }
                            }

                            @Override
                            public void onFailure(Call<UpdateProcessResult> call, Throwable t) {
                                if (requestCode == KEY_STEP_ONE) {
                                    arrInit.remove(arrInit.size() - 1);
                                } else if (requestCode == KEY_STEP_TWO) {
                                    arrProcess.remove(arrProcess.size() - 1);
                                } else if (requestCode == KEY_STEP_THREE) {
                                    arrFinish.remove(arrFinish.size() - 1);
                                }
                                CommonMethod.makeToast(ProductDetailActivity.this, "Update process Error, onFailure");
                            }
                        });
                    } else {
                        CommonMethod.makeToast(ProductDetailActivity.this, "Upload error");
                    }
                }

                @Override
                public void onFailure(Call<UploadImageResult> call, Throwable t) {
                    CommonMethod.makeToast(ProductDetailActivity.this, "Upload onFailure");
                }
            });
        } else {
            CommonMethod.makeToast(ProductDetailActivity.this, "Get image error");
        }
    }

    private File saveBitmapFile(Bitmap bitmap, String name) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        // String temp = null;
        File file = new File(extStorageDirectory, name + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, name + ".png");

        }

        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    private void closeFabMenu() {
        fabMenu.collapse();
    }

    //11/01/2017 ADD by HoangIT START
    private void showDialogNetworkSetting() {
        if (dialogNetworkSetting != null && !dialogNetworkSetting.isShowing() && !ProductDetailActivity.this.isDestroyed()) {
            dialogNetworkSetting.show();
        }
    }

    private void dismisDialogNetworkSetting() {
        if (dialogNetworkSetting != null && dialogNetworkSetting.isShowing() && !ProductDetailActivity.this.isDestroyed()) {
            dialogNetworkSetting.dismiss();
        }
    }

    public void handleNetworkSetting(boolean isNetworkConnected) {
        if (isNetworkConnected) {
            dismisDialogNetworkSetting();
        } else {
            showDialogNetworkSetting();
        }
    }

    private void showDialogGPSSetting() {
        if (dialogGPSSetting != null && !dialogGPSSetting.isShowing() && !ProductDetailActivity.this.isDestroyed()) {
            dialogGPSSetting.show();
        }
    }

    private void dismisDialogGPSSetting() {
        if (dialogGPSSetting != null && dialogGPSSetting.isShowing() && !ProductDetailActivity.this.isDestroyed()) {
            dialogGPSSetting.dismiss();
        }
    }

    public void handleGPSSetting(boolean statusGPS) {
        if (statusGPS) {
            dismisDialogGPSSetting();
        } else {
            showDialogGPSSetting();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
    //11/01/2017 ADD by HoangIT END
}
