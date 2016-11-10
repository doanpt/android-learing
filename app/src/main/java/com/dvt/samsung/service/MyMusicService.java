package com.dvt.samsung.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.dvt.samsung.finalapp.PlaySongActivity;
import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.media.MediaController;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.utils.CommonValue;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sev_user on 11/8/2016.
 */

public class MyMusicService extends Service {
    private Context context;
    private IBinder iBinder = new MyMusicService.MyBinder();
    private MediaController mediaController;
    private ArrayList<Song> arrSong;
    private BroadCastMusic broadCastMusic = new BroadCastMusic();
    private IntentFilter intentFilter = new IntentFilter();
    private RemoteViews remoteViews;
    private NotificationCompat.Builder mBuilder;
    private boolean isPlaying;
    private int currentSongPlay;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        prepareEnvironmentService(intent);
        return iBinder;
    }

    private void prepareEnvironmentService(Intent intent) {
        currentSongPlay = intent.getIntExtra(CommonValue.KEY_POSITION_SONG, 0);
        arrSong = intent.getParcelableArrayListExtra(CommonValue.KEY_LIST_SONG_CLICK);
        mediaController.setListSong(arrSong);
        runForeground();
        playSong(currentSongPlay);
        setPlaying(true);
    }

    public void playSong(int currentSongPlay) {
        if (mediaController == null) {
            mediaController = new MediaController(context);
            mediaController.getmPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //nếu đang lặp thì return
                    if (mediaController.getmPlayer().isLooping()) return;
                    //nếu phát ngẫu nhiên thì random song
                    if (mediaController.isShuffle()) {
                        mediaController.setIndexSong(new Random().nextInt(mediaController.getListSong().size()));
                        mediaController.playOrPause(false);
                        registerReceiver(broadCastMusic, intentFilter);
                        isPlaying = true;
                        remoteViews.setImageViewResource(R.id.ibPause, R.drawable.pause);
                        startForeground(CommonValue.NOTIFICATION_ID, mBuilder.build());
                        return;
                    }
                    if (mediaController.getLoop() == 2) {
                        return;
                    }
                    mediaController.next();
                }
            });
        } else {
            if (mediaController.getmPlayer().isPlaying())
                mediaController.stop();
            mediaController.getmPlayer().reset();
            if (mediaController.getLoop() == 1)
                mediaController.getmPlayer().setLooping(true);
            mediaController.setIndexSong(currentSongPlay);
            mediaController.playOrPause(true);
            registerReceiver(broadCastMusic, intentFilter);
            isPlaying = true;
            remoteViews.setImageViewResource(R.id.ibPause, R.drawable.pause);
            startForeground(CommonValue.NOTIFICATION_ID, mBuilder.build());
        }

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
        intentFilter.addAction(CommonValue.ACTION_PREVIOUS);
        intentFilter.addAction(CommonValue.ACTION_NEXT);
        intentFilter.addAction(CommonValue.ACTION_PAUSE_SONG);
        intentFilter.addAction(CommonValue.ACTION_START_FOREGROUND);
        intentFilter.addAction(CommonValue.ACTION_SEND_DATA);
        intentFilter.addAction(CommonValue.ACTION_STOP);
        registerReceiver(broadCastMusic, intentFilter);
        mediaController = new MediaController(context);
        mediaController.getmPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //nếu đang lặp thì return
                if (mediaController.getLoop() == 2) {
                    mediaController.getmPlayer().setLooping(true);
                    mediaController.playOrPause(true);
                    return;
                } else if (mediaController.getLoop() == 3) {
                    //neu che do lap all va da chay den het danh sach thi k play nua
                    if (mediaController.getIndexSong() + 1 == mediaController.getListSong().size()) {
                        mediaController.setIndexSong(0);
                        mediaController.playOrPause(true);
                        return;
                    }
                }
                mediaController.next();
                registerReceiver(broadCastMusic, intentFilter);
                isPlaying = true;
                remoteViews.setImageViewResource(R.id.ibPause, R.drawable.pause);
                startForeground(CommonValue.NOTIFICATION_ID, mBuilder.build());
                return;

            }
        });
        arrSong = new ArrayList<>();
    }

    class BroadCastMusic extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case CommonValue.ACTION_SEND_DATA:
                    String title = intent.getStringExtra(CommonValue.KEY_TITLE_SONG);
                    remoteViews.setTextViewText(R.id.tvTitle, title);
                    startForeground(CommonValue.NOTIFICATION_ID, mBuilder.build());
                    break;
                case CommonValue.ACTION_PREVIOUS:
                    mediaController.previous();
                    break;
                case CommonValue.ACTION_NEXT:
                    mediaController.next();
                    break;
                case CommonValue.ACTION_PAUSE_SONG:
                    if (mediaController.getmPlayer() == null) return;
                    if (mediaController.isPlaying()) {
                        mediaController.playOrPause(false);
                        remoteViews.setImageViewResource(R.id.ibPause, R.drawable.play);
                    } else {
                        mediaController.playOrPause(false);
                        remoteViews.setImageViewResource(R.id.ibPause, R.drawable.pause);
                    }
                    isPlaying = !isPlaying;
                    Intent intentPauseOrPlay = new Intent(CommonValue.ACTION_PAUSE_SONG_FROM_NOTIFICATION);
                    intentPauseOrPlay.putExtra(CommonValue.KEY_SEND_PAUSE, isPlaying);
                    sendBroadcast(intentPauseOrPlay);
                    startForeground(CommonValue.NOTIFICATION_ID, mBuilder.build());
                    break;
                case CommonValue.ACTION_STOP:
                    stopSelf();
                    Intent intentStop=new Intent(CommonValue.ACTION_STOP_ALL);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra(CommonValue.KEY_EXTRA_STOP_ALL,true);
                    sendBroadcast(intentStop);
