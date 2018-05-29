package com.cnc.hcm.cnctrack.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.api.ApiUtils;
import com.cnc.hcm.cnctrack.api.MHead;
import com.cnc.hcm.cnctrack.base.BaseActivity;
import com.cnc.hcm.cnctrack.dialog.DialogGPSSetting;
import com.cnc.hcm.cnctrack.model.AddContainProductResult;
import com.cnc.hcm.cnctrack.model.AddProductItem;
import com.cnc.hcm.cnctrack.model.AddProductResult;
import com.cnc.hcm.cnctrack.model.CategoryListResult;
import com.cnc.hcm.cnctrack.model.CheckContainProductResult;
import com.cnc.hcm.cnctrack.model.ProductListResult;
import com.cnc.hcm.cnctrack.service.GPSService;
import com.cnc.hcm.cnctrack.util.CommonMethod;
import com.cnc.hcm.cnctrack.util.Conts;
import com.cnc.hcm.cnctrack.util.UserInfo;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Android on 1/3/2018.
 */

public class AddProductActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAGG = AddProductActivity.class.getSimpleName();
    private EditText edtDeviceName;
    private Spinner edtManufacture, edtCategory;
    private EditText edtBarcode;
    private ImageView imgCameraBarCode, imgAddDevice, imgBack;
    private String customerId, qrCode, manufacture, productName, category;
    private List<MHead> arrHeads;
    private String accessToken;
    private String idTask;
    private ArrayAdapter manufactureAdapter;
    private ArrayAdapter categoryAdapter;
    private ArrayList<String> arrManufactures;
    private ArrayList<String> arrCategory;
    private ArrayList<ProductListResult.Product> listProduct;
    private ArrayList<CategoryListResult.Category> listCategory;
    private DialogGPSSetting dialogGPSSetting;
    private GPSService gpsService;
    private String titleWork, addressWork, timeWork;
    private ProgressDialog mProgressDialog;

    @Override
    public void onViewReady(@Nullable Bundle savedInstanceState) {
        //11/01/2017 ADD by HoangIT START
        bindService(new Intent(this, GPSService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        initObject();
        //11/01/2017 ADD by HoangIT END
        arrManufactures = new ArrayList<>();
        arrCategory = new ArrayList<>();
        arrHeads = new ArrayList<>();
        initView();
        getInformation();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_device;
    }

    //11/01/2017 ADD by HoangIT START
    private void initObject() {
        dialogGPSSetting = new DialogGPSSetting(this);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            gpsService = ((GPSService.MyBinder) iBinder).getGPSService();
            if (gpsService != null) {
                gpsService.setAddProductActivity(AddProductActivity.this);
            }

            Log.d(TAGG, "ServiceConnection at AddProductActivity, gpsService:= " + gpsService);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAGG, "onServiceDisconnected at AddProductActivity");
        }
    };
    //11/01/2017 ADD by HoangIT END

    private void getInformation() {
        customerId = getIntent().getStringExtra(Conts.KEY_CUSTOMER_ID);
        idTask = getIntent().getStringExtra(Conts.KEY_ID_TASK);
        titleWork = getIntent().getStringExtra(Conts.KEY_WORK_NAME);
        addressWork = getIntent().getStringExtra(Conts.KEY_WORK_LOCATION);
        timeWork = getIntent().getStringExtra(Conts.KEY_WORK_TIME);

        String barCode = getIntent().getStringExtra(Conts.KEY_DEVICE_ID);
        if (barCode != null) {
            edtBarcode.setText(barCode + "");
        }
        Log.d("ABD", customerId + " " + idTask);
        CommonMethod.makeToast(this, customerId);
        accessToken = UserInfo.getInstance(getApplicationContext()).getAccessToken();
        arrHeads.clear();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        ApiUtils.getAPIService(arrHeads).getListManufactures().enqueue(new Callback<ProductListResult>() {
            @Override
            public void onResponse(Call<ProductListResult> call, Response<ProductListResult> response) {
                Long status = response.body().getStatusCode();
                if (status != null && status == 200) {
                    arrManufactures.clear();
                    listProduct = (ArrayList<ProductListResult.Product>) response.body().getResult();
                    if (listProduct != null) {
                        for (ProductListResult.Product p : listProduct) {
                            arrManufactures.add(p.getName());
                            manufactureAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d(TAGG, "FATA AddProductActivity, getInformation(), listProduct = null");
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductListResult> call, Throwable t) {

            }
        });
        ApiUtils.getAPIService(arrHeads).getListCategory().enqueue(new Callback<CategoryListResult>() {
            @Override
            public void onResponse(Call<CategoryListResult> call, Response<CategoryListResult> response) {
                Long status = response.body().getStatusCode();
                if (status != null && status == 200) {
                    arrCategory.clear();
                    listCategory = (ArrayList<CategoryListResult.Category>) response.body().getResult();
                    if (listCategory != null) {
                        for (CategoryListResult.Category category : listCategory) {
                            arrCategory.add(category.getTitle());
                            categoryAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d(TAGG, "FATA AddProductActivity, getInformation(), listCategory = null");
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryListResult> call, Throwable t) {

            }
        });
    }

    private void initView() {
        manufactureAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrManufactures);
        manufactureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrCategory);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtDeviceName = findViewById(R.id.edt_device_name);
        edtBarcode = findViewById(R.id.edt_barcode);
        edtManufacture = findViewById(R.id.edt_manufacture);
        edtCategory = findViewById(R.id.edt_category);

        imgCameraBarCode = findViewById(R.id.img_scan_barcode);
//        frameManufacture = findViewById(R.id.frame_manufacture);
        imgAddDevice = findViewById(R.id.img_add_device);
        imgBack = findViewById(R.id.img_back);

        edtManufacture.setAdapter(manufactureAdapter);
        edtCategory.setAdapter(categoryAdapter);

//        frameManufacture.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgAddDevice.setOnClickListener(this);
        imgCameraBarCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_add_device:
                manufacture = edtManufacture.getSelectedItem().toString();
                qrCode = edtBarcode.getText().toString();
                productName = edtDeviceName.getText().toString();
                category = edtCategory.getSelectedItem().toString();
                addProduct(productName, category, manufacture, qrCode);
                break;
            case R.id.img_scan_barcode:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.initiateScan();
                break;
//            case R.id.frame_manufacture:
//                //FIXME doesn't show drop down list in autocomplex textview
////                edtManufacture.showDropDown();
//                break;
        }
    }

    private void addProduct(String productName, String category, String manufacture, final String qrCode) {
        if ("".equals(productName) || "".equals(manufacture) || "".equals(qrCode)) {
            CommonMethod.makeToast(this, "Param invalid");
        } else {
            mProgressDialog = new ProgressDialog(AddProductActivity.this);
            mProgressDialog.setMessage("Adding product");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            for (CategoryListResult.Category c : listCategory) {
                if (c.getTitle().equals(category)) {
                    category = c.getId();
                    break;
                }
            }
            for (ProductListResult.Product c : listProduct) {
                if (c.getName().equals(manufacture)) {
                    manufacture = c.getId();
                    break;
                }
            }
            List<MHead> arrHeads = new ArrayList<>();
            arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
            arrHeads.add(new MHead(Conts.KEY_CUSTOMER_ID, customerId));
            AddProductItem productItem = new AddProductItem();
            productItem.setBrand(manufacture);
            productItem.setCategory(category);
            productItem.setName(productName);
            productItem.setBarcode(qrCode);
            Log.d("HEAD_ITEM", productItem.toString());
            //sử dụng API: https://recode.atlassian.net/wiki/spaces/CNC/pages/158433284/Th+m+thi+t+b+c+a+kh+ch+h+ng
            ApiUtils.getAPIService(arrHeads).addCustomerProduct(productItem).enqueue(new Callback<AddProductResult>() {
                @Override
                public void onResponse(Call<AddProductResult> call, Response<AddProductResult> response) {
                    //TODO :FIX ERROR ADD
                    Long status = response.body().getStatusCode();
                    if (status != null && status == Conts.RESPONSE_STATUS_OK) {
                        List<MHead> arrHeads = new ArrayList<>();
                        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
                        arrHeads.add(new MHead(Conts.KEY_DEVICE_ID, qrCode));
                        addProduct2_4(arrHeads, qrCode);
                    } else {
                        mProgressDialog.dismiss();
                        CommonMethod.makeToast(AddProductActivity.this, "Add product error!");
                    }
                }

                @Override
                public void onFailure(Call<AddProductResult> call, Throwable t) {
                    CommonMethod.makeToast(AddProductActivity.this, "Call 5.3 error onFailure");
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    private void showLoadingDialog() {
        mProgressDialog = new ProgressDialog(AddProductActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loadding));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        }, 5000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                showLoadingDialog();
                final String content = result.getContents();
                String format = result.getFormatName();
                //sử dụng API:https://recode.atlassian.net/wiki/spaces/CNC/pages/158531628/Th+ng+tin+thi+t+b
                arrHeads.clear();
                arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
                Log.d("HEAD", "ac:" + accessToken);
                Log.d("HEAD", "cs:" + customerId);
                arrHeads.add(new MHead(Conts.KEY_CUSTOMER_ID, customerId));
                ApiUtils.getAPIService(arrHeads).getProductById(content).enqueue(new Callback<CheckContainProductResult>() {
                    @Override
                    public void onResponse(Call<CheckContainProductResult> call, Response<CheckContainProductResult> response) {
                        Long status = response.body().getStatusCode();
                        Log.d("ABD", status + "   ");
                        if (status != null && status == Conts.RESPONSE_STATUS_OK) {
                            arrHeads.clear();
                            arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
                            Log.d("HEAD", "ac:" + accessToken);
                            Log.d("HEAD", "cs:" + customerId);
                            arrHeads.add(new MHead(Conts.KEY_DEVICE_ID, content));
                            addProduct2_4(arrHeads, content);
                        } else if (status == Conts.RESPONSE_STATUS_404) {
                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                            edtBarcode.setText(content);
                            CommonMethod.makeToast(AddProductActivity.this, "New Product:" + content);
                            Log.d("ABDonResponse", status + "  404 ");
                        } else if (status == Conts.RESPONSE_STATUS_500) {
                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                            CommonMethod.makeToast(AddProductActivity.this, "Status 500");
                            //FIXME chưa biết status 500 làm gì
                            Log.d("ABDonResponse", status + "  500 ");
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckContainProductResult> call, Throwable t) {
                        //FIXME add more code to check onFailure
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        String cause = "";
                        if (t != null) {
                            cause = t.getCause().toString();
                        }
                        CommonMethod.makeToast(AddProductActivity.this, "onFailure 5.3:" + cause);
                    }
                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void addProduct2_4(List<MHead> arrHeads, final String qr) {
        ApiUtils.getAPIService(arrHeads).addProductContain(idTask).enqueue(new Callback<AddContainProductResult>() {
            @Override
            public void onResponse(Call<AddContainProductResult> call, Response<AddContainProductResult> response) {
                //TODO fix code
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                int status = response.code();
                if (status == Conts.RESPONSE_STATUS_OK) {
                    Intent productDetail = new Intent(AddProductActivity.this, ProductDetailActivity.class);
                    productDetail.putExtra(Conts.KEY_PRODUCT_ID, qr);
                    productDetail.putExtra(Conts.KEY_ACCESS_TOKEN, accessToken);
                    productDetail.putExtra(Conts.KEY_ID_TASK, idTask);
                    productDetail.putExtra(Conts.KEY_WORK_NAME, titleWork);
                    productDetail.putExtra(Conts.KEY_WORK_LOCATION, addressWork);
                    productDetail.putExtra(Conts.KEY_WORK_TIME, timeWork);
                    edtDeviceName.setText("");
                    edtBarcode.setText("");
                    startActivity(productDetail);
                    finish();
                    Log.d("ABDonResponse", status + "  200 ");
                } else {
                    Toast.makeText(AddProductActivity.this, "Add contain product error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddContainProductResult> call, Throwable t) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                String cause = "";
                if (t != null) {
                    cause = t.getCause().toString();
                }
                Toast.makeText(AddProductActivity.this, "onFailure 2.4:" + cause, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialogGPSSetting() {
        if (dialogGPSSetting != null && !dialogGPSSetting.isShowing() && !AddProductActivity.this.isDestroyed()) {
            dialogGPSSetting.show();
        }
    }

    private void dismisDialogGPSSetting() {
        if (dialogGPSSetting != null && dialogGPSSetting.isShowing() && !AddProductActivity.this.isDestroyed()) {
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