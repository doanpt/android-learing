package com.dvt.samsung.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dvt.samsung.adapter.SongAdapter;
import com.dvt.samsung.finalapp.MainActivity;
import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.model.Song;

import java.util.ArrayList;

/**
 * Created by Android on 11/7/2016.
 */

public class SongFragment extends Fragment {
    private View view;
    private SongAdapter adapter;
    private ArrayList<Song> arrSong;
    private RecyclerView lvSong;
    @SuppressLint("ValidFragment")
    public SongFragment(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.fragment_song, null);
        this.initializeComponent();
    }

    private void initializeComponent() {
        arrSong = new ArrayList<>();
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        arrSong.add(new Song("Price Tag", "pricetag", "sdfasdfasd", "Mroon 5", "No", 23412341));
        adapter = new SongAdapter(arrSong, getContext());
        lvSong= (RecyclerView) view.findViewById(R.id.lv_song);
        lvSong.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvSong.setLayoutManager(linearLayoutManager);

        lvSong.setAdapter(adapter);

    }

    public SongFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }
}
