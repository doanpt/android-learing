package com.dvt.samsung.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.database.Cursor;
import android.provider.MediaStore;

import com.dvt.samsung.model.Song;
import com.dvt.samsung.utils.CommonValue;

/**
 * Created by sev_user on 11/3/2016.
 */

public class MediaController {
    private static final String TAG = MediaController.class.getSimpleName();
    private List<Song> listSong = new ArrayList<>();
    private Context mContext;
    private MediaPlayer mPlayer;
    private int indexSong;
    //    private OnPlayStartedListener mListener;
    public static int STATE_IDLE = -1;
    public static int STATE_PLAYING = 1;
    public static int STATE_PAUSE = 2;
    public static int STATE_STOP = 3;
    public static int STATE_SEEKING = 4;
    private int mediaState = STATE_IDLE;
    private int loop=1;
    private boolean shuffle=true;
    private Random random = new Random();

    public MediaController(Context mContext) {
        this.mContext = mContext;
        mPlayer = new MediaPlayer();
        mediaState = STATE_IDLE;
//        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                next();
//            }
//        });
        indexSong = 0;
    }

    public void setIndexSong(int indexSong) {
        this.indexSong = indexSong;
    }

    public int getIndexSong() {
        return indexSong;
    }

    public MediaPlayer getmPlayer() {
        return mPlayer;
    }

    public void setmPlayer(MediaPlayer mPlayer) {
        this.mPlayer = mPlayer;
    }

    public void playOrPause(boolean isPlayAgain) {
        if (mediaState == STATE_IDLE || mediaState == STATE_STOP || isPlayAgain) {
            try {
                mPlayer.reset();
                Song song = listSong.get(indexSong);
                mPlayer.setDataSource(song.getPath());
                mPlayer.prepare();
                mPlayer.start();
                Intent intent = new Intent(CommonValue.ACTION_SEND_DATA);
                intent.putExtra(CommonValue.KEY_TITLE_SONG, song.getName());
                mContext.sendBroadcast(intent);
                mediaState = STATE_PLAYING;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mediaState == STATE_PAUSE) {
            mPlayer.start();
            mediaState = STATE_PLAYING;
        } else if (mediaState == STATE_PLAYING) {
            mPlayer.pause();
            mediaState = STATE_PAUSE;
        }
    }

    public void setListSong(List<Song> listSong) {
        this.listSong = listSong;
    }

    public void next() {
        if (shuffle) {
            indexSong = random.nextInt(listSong.size());
        } else {
            if (indexSong < listSong.size() - 2) {
                indexSong++;
            } else {
                indexSong = 0;
            }
        }
        playOrPause(true);
    }

    public void previous() {
        if (shuffle == true) {
            indexSong = random.nextInt(listSong.size());
        } else {
            if (indexSong > 0) {
                indexSong--;
            } else {
                indexSong = listSong.size() - 1;
            }
        }
        playOrPause(true);
    }

    public void stop() {
        if (mediaState == STATE_IDLE) {
            return;
        }
        mPlayer.stop();
        mediaState = STATE_STOP;
    }

    public List<Song> getListSong() {
        return listSong;
    }

    public boolean isPlaying() {
        return mediaState == STATE_PLAYING;
    }

    public void seekTo(int progress) {
        mPlayer.seekTo(progress);
    }

    public int getCurrentTime() {
        if (mediaState == STATE_PLAYING) {
            return mPlayer.getCurrentPosition();
        }
        return -1;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }
}
