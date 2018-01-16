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
import com.cnc.hcm.cnctracking.activity.MainActivity;
import com.cnc.hcm.cnctracking.activity.SplashActivity;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.model.CountTaskResult;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by giapmn on 1/2/18.
 */

public class YearsViewFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = YearsViewFragment.class.getSimpleName();

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

    private int[] countTask = new int[Conts.DEFAULT_VALUE_INT_12];
    private MainActivity mainActivity;

    public YearsViewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_years, container, false);
        initViews(view);
        getCountTask();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        int monthOfYears = Calendar.getInstance().get(Calendar.MONTH);
        handleActionGetTaskByFilterDate(monthOfYears);
    }

    private void initViews(View view) {
        for (int index = 0; index < arrLinear.length; index++) {
            arrTextView[index] = (TextView) view.findViewById(idTextView[index]);
            arrLinear[index] = (LinearLayout) view.findViewById(idLinear[index]);
            arrLinear[index].setOnClickListener(this);
        }

        Animation animationIn = AnimationUtils.loadAnimation(getContext(), R.anim.anim_move_up_in);
        view.startAnimation(animationIn);
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
        if (position != -1) {
            setSelected(position);
            String[] arrayDate = CommonMethod.getStartEndDate(position);
            String startDate = arrayDate[0];
            String endDate = arrayDate[1];

            Log.d(TAG, "---------------------------------------");
            Log.d(TAG, "StartDate: " + startDate);
            Log.d(TAG, "EndDate: " + endDate);

            String accessToken = UserInfo.getInstance(getContext()).getAccessToken();
            if (mainActivity != null) {
                mainActivity.tryGetTaskList(accessToken, startDate, endDate);
            }
        }
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

    private void getCountTask() {
        String startDate = SplashActivity.allDateInYears.get(0).toString();
        String endDate = SplashActivity.allDateInYears.get(SplashActivity.allDateInYears.size() - 1).toString();

        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, UserInfo.getInstance(getContext()).getAccessToken()));
        arrHeads.add(new MHead(Conts.KEY_START_DATE, startDate));
        arrHeads.add(new MHead(Conts.KEY_END_DATE, endDate));

        ApiUtils.getAPIService(arrHeads).getCountTask().enqueue(new Callback<CountTaskResult>() {
            @Override
            public void onResponse(Call<CountTaskResult> call, Response<CountTaskResult> response) {
                int statusCode = response.code();
                Log.e(TAG, "tryGetCountTask.onResponse(), statusCode: " + statusCode);

                if (response.isSuccessful()) {
                    Log.e(TAG, "tryGetCountTask.onResponse(), --> response: " + response.toString());

                    CountTaskResult countTaskResult = response.body();
                    if (countTaskResult != null) {
                        List<CountTaskResult.Result> results = countTaskResult.getResult();

                        if (results != null && results.size() > 0) {
                            Log.d(TAG, "CountResult -------------------");
                            for (int i = 0; i < results.size(); i++) {
                                Log.d(TAG, "CountResult: " + results.get(i).getDate().getDay() + "/" + results.get(i).getDate().getMonth() + " -  " + results.get(i).getCount());
                                switch (results.get(i).getDate().getMonth()) {
                                    case Conts.DEFAULT_VALUE_INT_1:
                                        updateCountTask(Conts.DEFAULT_VALUE_INT_0, results.get(i).getCount());
                                        break;
                                    case Conts.DEFAULT_VALUE_INT_2:
                                        updateCountTask(Conts.DEFAULT_VALUE_INT_1, results.get(i).getCount());
                                        break;
                                    case Conts.DEFAULT_VALUE_INT_3:
                                        updateCountTask(Conts.DEFAULT_VALUE_INT_2, results.get(i).getCount());
                                        break;
                                    case Conts.DEFAULT_VALUE_INT_4:
                                        updateCountTask(Conts.DEFAULT_VALUE_INT_3, results.get(i).getCount());
                                        break;
                                    case Conts.DEFAULT_VALUE_INT_5:
                                        updateCountTask(Conts.DEFAULT_VALUE_INT_4, results.get(i).getCount());
                                        break;
                                    case Conts.DEFAULT_VALUE_INT_6:
                                        updateCountTask(Conts.DEFAULT_VALUE_INT_5, results.get(i).getCount());
                                        break;
                                    case Conts.DEFAULT_VALUE_INT_7:
                                        updateCountTask(Conts.DEFAULT_VALUE_INT_6, results.get(i).getCount());
                                        break;
                                    case Conts.DEFAULT_VALUE_INT_8:
                                        updateCountTask(Conts.DEFAULT_VALUE_INT_7, results.get(i).getCount());
                                        break;
                                    case Conts.DEFAULT_VALUE_INT_9:
                                        updateCountTask(Conts.DEFAULT_VALUE_INT_8, results.get(i).getCount());
                                        break;
                                    case Conts.DEFAULT_VALUE_INT_10:
                                        updateCountTask(Conts.DEFAULT_VALUE_INT_9, results.get(i).getCount());
                                        break;
                                    case Conts.DEFAULT_VALUE_INT_11:
                                        updateCountTask(Conts.DEFAULT_VALUE_INT_10, results.get(i).getCount());
                                        break;
                                    case Conts.DEFAULT_VALUE_INT_12:
                                        updateCountTask(Conts.DEFAULT_VALUE_INT_11, results.get(i).getCount());

                                        break;
                                }
                            }

                            for (int j = 0; j < arrTextView.length; j++) {
                                arrTextView[j].setText(countTask[j] + Conts.BLANK);
                            }
                        }
                    } else {
                        CommonMethod.makeToast(getContext(), "Không count dc task nao");
                    }
                } else {
                    CommonMethod.makeToast(getContext(), response.toString());
                }
            }

            @Override
            public void onFailure(Call<CountTaskResult> call, Throwable t) {
                Log.e(TAG, "tryGetCountTask.onFailure() --> " + t);
                t.printStackTrace();
                CommonMethod.makeToast(getContext(), t.getMessage() != null ? t.getMessage().toString() : "onFailure");
            }
        });

    }

    private void updateCountTask(int index, int count) {
        countTask[index] = countTask[index] + count;
    }
}
