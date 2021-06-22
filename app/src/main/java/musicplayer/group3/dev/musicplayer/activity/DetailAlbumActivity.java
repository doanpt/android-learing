package musicplayer.group3.dev.musicplayer.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.adapter.TrackAdapter;
import musicplayer.group3.dev.musicplayer.common.Const;
import musicplayer.group3.dev.musicplayer.common.SharePreferencesController;
import musicplayer.group3.dev.musicplayer.item.ItemSong;
import musicplayer.group3.dev.musicplayer.listener.OnPlayMusic;
import musicplayer.group3.dev.musicplayer.media.MediaManager;
import musicplayer.group3.dev.musicplayer.service.MediaService;

public class DetailAlbumActivity extends Activity implements View.OnClickListener {

    private ImageView imvBack;
    private TextView tvSeach, tvSetting;
    private TextView tvTitleAlbums, tvInforAlbum;
    private TextView tvRandomList;
    private ListView lvListSong;

    //Bottom menu
    private View viewBottom;
    private LinearLayout llDetailTitleSong;
    private ImageView imvSong;
    private ImageView imvPausePlay;
    private ImageView imvPrevious, imvNext;
    private TextView tvTitleSong;
    private TextView tvNameArtist;

    //
    private int albumID;
    private String albumName;
    private String albumArtist;
    private ArrayList<ItemSong> arrItemTrack = new ArrayList<>();
    private Handler handler = new Handler();
    //
    private TrackAdapter trackAdapter;
    private boolean isFirstRun;
    private boolean isPauseOrPlay;

