package com.dvt.samsung.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.dvt.samsung.finalapp.MainActivity;
import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.utils.CommonValue;
import com.dvt.samsung.utils.MediaController;

import java.util.ArrayList;

/**
 * Created by Android on 11/4/2016.
 */

public class MusicService extends Service implements MediaController.OnPlayStartedListener {
    private MediaController mediaController;
    private Context context;
    private MusicBroadCast broadcast = new MusicBroadCast();
    private RemoteViews remoteViews;
    NotificationCompat.Builder mBuilder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        arrSong = (ArrayList<Song>) intent.getSerializableExtra(CommonValue.KEY_LIST_STRING);
//       mediaController=new MediaController(context,arrSong);
        mediaController = new MediaController(context);
        mediaController.setOnPlayStartedListener(this);
        if (!mediaController.isPlaying()) {
            mediaController.playOrPause(true);
// assign the song name to songName
            runForeground("dasdas");
//            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
//                    new Intent(getApplicationContext(), MainActivity.class),
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            Notification.Builder builder = new Notification.Builder(context);
//            builder.setAutoCancel(true);
//            builder.setContentTitle("WhatsApp Notification");
//            builder.setContentText("You have a new message");
//            builder.setSmallIcon(R.drawable.music_icon);
//            builder.setContentIntent(pi);
//            Notification notification = builder.build();
//            startForeground(CommonValue.NOTIFICATION_ID, notification);
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonValue.ACTION_CONTINUOUS);
        intentFilter.addAction(CommonValue.ACTION_SHUFFLE);
        intentFilter.addAction(CommonValue.ACTION_NEXT);
        intentFilter.addAction(CommonValue.ACTION_PREVIOUS);
        intentFilter.addAction(CommonValue.ACTION_PAUSE_SONG);
        intentFilter.addAction(CommonValue.ACTION_PLAY_SONG);
        intentFilter.addAction(CommonValue.ACTION_LOOP_ALL);
        intentFilter.addAction(CommonValue.ACTION_LOOP_ONE);
        intentFilter.addAction(CommonValue.ACTION_NO_LOOP);
        intentFilter.addAction(CommonValue.ACTION_STOP);
        intentFilter.addAction(CommonValue.ACTION_PLAY_BACKGROUND);
        registerReceiver(broadcast, intentFilter);
    }
    private void runForeground(String title) {
        remoteViews = new RemoteViews(getPackageName(),
                R.layout.layout_notification_music_service);
        mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.music_icon).setContent(
                remoteViews);
        remoteViews.setTextViewText(R.id.tvTitle, title);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 12, new Intent(this, MainActivity.class), 0);
        mBuilder.setContentIntent(pendingIntent);

        PendingIntent pre = PendingIntent.getBroadcast(context, 12, new Intent("pre"), 0);
        remoteViews.setOnClickPendingIntent(R.id.ibPre, pre);

        PendingIntent next = PendingIntent.getBroadcast(context, 12, new Intent("next"), 0);
        remoteViews.setOnClickPendingIntent(R.id.ibNext, next);

        Intent intentPause = new Intent("pause");
//        intentPause.putExtra("ispause", playing);
        PendingIntent pause = PendingIntent.getBroadcast(context, 12, intentPause, 0);
        remoteViews.setOnClickPendingIntent(R.id.ibPause, pause);

        PendingIntent close = PendingIntent.getBroadcast(context, 12, new Intent("close"), 0);
        remoteViews.setOnClickPendingIntent(R.id.ibNotiClose, close);

        startForeground(123, mBuilder.build());
    }
    @SuppressLint("NewApi")
    @Override
    public void onDestroy() {
        unregisterReceiver(broadcast);
        release();
        stopForeground(STOP_FOREGROUND_REMOVE);
        super.onDestroy();
    }

    public void release() {
        if (mediaController != null) {
            if (mediaController.isPlaying()) mediaController.stop();
            mediaController = null;
        }
    }

    @Override
    public void onMediaStarted(String songName, String totalTime, int totalTimeInt) {

    }

    private class MusicBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case CommonValue.ACTION_NEXT:
                    mediaController.next();
                    break;
                case CommonValue.ACTION_PREVIOUS:
                    mediaController.previous();
                    break;
                case CommonValue.ACTION_CONTINUOUS:
//                    mediaController.
                    break;
                case CommonValue.ACTION_SHUFFLE:
                    break;
                case CommonValue.ACTION_LOOP_ONE:
                    break;
                case CommonValue.ACTION_LOOP_ALL:
                    break;
                case CommonValue.ACTION_NO_LOOP:
                    break;
                case CommonValue.ACTION_PLAY_SONG:
                    mediaController.playOrPause(true);
                    break;
                case CommonValue.ACTION_PAUSE_SONG:
                    mediaController.playOrPause(false);
                    break;
                case CommonValue.ACTION_STOP:
                    unregisterReceiver(broadcast);
                    release();
                    break;
                case CommonValue.ACTION_PLAY_BACKGROUND:
                    break;
            }
        }
    }
}
