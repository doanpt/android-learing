package com.cnc.hcm.cnctracking.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.MainActivity;
import com.cnc.hcm.cnctracking.activity.SplashActivity;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.customeview.MySelectorDecorator;
import com.cnc.hcm.cnctracking.customeview.OneDayDecorator;
import com.cnc.hcm.cnctracking.model.CountTaskResult;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_SINGLE;

/**
 * Created by giapmn on 1/2/18.
 */

public class MonthViewFragment extends Fragment implements OnMonthChangedListener, OnDateSelectedListener {
    private static final String TAG = MonthViewFragment.class.getSimpleName();

    private TextView tvT2, tvT3, tvT4, tvT5, tvT6, tvT7, tvCN;
    private TextView[] listTextView = {tvT2, tvT3, tvT4, tvT5, tvT6, tvT7, tvCN};
    private int[] listId = new int[]{R.id.tv_number_task_thu_2, R.id.tv_number_task_thu_3, R.id.tv_number_task_thu_4,
            R.id.tv_number_task_thu_5, R.id.tv_number_task_thu_6, R.id.tv_number_task_thu_7, R.id.tv_number_task_chu_nhat};

    private MaterialCalendarView calendarView;
    private OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private MainActivity mainActivity;
    private List<String> arrAllDate;

    public MonthViewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrAllDate = SplashActivity.allDateInYears;
        Log.d(TAG, "AllDate: " + arrAllDate.size());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setMonthViewFragment(MonthViewFragment.this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_month, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mainActivity != null) {
            String date = CommonMethod.formatFullTimeToString(calendarView.getSelectedDate().getDate());
            String accessToken = UserInfo.getInstance(getContext()).getAccessToken();
            mainActivity.tryGetTaskList(accessToken, date, date);
        }
    }

    private void initViews(View view) {
        for (int i = 0; i < listTextView.length; i++) {
            listTextView[i] = (TextView) view.findViewById(listId[i]);
        }
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        Calendar instance = Calendar.getInstance();
        calendarView.setSelectedDate(instance.getTime());
        calendarView.setOnMonthChangedListener(this);
        calendarView.setOnDateChangedListener(this);
        calendarView.setTopbarVisible(false);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
        calendarView.setSelectionMode(SELECTION_MODE_SINGLE);
        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR) - 1, Calendar.JANUARY, 1);
        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR) + 1, Calendar.DECEMBER, 31);
        calendarView.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();
        calendarView.addDecorators(
                new MySelectorDecorator(getActivity()),
                oneDayDecorator
        );

        Animation animationIn = AnimationUtils.loadAnimation(getContext(), R.anim.anim_move_up_in);
        view.startAnimation(animationIn);
    }


    public void gotoCurrentDate() {
        Calendar instance = Calendar.getInstance();
        oneDayDecorator.setDate(instance.getTime());
        calendarView.invalidateDecorators();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        if (mainActivity != null) {
            mainActivity.updateMonthChange(date);
        }
        String dateOfWeek = CommonMethod.formatFullTimeToString(date.getDate());
        List<String> listWeek = null;
        for (int i = 0; i < arrAllDate.size(); i++) {
            if (arrAllDate.get(i).toString().equals(dateOfWeek)) {
                listWeek = new ArrayList<>();
                Log.d(TAG, "DateOfWeek: --------------------");
                for (int j = 0; j < 7; j++) {
                    listWeek.add(arrAllDate.get(i + j));
                    Log.d(TAG, "DateOfWeek: " + arrAllDate.get(i + j).toString());
                }
                break;
            }
        }
        if (listWeek != null && listWeek.size() > 0) {
            String startDate = listWeek.get(0).toString();
            String endDate = listWeek.get(listWeek.size() - 1).toString();

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
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();

        String accessToken = UserInfo.getInstance(getContext()).getAccessToken();
        String dateSelected = CommonMethod.formatFullTimeToString(date.getDate());
        Log.d(TAG, "onDateSelected: " + dateSelected);

        if (mainActivity != null) {
            mainActivity.tryGetTaskList(accessToken, dateSelected, dateSelected);
        }

    }
}
