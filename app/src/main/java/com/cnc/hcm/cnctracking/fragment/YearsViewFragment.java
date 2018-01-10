package com.cnc.hcm.cnctracking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.util.Conts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by giapmn on 1/2/18.
 */

public class YearsViewFragment extends Fragment implements View.OnClickListener {

    private LinearLayout llBgT1, llBgT2, llBgT3, llBgT4, llBgT5, llBgT6, llBgT7, llBgT8, llBgT9, llBgT10, llBgT11, llBgT12;
    private TextView tvNumberTaskT1, tvNumberTaskT2, tvNumberTaskT3, tvNumberTaskT4, tvNumberTaskT5, tvNumberTaskT6,
            tvNumberTaskT7, tvNumberTaskT8, tvNumberTaskT9, tvNumberTaskT10, tvNumberTaskT11, tvNumberTaskT12;

    private TextView arrTextView[] = {tvNumberTaskT1, tvNumberTaskT2, tvNumberTaskT3, tvNumberTaskT4, tvNumberTaskT5, tvNumberTaskT6,
            tvNumberTaskT7, tvNumberTaskT8, tvNumberTaskT9, tvNumberTaskT10, tvNumberTaskT11, tvNumberTaskT12};
    private LinearLayout arrLinear[] = {llBgT1, llBgT2, llBgT3, llBgT4, llBgT5, llBgT6, llBgT7, llBgT8, llBgT9, llBgT10, llBgT11, llBgT12};

    private int[] idTextView = new int[]{R.id.tv_number_task_t1, R.id.tv_number_task_t2, R.id.tv_number_task_t3, R.id.tv_number_task_t4
            , R.id.tv_number_task_t5, R.id.tv_number_task_t6, R.id.tv_number_task_t7, R.id.tv_number_task_t8
            , R.id.tv_number_task_t9, R.id.tv_number_task_t10, R.id.tv_number_task_t11, R.id.tv_number_task_t12};

    private int[] idLinear = new int[]{R.id.ll_bg_t1, R.id.ll_bg_t2, R.id.ll_bg_t3, R.id.ll_bg_t4, R.id.ll_bg_t5, R.id.ll_bg_t6,
            R.id.ll_bg_t7, R.id.ll_bg_t8, R.id.ll_bg_t9, R.id.ll_bg_t10, R.id.ll_bg_t11, R.id.ll_bg_t12};


    public YearsViewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_years, container, false);

        for (int index = 0; index < arrLinear.length; index++) {
            arrTextView[index] = (TextView) view.findViewById(idTextView[index]);
            arrLinear[index] = (LinearLayout) view.findViewById(idLinear[index]);
            arrLinear[index].setOnClickListener(this);
        }

        int monthOfYears = Calendar.getInstance().get(Calendar.MONTH);
        handleActionGetTaskByFilterDate(monthOfYears);

        Animation animationIn = AnimationUtils.loadAnimation(getContext(), R.anim.anim_move_up_in);
        view.startAnimation(animationIn);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View view) {
        int position = -1;
        switch (view.getId()) {
            case R.id.ll_bg_t1:
                position = 0;
                break;
            case R.id.ll_bg_t2:
                position = 1;
                break;
            case R.id.ll_bg_t3:
                position = 2;
                break;
            case R.id.ll_bg_t4:
                position = 3;
                break;
            case R.id.ll_bg_t5:
                position = 4;
                break;
            case R.id.ll_bg_t6:
                position = 5;
                break;
            case R.id.ll_bg_t7:
                position = 6;
                break;
            case R.id.ll_bg_t8:
                position = 7;
                break;
            case R.id.ll_bg_t9:
                position = 8;
                break;
            case R.id.ll_bg_t10:
                position = 9;
                break;
            case R.id.ll_bg_t11:
                position = 10;
                break;
            case R.id.ll_bg_t12:
                position = 11;
                break;
        }
        handleActionGetTaskByFilterDate(position);

    }


    private void handleActionGetTaskByFilterDate(int position) {
        setSelected(position);
        String[] arrayDate = getStartEndDate(position);
        String startDate = arrayDate[0];
        String endDate = arrayDate[1];

        Log.d("StartDate", "---------------------------------------");
        Log.d("StartDate", "StartDate: " + startDate);
        Log.d("StartDate", "EndDate: " + endDate);

    }

    private void setSelected(int position) {
        for (int index = 0; index < arrTextView.length; index++) {
            if (index == position) {
                arrLinear[index].setBackgroundResource(R.color.colorPrimaryDark);
            } else {
                arrLinear[index].setBackgroundResource(R.color.colorPrimary);
            }
        }
    }

    private String[] getStartEndDate(int position) {
        String[] arr = new String[2];
        Calendar calendar = Calendar.getInstance();
        int years = calendar.get(Calendar.YEAR);
        String month = position < 9 ? ("0" + (position + 1)) : ((position + 1) + Conts.BLANK);
        String dateFirstOfMonthTemp = years + "-" + month + "-01T00:01:00Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dateFirstOfMonth = null;
        try {
            dateFirstOfMonth = format.parse(dateFirstOfMonthTemp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateFirstOfMonth != null) {
            ArrayList<String> arrDateOfMonth = new ArrayList<>();
            calendar.setTime(dateFirstOfMonth);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            int myMonth = calendar.get(Calendar.MONTH);
            while (myMonth == calendar.get(Calendar.MONTH)) {
                arrDateOfMonth.add(format.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            String startDate = arrDateOfMonth.get(0);
            String endDate = arrDateOfMonth.get(arrDateOfMonth.size() - 1);
            arr[0] = startDate;
            arr[1] = endDate;
        }
        return arr;
    }
}
