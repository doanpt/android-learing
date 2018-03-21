package com.cnc.hcm.cnctracking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;

/**
 * Created by Android on 03/03/2018.
 */

public class DialogInfor extends Dialog implements View.OnClickListener {

    private Button btnOK;
    private TextView tvTitle, tvContent;
    private Context context;

    public DialogInfor(@NonNull Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_information);
        setCancelable(false);
        initView();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_dialog_title);
        tvContent = findViewById(R.id.tv_dialog_content);
        btnOK = (Button) findViewById(R.id.btn_dialog_confirm_ok);
        btnOK.setOnClickListener(this);
    }

    public void setTextTvTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTextTVContent(String content) {
        tvContent.setText(content);
    }

    public void setTextButton(String text) {
        btnOK.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_confirm_ok:
                dismiss();
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}