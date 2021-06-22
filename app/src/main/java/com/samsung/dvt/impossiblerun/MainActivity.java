package com.samsung.dvt.impossiblerun;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity implements View.OnClickListener, Runnable {
    private static final long TIME_REPEAT = 500;
    private Button btnLeft, btnRight;
    private ImageView imgSquare, imgBall, imgRetry;
    private TextView tvScore;
    private int fromDegree;
    private int mBallColor;
    private int mSquareColor;
    private int score;
    private Random random;
    private Thread mThread;
    private Handler mHandler;
    private boolean isPlay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initGame();
        random = new Random();
        mHandler = new Handler();
        mThread = new Thread(this);
        mThread.start();
    }

    private void initGame() {
        fromDegree = 0;
        mBallColor = 0;
        mSquareColor = 0;
        score=0;
    }

    private void initView() {
        btnLeft = (Button) findViewById(R.id.btn_left);
        btnRight = (Button) findViewById(R.id.btn_right);
        imgBall = (ImageView) findViewById(R.id.img_ball);
        imgSquare = (ImageView) findViewById(R.id.img_square);
        tvScore = (TextView) findViewById(R.id.tv_score);
        imgRetry = (ImageView) findViewById(R.id.img_retry);
        imgRetry.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
    }

    private void startBallAnimation() {
        mBallColor = random.nextInt(4);
        Log.d("Coo", "----" + mBallColor);
        final TranslateAnimation translateAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
        translateAnimation.setDuration(TIME_REPEAT);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                imgBall.setImageResource(R.drawable.ball_0 + mBallColor);
                imgBall.startAnimation(translateAnimation);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                clickLeftbutton();
                break;
            case R.id.btn_right:
                clickRightButton();
                break;
            case R.id.img_retry:
                initGame();
                isPlay=true;
                imgSquare.clearAnimation();
                tvScore.setText("00");
                imgRetry.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    private void clickRightButton() {
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegree, fromDegree+90, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(250);
        rotateAnimation.setFillAfter(true);
        imgSquare.startAnimation(rotateAnimation);
        fromDegree += 90;
        mSquareColor--;
        if (mSquareColor < 0) {
            mSquareColor = 3;
        }
        Log.d("Coo", "RightmSquareColor:" + mSquareColor);

    }

    private void clickLeftbutton() {
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegree, fromDegree - 90, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(250);
        rotateAnimation.setFillAfter(true);
        imgSquare.startAnimation(rotateAnimation);
        fromDegree -= 90;
        mSquareColor++;
        if (mSquareColor > 3) {
            mSquareColor = 0;
        }
        Log.d("Coo", "LeftmSquareColor:" + mSquareColor);
    }

    @Override
    public void run() {
        while (true) {
            if (isPlay == true) {
                startBallAnimation();
                try {
                    Thread.sleep(TIME_REPEAT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mSquareColor == mBallColor) {
                    score++;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvScore.setText("Score: " + score);
                        }
                    });
                    Log.d("Coo", "++Score:" + score + " squreColor:" + mSquareColor + "  balColor:" + mBallColor);
                } else {
                    isPlay = false;
                    score--;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            imgBall.setVisibility(View.INVISIBLE);
                            tvScore.setText("Game Over");
                            imgRetry.setVisibility(View.VISIBLE);
                        }
                    });
                    Log.d("Coo", "--Score:" + score + " squreColor:" + mSquareColor + "  balColor:" + mBallColor);
                }

            }
        }
    }
}
