package com.google.foods.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.foods.R;
import com.google.foods.utils.CommonValue;
import com.google.foods.utils.UserInfor;

/**
 * Created by sev_user on 8/1/2017.
 */

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DISPLAY_LENGTH = 4000;
    private static final long TIME_TEXT_APP_DELAY = 2000;
    private ImageView imvLauncher;
    private TextView tvNameApp;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        //post deplay 4 second after next to MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isAdminLogin = UserInfor.getInstance(SplashActivity.this).isAdminLogin();
                if (isAdminLogin) {
                    Intent intent = new Intent(SplashActivity.this, OrderFoodManagerActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }


                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void initView() {
        this.tvWelcome = (TextView) findViewById(R.id.tv_welcome);
        this.tvNameApp = (TextView) findViewById(R.id.tv_name_application);
        this.tvNameApp.setText(CommonValue.BLANK);
        this.imvLauncher = (ImageView) findViewById(R.id.iv_splash);
        //set animation for image and text welcome
        this.imvLauncher.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_image_welcome));
        this.tvWelcome.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_text_welcome));
        //post delay 2 second after display app name
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.this.tvNameApp.setText(R.string.app_name);
                SplashActivity.this.tvNameApp.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.anim_text_app_name));
            }
        }, TIME_TEXT_APP_DELAY);
    }

}
