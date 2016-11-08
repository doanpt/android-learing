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
import com.dvt.samsung.finalapp.MainFragmentActivity;
import com.dvt.samsung.finalapp.R;

/**
 * Created by Android on 11/7/2016.
 */

public class ArtistFragment extends Fragment {
    private View view;
    private ListView lvArtist;
    private Context context;

    @SuppressLint("ValidFragment")
    public ArtistFragment(Context context) {
        this.context = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.fragment_artist, null);
    }

    public ArtistFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lvArtist = (ListView) view.findViewById(R.id.lv_artist);
        if (MainFragmentActivity.artists != null) {
            lvArtist.setAdapter(new AlbumBaseAdapter(context, MainFragmentActivity.artists));
        }
        return view;
    }
}
