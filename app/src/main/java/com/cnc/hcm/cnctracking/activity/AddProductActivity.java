package com.cnc.hcm.cnctracking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * Created by Android on 1/3/2018.
 */

public class AddProductActivity extends Activity implements View.OnClickListener {
    private EditText edtDeviceName;
    private AutoCompleteTextView edtManufature;
    private EditText edtBarcode;
    private ImageView imgCameraBarCode, imgAddDevice, imgBack;
    private FrameLayout frameManufacture;
    private String idTask, qrCode, manufacture, productName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        initView();
        getTaskIdExtra();
    }

    private void getTaskIdExtra() {
        idTask = getIntent().getStringExtra(Conts.KEY_ID_TASK);
        CommonMethod.makeToast(this, idTask);
    }

    private void initView() {
        ArrayList<String> arrManufacture = new ArrayList<>();
        arrManufacture.add("Samsung");
        arrManufacture.add("LG");
        arrManufacture.add("Sony");
        arrManufacture.add("Tosiba");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrManufacture);

        edtDeviceName = findViewById(R.id.edt_device_name);
        edtBarcode = findViewById(R.id.edt_barcode);
        edtManufature = findViewById(R.id.edt_manufacture);

        imgCameraBarCode = findViewById(R.id.img_scan_barcode);
        frameManufacture = findViewById(R.id.frame_manufacture);
        imgAddDevice = findViewById(R.id.img_add_device);
        imgBack = findViewById(R.id.img_back);

        edtManufature.setAdapter(adapter);

        frameManufacture.setOnClickListener(this);
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
                manufacture = edtManufature.getText().toString();
                qrCode = edtBarcode.getText().toString();
                productName = edtDeviceName.getText().toString();
                addProduct(productName, manufacture, qrCode);
                break;
            case R.id.img_scan_barcode:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.initiateScan();
                break;
            case R.id.frame_manufacture:
                //FIXME doesn't show drop down list in autocomplex textview
                edtManufature.showDropDown();
                break;
        }
    }

    private void addProduct(String productName, String manufacture, String qrCode) {
        if ("".equals(productName) || "".equals(manufacture) || "".equals(qrCode)) {
            CommonMethod.makeToast(this, "Param invalid");
        } else {
            Intent productDetail=new Intent(this,ProductDetailActivity.class);
            startActivity(productDetail);
            CommonMethod.makeToast(this, "add product name:" + productName + " manufacture:" + manufacture + " QR code:" + qrCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
            } else {
                String content = result.getContents();
                String format = result.getFormatName();
                edtBarcode.setText(content);
                CommonMethod.makeToast(AddProductActivity.this, content + ", " + format);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
