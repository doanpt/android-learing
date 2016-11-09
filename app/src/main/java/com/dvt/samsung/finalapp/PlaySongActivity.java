package com.dvt.samsung.finalapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dvt.samsung.model.Song;
import com.dvt.samsung.service.MyMusicService;
import com.dvt.samsung.utils.CommonValue;

import java.text.SimpleDateFormat;


public class PlaySongActivity extends Activity implements View.OnClickListener {
    private ImageView btnPlay, btnPause, btnShuffle, btnPrevious, btnNext, btnLoopAll, btnLoopOne, btnNoLoop, btnConiuous;
    private SeekBar seekBar;
    private ImageView ivShare, ivFavorite, ivOther, ivBackToList, ivImageSong;
    private TextView tvNameAction, tvSongName, tvSongArtist, tvNumberSong, tvTimeSong;
    private MyMusicService myMusicService;
    private Handler handler = new Handler();
    private Song song;
    private String sizeOfSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        Intent intent = getIntent();
        song = intent.getParcelableExtra(CommonValue.KEY_SEND_A_SONG);
        sizeOfSong = intent.getStringExtra(CommonValue.KEY_SEND_SIZE_OF_SONG);
        if (isMyServiceRunning(MyMusicService.class)) {
            bindService(new Intent(this, MyMusicService.class), serviceConnection, BIND_AUTO_CREATE);
        }
        initView();
        PlaySongActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showUI();
                handler.postDelayed(this, 200);
            }
        });
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private void showUI() {
        if (myMusicService != null) {
            song = myMusicService.getMediaController().getListSong().get(myMusicService.getMediaController().getIndexSong());
            tvNameAction.setText(song.getName());
            tvSongName.setText(song.getName());
            tvSongArtist.setText(song.getArtist());
            tvNumberSong.setText(myMusicService.getMediaController().getIndexSong() + "/" + myMusicService.getMediaController().getListSong().size());
            long duration = myMusicService.getMediaController().getmPlayer().getDuration();
            long current = myMusicService.getMediaController().getmPlayer().getCurrentPosition();
            String time = milliSecondsToTimer(current) + "/" + milliSecondsToTimer(duration);
            seekBar.setProgress((int) current);
            seekBar.setMax((int) duration);
            tvTimeSong.setText(time);
//            ivImageSong.setImageBitmap(BitmapFactory.decodeByteArray(song.getaByte(), 0, song.getaByte().length));
        }
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";
        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }
        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        finalTimerString = finalTimerString + minutes + ":" + secondsString;
        // return timer string
        return finalTimerString;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyMusicService.MyBinder binder = (MyMusicService.MyBinder) service;
            myMusicService = binder.getMyService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myMusicService = null;
        }
    };

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void initView() {
        btnShuffle = (ImageView) findViewById(R.id.iv_continuous);
        btnConiuous = (ImageView) findViewById(R.id.iv_no_continuous);
        btnPlay = (ImageView) findViewById(R.id.iv_play);
        btnPause = (ImageView) findViewById(R.id.iv_pause);
        btnPrevious = (ImageView) findViewById(R.id.iv_previous);
        btnNext = (ImageView) findViewById(R.id.iv_next);
        btnLoopAll = (ImageView) findViewById(R.id.iv_loop_all);
        btnLoopOne = (ImageView) findViewById(R.id.iv_loop_one);
        btnNoLoop = (ImageView) findViewById(R.id.iv_no_loop);
        ivBackToList = (ImageView) findViewById(R.id.im_back);
        tvNameAction = (TextView) findViewById(R.id.tv_name_action);
        ivImageSong = (ImageView) findViewById(R.id.iv_song_image);
        seekBar = (SeekBar) findViewById(R.id.sb_time_music);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (myMusicService != null && fromUser == true) {
                    myMusicService.getMediaController().getmPlayer().seekTo(progress);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ivShare = (ImageView) findViewById(R.id.im_share);
        ivFavorite = (ImageView) findViewById(R.id.im_favorite);
        ivOther = (ImageView) findViewById(R.id.im_other);
        tvNumberSong = (TextView) findViewById(R.id.tv_number_song);
        tvSongArtist = (TextView) findViewById(R.id.tv_artist);
        tvSongName = (TextView) findViewById(R.id.tv_song_name);
        tvTimeSong = (TextView) findViewById(R.id.tv_time);
        btnShuffle.setOnClickListener(this);
        btnConiuous.setOnClickListener(this);
        btnLoopAll.setOnClickListener(this);
        btnLoopOne.setOnClickListener(this);
        btnNoLoop.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        ivBackToList.setOnClickListener(this);
        tvSongName.setText(song.getName());
        tvSongArtist.setText(song.getArtist());
        tvNumberSong.setText(sizeOfSong);
        tvNameAction.setText(song.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.iv_continuous:
                btnConiuous.setVisibility(View.VISIBLE);
                btnShuffle.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_no_continuous:
                btnConiuous.setVisibility(View.INVISIBLE);
                btnShuffle.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_play:
                Intent intentPlay = new Intent(CommonValue.ACTION_PAUSE_SONG);
                sendBroadcast(intentPlay);
                btnPlay.setVisibility(View.INVISIBLE);
                btnPause.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_pause:
                Intent intentPause = new Intent(CommonValue.ACTION_PAUSE_SONG);
                sendBroadcast(intentPause);
                btnPause.setVisibility(View.INVISIBLE);
                btnPlay.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_previous:
                Intent intentPrevious = new Intent(CommonValue.ACTION_PREVIOUS);
                sendBroadcast(intentPrevious);
                break;
            case R.id.iv_next:
                Intent intentNext = new Intent(CommonValue.ACTION_NEXT);
                sendBroadcast(intentNext);
                break;
            case R.id.iv_loop_all:
                btnLoopAll.setVisibility(View.INVISIBLE);
                btnLoopOne.setVisibility(View.INVISIBLE);
                btnNoLoop.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_loop_one:
                btnLoopAll.setVisibility(View.VISIBLE);
                btnLoopOne.setVisibility(View.INVISIBLE);
                btnNoLoop.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_no_loop:
                btnLoopAll.setVisibility(View.INVISIBLE);
                btnLoopOne.setVisibility(View.VISIBLE);
                btnNoLoop.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
