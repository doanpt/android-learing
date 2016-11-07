package com.dvt.samsung.finalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by sev_user on 11/7/2016.
 */

public class SplashActivity extends Activity {
    private ImageView ivSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ivSplash= (ImageView) findViewById(R.id.iv_splash);
        Animation animation= AnimationUtils.loadAnimation(SplashActivity.this,R.anim.rotate_move_down);
        ivSplash.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }, 3000);
    }
}
