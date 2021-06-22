package musicplayer.group3.dev.musicplayer.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.view.animation.TranslateAnimation;
import android.widget.RemoteViews;
import android.widget.TextView;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.activity.DetailSongActivity;
import musicplayer.group3.dev.musicplayer.common.Const;
import musicplayer.group3.dev.musicplayer.common.SharePreferencesController;
import musicplayer.group3.dev.musicplayer.item.ItemSong;
import musicplayer.group3.dev.musicplayer.media.MediaManager;

/**
 * Created by sev_user on 4/12/2017.
 */

public class MediaService extends Service {

    //mediaplayer object.
    private MediaManager mediaManager;
    private BroadCastMusic broadCastMusic = new BroadCastMusic();
    private IntentFilter intentFilter = new IntentFilter();
    private RemoteViews remoteViews;
    private NotificationCompat.Builder mBuilder;

    //detect sharking
//    private ShakeListener mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private boolean detectShaking;
    private SharedPreferences sharedPreferences;

    private Context context;

    //binder để get service
    private MyBinder myBinder = new MediaService.MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        runForeground();
        return myBinder;
    }

    public class MyBinder extends Binder {
        public MediaService getMediaService() {
            return MediaService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaManager = MediaManager.getInstance(context);
        context = this;
        intentFilter.addAction(Const.ACTION_PREVIOUS);
        intentFilter.addAction(Const.ACTION_NEXT);
        intentFilter.addAction(Const.ACTION_PAUSE_SONG);
        intentFilter.addAction(Const.ACTION_START_FOREGROUND);
        intentFilter.addAction(Const.ACTION_SEND_DATA);
        intentFilter.addAction(Const.ACTION_STOP);
        registerReceiver(broadCastMusic, intentFilter);
    }

    public MediaManager getMediaManager() {
        return mediaManager;
    }

    class BroadCastMusic extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Const.ACTION_SEND_DATA:
                    String title = intent.getStringExtra(Const.KEY_TITLE_SONG);
                    remoteViews.setTextViewText(R.id.tvTitle, title);
                    startForeground(Const.NOTIFICATION_ID, mBuilder.build());
                    break;
                case Const.ACTION_PREVIOUS:
                    mediaManager.previous();
                    break;
                case Const.ACTION_NEXT:
                    mediaManager.next();
                    break;
                case Const.ACTION_PAUSE_SONG:
                    if (mediaManager.getmPlayer() == null) return;
                    if (mediaManager.getmPlayer().isPlaying()) {
                        remoteViews.setImageViewResource(R.id.ibPause, R.drawable.ic_play);
                    } else {
                        remoteViews.setImageViewResource(R.id.ibPause, R.drawable.ic_pause);
                    }
                    mediaManager.play(false);
                    startForeground(Const.NOTIFICATION_ID, mBuilder.build());
                    break;
                case Const.ACTION_STOP:
                    mediaManager.getmPlayer().stop();
                    stopSelf();
                    Intent intentStop = new Intent(Const.ACTION_STOP_ALL);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra(Const.KEY_EXTRA_STOP_ALL,true);
                    sendBroadcast(intentStop);
//                    onDestroy();
                    break;
            }
        }
    }

//    public void playSongService(boolean isPlay) {
//        if(mediaManager==null){
//            mediaManager=MediaManager.getInstance(context);
//            mediaManager.play(isPlay);
//        }else{
//            mediaManager.play(isPlay);
//        }
//        startForeground(Const.REQUEST_CODE_NOTIFICATION, mBuilder.build());
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void runForeground() {
        remoteViews = new RemoteViews(getPackageName(),
                R.layout.layout_notification_music_service);
        mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.music_icon).setContent(
                remoteViews);

        Intent intentPress = new Intent(this, DetailSongActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Const.REQUEST_CODE_NOTIFICATION, intentPress, 0);
        mBuilder.setContentIntent(pendingIntent);
        PendingIntent pre = PendingIntent.getBroadcast(context, Const.REQUEST_CODE_NOTIFICATION,
                new Intent(Const.ACTION_PREVIOUS), 0);
        remoteViews.setOnClickPendingIntent(R.id.ibPre, pre);
        PendingIntent next = PendingIntent.getBroadcast(context, Const.REQUEST_CODE_NOTIFICATION,
                new Intent(Const.ACTION_NEXT), 0);
        remoteViews.setOnClickPendingIntent(R.id.ibNext, next);
        Intent intentP = new Intent(Const.ACTION_PAUSE_SONG);
        intentP.putExtra(Const.KEY_SEND_PAUSE, mediaManager.getmPlayer().isPlaying());
        PendingIntent pause = PendingIntent.getBroadcast(context, Const.REQUEST_CODE_NOTIFICATION, intentP, 0);
        remoteViews.setOnClickPendingIntent(R.id.ibPause, pause);
        PendingIntent close = PendingIntent.getBroadcast(context, Const.REQUEST_CODE_NOTIFICATION,
                new Intent(Const.ACTION_STOP), 0);
        remoteViews.setOnClickPendingIntent(R.id.ibNotiClose, close);

//        mBuilder.setStyle(bigText);
//        mBuilder.addAction(R.drawable.ic_play,getString(R.string.app_name),next);
//        mBuilder.setPriority(NotificationCompat.PRIORITY_MIN);

        ItemSong song = mediaManager.getArrItemSong().get(mediaManager.getCurrentIndex());
        remoteViews.setTextViewText(R.id.tvTitle, song.getDisplayName());
        NotificationChannel channel = new NotificationChannel("Music", "Music App", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        mBuilder.setChannelId(channel.getId());
        startForeground(Const.NOTIFICATION_ID, mBuilder.build());
    }

    public NotificationCompat.Builder getmBuilder() {
        return mBuilder;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadCastMusic);
        super.onDestroy();
    }
}
