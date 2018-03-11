package com.cnc.hcm.cnctracking.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.model.ChangeTicketAppointmentResult;
import com.cnc.hcm.cnctracking.model.GetChangeTicketAppointmentReasonsResult;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeTimeActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = ChangeTimeActivity.class.getSimpleName();
    public static final int CODE_CHANGE_TIME = 123;
    private Button btn_save, btn_cancel;
    private TextView tv_time;
    private TextView tv_date;
    private RadioGroup rg_reason;
    private Calendar mCalendar;
    private String mReason;
    private GetChangeTicketAppointmentReasonsResult mGetChangeTicketAppointmentReasonsResult;
    private ProgressDialog mProgressDialog;
    private String mIdTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_time);

        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        tv_time = findViewById(R.id.tv_time);
        tv_date = findViewById(R.id.tv_date);
        rg_reason = findViewById(R.id.rg_reason);

        String time = getIntent().getStringExtra(Conts.KEY_WORK_TIME);
        mIdTask = getIntent().getStringExtra(Conts.KEY_ID_TASK);
        if (!TextUtils.isEmpty(time)) {
            Date date = CommonMethod.formatTimeFromServerToDate2(time);

            mCalendar = CommonMethod.getInstanceCalendar();
            mCalendar.setTime(date);
            updateDateTimeUI();
        }

        loadListAvailableReason();
        rg_reason.setOnCheckedChangeListener(this);

        tv_time.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    private void loadListAvailableReason() {
        showDialogLoadding();
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, UserInfo.getInstance(this).getAccessToken()));
        ApiUtils.getAPIService(arrHeads).getChangeTicketAppointmentReasons().enqueue(new Callback<GetChangeTicketAppointmentReasonsResult>() {
            @Override
            public void onResponse(Call<GetChangeTicketAppointmentReasonsResult> call, Response<GetChangeTicketAppointmentReasonsResult> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    mGetChangeTicketAppointmentReasonsResult = response.body();
                    Log.e(TAG, "loadListAvailableReason.onResponse(), statusCode: " + statusCode + ", mGetChangeTicketAppointmentReasonsResult: " + mGetChangeTicketAppointmentReasonsResult);
                    onListAvailableReasonLoaded();
                    dismisDialogLoading();
                }
            }

            @Override
            public void onFailure(Call<GetChangeTicketAppointmentReasonsResult> call, Throwable t) {
                dismisDialogLoading();
                Log.e(TAG, "loadListAvailableReason.onFailure() --> " + t);
            }
        });
    }

    private void onListAvailableReasonLoaded() {
        if (mGetChangeTicketAppointmentReasonsResult != null) {
            List<GetChangeTicketAppointmentReasonsResult.Result> result = mGetChangeTicketAppointmentReasonsResult.result;
            if (result != null) {
                for (GetChangeTicketAppointmentReasonsResult.Result item : result) {
                    String rbString = Conts.BLANK;
                    if (item != null) {
                        rbString += item.reason;
                        if (item.action != null) {
                            rbString += "; " + item.action.title;
                        }
                        rbString += ".";
                        RadioButton rb = new RadioButton(this);
                        rb.setText(rbString);
                        rg_reason.addView(rb);
                    }
                }
            }
        }
    }

    private void showDialogLoadding() {
        dismisDialogLoading();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loadding));
        mProgressDialog.show();
    }

    private void dismisDialogLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void updateDateTimeUI() {
        tv_time.setText(CommonMethod.formatDateToString(mCalendar.getTime().getTime()));
        tv_date.setText(CommonMethod.formatTimeStandand(mCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismisDialogLoading();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_time:
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
                break;
            case R.id.tv_date:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;

            case R.id.btn_save:
                tryToSaveChangedTime();
                break;
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    private void tryToSaveChangedTime() {
        if (TextUtils.isEmpty(mReason)) {
            Toast.makeText(ChangeTimeActivity.this, "Bạn phải chọn lý do thay đổi trước", Toast.LENGTH_LONG).show();
            return;
        }

        showDialogLoadding();
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, UserInfo.getInstance(this).getAccessToken()));
        arrHeads.add(new MHead(Conts.KEY_APPOINTMENT_DATE, CommonMethod.formatFullTimeToString(mCalendar.getTime())));
        arrHeads.add(new MHead(Conts.KEY_REASON_ID, mReason));
        ApiUtils.getAPIService(arrHeads).changeTicketAppointment(mIdTask).enqueue(new Callback<ChangeTicketAppointmentResult>() {
            @Override
            public void onResponse(Call<ChangeTicketAppointmentResult> call, Response<ChangeTicketAppointmentResult> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    ChangeTicketAppointmentResult changeTicketAppointmentResult = response.body();
                    Log.e(TAG, "loadListAvailableReason.onResponse(), statusCode: " + statusCode + ", changeTicketAppointmentResult: " + changeTicketAppointmentResult);
                    onTicketAppointmentChanged(changeTicketAppointmentResult);
                    dismisDialogLoading();
                }
            }

            @Override
            public void onFailure(Call<ChangeTicketAppointmentResult> call, Throwable t) {
                dismisDialogLoading();
                Log.e(TAG, "loadListAvailableReason.onFailure() --> " + t);
            }
        });
    }

    private void onTicketAppointmentChanged(ChangeTicketAppointmentResult changeTicketAppointmentResult) {
        if (changeTicketAppointmentResult != null && changeTicketAppointmentResult.getStatusCode() == 200) {
            Toast.makeText(this, "Thay đổi lịch hẹn thành công", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Thay đổi lịch hẹn không thành công", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        try {
            mReason = mGetChangeTicketAppointmentReasonsResult.result.get(i%mGetChangeTicketAppointmentReasonsResult.result.size() - 1)._id;
            Log.e(TAG, "onCheckedChanged() --> id: " + mReason + ", i: " + i);
        } catch (Exception e) {
            Log.e(TAG, "onCheckedChanged() --> e: " + e);
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        updateDateTimeUI();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        updateDateTimeUI();
    }
}
