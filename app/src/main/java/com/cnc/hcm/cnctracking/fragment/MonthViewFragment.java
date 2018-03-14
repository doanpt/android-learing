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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
    private TextView tvSelectedT2, tvSelectedT3, tvSelectedT4, tvSelectedT5, tvSelectedT6, tvSelectedT7, tvSelectedCN;
    private TextView[] listTextView = {tvT2, tvT3, tvT4, tvT5, tvT6, tvT7, tvCN};
    private TextView[] listTextViewSelected = {tvSelectedT2, tvSelectedT3, tvSelectedT4, tvSelectedT5, tvSelectedT6, tvSelectedT7, tvSelectedCN};


    private int[] listId = new int[]{R.id.tv_number_task_thu_2, R.id.tv_number_task_thu_3, R.id.tv_number_task_thu_4,
            R.id.tv_number_task_thu_5, R.id.tv_number_task_thu_6, R.id.tv_number_task_thu_7, R.id.tv_number_task_chu_nhat};
    private int[] listIdSelected = new int[]{R.id.tv_border_select_t2, R.id.tv_border_select_t3, R.id.tv_border_select_t4,
            R.id.tv_border_select_t5, R.id.tv_border_select_t6, R.id.tv_border_select_t7, R.id.tv_border_select_cn};

    private MaterialCalendarView calendarView;
    private OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private MainActivity mainActivity;
    private List<String> arrAllDate = new ArrayList<>();
    private List<String> listWeek = new ArrayList<>();


    public MonthViewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setMonthViewFragment(MonthViewFragment.this);
            String date = CommonMethod.formatFullTimeToString(calendarView.getSelectedDate().getDate());
            String accessToken = UserInfo.getInstance(getContext()).getAccessToken();
            mainActivity.tryGetTaskList(accessToken, date, date);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_month, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        for (int i = 0; i < listTextView.length; i++) {
            listTextView[i] = (TextView) view.findViewById(listId[i]);
            listTextViewSelected[i] = (TextView) view.findViewById(listIdSelected[i]);
        }
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        Calendar instance = CommonMethod.getInstanceCalendar();
        calendarView.setSelectedDate(instance.getTime());
        calendarView.setOnMonthChangedListener(this);
        calendarView.setOnDateChangedListener(this);
        calendarView.setTopbarVisible(false);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        calendarView.setSelectionMode(SELECTION_MODE_SINGLE);
        Calendar instance1 = CommonMethod.getInstanceCalendar();
        instance1.set(instance1.get(Calendar.YEAR) - 1, Calendar.JANUARY, 1);
        Calendar instance2 = CommonMethod.getInstanceCalendar();
        instance2.set(instance2.get(Calendar.YEAR) + 1, Calendar.DECEMBER, 31);
        calendarView.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();
        calendarView.addDecorators(
                new MySelectorDecorator(getActivity()),
                oneDayDecorator
        );

        int currentDate = instance.get(Calendar.DATE);
        setBorderDateSelected(currentDate);

        Animation animationIn = AnimationUtils.loadAnimation(getContext(), R.anim.anim_move_up_in);
        view.startAnimation(animationIn);
    }

    private void setBorderDateSelected(int currentDate) {
        for (int index = 0; index < listWeek.size(); index++) {
            String time = listWeek.get(index).toString();
            Date dateTemp = CommonMethod.formatTimeFromServerToDate(time);
            int dateIndex = dateTemp.getDate();
            if (dateIndex == currentDate) {
                listTextViewSelected[index].setVisibility(View.VISIBLE);
            } else {
                listTextViewSelected[index].setVisibility(View.INVISIBLE);
            }
        }
    }


    public String gotoCurrentDate() {
        Calendar instance = CommonMethod.getInstanceCalendar();
        String dateSelected = CommonMethod.formatFullTimeToString(instance.getTime());

        calendarView.setSelectedDate(instance.getTime());
        oneDayDecorator.setDate(instance.getTime());
        calendarView.invalidateDecorators();
        int currentDay = instance.get(Calendar.DAY_OF_MONTH);
        setBorderDateSelected(currentDay);
        return dateSelected;
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        if (mainActivity != null) {
            mainActivity.updateMonthChange(date);
            mainActivity.showProgressLoadding();
        }

        for (int i = 0; i < listTextViewSelected.length; i++) {
            listTextViewSelected[i].setVisibility(View.INVISIBLE);
        }

        String dateOfWeek = CommonMethod.formatFullTimeToString(date.getDate());
        Log.d(TAG, "DateOfWeek: -------------------- " + dateOfWeek);
        arrAllDate.clear();
        arrAllDate.addAll(getAllDateInYear());
        Log.d(TAG, "AllDate: " + arrAllDate.size());
        listWeek.clear();
        for (int i = 0; i < arrAllDate.size(); i++) {
            if (arrAllDate.get(i).toString().equals(dateOfWeek)) {
                for (int j = 0; j < 7; j++) {
                    listWeek.add(arrAllDate.get(i + j));
                    Log.d(TAG, "DateOfWeek: " + arrAllDate.get(i + j).toString());
                    listTextView[j].setText(getContext().getResources().getString(R.string.text_am_1));
                }
                break;
            }
        }
        if (listWeek != null && listWeek.size() > 0) {
            String startDate = listWeek.get(0).toString();
            String endDate = listWeek.get(listWeek.size() - 1).toString();
            Log.d(TAG, "DateOfWeek: StartDate" + startDate);
            Log.d(TAG, "DateOfWeek: EndDate" + endDate);

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
                                    Log.d(TAG, "CountResult: " + results.get(i).getDate().getDay() + "/" + results.get(i).getDate().getMonth() + "/" + results.get(i).getDate().getYear() + " -  " + results.get(i).getCount());
                                    listTextView[i].setText(results.get(i).getCount() + Conts.BLANK);
                                    if (results.get(i).getCount() <= Conts.DEFAULT_VALUE_INT_0) {
                                        listTextView[i].setVisibility(View.INVISIBLE);
                                    } else {
                                        listTextView[i].setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        } else {
                            CommonMethod.makeToast(getContext(), "Không count dc task nao");
                        }
                    } else {
                        CommonMethod.makeToast(getContext(), response.toString());

                    }

                    if (mainActivity != null) {
                        mainActivity.dismisProgressLoading();
                    }
                }

                @Override
                public void onFailure(Call<CountTaskResult> call, Throwable t) {
                    Log.e(TAG, "tryGetCountTask.onFailure() --> " + t);
                    t.printStackTrace();
                    CommonMethod.makeToast(getContext(), t.getMessage() != null ? t.getMessage().toString() : "onFailure");
                    if (mainActivity != null) {
                        mainActivity.dismisProgressLoading();
                    }
                }
            });
        } else {
            if (mainActivity != null) {
                mainActivity.dismisProgressLoading();
            }
            CommonMethod.makeToast(getContext(), "listWeek " + listWeek);
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
            mainActivity.setDateSelected(dateSelected);
            mainActivity.tryGetTaskList(accessToken, dateSelected, dateSelected);
        }
        setBorderDateSelected(date.getDay());

    }

    public List<String> getAllDateInYear() {
        List<String> allDate = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            Calendar calendar = CommonMethod.getInstanceCalendar();
            calendar.set(Calendar.DATE, 01);
            calendar.set(Calendar.MONTH, 01);
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + i);

            int years = calendar.get(Calendar.YEAR);
            for (int index = 0; index < 12; index++) {
                String month = index < 9 ? ("0" + (index + 1)) : ((index + 1) + Conts.BLANK);
                String dateFirstOfMonthTemp = years + "-" + month + "-01" + Conts.FORMAT_TIME_FULL;
                SimpleDateFormat format = new SimpleDateFormat(Conts.FORMAT_DATE_FULL);
                Date dateFirstOfMonth = null;
                try {
                    dateFirstOfMonth = format.parse(dateFirstOfMonthTemp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (dateFirstOfMonth != null) {
                    calendar.setTime(dateFirstOfMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    int myMonth = calendar.get(Calendar.MONTH);
                    while (myMonth == calendar.get(Calendar.MONTH)) {
                        allDate.add(format.format(calendar.getTime()));
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                }
            }
        }

        return allDate;
    }
}
