package musicplayer.group3.dev.musicplayer.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.adapter.SongAdapter;
import musicplayer.group3.dev.musicplayer.common.Const;
import musicplayer.group3.dev.musicplayer.item.ItemSong;
import musicplayer.group3.dev.musicplayer.listener.OnPlayMusic;
import musicplayer.group3.dev.musicplayer.media.MediaManager;
import musicplayer.group3.dev.musicplayer.service.MediaService;

public class SearchActivity extends AppCompatActivity {

    private ImageView imvBack;
    private EditText editInput;
    private ListView lvSearch;
    private SongAdapter songAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        imvBack = (ImageView) findViewById(R.id.imv_back_search);
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lvSearch = (ListView) findViewById(R.id.lv_search);
        songAdapter = new SongAdapter(this,onPlayMusic);
        songAdapter.setArrItemSong(MediaManager.getInstance(this).getSongList(null, null));
        lvSearch.setAdapter(songAdapter);
//        lvSearch.setOnItemClickListener(this);

        editInput = (EditText) findViewById(R.id.edt_search);
        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textFill = s.toString();
                songAdapter.setArrSong(songAdapter.pushData());
                songAdapter.filter(textFill);
                songAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    songAdapter.clearArr();
                    songAdapter.setArrItemSong(songAdapter.pushData());
                    songAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    OnPlayMusic onPlayMusic=new OnPlayMusic() {
        @Override
        public void playSong(List<ItemSong> list, int position) {
            if (!isMyServiceRunning(MediaService.class)) {
                Intent intent = new Intent(SearchActivity.this, MediaService.class);
                startService(intent);
            }
            bindService(new Intent(SearchActivity.this, MediaService.class), serviceConnection, BIND_AUTO_CREATE);
//            mediaManager.setArrItemSong((ArrayList<ItemSong>) list);
//            mediaManager.setCurrentIndex(position);
//            mediaManager.play(true);
//            mainActivity.showBottomLayout(true);
//            mainActivity.setInforBottomLayout(list.get(position).getDisplayName(), list.get(position).getArtist());
            Intent intent = new Intent();
            intent.putExtra(Const.KEY_ACTION_SEARCH_SONG_NAME, songAdapter.getItem(position).getDisplayName());
            setResult(RESULT_OK, intent);
            finish();
        }
    };
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
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
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent();
//        intent.putExtra(Const.KEY_ACTION_SEARCH_SONG_NAME, songAdapter.getItem(position).getDisplayName());
//        setResult(RESULT_OK, intent);
//        finish();
//    }
}
