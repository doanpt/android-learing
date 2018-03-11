package com.cnc.hcm.cnctracking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.MainActivity;
import com.cnc.hcm.cnctracking.model.ItemTask;
import com.cnc.hcm.cnctracking.util.CommonMethod;

/**
 * Created by Hoang on 03/10/2018.
 */

public class DialogNotiTaskAppointment extends Dialog implements View.OnClickListener {

    private Context context;
    private TextView tvTimeAppointment, tvTitleTask;
    private Button btnCancel, btnViewTask;
    private MainActivity mainActivity;
    private ItemTask itemTask;


    public DialogNotiTaskAppointment(@NonNull Context context) {
        super(context);
        this.context = context;
        this.mainActivity = (MainActivity) context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_noti_task_appointment);
        initViews();
    }

    private void initViews() {
        tvTimeAppointment = (TextView) findViewById(R.id.tv_time_appointment_task);
        tvTitleTask = (TextView) findViewById(R.id.tv_title_task_dialog_noti_appointment);

        btnCancel = (Button) findViewById(R.id.btn_cancel_noti);
        btnCancel.setOnClickListener(this);
        btnViewTask = (Button) findViewById(R.id.btn_view_task);
        btnViewTask.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel_noti:
                dismiss();
                break;
            case R.id.btn_view_task:
                if (mainActivity != null && itemTask != null) {
                    mainActivity.showTaskDetail(itemTask.getTaskResult()._id);
                } else {
                    CommonMethod.makeToast(context, "null");
                }
                dismiss();
                break;
        }
    }

    public void setData(ItemTask itemTask) {
        this.itemTask = itemTask;
        if (tvTimeAppointment != null && tvTitleTask != null && itemTask != null) {
            String appointmentDate = itemTask.getTaskResult().appointmentDate.substring(0, itemTask.getTaskResult().appointmentDate.lastIndexOf(".")) + "Z";
            String time = CommonMethod.formatTimeFromServerToString(appointmentDate);

            tvTitleTask.setText("Ticket " + itemTask.getTaskResult().title + " " + "còn 30 phút nữa đến lịch hẹn.");
            tvTimeAppointment.setText(time);
        }
    }

}
