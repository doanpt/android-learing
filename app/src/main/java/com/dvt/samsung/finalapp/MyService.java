//package com.dvt.samsung.finalapp;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Binder;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.widget.Toast;
//
///**
// * Created by sev_user on 11/7/2016.
// */
//
//public class MyService extends Service {
//    private IBinder iBinder=new MyBinder();
//    private Context context;
//    private MediaPlayer mediaPlayer;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        context=this;
//        mediaPlayer= MediaPlayer.create(context,R.raw.chuyen);
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return iBinder;
//    }
//    public class MyBinder extends Binder{
//        MyService getMyService(){
//            return MyService.this;
//        }
//    }
//    public void playMedia(){
//        if(mediaPlayer!=null){
//            mediaPlayer.start();
//        }
//    }
//
//    public void pause(){
//        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
//            mediaPlayer.pause();
//        }
//    }
//
//    public void forward(){
//        if(mediaPlayer!=null){
//            mediaPlayer.seekTo(mediaPlayer.getDuration()-10000);
//        }
//    }
//    public void toastA(){
//        Toast.makeText(context,"Click A",Toast.LENGTH_SHORT).show();
//    }
//    public void toastB(){
//        Toast.makeText(context,"Click B",Toast.LENGTH_SHORT).show();
//    }
//
//
//}
