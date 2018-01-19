package com.cnc.hcm.cnctracking.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.ProductDetailAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.dialog.DialogGPSSetting;
import com.cnc.hcm.cnctracking.dialog.DialogNetworkSetting;
import com.cnc.hcm.cnctracking.model.GetProductDetailResult;
import com.cnc.hcm.cnctracking.model.UploadImageResult;
import com.cnc.hcm.cnctracking.service.GPSService;
import com.cnc.hcm.cnctracking.util.BarcodeUtils;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.w3c.dom.Text;

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
    private FloatingActionButton fabNote, fabProduct, fabStep1, fabStep2, fabStep3;
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
    private String accessToken;
    private String deviceID;
    private String idTask;

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
                if (code == Conts.RESPONSE_STATUS_OK) {
                    //TODO display view
                    displayDetailWork(response.body());
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Get Detail error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetProductDetailResult> call, Throwable t) {

            }
        });
    }

    private void displayDetailWork(GetProductDetailResult body) {

    }

    //11/01/2017 ADD by HoangIT START
    private void initObject() {
        Intent passData = getIntent();
        accessToken = passData.getStringExtra(Conts.KEY_ACCESS_TOKEN);
        deviceID = passData.getStringExtra(Conts.KEY_PRODUCT_ID);
        idTask = passData.getStringExtra(Conts.KEY_ID_TASK);
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
                llViewControl.setVisibility(View.VISIBLE);
                flBlurView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                llViewControl.setVisibility(View.GONE);
                flBlurView.setVisibility(View.GONE);
            }
        });

        tvCompleteWork = (TextView) findViewById(R.id.tv_complete_work);
        tvCompleteWork.setOnClickListener(this);

        fabNote = (FloatingActionButton) findViewById(R.id.fab_note);
        fabProduct = (FloatingActionButton) findViewById(R.id.fab_add_product);
        fabStep1 = (FloatingActionButton) findViewById(R.id.fab_step_one);
        fabStep2 = (FloatingActionButton) findViewById(R.id.fab_step_two);
        fabStep3 = (FloatingActionButton) findViewById(R.id.fab_step_three);
        tvName = findViewById(R.id.tv_work_name_work_detail);
        tvLocation = findViewById(R.id.tv_location_work_detail);
        tvHour = findViewById(R.id.tv_hour_work_detail);
        tvDistance = findViewById(R.id.tv_distance_work_detail);

        String url = "http://35.198.195.55:3001/uploads/user/image/12694730_1027043980685790_4855700393915351375.jpg";
        arrInit = new ArrayList<>();
        arrProcess = new ArrayList<>();
        arrFinish = new ArrayList<>();
        arrFinish.add(url);
        arrFinish.add(url);
        arrFinish.add(url);
        arrFinish.add(url);
        arrFinish.add(url);
        arrInit.add(url);
        arrInit.add(url);
        arrInit.add(url);
        arrInit.add(url);
        arrInit.add(url);

        arrProcess.add(url);
        arrProcess.add(url);
        arrProcess.add(url);
        arrProcess.add(url);
        arrProcess.add(url);
        arrProcess.add(url);
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

        fabNote.setOnClickListener(this);
        fabProduct.setOnClickListener(this);
        fabStep1.setOnClickListener(this);
        fabStep2.setOnClickListener(this);
        fabStep3.setOnClickListener(this);
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
            case R.id.tv_complete_work:
                //TODO complete work
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
                takePicture(KEY_STEP_ONE);
                closeFabMenu();
                break;
            case R.id.fab_step_three:
                takePicture(KEY_STEP_ONE);
                closeFabMenu();
                break;

        }
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
        File file = null;// initialize file here
        if (requestCode == KEY_STEP_ONE) {
            photo = (Bitmap) data.getExtras().get("data");
            file = saveBitmapFile(photo, "KEY_STEP_ONE_" + System.currentTimeMillis());
        }
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            List<MHead> arrHeads = new ArrayList<>();
            arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));

            ApiUtils.getAPIService(arrHeads).uploadPhoto(body, idTask).enqueue(new Callback<UploadImageResult>() {
                @Override
                public void onResponse(Call<UploadImageResult> call, Response<UploadImageResult> response) {
                    Long code = response.body().getStatusCode();
                    if (code == Conts.RESPONSE_STATUS_OK) {
                        String url = response.body().getResult().getImageURL();
                        if (requestCode == KEY_STEP_ONE) {
                            arrInit.add(url);
                        } else if (requestCode == KEY_STEP_TWO) {
                            arrProcess.add(url);
                        } else if (requestCode == KEY_STEP_THREE) {
                            arrFinish.add(url);
                        }
                        //FIXME làm sao để lúc post biết đc post những thằng nào.
                    } else {
                        CommonMethod.makeToast(ProductDetailActivity.this, "Upload status error");
                    }
                }

                @Override
                public void onFailure(Call<UploadImageResult> call, Throwable t) {

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
