package musicplayer.group3.dev.musicplayer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.activity.MainActivity;
import musicplayer.group3.dev.musicplayer.adapter.SongAdapter;
import musicplayer.group3.dev.musicplayer.item.ItemSong;
import musicplayer.group3.dev.musicplayer.listener.OnPlayMusic;
import musicplayer.group3.dev.musicplayer.media.MediaManager;
import musicplayer.group3.dev.musicplayer.service.MediaService;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongFragment extends Fragment {

    private static final String TAG = "SongFragment";
    private ListView lvSong;
    private SongAdapter songAdapter;
    private MainActivity mainActivity;
    private MediaManager mediaManager;
    private static SongFragment songFragment;

    public static SongFragment getInstance() {
        if (songFragment == null) {
            return new SongFragment();
        }
        return songFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() - SongFragment");
        View view = inflater.inflate(R.layout.fragment_song, container, false);
        initView(view);
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainActivity = (MainActivity) getActivity();
        mediaManager = MediaManager.getInstance(mainActivity);
        songAdapter = new SongAdapter(mainActivity, playSong);

    }

    OnPlayMusic playSong = new OnPlayMusic() {
        @Override
        public void playSong(List<ItemSong> list, int position) {
            if (!mainActivity.isMyServiceRunning(MediaService.class)) {
                Intent intent = new Intent(mainActivity, MediaService.class);
                mainActivity.startService(intent);
            }
            mainActivity.bindService(new Intent(mainActivity, MediaService.class), mainActivity.serviceConnection, mainActivity.BIND_AUTO_CREATE);
            mediaManager.setArrItemSong((ArrayList<ItemSong>) list);
            mediaManager.setCurrentIndex(position);
            mediaManager.play(true);
            mainActivity.showBottomLayout(true);
            mainActivity.setInforBottomLayout(list.get(position).getDisplayName(), list.get(position).getArtist());
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated() - SongFragment");
    }

    private void initView(View view) {
        lvSong = (ListView) view.findViewById(R.id.lv_song);
        lvSong.setAdapter(songAdapter);
    }
}
