package com.dvt.samsung.finalapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class PlaySongActivity extends Activity implements View.OnClickListener {
    private ImageView btnPlay;
    private ImageView btnPause;
    private ImageView btnShuffle;
    private ImageView btnPrevious;
    private ImageView btnNext;
    private ImageView btnLoopAll;
    private ImageView btnLoopOne;
    private ImageView btnNoLoop;
    private ImageView btnConiuous;
    private SeekBar seekBar;
    private ImageView ivShare;
    private ImageView ivFavorite;
    private ImageView ivOther;
    private ImageView ivBackToList;
    private TextView tvNameAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        initView();
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
        seekBar = (SeekBar) findViewById(R.id.sb_time_music);
        ivShare = (ImageView) findViewById(R.id.im_share);
        ivFavorite = (ImageView) findViewById(R.id.im_favorite);
        ivOther = (ImageView) findViewById(R.id.im_other);
        btnShuffle.setOnClickListener(this);
        btnConiuous.setOnClickListener(this);
        btnLoopAll.setOnClickListener(this);
        btnLoopOne.setOnClickListener(this);
        btnNoLoop.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_continuous:
                btnConiuous.setVisibility(View.VISIBLE);
                btnShuffle.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_no_continuous:
                btnConiuous.setVisibility(View.INVISIBLE);
                btnShuffle.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_play:
                btnPlay.setVisibility(View.INVISIBLE);
                btnPause.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_pause:
                btnPause.setVisibility(View.INVISIBLE);
                btnPlay.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_previous:
                break;
            case R.id.iv_next:
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
