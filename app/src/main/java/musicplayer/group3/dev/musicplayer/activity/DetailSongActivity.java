package musicplayer.group3.dev.musicplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.common.Const;
import musicplayer.group3.dev.musicplayer.common.SharePreferencesController;
import musicplayer.group3.dev.musicplayer.item.ItemSong;
import musicplayer.group3.dev.musicplayer.media.MediaManager;

public class DetailSongActivity extends Activity implements View.OnClickListener {
    private MediaManager mediaManager;
    private TextView tvSongName, tvArtistName, tvCurrentTime, tvTotalTime, tvIndexOfSong;
    private ImageView btnPlayPause, btnPrevious, btnNext, btnLoop, btnShuffle,btnShare, btnBack;
    private SeekBar sbTime;
    private Handler handler = new Handler();
    //    private MediaService myMusicService;
    private ItemSong song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_song);
        mediaManager = MediaManager.getInstance(this);
        initView();
        DetailSongActivity.this.runOnUiThread(runnable);
    }

    private void initView() {
        tvArtistName = (TextView) findViewById(R.id.tv_artists_name);
        tvSongName = (TextView) findViewById(R.id.tv_song_name);
        tvCurrentTime = (TextView) findViewById(R.id.tv_time_progress);
        tvIndexOfSong = (TextView) findViewById(R.id.tv_index_of_song);
        tvTotalTime = (TextView) findViewById(R.id.tv_time_total);
        btnLoop = (ImageView) findViewById(R.id.img_loop);
        btnNext = (ImageView) findViewById(R.id.img_next);
        btnPlayPause = (ImageView) findViewById(R.id.img_play_pause);
        btnPrevious = (ImageView) findViewById(R.id.img_previous);
        btnShare= (ImageView) findViewById(R.id.img_share);
        btnShuffle = (ImageView) findViewById(R.id.img_shuffle);
        btnBack= (ImageView) findViewById(R.id.img_back);
        sbTime = (SeekBar) findViewById(R.id.sb_detail_time);
        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    mediaManager.getmPlayer().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnLoop.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnShuffle.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            showUI();
            handler.postDelayed(this, 200);
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.img_share:
                ItemSong song=mediaManager.getArrItemSong().get(mediaManager.getCurrentIndex());
                File audio = new File(song.getDataPath());
                intent = new Intent(Intent.ACTION_SEND).setType("audio/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(audio));
                startActivity(Intent.createChooser(intent, "Share to"));
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.img_loop:
                changeStateLoop();
                break;
            case R.id.img_next:
                intent = new Intent(Const.ACTION_NEXT);
                sendBroadcast(intent);
                break;
            case R.id.img_previous:
                intent = new Intent(Const.ACTION_PREVIOUS);
                sendBroadcast(intent);
                break;
            case R.id.img_play_pause:
                Intent intentPause = new Intent(Const.ACTION_PAUSE_SONG);
                sendBroadcast(intentPause);
                break;
            case R.id.img_shuffle:
                changeStateShuffle();
                break;
        }
    }

    private void changeStateShuffle() {
        SharePreferencesController sharePreference = SharePreferencesController.getInstance(this);
        boolean shuffle = sharePreference.getBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_TRUE);
        if (shuffle) {
            sharePreference.putBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_FLASE);
            btnShuffle.setImageResource(R.drawable.ic_no_shuffle);
        } else {
            sharePreference.putBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_TRUE);
            //TODO
            //FIXME
            //Thay ảnh
            btnShuffle.setImageResource(R.drawable.shuffle);
        }
    }

    private void changeStateLoop() {
        SharePreferencesController sharePreference = SharePreferencesController.getInstance(this);
        int loop = sharePreference.getInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_NO_LOOP);
        switch (loop) {
            case Const.MEDIA_STATE_LOOP_ONE:
                sharePreference.putInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_LOOP_ALL);
                btnLoop.setImageResource(R.drawable.ic_repeat);
                break;
            case Const.MEDIA_STATE_LOOP_ALL:
                sharePreference.putInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_NO_LOOP);
                btnLoop.setImageResource(R.drawable.ic_repeat_no);
                break;
            case Const.MEDIA_STATE_NO_LOOP:
                btnLoop.setImageResource(R.drawable.ic_repeat_one);
                sharePreference.putInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_LOOP_ONE);
                break;
        }
    }

    private void updateButtonPlayPause() {
        if (mediaManager.getmPlayer().isPlaying()) {
            btnPlayPause.setImageResource(R.drawable.ic_pause);
        } else {
            btnPlayPause.setImageResource(R.drawable.ic_play);
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

    private void showUI() {
        song = mediaManager.getArrItemSong().get(mediaManager.getCurrentIndex());
        tvSongName.setText(song.getDisplayName());
        tvArtistName.setText(song.getArtist());
        int current = mediaManager.getmPlayer().getCurrentPosition();
        int totalTime = mediaManager.getmPlayer().getDuration();
        tvIndexOfSong.setText(mediaManager.getCurrentIndex() + "/" + mediaManager.getArrItemSong().size());
        tvCurrentTime.setText(milliSecondsToTimer(current));
        tvTotalTime.setText(milliSecondsToTimer(totalTime));
        sbTime.setProgress(current);
        sbTime.setMax(totalTime);
        updateButtonPlayPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
