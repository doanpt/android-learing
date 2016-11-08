package com.dvt.samsung.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dvt.samsung.media.MediaController;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.utils.CommonMethod;

import java.util.ArrayList;

/**
 * Created by sev_user on 11/8/2016.
 */

public class MyMusicService extends Service {
    private Context context;
    private IBinder iBinder = new MyMusicService.MyBinder();
    private MediaController mediaController;
    private ArrayList<Song> arrSong;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class MyBinder extends Binder {
        public MyMusicService getMyService() {
            return MyMusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mediaController = new MediaController(context);
        arrSong = new ArrayList<>();
    }

    public void setArrSong(ArrayList<Song> arr) {
        arrSong = arr;
    }

}
