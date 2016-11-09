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
import android.widget.RemoteViews;
import android.widget.Toast;

import com.dvt.samsung.finalapp.MainFragmentActivity;
import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.media.MediaController;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.utils.CommonValue;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sev_user on 11/8/2016.
 */

public class MyMusicService extends Service {
    private Context context;
    private IBinder iBinder = new MyMusicService.MyBinder();
    private MediaController mediaController;
    private ArrayList<Song> arrSong;
    private BroadCastMusic broadCastMusic = new BroadCastMusic();
    private RemoteViews remoteViews;
    private NotificationCompat.Builder mBuilder;
    private boolean isPlaying;
    private int currentSongPlay;
    private IntentFilter intentFilter = new IntentFilter();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        prepareEnvironmentService(intent);
        return iBinder;
    }

    private void prepareEnvironmentService(Intent intent) {
        currentSongPlay = intent.getIntExtra(CommonValue.KEY_POSITION_SONG, 0);
        arrSong = intent.getParcelableArrayListExtra(CommonValue.KEY_LIST_SONG_CICK);
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
//                    if (shuffle) {
//                        current = random.nextInt(songs.length);
//                        play(current);
//                        return;
//                    }

//                    if (LOOP == 2 && current + 1 == songs.length) {
//                        if (ibPause != null && ibPlay != null) {
//                            ibPause.setImageResource(R.drawable.ic_play_circle_outline);
//                            ibPlay.setImageResource(R.drawable.ic_play_circle_outline);
//                        }
//                        return;
//                    }

                    mediaController.next();
                }
            });
        } else {
            if (mediaController.getmPlayer().isPlaying())
                mediaController.stop();
            mediaController.setIndexSong(currentSongPlay);
            mediaController.playOrPause(true);
            registerReceiver(broadCastMusic, intentFilter);
//        mediaController.reset();
//        if (LOOP == 0) player.setLooping(true);
//        current = pos;
//        try {
//            String path = songs[pos].getPath();
//            player.setDataSource(path);
//            player.prepare();
//            player.start();
//
//            Intent intent = new Intent("data");
//            intent.putExtra("title", getTitle());
//            intent.putExtra("artist", getArtist());
//            sendBroadcast(intent);
//        } catch (IOException e) {
//            next();
//        }
        }
        isPlaying = true;
        remoteViews.setImageViewResource(R.id.ibPause, R.drawable.pause);
        startForeground(123, mBuilder.build());
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
        arrSong = new ArrayList<>();
    }

    public void setArrSong(ArrayList<Song> arr) {
        arrSong = arr;
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
                    startForeground(CommonValue.NOTIFICATION_ID, mBuilder.build());
                    break;
                case CommonValue.ACTION_STOP:
                    Toast.makeText(context, "STOP", Toast.LENGTH_SHORT).show();
//                    if (player != null && player.isPlaying()) player.stop();
//                    player.release();
                    //stopForeground(STOP_FOREGROUND_REMOVE);
                    if (mediaController != null) {
                        mediaController.stop();
                        onDestroy();
                    }

//                    stopSelf();
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
        PendingIntent pendingIntent = PendingIntent.getActivity(this, CommonValue.REQUEST_CODE_NOTIFICATION, new Intent(this, MainFragmentActivity.class), 0);
        mBuilder.setContentIntent(pendingIntent);
        PendingIntent pre = PendingIntent.getBroadcast(context, CommonValue.REQUEST_CODE_NOTIFICATION, new Intent(CommonValue.ACTION_PREVIOUS), 0);
        remoteViews.setOnClickPendingIntent(R.id.ibPre, pre);
        PendingIntent next = PendingIntent.getBroadcast(context, CommonValue.REQUEST_CODE_NOTIFICATION, new Intent(CommonValue.ACTION_NEXT), 0);
        remoteViews.setOnClickPendingIntent(R.id.ibNext, next);
        Intent intentP = new Intent(CommonValue.ACTION_PAUSE_SONG);
        intentP.putExtra(CommonValue.KEY_SEND_PAUSE, isPlaying);
        PendingIntent pause = PendingIntent.getBroadcast(context, CommonValue.REQUEST_CODE_NOTIFICATION, intentP, 0);
        remoteViews.setOnClickPendingIntent(R.id.ibPause, pause);
        PendingIntent close = PendingIntent.getBroadcast(context, CommonValue.REQUEST_CODE_NOTIFICATION, new Intent(CommonValue.ACTION_STOP), 0);
        remoteViews.setOnClickPendingIntent(R.id.ibNotiClose, close);
        startForeground(CommonValue.NOTIFICATION_ID, mBuilder.build());
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public int getCurrentSongPlay() {
        return currentSongPlay;
    }

    public void setCurrentSongPlay(int currentSongPlay) {
        this.currentSongPlay = currentSongPlay;
    }

    public MediaController getMediaController() {
        return mediaController;
    }

    public void setMediaController(MediaController mediaController) {
        this.mediaController = mediaController;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadCastMusic);
        stopForeground(true);
        super.onDestroy();

    }
}
