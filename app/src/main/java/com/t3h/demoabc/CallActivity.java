package com.t3h.demoabc;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;

import static com.t3h.demoabc.GsmCall.Status.ACTIVE;


public class CallActivity extends AppCompatActivity {
    private String LOG_TAG = "CallActivity";
    private Disposable updatesDisposable = Disposables.empty();
    private Disposable timerDisposable = Disposables.empty();
    private ImageView buttonHangup, buttonAnswer;
    private TextView textDisplayName, textStatus, textDuration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        hideBottomNavigationBar();
        initView();
    }

    private void initView() {
        textDisplayName = findViewById(R.id.textDisplayName);
        textStatus = findViewById(R.id.textStatus);
        textDuration = findViewById(R.id.textDuration);
        buttonAnswer = findViewById(R.id.buttonAnswer);
        buttonHangup = findViewById(R.id.buttonHangup);
        buttonHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallManager.cancelCall();
            }
        });
        buttonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallManager.acceptCall();
            }
        });

    }

    private void hideBottomNavigationBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatesDisposable =
                CallManager.updates()
                        .doOnEach(new Consumer<Notification<GsmCall>>() {
                            @Override
                            public void accept(Notification<GsmCall> gsmCallNotification) throws Exception {
                                Log.i(LOG_TAG, "updated call: " + gsmCallNotification);
                            }
                        }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(LOG_TAG, "Error processing call", throwable);
                    }
                }).subscribe(new Consumer<GsmCall>() {
                    @Override
                    public void accept(GsmCall gsmCall) throws Exception {
                        updateView(gsmCall);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        updatesDisposable.dispose();
    }

    private void updateView(GsmCall gsmCall) {
        if (gsmCall.getStatus() == ACTIVE) {
            textStatus.setVisibility(View.GONE);
            textDuration.setVisibility(View.VISIBLE);
        } else {
            textDuration.setVisibility(View.GONE);
            textStatus.setVisibility(View.VISIBLE);
        }
        switch (gsmCall.getStatus()) {
            case CONNECTING:
                textStatus.setText("Connecting…");
                break;
            case DIALING:
                textStatus.setText("Calling…");
                break;
            case RINGING:
                textStatus.setText("Incoming call");
                break;
            case ACTIVE:
            case UNKNOWN:
                textStatus.setText("");
                break;
            case DISCONNECTED:
                textStatus.setText("Finished call");
                break;
        }

        if (gsmCall.getStatus() == GsmCall.Status.DISCONNECTED) {
            buttonHangup.setVisibility(View.GONE);
            buttonHangup.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3000);
        } else {
            buttonHangup.setVisibility(View.VISIBLE);
        }
        switch (gsmCall.getStatus()) {
            case ACTIVE:
                startTimer();
                break;
            case DISCONNECTED:
                stopTimer();
                break;
        }
        if (!gsmCall.getDisplayName().equals("")) {
            textDisplayName.setText(gsmCall.getDisplayName());
        } else {
            textDisplayName.setText("Unknow");
        }
        if (gsmCall.getStatus() == GsmCall.Status.RINGING) {
            buttonAnswer.setVisibility(View.VISIBLE);
        } else {
            buttonAnswer.setVisibility(View.GONE);
        }
    }

    private void startTimer() {
        timerDisposable =
                Observable.interval(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                textDuration.setText(toDurationString(aLong));
                            }
                        });
    }

    private void stopTimer() {
        timerDisposable.dispose();
    }

    @SuppressLint("DefaultLocale")
    private String toDurationString(Long time) {
        return String.format("%02d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60));
    }
}
