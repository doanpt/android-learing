package com.cnc.hcm.cnctracking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.util.Conts;


/**
 * Created by giapmn on 8/30/17.
 */

public class DialogSetTimePostServer extends Dialog implements View.OnClickListener {

    private EditText edtInputQuantity;
    private Button btnCancel, btnOk;
    private TextView tvCurrentTime;

    private Context context;
    private OnClickButtonDialogResetDataFoodListener onClickButtonOKDialogResetDataFoodListener;

    public DialogSetTimePostServer(@NonNull Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_set_time_post_server);
        initView();
    }

    private void initView() {
        edtInputQuantity = (EditText) findViewById(R.id.edt_input_total_quantity_for_list_food);

        btnCancel = (Button) findViewById(R.id.btn_exit_dialog_reset_data);
        btnCancel.setOnClickListener(this);
        btnOk = (Button) findViewById(R.id.btn_ok_dialog_reset_data);
        btnOk.setOnClickListener(this);

        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok_dialog_reset_data:
                if (!edtInputQuantity.getText().toString().trim().equals(Conts.BLANK)) {
                    int time = Integer.parseInt(edtInputQuantity.getText().toString());
                    if (onClickButtonOKDialogResetDataFoodListener != null) {
                        onClickButtonOKDialogResetDataFoodListener.onClickButtonOK(time);
                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.please_input_param), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_exit_dialog_reset_data:
                if (onClickButtonOKDialogResetDataFoodListener != null) {
                    onClickButtonOKDialogResetDataFoodListener.onClickButtonExit();
                }
                break;
        }
        dismiss();
    }

    public interface OnClickButtonDialogResetDataFoodListener {
        void onClickButtonOK(int time);

        void onClickButtonExit();
    }

    public void setOnClickButtonDialogResetDataFoodListener(OnClickButtonDialogResetDataFoodListener onClickButtonOKDialogResetDataFoodListener) {
        this.onClickButtonOKDialogResetDataFoodListener = onClickButtonOKDialogResetDataFoodListener;
    }

    public void releaseData() {
        edtInputQuantity.setText(Conts.BLANK);
    }

    public void setTextCurrentTime(String str) {
        tvCurrentTime.setText(str + Conts.BLANK);
    }
}
