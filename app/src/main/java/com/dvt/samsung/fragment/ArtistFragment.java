package com.dvt.samsung.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dvt.samsung.adapter.ArtistBaseAdapter;
import com.dvt.samsung.finalapp.MainFragmentActivity;
import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.finalapp.ShowListSongActivity;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.utils.CommonValue;
import com.dvt.samsung.listener.OnListListener;

import java.util.ArrayList;
import java.util.List;

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
            lvArtist.setAdapter(new ArtistBaseAdapter(context, MainFragmentActivity.artists, new OnListListener() {
                @Override
                public void onItemClick(int pos) {

                }

                @Override
                public void onItemClick(List<Song> songs) {
                    Intent intent = new Intent(getActivity(), ShowListSongActivity.class);
                    intent.putParcelableArrayListExtra(CommonValue.KEY_LIST_SONG_CLICK, (ArrayList<? extends Parcelable>) songs);
                    intent.putExtra(CommonValue.KEY_FROM_SHOW_LIST,1);
                    startActivity(intent);
                }
            }));
        }
        return view;
    }
}
