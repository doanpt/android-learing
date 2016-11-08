package com.dvt.samsung.finalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dvt.samsung.adapter.SongBaseAdapter;
import com.dvt.samsung.fragment.AlbumFragment;
import com.dvt.samsung.model.Song;
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
            public void playSong(List<Song> paths, int postion) {

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
    }
}
