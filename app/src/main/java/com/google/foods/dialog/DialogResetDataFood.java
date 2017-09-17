package com.google.foods.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.foods.R;

/**
 * Created by giapmn on 8/30/17.
 */

public class DialogResetDataFood extends Dialog implements View.OnClickListener {

    private EditText edtInputQuantity;
    private Button btnCancel, btnOk;

    private Context context;
    private OnClickButtonOKDialogResetDataFoodListener onClickButtonOKDialogResetDataFoodListener;

    public DialogResetDataFood(@NonNull Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_confirm_reset_data);
        initView();
    }

    private void initView() {
        edtInputQuantity = (EditText) findViewById(R.id.edt_input_total_quantity_for_list_food);

        btnCancel = (Button) findViewById(R.id.btn_exit_dialog_reset_data);
        btnCancel.setOnClickListener(this);
        btnOk = (Button) findViewById(R.id.btn_ok_dialog_reset_data);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok_dialog_reset_data:
                int totalQuantity = Integer.parseInt(edtInputQuantity.getText().toString());
                if (onClickButtonOKDialogResetDataFoodListener != null) {
                    onClickButtonOKDialogResetDataFoodListener.onClickButtonOK(totalQuantity);
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

    public interface OnClickButtonOKDialogResetDataFoodListener {
        void onClickButtonOK(int totalQuantity);

        void onClickButtonExit();
    }

    public void setOnClickButtonOKDialogResetDataFoodListener(OnClickButtonOKDialogResetDataFoodListener onClickButtonOKDialogResetDataFoodListener) {
        this.onClickButtonOKDialogResetDataFoodListener = onClickButtonOKDialogResetDataFoodListener;
    }
}
