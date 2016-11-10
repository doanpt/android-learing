package com.dvt.samsung.finalapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
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
    private SharedPreferences sharedPreferences;
    public int loopMusic;
    public boolean shuffle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        sharedPreferences = getSharedPreferences(CommonValue.KEY_SAVE_MODE, Context.MODE_PRIVATE);
        loopMusic = sharedPreferences.getInt(CommonValue.KEY_LOOP_MUSIC, 1);
        shuffle = sharedPreferences.getBoolean(CommonValue.KEY_SHUFFLE, true);
        Intent intent = getIntent();
//        song = intent.getParcelableExtra(CommonValue.KEY_SEND_A_SONG);
        sizeOfSong = intent.getStringExtra(CommonValue.KEY_SEND_SIZE_OF_SONG);
        if (isMyServiceRunning(MyMusicService.class)) {
            bindService(new Intent(this, MyMusicService.class), serviceConnection, BIND_AUTO_CREATE);
        }
        initView();
        setViewLoopAndShuffle(loopMusic, shuffle);
        PlaySongActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showUI();
                handler.postDelayed(this, 200);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void setViewLoopAndShuffle(int loopMusic, boolean shuffle) {
        switch (loopMusic) {
            case 1:
                btnNoLoop.setVisibility(View.VISIBLE);
                btnLoopOne.setVisibility(View.INVISIBLE);
                btnLoopAll.setVisibility(View.INVISIBLE);
                break;
            case 2:
                btnNoLoop.setVisibility(View.INVISIBLE);
                btnLoopOne.setVisibility(View.VISIBLE);
                btnLoopAll.setVisibility(View.INVISIBLE);
                break;
            case 3:
                btnNoLoop.setVisibility(View.INVISIBLE);
                btnLoopOne.setVisibility(View.INVISIBLE);
                btnLoopAll.setVisibility(View.VISIBLE);
                break;
        }
        if (shuffle) {
            btnShuffle.setVisibility(View.VISIBLE);
            btnConiuous.setVisibility(View.INVISIBLE);
        } else {
            btnShuffle.setVisibility(View.INVISIBLE);
            btnConiuous.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            unbindService(serviceConnection);
        } catch (Exception e) {
            Log.e("ERROR BIND SERVICE", "SERVICE NOT BIND");
        }
        super.onDestroy();
    }

    private void showUI() {
        if (myMusicService != null) {
            song = myMusicService.getMediaController().getListSong().get(myMusicService.getMediaController().getIndexSong());
            tvNameAction.setText(song.getName());
            tvSongName.setText(song.getName());
            tvSongArtist.setText(song.getArtist());
            tvNumberSong.setText((myMusicService.getMediaController().getIndexSong() + 1) + "/" + myMusicService.getMediaController().getListSong().size());
            long duration = myMusicService.getMediaController().getmPlayer().getDuration();
            long current = myMusicService.getMediaController().getmPlayer().getCurrentPosition();
            String time = milliSecondsToTimer(current) + "/" + milliSecondsToTimer(duration);
            seekBar.setProgress((int) current);
            seekBar.setMax((int) duration);
            tvTimeSong.setText(time);
            tvSongName.setText(song.getName());
            tvSongArtist.setText(song.getArtist());
            tvNameAction.setText(song.getName());
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
            myMusicService.getMediaController().setShuffle(shuffle);
            myMusicService.getMediaController().setLoop(loopMusic);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myMusicService = null;
            myMusicService.setPlaying(false);
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
        tvNumberSong.setText(sizeOfSong);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.iv_continuous:
                shuffle = false;
                myMusicService.getMediaController().setShuffle(shuffle);
                sharedPreferences.edit().putBoolean(CommonValue.KEY_SHUFFLE, shuffle).commit();
                btnConiuous.setVisibility(View.VISIBLE);
                btnShuffle.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_no_continuous:
                shuffle = true;
                myMusicService.getMediaController().setShuffle(shuffle);
                sharedPreferences.edit().putBoolean(CommonValue.KEY_SHUFFLE, shuffle).commit();
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
                loopMusic = 1;
                myMusicService.getMediaController().setLoop(loopMusic);
                sharedPreferences.edit().putInt(CommonValue.KEY_LOOP_MUSIC, loopMusic).commit();
                btnLoopAll.setVisibility(View.INVISIBLE);
                btnLoopOne.setVisibility(View.INVISIBLE);
                btnNoLoop.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_loop_one:
                loopMusic = 3;
                myMusicService.getMediaController().setLoop(loopMusic);
                sharedPreferences.edit().putInt(CommonValue.KEY_LOOP_MUSIC, loopMusic).commit();
                btnLoopAll.setVisibility(View.VISIBLE);
                btnLoopOne.setVisibility(View.INVISIBLE);
                btnNoLoop.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_no_loop:
                loopMusic = 2;
                sharedPreferences.edit().putInt(CommonValue.KEY_LOOP_MUSIC, loopMusic).commit();
                myMusicService.getMediaController().setLoop(loopMusic);
                btnLoopAll.setVisibility(View.INVISIBLE);
                btnLoopOne.setVisibility(View.VISIBLE);
                btnNoLoop.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
