package musicplayer.group3.dev.musicplayer.minigame.ac.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.common.Const;
import musicplayer.group3.dev.musicplayer.item.ItemSong;
import musicplayer.group3.dev.musicplayer.media.MediaManager;

public class MiniGameSongAct extends Activity implements Runnable, OnClickListener {
    private static final int UPDATE_TIME_PROGESS = 0;
    private static final int FLAG_TURN_OFF_SONG = 0;
    private static final int FLAG_PLAY_SONG = 1;
    protected static final String TAG = "MiniGameSongAct";
    private TextView tvScore, tvTimeProgress;
    private Button btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD;
    private ImageView imvPlayGame;
    private LinearLayout llAnswer;

    private ArrayList<ItemSong> arrAllSong = new ArrayList<ItemSong>();
    private ArrayList<Integer> arrIndex = new ArrayList<Integer>();
    private ArrayList<Integer> arrIndexAnswer = new ArrayList<Integer>();

    private Thread thread;
    private Random rd;
    private MediaPlayer mPlayer;

    private boolean isRunning;
    private boolean isPause = false;
    private boolean isUpdated;

    private boolean flagStartGame, flagGameOver;
    private int timeProgress;
    private int score = 0;
    private String correctAnswer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mini_game);
        initView();
        queryAudioSongs();
        initPlayer();
        initIndexArr();
    }

    private void queryAudioSongs() {
        if (arrAllSong.size() > 0) {
            return;
        } else {
            arrAllSong = MediaManager.getInstance(this).getSongList(null, null);
        }
    }

    private void initIndexArr() {
        for (int index = 0; index < arrAllSong.size(); index++) {
            arrIndex.add(new Integer(index));
        }
        for (int index = 0; index < 4; index++) {
            arrIndexAnswer.add(new Integer(index));
        }
    }

    private void initView() {
        isRunning = true;
        flagStartGame = true;
        flagGameOver = false;
        timeProgress = 3;
        rd = new Random();

        llAnswer = (LinearLayout) findViewById(R.id.ll_answer);
        llAnswer.setVisibility(View.GONE);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvTimeProgress = (TextView) findViewById(R.id.tv_time_progress);
        tvTimeProgress.setVisibility(View.GONE);

        imvPlayGame = (ImageView) findViewById(R.id.imv_start_mini_game);
        btnAnswerA = (Button) findViewById(R.id.btn_answer_a);
        btnAnswerB = (Button) findViewById(R.id.btn_answer_b);
        btnAnswerC = (Button) findViewById(R.id.btn_answer_c);
        btnAnswerD = (Button) findViewById(R.id.btn_answer_d);

        btnAnswerA.setOnClickListener(this);
        btnAnswerB.setOnClickListener(this);
        btnAnswerC.setOnClickListener(this);
        btnAnswerD.setOnClickListener(this);
        imvPlayGame.setOnClickListener(this);


    }

    public void initPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(new OnPreparedListener() {

            //Chuan bi xong
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "onPrepared....");
            }
        });
        mPlayer.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //Khi setDataSource k co
                Log.e(TAG, "error....");
                return false;
            }
        });
        mPlayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                //Sau khi xong 1 bai hat
            }
        });
    }