//                    onDestroy();
                    break;
            }
        }
    }

    private void runForeground() {
        remoteViews = new RemoteViews(getPackageName(),
                R.layout.layout_notification_music_service);
        mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.notification_icon).setContent(
                remoteViews);
        remoteViews.setTextViewText(R.id.tvTitle, "Ahihi");
        Intent intentPress = new Intent(this, PlaySongActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, CommonValue.REQUEST_CODE_NOTIFICATION, intentPress, 0);
        mBuilder.setContentIntent(pendingIntent);
        PendingIntent pre = PendingIntent.getBroadcast(context, CommonValue.REQUEST_CODE_NOTIFICATION,
                new Intent(CommonValue.ACTION_PREVIOUS), 0);
        remoteViews.setOnClickPendingIntent(R.id.ibPre, pre);
        PendingIntent next = PendingIntent.getBroadcast(context, CommonValue.REQUEST_CODE_NOTIFICATION,
                new Intent(CommonValue.ACTION_NEXT), 0);
        remoteViews.setOnClickPendingIntent(R.id.ibNext, next);
        Intent intentP = new Intent(CommonValue.ACTION_PAUSE_SONG);
        intentP.putExtra(CommonValue.KEY_SEND_PAUSE, isPlaying);
        PendingIntent pause = PendingIntent.getBroadcast(context, CommonValue.REQUEST_CODE_NOTIFICATION, intentP, 0);
        remoteViews.setOnClickPendingIntent(R.id.ibPause, pause);
        PendingIntent close = PendingIntent.getBroadcast(context, CommonValue.REQUEST_CODE_NOTIFICATION,
                new Intent(CommonValue.ACTION_STOP), 0);
        remoteViews.setOnClickPendingIntent(R.id.ibNotiClose, close);
        startForeground(CommonValue.NOTIFICATION_ID, mBuilder.build());
    }

    public MediaController getMediaController() {
        return mediaController;
    }


    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(broadCastMusic);
            release();
        }catch (Exception e){
            Log.d("Error","ERROR ON DESTROY");
        }
        super.onDestroy();
    }

    private void release() {
        if (mediaController.getmPlayer() != null) {
            if (mediaController.getmPlayer().isPlaying()) mediaController.getmPlayer().stop();
            mediaController.getmPlayer().release();
            mediaController.setmPlayer(null);
        }
    }

//    public boolean isPlaying() {
//        return isPlaying;
//    }
//
//    public int getCurrentSongPlay() {
//        return currentSongPlay;
//    }
//
//    public void setCurrentSongPlay(int currentSongPlay) {
//        this.currentSongPlay = currentSongPlay;
//    }
//    public void setMediaController(MediaController mediaController) {
//        this.mediaController = mediaController;
//    }

}
