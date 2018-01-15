package com.cnc.hcm.cnctracking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class SplashActivity extends AppCompatActivity {

    public static List<String> allDateInYears = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        allDateInYears = getAllDateInYear();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isLogin = UserInfo.getInstance(SplashActivity.this).getIsLogin();
                if (isLogin) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 500);
    }


    public List<String> getAllDateInYear() {
        List<String> allDate = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
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
        return allDate;
    }
}