//    private void queryAudioSongs() {
//        if (arrAllSong.size() > 0) {
//            return;
//        }
//        String cols[] = new String[]{MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.DISPLAY_NAME,
//                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM,
//                MediaStore.Audio.Media.ALBUM_ID,
//                MediaStore.Audio.Media.ARTIST};
//
//        Cursor cursor = getContentResolver().query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cols, null, null,
//                null, null);
//        if (cursor == null)
//            return;
//
//        int indexData = cursor.getColumnIndex(cols[0]);
//        int indexTitle = cursor.getColumnIndex(cols[1]);
//        int indexDisplayName = cursor.getColumnIndex(cols[2]);
//        int indexDuration = cursor.getColumnIndex(cols[3]);
//        int indexAlbum = cursor.getColumnIndex(cols[4]);
//        int indexAlbumID = cursor.getColumnIndex(cols[5]);
//        int indexArtist = cursor.getColumnIndex(cols[6]);
//
//        String dataPath, title, displayName, album, albumID, artist;
//        int duration;
//        cursor.moveToFirst();
//
//        arrAllSong.clear();
//        while (!cursor.isAfterLast()) {
//            dataPath = cursor.getString(indexData);
//            title = cursor.getString(indexTitle);
//            displayName = cursor.getString(indexDisplayName);
//            album = cursor.getString(indexAlbum);
//            albumID = cursor.getString(indexAlbumID);
//            artist = cursor.getString(indexArtist);
//            duration = cursor.getInt(indexDuration);
//            arrAllSong.add(new ItemSong(dataPath, title, displayName, album, albumID,
//                    artist, duration));
//            cursor.moveToNext();
//        }
//        cursor.close();
//    }

    public void play(int position) {
        mPlayer.reset();
        try {
            mPlayer.setDataSource(this, Uri.parse(arrAllSong.get(position).getDataPath()));
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        mPlayer.stop();
        mPlayer.reset();
    }

    public void seekTo(int msec) {
        mPlayer.seekTo(msec);
        //mPlayer.start();
    }

    private void startThread() {
        thread = new Thread(this);
        thread.start();
    }

    private void nextSong() {
        if (arrIndex.size() == 0) {
            initIndexArr();
        }
        if (arrAllSong.size() > 4) {
            Collections.shuffle(arrIndex);
            Collections.shuffle(arrIndexAnswer);
            Collections.shuffle(arrAllSong);
            int position = arrIndex.get(0);
            int duration = arrAllSong.get(position).getDuration();
            while (((duration / 1000) < 50)) {
                position++;
                duration = arrAllSong.get(position).getDuration();
            }
            String answerA = arrAllSong.get(position).getTitle();
            String answerB = arrAllSong.get(arrIndexAnswer.get(1)).getTitle();
            String answerC = arrAllSong.get(arrIndexAnswer.get(2)).getTitle();
            String answerD = arrAllSong.get(arrIndexAnswer.get(3)).getTitle();
            //Cat chuoi de khi hien thi se khong bi tran` ky tu
            if (answerA.length() >= 24) {
                answerA = answerA.substring(0, 23);
            }
            if (answerB.length() >= 24) {
                answerB = answerB.substring(0, 23);
            }
            if (answerC.length() >= 24) {
                answerC = answerC.substring(0, 23);
            }
            if (answerD.length() >= 24) {
                answerD = answerD.substring(0, 23);
            }
            String answer[] = new String[]{answerA, answerB, answerC, answerD};
            btnAnswerA.setText(answer[arrIndexAnswer.get(0)]);
            btnAnswerB.setText(answer[arrIndexAnswer.get(1)]);
            btnAnswerC.setText(answer[arrIndexAnswer.get(2)]);
            btnAnswerD.setText(answer[arrIndexAnswer.get(3)]);
            playSong(position);
            this.correctAnswer = answerA;
        } else {
        }
    }

    private void playSong(int position) {

        int duration = arrAllSong.get(position).getDuration() - 30000;
        play(position);
        seekTo(rd.nextInt(duration) + 10000);
        arrIndex.remove(0);
    }

    private void stopSong() {
        mPlayer.reset();
        mPlayer.stop();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            final int what = msg.what;
            int flagPlaySong = msg.arg2;
            int progress = msg.arg1;
            switch (what) {
                case UPDATE_TIME_PROGESS:
                    tvTimeProgress.setText(progress + "");
                    if (flagPlaySong == FLAG_PLAY_SONG) {
                        llAnswer.setVisibility(View.VISIBLE);
                        if (arrAllSong.size() > 4) {
                            nextSong();
                        } else {
                            //SnackBar
                        }

                    }
                    break;
            }
        }
    };

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(1000);
                timeProgress--;
                if (timeProgress <= 0) {
                    if (flagStartGame) {
                        timeProgress = 10;
                        flagGameOver = false;
                        flagStartGame = false;
                        Message message = new Message();
                        message.arg1 = timeProgress;
                        message.arg2 = FLAG_PLAY_SONG;
                        message.what = UPDATE_TIME_PROGESS;
                        message.setTarget(handler);
                        message.sendToTarget();
                    } else {
                        isRunning = false;
                        flagGameOver = true;
                        timeProgress = 10;
                        break;
                    }
                } else {
                    Message message = new Message();
                    message.arg1 = timeProgress;
                    message.arg2 = FLAG_TURN_OFF_SONG;
                    message.what = UPDATE_TIME_PROGESS;
                    message.setTarget(handler);
                    message.sendToTarget();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (isPause()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        if (flagGameOver) {
            stop();
            int hightScore = readHightScore();
            if (hightScore < score) {
                showGameOver(true);
            } else {
                showGameOver(false);
            }
        }
    }

    public int readHightScore() {
        SharedPreferences pre = getSharedPreferences(Const.FILE_SAVE_HIGHT_SCORE, MODE_PRIVATE);
        return pre.getInt(Const.KEY_HIGHT_SCORE, -1);

    }

    public void writeHightScore(int hightScore) {
        SharedPreferences pref = getSharedPreferences(Const.FILE_SAVE_HIGHT_SCORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Const.KEY_HIGHT_SCORE, hightScore);
        editor.apply();
    }

    public void showGameOver(boolean isStatus) {
        stopSong();
        Intent intentGameOver = new Intent();
        intentGameOver.setClass(MiniGameSongAct.this, HightScoreActivity.class);
        intentGameOver.putExtra(Const.KEY_SCORE, tvScore.getText().toString());
        intentGameOver.putExtra(Const.KEY_STATUS, isStatus);
        startActivityForResult(intentGameOver, Const.KEY_OVER);
        if (isStatus) {
            writeHightScore(score);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.KEY_OVER:
                if (resultCode == RESULT_OK) {
                    isRunning = true;
                    timeProgress = 10;
                    score = 0;
                    tvScore.setText(score + Const.BLANK);
                    startThread();
                    if (arrAllSong.size() > 4) {
                        nextSong();
                    } else {
                        //SnackBar
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    finish();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        String userAnswer = "";
        switch (v.getId()) {
            case R.id.imv_start_mini_game:
                imvPlayGame.setVisibility(View.GONE);
                tvTimeProgress.setVisibility(View.VISIBLE);
                startThread();
                break;
            case R.id.btn_answer_a:
                userAnswer = btnAnswerA.getText().toString();
                handlerQuestion(userAnswer);
                break;
            case R.id.btn_answer_b:
                userAnswer = btnAnswerB.getText().toString();
                handlerQuestion(userAnswer);
                break;
            case R.id.btn_answer_c:
                userAnswer = btnAnswerC.getText().toString();
                handlerQuestion(userAnswer);
                break;
            case R.id.btn_answer_d:
                userAnswer = btnAnswerD.getText().toString();
                handlerQuestion(userAnswer);
                break;
            default:
                break;
        }

    }

    private void handlerQuestion(String userAnswer) {
        if (userAnswer.equals(this.correctAnswer)) {
            score++;
            tvScore.setText(score + Const.BLANK);
            timeProgress = 11;
            if (arrAllSong.size() > 4) {
                nextSong();
            } else {
                //SnackBar
            }
            ;
        } else {
            isRunning = false;
            flagGameOver = true;
            timeProgress = 11;
        }
    }

    public void pause() {
        isPause = true;
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayer.start();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        stop();
    }

    public boolean isPause() {
        return isPause;
    }
}
