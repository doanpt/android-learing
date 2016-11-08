package com.dvt.samsung.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.dvt.samsung.adapter.SongBaseAdapter;
import com.dvt.samsung.finalapp.MainFragmentActivity;
import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.service.MyMusicService;
import com.dvt.samsung.utils.OnPlayMusic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 11/7/2016.
 */

public class SongFragment extends Fragment {
    private View view;
    //    private SongAdapter adapter;
    private SongBaseAdapter adapter;
    private ArrayList<Song> arrSong;
    //    private RecyclerView lvSong;
    private ListView lvSong;
    private Context context;
    private MainFragmentActivity mainFragmentActivity;

    @SuppressLint("ValidFragment")
    public SongFragment(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        this.context = context;
        view = layoutInflater.inflate(R.layout.fragment_song, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainFragmentActivity = (MainFragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainFragmentActivity = null;
    }

    public SongFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lvSong = (ListView) view.findViewById(R.id.lv_song);
        if (MainFragmentActivity.songs != null) {
            lvSong.setAdapter(new SongBaseAdapter(context, (ArrayList<Song>) MainFragmentActivity.songs, new OnPlayMusic() {
                @Override
                public void playSong(List<Song> paths, int postion) {
                    mainFragmentActivity.playSong(paths, postion);
                }
            }));
        }
        return view;
    }


    private void initializeComponent() {
//        adapter = new SongBaseAdapter(getContext(), arrSong);


        //        adapter = new SongAdapter(arrSong, getContext());
//        lvSong = (RecyclerView) view.findViewById(R.id.lv_song);
//        lvSong.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        lvSong.setLayoutManager(linearLayoutManager);
    }
}
