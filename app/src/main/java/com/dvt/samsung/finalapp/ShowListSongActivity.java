package com.dvt.samsung.finalapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dvt.samsung.adapter.SongBaseAdapter;
import com.dvt.samsung.fragment.AlbumFragment;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.service.MyMusicService;
import com.dvt.samsung.utils.CommonValue;
import com.dvt.samsung.utils.OnListListener;
import com.dvt.samsung.utils.OnPlayMusic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 11/8/2016.
 */

public class ShowListSongActivity extends Activity {
    private ArrayList<Song> arraySong;
    private SongBaseAdapter adapter;
    private ListView lvShowSong;
    private TextView tvTitle;
    private ImageView ivBack, ivSearch;
    private int fromShow;
    private MyMusicService myMusicService;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_song);
        arraySong = new ArrayList<>();
        Intent intent = getIntent();
        arraySong = intent.getParcelableArrayListExtra(CommonValue.KEY_LIST_SONG_CICK);
        fromShow = intent.getIntExtra(CommonValue.KEY_FROM_SHOW_LIST, 0);
        tvTitle = (TextView) findViewById(R.id.tv_name_viewpager);
        ivBack = (ImageView) findViewById(R.id.im_back_viewpager);
        ivSearch = (ImageView) findViewById(R.id.im_other_viewpager);
        lvShowSong = (ListView) findViewById(R.id.lv_show_song);
        adapter = new SongBaseAdapter(this, arraySong, new OnPlayMusic() {
            @Override
            public void playSong(List<Song> paths, int position) {
//                click music on album then play
                playSongList(paths, position);
                Intent intent = new Intent(ShowListSongActivity.this, PlaySongActivity.class);
                intent.putExtra(CommonValue.KEY_SEND_A_SONG, paths.get(position));
                String sizeOfSong = (position) + "/" + paths.size();
                intent.putExtra(CommonValue.KEY_SEND_SIZE_OF_SONG, sizeOfSong);
                startActivity(intent);
            }
        });
        lvShowSong.setAdapter(adapter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String title;
        if (fromShow == 0) {
            title = arraySong.get(0).getAlbum();
        } else {
            title = arraySong.get(0).getArtist();
        }
        tvTitle.setText(title);
        if (isMyServiceRunning(MyMusicService.class)) {
            bindService(new Intent(this, MyMusicService.class), serviceConnection, BIND_AUTO_CREATE);
            ShowListSongActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (myMusicService != null) {
                        handler.removeCallbacks(this);
                    }
                    handler.postDelayed(this, 100);
                }
            });
        }
    }

    private boolean isRun = false;

    public void playSongList(final List<Song> paths, final int position) {
        if (!isMyServiceRunning(MyMusicService.class)) {
            Intent intent = new Intent(this, MyMusicService.class);
            intent.putExtra(CommonValue.KEY_POSITION_SONG, position);
            intent.putExtra(CommonValue.KEY_LIST_SONG_CICK, (ArrayList<? extends Parcelable>) paths);
            startService(intent);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        } else {
            isRun = false;
            ShowListSongActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (myMusicService != null && isRun == false) {
                        ArrayList<Song> mySongs = new ArrayList<>();
                        for (Song song : paths) {
                            mySongs.add(song);
                        }
                        Log.d("run", "sdfsdfa");
                        isRun = true;
                        myMusicService.getMediaController().setListSong(mySongs);
                        myMusicService.playSong(position);
                        myMusicService.setPlaying(true);
                        handler.removeCallbacks(this);
                    }
                    handler.postDelayed(this, 100);
                }
            });
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
        }
    };

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
    protected void onDestroy() {
        try {
            unbindService(serviceConnection);
        } catch (Exception e) {
            Log.e("ERROR BIND SERVICE", "SERVICE NOT BIND");
        }
        super.onDestroy();
    }
}
