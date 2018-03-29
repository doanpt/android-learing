package com.cnc.hcm.cnctracking.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.ProductDetailAdapter;
import com.cnc.hcm.cnctracking.adapter.ProductProcessAdapter;
import com.cnc.hcm.cnctracking.adapter.ServiceProcessAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.dialog.DialogGPSSetting;
import com.cnc.hcm.cnctracking.dialog.DialogInfor;
import com.cnc.hcm.cnctracking.dialog.DialogNetworkSetting;
import com.cnc.hcm.cnctracking.model.GetProductDetailResult;
import com.cnc.hcm.cnctracking.model.Services;
import com.cnc.hcm.cnctracking.model.SubmitProcessParam;
import com.cnc.hcm.cnctracking.model.TraddingProduct;
import com.cnc.hcm.cnctracking.model.UpdateProcessResult;
import com.cnc.hcm.cnctracking.model.UploadImageResult;
import com.cnc.hcm.cnctracking.service.GPSService;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private static final int KEY_RESULT_FROM_LIST_SERVICE_ACTIVITY = 1000;
    private static final int KEY_PROCESS_SERVICE = 4;
    private static final int KEY_PROCESS_PRODUCT = 5;
    private static final int KEY_ADD_NOTE = 6;
    private LinearLayout llViewControl;
    private FrameLayout flBlurView;
    private FloatingActionsMenu fabMenu;
    private TextView tvCompleteWork, tvDeviceCode, tvDeviceName, tvName, tvLocation, tvHour, tvProductID, llCompleteWork, tvNoteWork;
    private FloatingActionButton fabNote, fabProduct, fabStep1, fabStep2, fabStep3;
    private DialogNetworkSetting dialogNetworkSetting;
    private DialogGPSSetting dialogGPSSetting;
    private GPSService gpsService;
    private ArrayList<String> arrInit, arrProcess, arrFinish;
    private ArrayList<TraddingProduct.Result> arrTrading;
    private ArrayList<Services.Result> arrService;
    private ProductDetailAdapter initAdapter, processAdapter, finishAdapter;
    private ProductProcessAdapter productAdapter;
    private ServiceProcessAdapter serviceAdapter;
    private RecyclerView rvInitRecycler, rvProcessRecycler, rvFinishRecycler, rvListServiceRecycler, rvListProductRecycler;
    private TextView tvTitleProcess, tvTitleListService, tvTitleListProduct;
    private LinearLayout llDetailWorkNote;
    private FrameLayout flLineService, flLineProduct, flLineProcess;
    private String accessToken, deviceID, idTask, workName, address, timeWork;
    //    private LinearLayout llComplete;
    private TextView tvStartDate, tvEndDate, tvTotalTime;
    private ImageView imvBack, imgDevice;
    private ArrayList<SubmitProcessParam.Service> arrPushProcess2Service;
    private ArrayList<SubmitProcessParam.Product> arrPushProcess2Product;
    private boolean isNewProduct = false;
    private DialogInfor dialogInfor;
    private String note = Conts.BLANK;
    private ProgressDialog mProgressDialog;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        //11/01/2017 ADD by HoangIT START
        bindService(new Intent(this, GPSService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        showLoadingDialog();
        initObject();
        //11/01/2017 ADD by HoangIT END
        initViews();
        getData();
    }

    private void showLoadingDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loadding));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    private void dismisLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void getData() {
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        arrHeads.add(new MHead(Conts.KEY_DEVICE_ID, deviceID));
        ApiUtils.getAPIService(arrHeads).getDetailProduct(idTask).enqueue(new Callback<GetProductDetailResult>() {
            @Override
            public void onResponse(Call<GetProductDetailResult> call, Response<GetProductDetailResult> response) {
                dismisLoadingDialog();
                Long code = response.body().getStatusCode();
                Log.d(TAGG, "getData.onResponse, code: " + code);
                if (code != null && code == Conts.RESPONSE_STATUS_OK) {
                    displayDetailWork(response.body());
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Get Detail error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetProductDetailResult> call, Throwable t) {
                dismisLoadingDialog();
                Log.e(TAGG, "getData.onResponse", t);
                Toast.makeText(ProductDetailActivity.this, "Get Detail failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void visiableRecycler() {
        if (arrInit.size() == 0) {
            rvInitRecycler.setVisibility(View.GONE);
        } else {
            rvInitRecycler.setVisibility(View.VISIBLE);
        }
        if (arrProcess.size() == 0) {
            rvProcessRecycler.setVisibility(View.GONE);
            tvTitleProcess.setVisibility(View.GONE);
            flLineProcess.setVisibility(View.GONE);
        } else {
            rvProcessRecycler.setVisibility(View.VISIBLE);
            tvTitleProcess.setVisibility(View.VISIBLE);
            flLineProcess.setVisibility(View.VISIBLE);
        }
        if (arrFinish.size() == 0) {
            rvFinishRecycler.setVisibility(View.GONE);
        } else {
            rvFinishRecycler.setVisibility(View.VISIBLE);
        }
        if (arrTrading.size() == 0) {
            rvListProductRecycler.setVisibility(View.GONE);
            tvTitleListProduct.setVisibility((View.GONE));
            flLineProduct.setVisibility(View.GONE);
        } else {
            rvListProductRecycler.setVisibility(View.VISIBLE);
            tvTitleListProduct.setVisibility((View.VISIBLE));
            flLineProduct.setVisibility(View.VISIBLE);
        }
        if (arrService.size() == 0) {
            rvListServiceRecycler.setVisibility(View.GONE);
            tvTitleListService.setVisibility(View.GONE);
            flLineService.setVisibility(View.GONE);
        } else {
            rvListServiceRecycler.setVisibility(View.VISIBLE);
            tvTitleListService.setVisibility(View.VISIBLE);
            flLineService.setVisibility(View.VISIBLE);
        }

        if (note.equals(Conts.BLANK)) {
            llDetailWorkNote.setVisibility(View.GONE);
        } else {
            llDetailWorkNote.setVisibility(View.VISIBLE);
            tvNoteWork.setText(note);
        }

    }

    private void displayDetailWork(GetProductDetailResult body) {
        tvName.setText(workName);
        tvLocation.setText(address);
        tvHour.setText(timeWork);
        tvDeviceCode.setText(body.getResult().getDevice().getId());
        tvDeviceName.setText(body.getResult().getDevice().getDetail().getName());
        String path;
        try {
            if (body.getResult().getDevice().getDetail().getPhoto() == null) {
                path = body.getResult().getDevice().getDetail().getBrand().getPhoto();
            } else {
                path = body.getResult().getDevice().getDetail().getPhoto().toString();
            }
        } catch (Exception e) {
            path = "";
        }
        Picasso.with(ProductDetailActivity.this).load(Conts.URL_BASE + path).placeholder(R.drawable.ic_errror_image).error(R.drawable.ic_errror_image).into(imgDevice);
        arrInit.clear();
        arrProcess.clear();
        arrFinish.clear();
        arrService.clear();
        arrTrading.clear();
        arrInit.addAll(body.getResult().getBefore().getPhotos());
        arrProcess.addAll(body.getResult().getProcess().getPhotos());
        arrService.addAll(body.getResult().getProcess().getServices());
        arrTrading.addAll(body.getResult().getProcess().getProducts());
        note = body.getResult().getProcess().getNote();
        arrFinish.addAll(body.getResult().getAfter().getPhotos());
        if (body.getResult().getStatus().getId() == 3) {
            llCompleteWork.setVisibility(View.VISIBLE);
            fabMenu.setVisibility(View.GONE);
//            llComplete.setVisibility(View.VISIBLE);
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
        Log.d("doan.pt", accessToken + " " + deviceID + " " + idTask + " detail-getextra");
        dialogGPSSetting = new DialogGPSSetting(this);
        dialogNetworkSetting = new DialogNetworkSetting(this);
        dialogInfor = new DialogInfor(this);
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
        tvCompleteWork = (TextView) findViewById(R.id.tv_complete_work_float_button);
        llCompleteWork = findViewById(R.id.tv_work_complete);
        tvDeviceName = findViewById(R.id.tv_device_name);
        tvDeviceCode = findViewById(R.id.tv_device_code);
        imgDevice = findViewById(R.id.img_device_image);
        tvNoteWork = findViewById(R.id.tv_detail_work_note);
        tvCompleteWork.setOnClickListener(this);
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

//        llComplete = (LinearLayout) findViewById(R.id.ll_complete_task);
        tvStartDate = findViewById(R.id.tv_start_date);
        tvEndDate = findViewById(R.id.tv_end_date);
        tvTotalTime = findViewById(R.id.tv_total_time);
        fabNote = (FloatingActionButton) findViewById(R.id.fab_note);
        fabProduct = (FloatingActionButton) findViewById(R.id.fab_add_product);
        fabStep1 = (FloatingActionButton) findViewById(R.id.fab_step_one);
        fabStep2 = (FloatingActionButton) findViewById(R.id.fab_step_two);
        fabStep3 = (FloatingActionButton) findViewById(R.id.fab_step_three);
        tvName = findViewById(R.id.tv_work_name_work_detail);
        tvLocation = findViewById(R.id.tv_location_work_detail);
        tvHour = findViewById(R.id.tv_hour_work_detail);

        arrInit = new ArrayList<>();
        arrProcess = new ArrayList<>();
        arrFinish = new ArrayList<>();
        arrTrading = new ArrayList<>();
        arrService = new ArrayList<>();
        arrPushProcess2Product = new ArrayList<>();
        arrPushProcess2Service = new ArrayList<>();

        initAdapter = new ProductDetailAdapter(this, arrInit);
        processAdapter = new ProductDetailAdapter(this, arrProcess);
        finishAdapter = new ProductDetailAdapter(this, arrFinish);
        productAdapter = new ProductProcessAdapter(this, arrTrading);
        serviceAdapter = new ServiceProcessAdapter(this, arrService);
        rvInitRecycler = findViewById(R.id.rv_initial_condition);
        rvProcessRecycler = findViewById(R.id.rv_process_condition);
        rvFinishRecycler = findViewById(R.id.rv_finish_condition);
        rvListServiceRecycler = findViewById(R.id.rv_list_service);
        rvListProductRecycler = findViewById(R.id.rv_list_product);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.scrollToPosition(0);
        rvInitRecycler.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager1.scrollToPosition(0);
        rvProcessRecycler.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager2.scrollToPosition(0);
        rvFinishRecycler.setLayoutManager(layoutManager2);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.scrollToPosition(0);
        rvListProductRecycler.setLayoutManager(layoutManager3);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.scrollToPosition(0);
        rvListServiceRecycler.setLayoutManager(layoutManager4);

        rvInitRecycler.setAdapter(initAdapter);
        rvProcessRecycler.setAdapter(processAdapter);
        rvFinishRecycler.setAdapter(finishAdapter);
        rvListProductRecycler.setAdapter(productAdapter);
        rvListServiceRecycler.setAdapter(serviceAdapter);

        fabNote.setOnClickListener(this);
        fabProduct.setOnClickListener(this);
        fabStep1.setOnClickListener(this);
        fabStep2.setOnClickListener(this);
        fabStep3.setOnClickListener(this);

        imvBack = (ImageView) findViewById(R.id.img_back_work_detail);
        imvBack.setOnClickListener(this);

        tvTitleProcess = (TextView) findViewById(R.id.tv_title_process_condition);
        tvTitleListService = (TextView) findViewById(R.id.tv_title_list_service);
        tvTitleListProduct = (TextView) findViewById(R.id.tv_title_list_product);
        llDetailWorkNote = (LinearLayout) findViewById(R.id.ll_detail_work_note);

        flLineService = (FrameLayout) findViewById(R.id.fl_line_service);
        flLineProduct = (FrameLayout) findViewById(R.id.fl_line_product);
        flLineProcess = (FrameLayout) findViewById(R.id.fl_line_process_condition);
    }

    @Override
    protected void onPause() {
        dismisLoadingDialog();
        super.onPause();
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
            case R.id.tv_complete_work_float_button:
                if (arrPushProcess2Service.size() == 0) {
                    dialogInfor.setTextTvTitle(getString(R.string.title_dialog_inform));
                    dialogInfor.setTextTVContent(getString(R.string.content_dialog_inform));
                    showInforDialog();
                    return;
                }
                if (arrInit.size() == 0) {
                    dialogInfor.setTextTvTitle(getString(R.string.title_dialog_inform));
                    dialogInfor.setTextTVContent(getString(R.string.content_dialog_inform));
                    showInforDialog();
                    return;
                }
                if (arrProcess.size() == 0) {
                    dialogInfor.setTextTvTitle(getString(R.string.title_dialog_inform));
                    dialogInfor.setTextTVContent(getString(R.string.content_dialog_inform3));
                    showInforDialog();
                    return;
                }
                if (arrFinish.size() == 0) {
                    dialogInfor.setTextTvTitle(getString(R.string.title_dialog_inform));
                    dialogInfor.setTextTVContent(getString(R.string.content_dialog_inform4));
                    showInforDialog();
                    return;
                }
                completeWork();
                closeFabMenu();
                break;
            case R.id.fab_note:
                if (arrInit.size() == 0) {
                    dialogInfor.setTextTvTitle(getString(R.string.title_dialog_inform));
                    dialogInfor.setTextTVContent(getString(R.string.content_dialog_inform));
                    showInforDialog();
                    return;
                }
                Intent intentNote = new Intent(ProductDetailActivity.this, AddNoteActivity.class);
                intentNote.putExtra(Conts.KEY_CURRENT_NOTE, note);
                startActivityForResult(intentNote, KEY_ADD_NOTE);
                closeFabMenu();
                break;
            case R.id.fab_add_product:
                if (arrInit.size() == 0) {
                    dialogInfor.setTextTvTitle(getString(R.string.title_dialog_inform));
                    dialogInfor.setTextTVContent(getString(R.string.content_dialog_inform));
                    showInforDialog();
                    return;
                }
                //TODO add product
//                CommonMethod.makeToast(this, "Tính năng đang hoàn thiện. Vui lòng thử lại sau!");
                Intent intent = new Intent(this, ListProductAndServiceActivity.class);
                startActivityForResult(intent, KEY_RESULT_FROM_LIST_SERVICE_ACTIVITY);
                closeFabMenu();
                break;
            case R.id.fab_step_one:
                takePicture(KEY_STEP_ONE);
                closeFabMenu();
                break;
            case R.id.fab_step_two:
                if (arrInit.size() == 0) {
                    dialogInfor.setTextTvTitle(getString(R.string.title_dialog_inform));
                    dialogInfor.setTextTVContent(getString(R.string.content_dialog_inform));
                    showInforDialog();
                    return;
                }
                takePicture(KEY_STEP_TWO);
                closeFabMenu();
                break;
            case R.id.fab_step_three:
                if (arrInit.size() == 0 || arrProcess.size() == 0) {
                    dialogInfor.setTextTvTitle(getString(R.string.title_dialog_inform));
                    dialogInfor.setTextTVContent(getString(R.string.content_dialog_inform2));
                    showInforDialog();
                    return;
                }
                takePicture(KEY_STEP_THREE);
                closeFabMenu();
                break;
            case R.id.img_back_work_detail:
                finish();
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
                if (status != null && status == Conts.RESPONSE_STATUS_OK) {
                    CommonMethod.makeToast(ProductDetailActivity.this, "Complete OK!!!");
//                    llComplete.setVisibility(View.VISIBLE);
                    fabMenu.setVisibility(View.GONE);
                    tvCompleteWork.setVisibility(View.VISIBLE);
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
//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, keyResult);


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                photoFile.delete();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.cnc.hcm.cnctracking.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, keyResult);
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private Bitmap setPic() {
        // Get the dimensions of the Screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int targetW = displayMetrics.widthPixels;
        int targetH = displayMetrics.heightPixels;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bounds);
        int photoW = bounds.outWidth;
        int photoH = bounds.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);


        // Decode the image file into a Bitmap sized to fill the View
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        bmOptions.inDither = true;
        bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        Bitmap rotaBitmap = null;
        try {
            ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

            int rotationAngle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            rotaBitmap = Bitmap.createBitmap(bitmap, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rotaBitmap;
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case KEY_STEP_ONE:
            case KEY_STEP_TWO:
            case KEY_STEP_THREE:
                checkResultSteps(requestCode, resultCode, data);
                break;
            case KEY_ADD_NOTE:
                checkDataFromNoteActivity(resultCode, data);
                break;
            case KEY_RESULT_FROM_LIST_SERVICE_ACTIVITY:
                if (resultCode != RESULT_OK) {
                    return;
                }
                isNewProduct = true;
                String type = data.getStringExtra(Conts.KEY_CHECK_TYPE_RESULT);
                TraddingProduct.Result resultTradding;
                Services.Result resultService;
                List<MHead> arrNewHeads = new ArrayList<>();
                arrNewHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
                arrNewHeads.add(new MHead(Conts.KEY_DEVICE_ID, deviceID));
                final SubmitProcessParam param = new SubmitProcessParam();
                if (type.equals(Conts.KEY_PRODUCT)) {
                    resultTradding = (TraddingProduct.Result) data.getSerializableExtra(Conts.KEY_SERVICE_PRODUCT_RESULT);
                    getAllProcess2();
                    for (int i = 0; i < arrPushProcess2Product.size(); i++) {
                        if (arrPushProcess2Product.get(i).getProduct().equals(resultTradding.getId())) {
                            arrPushProcess2Product.get(i).setQuantity(arrPushProcess2Product.get(i).getQuantity() + 1);
                            isNewProduct = false;
                        }
                    }
                    SubmitProcessParam.Product product = new SubmitProcessParam.Product();
                    product.setProduct(resultTradding.getId());
                    product.setQuantity(Long.valueOf(resultTradding.getQuantity()));
                    arrPushProcess2Product.add(product);
                    param.setProcess(new SubmitProcessParam.Process());
                    param.getProcess().setServices(arrPushProcess2Service);
                    param.getProcess().setProducts(arrPushProcess2Product);
                    param.getProcess().setPhotos(arrProcess);
                    param.getProcess().setNote(note);
                    uploadProcess(arrNewHeads, idTask, param, KEY_PROCESS_PRODUCT, resultTradding, null);
                } else if (type.equals(Conts.KEY_SERVICE)) {
                    getAllProcess2();
                    resultService = (Services.Result) data.getSerializableExtra(Conts.KEY_SERVICE_PRODUCT_RESULT);
                    for (SubmitProcessParam.Service p : arrPushProcess2Service) {
                        if (p.getProduct().equals(resultService.getId())) {
                            CommonMethod.makeToast(ProductDetailActivity.this, "Service already added");
                            return;
                        }
                    }
                    SubmitProcessParam.Service service = new SubmitProcessParam.Service();
                    service.setProduct(resultService.getId());
                    service.setQuantity((long) 1);
                    arrPushProcess2Service.add(service);
                    param.setProcess(new SubmitProcessParam.Process());
                    param.getProcess().setServices(arrPushProcess2Service);
                    param.getProcess().setProducts(arrPushProcess2Product);
                    param.getProcess().setPhotos(arrProcess);
                    param.getProcess().setNote(note);
                    uploadProcess(arrNewHeads, idTask, param, KEY_PROCESS_SERVICE, null, resultService);
                }
                break;
        }

    }

    private void checkDataFromNoteActivity(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            List<MHead> arrNewHeads = new ArrayList<>();
            arrNewHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
            arrNewHeads.add(new MHead(Conts.KEY_DEVICE_ID, deviceID));
            SubmitProcessParam param = new SubmitProcessParam();
            String note = data.getStringExtra(Conts.KEY_RESULT_ADD_NOTE);
            getAllProcess2();
            param.setProcess(new SubmitProcessParam.Process());
            param.getProcess().setServices(arrPushProcess2Service);
            param.getProcess().setProducts(arrPushProcess2Product);
            param.getProcess().setPhotos(arrProcess);
            param.getProcess().setNote(note);
            uploadProcess(arrNewHeads, idTask, param, KEY_ADD_NOTE, null, null);
        } else if (resultCode == RESULT_CANCELED) {
            //Don't nothing
        }
    }

    private void getAllProcess2() {
        arrPushProcess2Service.clear();
        arrPushProcess2Product.clear();
        for (Services.Result result : arrService) {
            SubmitProcessParam.Service s = new SubmitProcessParam.Service();
            s.setProduct(result.getId());
            s.setQuantity((long) 1);
            arrPushProcess2Service.add(s);
        }
        for (TraddingProduct.Result result : arrTrading) {
            SubmitProcessParam.Product s = new SubmitProcessParam.Product();
            s.setProduct(result.getId());
            s.setQuantity(Long.valueOf(result.getQuantity()));
            arrPushProcess2Product.add(s);
        }
    }

    private void checkResultSteps(final int requestCode, int resultCode, Intent data) {
        showLoadingDialog();
        File file = null;
        Bitmap photo = setPic();
        if (photo != null) {
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
                    if (code != null && code == Conts.RESPONSE_STATUS_OK) {
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
                            getAllProcess2();
                            arrImage.addAll(arrProcess);
                            arrImage.add(url);
                            param.setProcess(new SubmitProcessParam.Process());
                            param.getProcess().setServices(arrPushProcess2Service);
                            param.getProcess().setProducts(arrPushProcess2Product);
                            param.getProcess().setNote(note);
                            param.getProcess().setPhotos(arrImage);
                        } else if (requestCode == KEY_STEP_THREE) {
                            arrImage.addAll(arrFinish);
                            arrImage.add(url);
                            param.setAfter(new SubmitProcessParam.After());
                            param.getAfter().setPhotos(arrImage);
                        }
                        Log.d("ABC", idTask + " " + param.getProcess());
                        uploadProcess(arrNewHeads, idTask, param, requestCode, null, null);
                    } else {
                        CommonMethod.makeToast(ProductDetailActivity.this, "Upload error");
                    }
                }

                @Override
                public void onFailure(Call<UploadImageResult> call, Throwable t) {
                    dismisLoadingDialog();
                    CommonMethod.makeToast(ProductDetailActivity.this, "Upload onFailure");
                }
            });
        } else {
            dismisLoadingDialog();
            CommonMethod.makeToast(ProductDetailActivity.this, "Get image error");
        }

    }

    private void uploadProcess(List<MHead> arrHeads, String idTask, final SubmitProcessParam param, final int requestCode, final TraddingProduct.Result product, final Services.Result service) {

        if (mProgressDialog != null) {
            mProgressDialog.setMessage("Update process...");
        }

        ApiUtils.getAPIService(arrHeads).updateProcess(idTask, param).enqueue(new Callback<UpdateProcessResult>() {
            @Override
            public void onResponse(Call<UpdateProcessResult> call, Response<UpdateProcessResult> response) {
                Long status = response.body().getStatusCode();
                Log.d(TAGG, "updateProcess.onResponse, status: " + status);
                if (status != null && status == Conts.RESPONSE_STATUS_OK) {
                    if (requestCode == KEY_STEP_ONE) {
                        arrInit.add(param.getBefore().getPhotos().get(param.getBefore().getPhotos().size() - 1));
                        initAdapter.notifyDataSetChanged();
                    } else if (requestCode == KEY_STEP_TWO) {
                        arrProcess.add(param.getProcess().getPhotos().get(param.getProcess().getPhotos().size() - 1));
                        processAdapter.notifyDataSetChanged();
                    } else if (requestCode == KEY_STEP_THREE) {
                        arrFinish.add(param.getAfter().getPhotos().get(param.getAfter().getPhotos().size() - 1));
                        finishAdapter.notifyDataSetChanged();
                    } else if (requestCode == KEY_PROCESS_SERVICE) {
                        arrService.add(service);
                        serviceAdapter.notifyDataSetChanged();
                    } else if (requestCode == KEY_PROCESS_PRODUCT) {
                        if (isNewProduct) {
                            arrTrading.add(product);
                        } else {
                            for (int i = 0; i < arrTrading.size(); i++) {
                                if (arrTrading.get(i).getId().equals(product.getId())) {
                                    arrTrading.get(i).setQuantity(arrTrading.get(i).getQuantity() + 1);
                                    break;
                                }
                            }
                        }
                        productAdapter.notifyDataSetChanged();
                    } else if (requestCode == KEY_ADD_NOTE) {
                        note = param.getProcess().getNote();
                        tvNoteWork.setText(note);
                    }
                    visiableRecycler();
                    CommonMethod.makeToast(ProductDetailActivity.this, "Update Step OK!!!");
                } else {
                    CommonMethod.makeToast(ProductDetailActivity.this, "Update Process Error");
                }
                dismisLoadingDialog();
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
                dismisLoadingDialog();
                CommonMethod.makeToast(ProductDetailActivity.this, "Update process Error, onFailure");
            }
        });
    }

    private File saveBitmapFile(Bitmap bitmap, String name) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream;
        // String temp = null;
        File file = new File(extStorageDirectory, "/CoolBackup/" + name + ".jpg");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "/CoolBackup/" + name + ".jpg");

        }

        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
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

    private void showInforDialog() {
        if (dialogInfor != null) {
            dialogInfor.show();
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
