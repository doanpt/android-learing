package com.dvt.samsung.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.database.Cursor;
import android.provider.MediaStore;

import com.dvt.samsung.model.Song;

/**
 * Created by sev_user on 11/3/2016.
 */

public class MediaController {
    private static final String TAG = MediaController.class.getSimpleName();
    private List<Song> listSong = new ArrayList<>();
    private Context mContext;
    private MediaPlayer mPlayer;
    private int indexSong;
    private OnPlayStartedListener mListener;
    public static int STATE_IDLE = -1;
    public static int STATE_PLAYING = 1;
    public static int STATE_PAUSE = 2;
    public static int STATE_STOP = 3;
    public static int STATE_SEEKING = 4;
    private int mediaState = STATE_IDLE;

    public MediaController(Context mContext) {
        this.mContext = mContext;
        getAllSong();
        mPlayer = new MediaPlayer();
        mediaState = STATE_IDLE;
        indexSong = 0;
    }

    public MediaController(Context mContext, List<Song> arrSong) {
        this.mContext = mContext;
        this.listSong = arrSong;
        mPlayer = new MediaPlayer();
        mediaState = STATE_IDLE;
        indexSong = 0;
    }

    public void setIndexSong(int indexSong) {
        this.indexSong = indexSong;
    }

    private void getAllSong() {
        if (listSong.size() > 0) {
            return;
        }
        String projection[] = new String[]{
                //Name
                MediaStore.MediaColumns.TITLE,
                //FileName
                MediaStore.MediaColumns.DISPLAY_NAME,
                //Path
                MediaStore.MediaColumns.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION
        };
        Cursor cursor = mContext.getContentResolver()
                .query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                );
        if (cursor == null) {
            return;
        }
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex(
                    MediaStore.MediaColumns.TITLE));
            String fileName = cursor.getString(cursor.getColumnIndex(
                    MediaStore.MediaColumns.DISPLAY_NAME));
            String path = cursor.getString(cursor.getColumnIndex(
                    MediaStore.MediaColumns.DATA));
            String artist = cursor.getString(cursor.getColumnIndex(
                    MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(
                    MediaStore.Audio.Media.ALBUM));
            int duration = cursor.getInt(cursor.getColumnIndex(
                    MediaStore.Audio.Media.DURATION));
            listSong.add(new Song(name, fileName, path, artist, album, duration));
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void playOrPause(boolean isPlayAgain) {
        if (mediaState == STATE_IDLE || mediaState == STATE_STOP || isPlayAgain) {
            try {
                mPlayer.reset();
                Song song = listSong.get(indexSong);
                mPlayer.setDataSource(song.getPath());
                mPlayer.prepare();
                mPlayer.start();
                mListener.onMediaStarted(song.getName(), song.getTime(), mPlayer.getDuration());
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
        if (indexSong < listSong.size() - 2) {
            indexSong++;
        } else {
            indexSong = 0;
        }
        playOrPause(true);
    }

    public void previous() {
        if (indexSong > 0) {
            indexSong--;
        } else {
            indexSong = listSong.size() - 1;
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

    public void setOnPlayStartedListener(OnPlayStartedListener event) {
        mListener = event;
    }

    public interface OnPlayStartedListener {
        void onMediaStarted(String songName, String totalTime, int totalTimeInt);
    }
}
