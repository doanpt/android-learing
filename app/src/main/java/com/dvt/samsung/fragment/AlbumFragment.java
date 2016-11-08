package com.dvt.samsung.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dvt.samsung.adapter.AlbumBaseAdapter;
import com.dvt.samsung.adapter.SongBaseAdapter;
import com.dvt.samsung.finalapp.MainFragmentActivity;
import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.model.Song;

import java.util.ArrayList;

/**
 * Created by Android on 11/7/2016.
 */

public class AlbumFragment extends Fragment {
    private View view;
    private ListView lvAlbum;
    private Context context;

    @SuppressLint("ValidFragment")
    public AlbumFragment(Context context) {
        this.context = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.fragment_album, null);
    }

    public AlbumFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lvAlbum = (ListView) view.findViewById(R.id.lv_album);
        if (MainFragmentActivity.albums != null) {
            lvAlbum.setAdapter(new AlbumBaseAdapter(context, MainFragmentActivity.albums));
        }
        return view;
    }
}
