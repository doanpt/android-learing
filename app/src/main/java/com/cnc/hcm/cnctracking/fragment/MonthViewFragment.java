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

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.MainActivity;
import com.cnc.hcm.cnctracking.customeview.MySelectorDecorator;
import com.cnc.hcm.cnctracking.customeview.OneDayDecorator;
import com.cnc.hcm.cnctracking.util.Conts;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.Calendar;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_SINGLE;

/**
 * Created by giapmn on 1/2/18.
 */

public class MonthViewFragment extends Fragment {
    private static final String TAG = MonthViewFragment.class.getSimpleName();

    private MaterialCalendarView calendarView;
    private OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private MainActivity mainActivity;

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
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_month, container, false);


        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        Calendar instance = Calendar.getInstance();
        calendarView.setSelectedDate(instance.getTime());
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                if (mainActivity != null) {
                    mainActivity.updateMonthChange(date);
                }

                Log.d(TAG, "onMonthChanged: ");

            }
        });
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                oneDayDecorator.setDate(date.getDate());
                widget.invalidateDecorators();
                Log.d(TAG, "onDateSelected: " + date);
            }
        });
        calendarView.setTopbarVisible(false);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
        calendarView.setSelectionMode(SELECTION_MODE_SINGLE);
        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);
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
        return view;
    }

    public void gotoCurrentDate() {
        Calendar instance = Calendar.getInstance();
        oneDayDecorator.setDate(instance.getTime());
        calendarView.invalidateDecorators();
    }
}