    private MediaManager mediaManager;
    private UpdatePlayNewSong updatePlayNewSong = new UpdatePlayNewSong();
    private IntentFilter intentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_album);
        getData();
        initView();
        mediaManager = MediaManager.getInstance(this);
        intentFilter.addAction(Const.ACTION_SEND_DATA);
        registerReceiver(updatePlayNewSong, intentFilter);
        DetailAlbumActivity.this.runOnUiThread(runnable);
    }

    private void initView() {
        isPauseOrPlay = false;
        //init action
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvBack.setOnClickListener(this);
        tvSeach = (TextView) findViewById(R.id.tv_action_search);
        tvSetting = (TextView) findViewById(R.id.tv_action_setting);
        tvSeach.setOnClickListener(this);
        tvSetting.setOnClickListener(this);

        tvTitleAlbums = (TextView) findViewById(R.id.tv_title_album_detail);
        tvTitleAlbums.setText(albumName);
        tvInforAlbum = (TextView) findViewById(R.id.tv_infor_detail_album);
        tvInforAlbum.setText(albumArtist + "  |  " + trackAdapter.convertToDate(countDuration()));
        tvRandomList = (TextView) findViewById(R.id.tv_random_list);
        tvRandomList.setOnClickListener(this);

        lvListSong = (ListView) findViewById(R.id.lv_detail_al_item_song);
        lvListSong.setAdapter(trackAdapter);

        //Bottom Menu
        viewBottom = (View) findViewById(R.id.bottom_menu);
        llDetailTitleSong = (LinearLayout) findViewById(R.id.ll_detail_title_song);
        llDetailTitleSong.setOnClickListener(this);
        imvSong = (ImageView) findViewById(R.id.imv_image_song);
        imvSong.setOnClickListener(this);
        imvNext = (ImageView) findViewById(R.id.imv_next);
        imvPrevious = (ImageView) findViewById(R.id.imv_previous);
        imvPausePlay = (ImageView) findViewById(R.id.imv_pause_play);
        imvNext.setOnClickListener(this);
        imvPrevious.setOnClickListener(this);
        imvPausePlay.setOnClickListener(this);
        tvTitleSong = (TextView) findViewById(R.id.tv_bottom_title_song);
        tvTitleSong.setSelected(true);
        tvNameArtist = (TextView) findViewById(R.id.tv_bottom_name_artist);
        isFirstRun = false;
        showBottomLayout(isFirstRun);
    }


    private void showBottomLayout(boolean isFirstRun) {
        if (isFirstRun) {
            viewBottom.setVisibility(View.VISIBLE);
        } else {
            viewBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.tv_action_search:
                actionSearch();
                break;
            case R.id.tv_action_setting:
                //actionSetting();
                //Snackbar.make(v, "Please input Setting", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.tv_random_list:
                //Snackbar.make(v, "Action SEARCH not implement", Snackbar.LENGTH_LONG).show();
                actionPlayRandomList();
                break;
            case R.id.imv_image_song:
                actionShowDetailSong();
                break;
            case R.id.ll_detail_title_song:
                actionShowDetailSong();
                break;
            case R.id.imv_next:
                intent = new Intent(Const.ACTION_NEXT);
                sendBroadcast(intent);
                break;
            case R.id.imv_previous:
                intent = new Intent(Const.ACTION_PREVIOUS);
                sendBroadcast(intent);
                break;
            case R.id.imv_pause_play:
                Intent intentPause = new Intent(Const.ACTION_PAUSE_SONG);
                sendBroadcast(intentPause);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQUEST_CODE_ACTION_SEARCH_DETAIL_ALBUM:
                if (resultCode == RESULT_OK) {
                    //TODO play music
                    //setinfor
                    if (!isFirstRun) {
                        showBottomLayout(true);
                        isFirstRun = true;
                    }
                    //set title
//                    setInforBottomLayout(data.getStringExtra(Const.KEY_ACTION_SEARCH_SONG_NAME),
//                            data.getStringExtra(Const.KEY_ACTION_SEARCH_ARTIST_NAME));
                }
                break;
        }
    }

    private void actionSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, Const.REQUEST_CODE_ACTION_SEARCH_DETAIL_ALBUM);
    }

    private void actionShowDetailSong() {
        Intent intent = new Intent(this, DetailSongActivity.class);
        startActivity(intent);
    }

    private void actionPlayRandomList() {
        SharePreferencesController.getInstance(this).putBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_TRUE);
        mediaManager.setArrItemSong(arrItemTrack);
        mediaManager.setCurrentIndex(new Random().nextInt(arrItemTrack.size() - 1));
        mediaManager.play(true);
        Intent intent = new Intent(this, DetailSongActivity.class);
        startActivity(intent);
    }

    public void getData() {
        Intent intent = getIntent();
        albumID = intent.getIntExtra(Const.KEY_ALBUM_ID, -1);
        albumName = intent.getStringExtra(Const.KEY_ALBUM_NAME);
        albumArtist = intent.getStringExtra(Const.KEY_ALBUM_ARTIST);
        trackAdapter = new TrackAdapter(this, playMusic);
        arrItemTrack = MediaManager.getInstance(this)
                .getSongList(MediaStore.Audio.Media.ALBUM_ID + "=?", new String[]{albumID + ""});
        trackAdapter.setArrItemTrack(arrItemTrack);
    }

    public void setInforBottomLayout(String nameSong, String nameArtist) {
        tvTitleSong.setText(nameSong);
        tvNameArtist.setText(nameArtist);
    }

    private int countDuration() {
        int sum = 0;
        for (ItemSong item : arrItemTrack) {
            sum += item.getDuration();
        }
        return sum;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateInforBottomLayout();
            handler.postDelayed(this, 200);
        }
    };

    private void updateInforBottomLayout() {
        if (mediaManager.getmPlayer().isPlaying()) {
            imvPausePlay.setImageResource(R.drawable.ic_pause);
        } else {
            imvPausePlay.setImageResource(R.drawable.ic_play);
        }
    }

    OnPlayMusic playMusic = new OnPlayMusic() {
        @Override
        public void playSong(List<ItemSong> list, int position) {
            if (!isMyServiceRunning(MediaService.class)) {
                Intent intent = new Intent(DetailAlbumActivity.this, MediaService.class);
                startService(intent);
            }
            bindService(new Intent(DetailAlbumActivity.this, MediaService.class), serviceConnection, BIND_AUTO_CREATE);
            mediaManager.setArrItemSong((ArrayList<ItemSong>) list);
            mediaManager.setCurrentIndex(position);
            mediaManager.play(true);
            showBottomLayout(true);
            tvTitleSong.setText(trackAdapter.getItem(position).getDisplayName());
            tvNameArtist.setText(trackAdapter.getItem(position).getArtist());
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    class UpdatePlayNewSong extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Const.ACTION_SEND_DATA:
                    setInforBottomLayout(intent.getStringExtra(Const.KEY_TITLE_SONG), intent.getStringExtra(Const.KEY_NAME_ARTIST));
                    break;
            }
        }
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
    protected void onDestroy() {
        try {
            unbindService(serviceConnection);
            unregisterReceiver(updatePlayNewSong);
        } catch (Exception e) {
            Log.d("Error", "Error unbind service connect");
        }
        super.onDestroy();
    }
}
