package com.cnc.hcm.cnctrack.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.api.ApiUtils;
import com.cnc.hcm.cnctrack.api.MHead;
import com.cnc.hcm.cnctrack.base.BaseActivity;
import com.cnc.hcm.cnctrack.dialog.DialogNotification;
import com.cnc.hcm.cnctrack.model.ChangeTicketAppointmentResult;
import com.cnc.hcm.cnctrack.model.GetChangeTicketAppointmentReasonsResult;
import com.cnc.hcm.cnctrack.util.CommonMethod;
import com.cnc.hcm.cnctrack.util.Conts;
import com.cnc.hcm.cnctrack.util.UserInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeTimeActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = ChangeTimeActivity.class.getSimpleName();
    public static final int CODE_CHANGE_TIME = 123;
    private TextView tv_time;
    private TextView tv_date;
    private RadioGroup rg_reason;
    private Calendar mCalendar;
    private String mReason;
    private GetChangeTicketAppointmentReasonsResult mGetChangeTicketAppointmentReasonsResult;
    private ProgressDialog mProgressDialog;
    private DialogNotification dialogNotification;
    private String mIdTask;

    private ArrayList<CheckBox> arrReasonCheckbox = new ArrayList<>();

    @Override
    public void onViewReady(@Nullable Bundle savedInstanceState) {
        initObjects();
        ImageView imvSave = findViewById(R.id.img_change_time);
        ImageView imvCancel = findViewById(R.id.img_back);
        tv_time = findViewById(R.id.tv_time);
        tv_date = findViewById(R.id.tv_date);
        rg_reason = findViewById(R.id.rg_reason);

        String time = getIntent().getStringExtra(Conts.KEY_WORK_TIME);
        mIdTask = getIntent().getStringExtra(Conts.KEY_ID_TASK);
        if (!TextUtils.isEmpty(time)) {
            Date date = CommonMethod.formatTimeFromServerToDate(time);

            mCalendar = CommonMethod.getInstanceCalendar();
            mCalendar.setTime(date);
            updateDateTimeUI();
        }

        loadListAvailableReason();
        rg_reason.setOnCheckedChangeListener(this);

        tv_time.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        imvSave.setOnClickListener(this);
        imvCancel.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_time;
    }

    private void initObjects() {
        dialogNotification = new DialogNotification(this);
        dialogNotification.setTitle(getString(R.string.error_occurred));
        dialogNotification.setTextBtnOK(getString(R.string.try_again));
        dialogNotification.setOnClickDialogNotificationListener(new DialogNotification.OnClickDialogNotificationListener() {
            @Override
            public void onClickButtonOK() {
                tryToSaveChangedTime();
            }

            @Override
            public void onClickButtonExit() {

            }
        });
    }

    private void loadListAvailableReason() {
        showProgressLoadding();
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
                    dismisProgressLoading();
                }
            }

            @Override
            public void onFailure(Call<GetChangeTicketAppointmentReasonsResult> call, Throwable t) {
                dismisProgressLoading();
                Log.e(TAG, "loadListAvailableReason.onFailure() --> " + t);
            }
        });
    }

    private void onListAvailableReasonLoaded() {
        if (mGetChangeTicketAppointmentReasonsResult != null) {
            final List<GetChangeTicketAppointmentReasonsResult.Result> result = mGetChangeTicketAppointmentReasonsResult.result;
            if (result != null) {
                arrReasonCheckbox.clear();
                for (int i = 0; i < result.size(); i++) {
                    GetChangeTicketAppointmentReasonsResult.Result item = result.get(i);
                    String rbString = Conts.BLANK;
                    if (item != null) {
                        rbString += item.reason;
                        if (item.action != null) {
                            rbString += "; " + item.action.title;
                        }
                        rbString += ".";

                        LinearLayout linearLayout = new LinearLayout(this);
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 170));
                        linearLayout.setOrientation(LinearLayout.VERTICAL);

                        TextView tvLine = new TextView(this);
                        tvLine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 4));
                        tvLine.setBackgroundColor(getResources().getColor(R.color.color_line_dark));

                        linearLayout.addView(tvLine);

                        CheckBox cbxReason = new CheckBox(this);
                        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.bottomMargin = 20;
                        params.topMargin = 20;
                        cbxReason.setLayoutParams(params);
                        cbxReason.setTextSize(14f);
                        cbxReason.setText(rbString);
                        final int finalI = i;
                        cbxReason.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                try {
                                    if (b) {
                                        mReason = result.get(finalI % (result.size() - 1))._id;
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "onCheckedChanged() --> e: " + e);
                                }
                            }
                        });
                        arrReasonCheckbox.add(cbxReason);
                        linearLayout.addView(cbxReason);
                        rg_reason.addView(linearLayout);

                    }
                }
            }
        }
    }


    private void updateDateTimeUI() {
        tv_time.setText(CommonMethod.formatHourMinuteToString(mCalendar.getTimeInMillis()));
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
        dismisProgressLoading();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_time:
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = mCalendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, true);
                timePickerDialog.show();
                break;
            case R.id.tv_date:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;

            case R.id.img_change_time:
                tryToSaveChangedTime();
                break;
            case R.id.img_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    private void tryToSaveChangedTime() {
        if (!checkReasonEmpty()) {
            Toast.makeText(ChangeTimeActivity.this, "Bạn phải chọn lý do thay đổi trước", Toast.LENGTH_LONG).show();
            return;
        }

        showProgressLoadding();
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, UserInfo.getInstance(this).getAccessToken()));
        long seventHour = TimeUnit.HOURS.toMillis(7);
        long time = mCalendar.getTimeInMillis() - seventHour;
        mCalendar.setTimeInMillis(time);
        String appointmentDateChange = CommonMethod.formatFullTimeToString(mCalendar.getTime());
        arrHeads.add(new MHead(Conts.KEY_APPOINTMENT_DATE, appointmentDateChange));
        arrHeads.add(new MHead(Conts.KEY_REASON_ID, mReason));

        changeTicketAppointment(arrHeads);
    }

    private boolean checkReasonEmpty() {
        for (int i = 0; i < arrReasonCheckbox.size(); i++) {
            if (arrReasonCheckbox.get(i).isChecked()) {
                return true;
            }
        }
        return false;
    }

    private void changeTicketAppointment(List<MHead> arrHeads) {
        ApiUtils.getAPIService(arrHeads).changeTicketAppointment(mIdTask).enqueue(new Callback<ChangeTicketAppointmentResult>() {
            @Override
            public void onResponse(Call<ChangeTicketAppointmentResult> call, Response<ChangeTicketAppointmentResult> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    ChangeTicketAppointmentResult changeTicketAppointmentResult = response.body();
                    Log.e(TAG, "loadListAvailableReason.onResponse(), statusCode: " + statusCode + ", changeTicketAppointmentResult: " + changeTicketAppointmentResult);
                    onTicketAppointmentChanged(changeTicketAppointmentResult);
                    dismisProgressLoading();
                }
            }

            @Override
            public void onFailure(Call<ChangeTicketAppointmentResult> call, Throwable t) {
                dismisProgressLoading();
                Log.e(TAG, "changeTicketAppointment.onFailure() --> " + t);
                if (dialogNotification != null) {
                    dialogNotification.setContentMessage("changeTicketAppointment.onFailure()\n" + t.getMessage());
                    dialogNotification.show();
                }
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
            mReason = mGetChangeTicketAppointmentReasonsResult.result.get(i % mGetChangeTicketAppointmentReasonsResult.result.size() - 1)._id;
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
