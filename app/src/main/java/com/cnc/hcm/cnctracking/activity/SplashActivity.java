package com.cnc.hcm.cnctracking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.base.BaseActivity;
import com.cnc.hcm.cnctracking.util.UserInfo;


public class SplashActivity extends BaseActivity {

    @Override
    public void onViewReady(@Nullable Bundle savedInstanceState) {
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }
}
