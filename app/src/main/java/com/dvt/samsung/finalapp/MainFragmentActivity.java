package com.dvt.samsung.finalapp;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dvt.samsung.adapter.ViewPagerAdapter;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.service.MyMusicService;
import com.dvt.samsung.utils.CommonValue;
import com.dvt.samsung.listener.OnPlayMusic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Android on 11/7/2016.
 */

public class MainFragmentActivity extends FragmentActivity implements OnPlayMusic {
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tab;
    private ImageView ivBack;
    private MyMusicService myMusicService;
    public static List<Song> songs;
    public static HashMap<String, List<Song>> albums;
    public static HashMap<String, List<Song>> artists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        songs = new ArrayList<>();
        albums = new HashMap<>();
        artists = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isStorageAllowed()) {
                requestStoragePermission();
                return;
            } else {
                queryAll();
            }
        } else {
            queryAll();
        }
        initView();
        if (isMyServiceRunning(MyMusicService.class)) {
            bindService(new Intent(this, MyMusicService.class), serviceConnection, BIND_AUTO_CREATE);
        }
    }

    private boolean isStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    //Requesting permission
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CommonValue.REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    queryAll();
                } else {
                    Toast.makeText(MainFragmentActivity.this, "Permission denied to read your External storage ! please to setting and accept permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void queryAll() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, selection, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int pathColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);
            albums.put("Undefine", new ArrayList<Song>());
            artists.put("Undefine", new ArrayList<Song>());

            do {
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                String path = musicCursor.getString(pathColumn);
                Song song = new Song();
                song.setName(thisTitle);
                song.setArtist(thisArtist);
                song.setPath(path);
                song.setAlbum(thisAlbum);
                if (thisAlbum != null) {
                    if (albums.containsKey(thisAlbum)) albums.get(thisAlbum).add(song);
                    else {
                        List<Song> listAlbums = new ArrayList<>();
                        listAlbums.add(song);
                        albums.put(thisAlbum, listAlbums);
                    }
                } else {
                    albums.get("Undefine").add(song);
                }

                if (thisArtist != null) {
                    if (artists.containsKey(thisAlbum)) artists.get(thisAlbum).add(song);
                    else {
                        List<Song> listArtists = new ArrayList<>();
                        listArtists.add(song);
                        artists.put(thisAlbum, listArtists);
                    }
                } else {
                    artists.get("Undefine").add(song);
                }
                songs.add(song);
            }
            while (musicCursor.moveToNext());
            if (albums.get("Undefine").isEmpty()) albums.remove("Undefine");
            if (artists.get("Undefine").isEmpty()) artists.remove("Undefine");
            Log.d("tag", "size songs: " + songs.size());
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyMusicService.MyBinder binder = (MyMusicService.MyBinder) service;
            myMusicService = binder.getMyService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myMusicService = null;
            myMusicService.setPlaying(false);
        }
    };

    private void initView() {
        viewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPagerAdapter);
        int positionTabClick = getIntent().getIntExtra(CommonValue.KEY_MAIN_CLICK_ITEM, 0);
        String[] tabBackgroundIds = new String[]{"Bài hát", "Album", "Nghệ sĩ", "Playlist", "Download"};
        tab = (TabLayout) findViewById(R.id.tab);
        tab.setupWithViewPager(viewPager);
        for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
            tab.getTabAt(i).setText(tabBackgroundIds[i]);
        }
        TabLayout.Tab tabSelection = tab.getTabAt(positionTabClick);
        tabSelection.select();
        ivBack = (ImageView) findViewById(R.id.im_back_viewpager);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void playSong(List<Song> paths, int position) {
        if (!isMyServiceRunning(MyMusicService.class)) {
            Intent intent = new Intent(this, MyMusicService.class);
            intent.putExtra(CommonValue.KEY_POSITION_SONG, position);
            intent.putExtra(CommonValue.KEY_LIST_SONG_CLICK, (ArrayList<? extends Parcelable>) paths);
            startService(intent);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        } else {
            ArrayList<Song> mySongs = new ArrayList<>();
            for (Song song : paths) {
                mySongs.add(song);
            }
            if (myMusicService != null) {
                myMusicService.getMediaController().setListSong(mySongs);
                myMusicService.playSong(position);
            }
            myMusicService.setPlaying(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
