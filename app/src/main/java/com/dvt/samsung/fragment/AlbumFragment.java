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

import com.dvt.samsung.adapter.AlbumBaseAdapter;
import com.dvt.samsung.adapter.SongBaseAdapter;
import com.dvt.samsung.finalapp.MainFragmentActivity;
import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.finalapp.ShowListSongActivity;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.utils.CommonValue;
import com.dvt.samsung.utils.OnListListener;

import java.util.ArrayList;
import java.util.List;

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
            lvAlbum.setAdapter(new AlbumBaseAdapter(context, MainFragmentActivity.albums, new OnListListener() {
                @Override
                public void onItemClick(int pos) {

                }

                @Override
                public void onItemClick(List<Song> songs) {
                    Intent intent = new Intent(getActivity(), ShowListSongActivity.class);
                    intent.putParcelableArrayListExtra(CommonValue.KEY_LIST_SONG_CICK, (ArrayList<? extends Parcelable>) songs);
                    intent.putExtra(CommonValue.KEY_FROM_SHOW_LIST, 0);
                    startActivity(intent);
                }
            }));
        }
        return view;
    }

}