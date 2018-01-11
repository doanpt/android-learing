package com.cnc.hcm.cnctracking.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.dialog.DialogGPSSetting;
import com.cnc.hcm.cnctracking.dialog.DialogNetworkSetting;
import com.cnc.hcm.cnctracking.service.GPSService;
import com.cnc.hcm.cnctracking.util.BarcodeUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAGG = ProductDetailActivity.class.getSimpleName();
    private LinearLayout llViewControl;
    private FrameLayout flBlurView;
    private FloatingActionsMenu fabMenu;
    private RelativeLayout rlHeaderBacode;
    private LinearLayout llContentBarcode;
    private ImageView imvExpandBarcode;
    private ImageView imvQRCode, imvCode128;
    private TextView tvCompleteWork;

    private FloatingActionButton fabNote, fabProduct, fabStep1, fabStep2, fabStep3;
    private DialogNetworkSetting dialogNetworkSetting;
    private DialogGPSSetting dialogGPSSetting;
    private GPSService gpsService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        //11/01/2017 ADD by HoangIT START
        bindService(new Intent(this, GPSService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        initObject();
        //11/01/2017 ADD by HoangIT END
        initViews();
        try {
            imvQRCode.setImageBitmap(BarcodeUtils.encodeAsBitmap("1234567890123", BarcodeFormat.QR_CODE, 512, 512));
            imvCode128.setImageBitmap(BarcodeUtils.encodeAsBitmap("1234567890123", BarcodeFormat.CODE_128, 512, 200));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    //11/01/2017 ADD by HoangIT START
    private void initObject() {
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

        imvExpandBarcode = (ImageView) findViewById(R.id.imv_expand_barcode);
        rlHeaderBacode = (RelativeLayout) findViewById(R.id.rl_header_barcode);
        rlHeaderBacode.setOnClickListener(this);
        llContentBarcode = (LinearLayout) findViewById(R.id.ll_content_barcode);

        imvQRCode = (ImageView) findViewById(R.id.imv_qrcode);
        imvCode128 = (ImageView) findViewById(R.id.imv_code_128);

        tvCompleteWork = (TextView) findViewById(R.id.tv_complete_work);
        tvCompleteWork.setOnClickListener(this);

        fabNote = (FloatingActionButton) findViewById(R.id.fab_note);
        fabProduct = (FloatingActionButton) findViewById(R.id.fab_add_product);
        fabStep1 = (FloatingActionButton) findViewById(R.id.fab_step_one);
        fabStep2 = (FloatingActionButton) findViewById(R.id.fab_step_two);
        fabStep3 = (FloatingActionButton) findViewById(R.id.fab_step_three);
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
            case R.id.rl_header_barcode:
                if (llContentBarcode.getVisibility() == View.GONE) {
                    llContentBarcode.setVisibility(View.VISIBLE);
                    imvExpandBarcode.setImageResource(R.drawable.ic_expand_task_details);
                } else {
                    llContentBarcode.setVisibility(View.GONE);
                    imvExpandBarcode.setImageResource(R.drawable.ic_chevron_right);
                }
                break;
            case R.id.tv_complete_work:
                closeFabMenu();
                break;
            case R.id.fab_note:
                closeFabMenu();
                break;
            case R.id.fab_add_product:
                closeFabMenu();
                break;
            case R.id.fab_step_one:
                closeFabMenu();
                break;
            case R.id.fab_step_two:
                closeFabMenu();
                break;
            case R.id.fab_step_three:
                closeFabMenu();
                break;

        }
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
