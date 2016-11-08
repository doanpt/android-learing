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
import android.widget.ImageView;
import android.widget.ListView;

import com.dvt.samsung.adapter.SongBaseAdapter;
import com.dvt.samsung.finalapp.MainFragmentActivity;
import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.service.MyMusicService;

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

    @SuppressLint("ValidFragment")
    public SongFragment(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        this.context = context;
     //   this.initializeComponent();
        view = layoutInflater.inflate(R.layout.fragment_song, null);
    }

    public SongFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lvSong = (ListView) view.findViewById(R.id.lv_song);
        if (MainFragmentActivity.songs != null) {
            lvSong.setAdapter(new SongBaseAdapter(context, (ArrayList<Song>) MainFragmentActivity.songs));
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
//
//    public ArrayList<Song> getAllSong() {
//        ArrayList<Song> listSong = new ArrayList<>();
//        if (listSong.size() > 0) {
//            return listSong;
//        }
//        String projection[] = new String[]{
//                //Name
//                MediaStore.MediaColumns.TITLE,
//                //FileName
//                MediaStore.MediaColumns.DISPLAY_NAME,
//                //Path
//                MediaStore.MediaColumns.DATA,
//                MediaStore.Audio.Media.ARTIST,
//                MediaStore.Audio.Media.ALBUM,
//                MediaStore.Audio.Media.DURATION
//        };
//        Cursor cursor = context.getContentResolver()
//                .query(
//                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                        projection,
//                        null,
//                        null,
//                        null
//                );
//        if (cursor == null) {
//            return listSong;
//        }
//        cursor.moveToFirst();
//        while (cursor.isAfterLast() == false) {
//            String name = cursor.getString(cursor.getColumnIndex(
//                    MediaStore.MediaColumns.TITLE));
//            String fileName = cursor.getString(cursor.getColumnIndex(
//                    MediaStore.MediaColumns.DISPLAY_NAME));
//            String path = cursor.getString(cursor.getColumnIndex(
//                    MediaStore.MediaColumns.DATA));
//            String artist = cursor.getString(cursor.getColumnIndex(
//                    MediaStore.Audio.Media.ARTIST));
//            String album = cursor.getString(cursor.getColumnIndex(
//                    MediaStore.Audio.Media.ALBUM));
//            int duration = cursor.getInt(cursor.getColumnIndex(
//                    MediaStore.Audio.Media.DURATION));
//            listSong.add(new Song(name, fileName, path, artist, album, duration));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return listSong;
//    }

}
