package com.dvt.samsung.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dvt.samsung.adapter.SongBaseAdapter;
import com.dvt.samsung.finalapp.MainFragmentActivity;
import com.dvt.samsung.finalapp.PlaySongActivity;
import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.utils.CommonValue;
import com.dvt.samsung.listener.OnPlayMusic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 11/7/2016.
 */

public class SongFragment extends Fragment {
    //implements View.OnClickListener {
    private View view;
    private SongBaseAdapter adapter;
    private ArrayList<Song> arrSong;
    private ListView lvSong;
    private Context context;
    private MainFragmentActivity mainFragmentActivity;
    private ImageView ivSearch;
    private TextView tvName;
    private SearchView editSearch;
//    private ImageView btnPlay, btnPause, btnShuffle, btnPrevious, btnNext, btnLoopAll, btnLoopOne, btnNoLoop, btnConiuous;
//    public int loopMusic;
//    public boolean shuffle = true;
//    private SharedPreferences sharedPreferences;
//    private MyMusicService myMusicService;

    //private SongAdapter adapter;
    //private RecyclerView lvSong;

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lvSong = (ListView) view.findViewById(R.id.lv_song);
        ivSearch = (ImageView) mainFragmentActivity.findViewById(R.id.im_other_viewpager);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvName.setVisibility(View.INVISIBLE);
                editSearch.setVisibility(View.VISIBLE);
                editSearch.setFocusable(true);
                editSearch.setFocusableInTouchMode(true);
                editSearch.requestFocus();
                editSearch.requestFocusFromTouch();
                editSearch.setActivated(true);
                editSearch.setIconifiedByDefault(true);
            }
        });
        tvName = (TextView) mainFragmentActivity.findViewById(R.id.tv_name_viewpager);
        editSearch = (SearchView) mainFragmentActivity.findViewById(R.id.edt_search);
//        sharedPreferences = mainFragmentActivity.getSharedPreferences(CommonValue.KEY_SAVE_MODE, Context.MODE_PRIVATE);
//        loopMusic = sharedPreferences.getInt(CommonValue.KEY_LOOP_MUSIC, 1);
//        shuffle = sharedPreferences.getBoolean(CommonValue.KEY_SHUFFLE, true);
//        int id = editSearch.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
//        TextView textView = (TextView) editSearch.findViewById(id);
//        textView.setTextColor(Color.WHITE);
        editSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
                tvName.setVisibility(View.VISIBLE);
                editSearch.setVisibility(View.INVISIBLE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrSong = (ArrayList<Song>) MainFragmentActivity.songs;
                adapter = new SongBaseAdapter(getContext(), arrSong, new OnPlayMusic() {
                    @Override
                    public void playSong(List<Song> paths, int position) {
                        arrSong = (ArrayList<Song>) paths;
                        mainFragmentActivity.playSong(paths, position);
                        Intent intent = new Intent(getActivity(), PlaySongActivity.class);
                        intent.putExtra(CommonValue.KEY_SEND_A_SONG, MainFragmentActivity.songs.get(position));
                        String sizeOfSong = position + "/" + MainFragmentActivity.songs.size();
                        intent.putExtra(CommonValue.KEY_SEND_SIZE_OF_SONG, sizeOfSong);
                        startActivity(intent);
                    }
                });
                lvSong.setAdapter(adapter);

                adapter.getFilter().filter(newText.toLowerCase().trim());
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        if (MainFragmentActivity.songs != null) {
            adapter = new SongBaseAdapter(context, (ArrayList<Song>) MainFragmentActivity.songs, new OnPlayMusic() {
                @Override
                public void playSong(List<Song> paths, int position) {
                    arrSong = (ArrayList<Song>) paths;
                    mainFragmentActivity.playSong(paths, position);
                    Intent intent = new Intent(getActivity(), PlaySongActivity.class);
                    intent.putExtra(CommonValue.KEY_SEND_A_SONG, MainFragmentActivity.songs.get(position));
                    String sizeOfSong = position + "/" + MainFragmentActivity.songs.size();
                    intent.putExtra(CommonValue.KEY_SEND_SIZE_OF_SONG, sizeOfSong);
                    startActivity(intent);
                }
            });
            lvSong.setAdapter(adapter);
        }
//        initMiniPlayView(view);
//        setViewLoopAndShuffle(loopMusic, shuffle);
//        if (isMyServiceRunning(MyMusicService.class)) {
//            getActivity().bindService(new Intent(getActivity(), MyMusicService.class), serviceConnection, getActivity().BIND_AUTO_CREATE);
//        }
        return view;
    }

//    public boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) mainFragmentActivity.getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }

//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            MyMusicService.MyBinder binder = (MyMusicService.MyBinder) service;
//            myMusicService = binder.getMyService();
//            myMusicService.getMediaController().setShuffle(shuffle);
//            myMusicService.getMediaController().setLoop(loopMusic);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            myMusicService = null;
//            myMusicService.setPlaying(false);
//        }
//    };

//    private void setViewLoopAndShuffle(int loopMusic, boolean shuffle) {
//        switch (loopMusic) {
//            case 1:
//                btnNoLoop.setVisibility(View.VISIBLE);
//                btnLoopOne.setVisibility(View.INVISIBLE);
//                btnLoopAll.setVisibility(View.INVISIBLE);
//                break;
//            case 2:
//                btnNoLoop.setVisibility(View.INVISIBLE);
//                btnLoopOne.setVisibility(View.VISIBLE);
//                btnLoopAll.setVisibility(View.INVISIBLE);
//                break;
//            case 3:
//                btnNoLoop.setVisibility(View.INVISIBLE);
//                btnLoopOne.setVisibility(View.INVISIBLE);
//                btnLoopAll.setVisibility(View.VISIBLE);
//                break;
//        }
//        if (shuffle) {
//            btnShuffle.setVisibility(View.VISIBLE);
//            btnConiuous.setVisibility(View.INVISIBLE);
//        } else {
//            btnShuffle.setVisibility(View.INVISIBLE);
//            btnConiuous.setVisibility(View.VISIBLE);
//        }
//    }

//    private void initMiniPlayView(View view) {
//        btnShuffle = (ImageView) view.findViewById(R.id.iv_continuous);
//        btnConiuous = (ImageView) view.findViewById(R.id.iv_no_continuous);
//        btnPlay = (ImageView) view.findViewById(R.id.iv_play);
//        btnPause = (ImageView) view.findViewById(R.id.iv_pause);
//        btnPrevious = (ImageView) view.findViewById(R.id.iv_previous);
//        btnNext = (ImageView) view.findViewById(R.id.iv_next);
//        btnLoopAll = (ImageView) view.findViewById(R.id.iv_loop_all);
//        btnLoopOne = (ImageView) view.findViewById(R.id.iv_loop_one);
//        btnNoLoop = (ImageView) view.findViewById(R.id.iv_no_loop);
//        btnShuffle.setOnClickListener(this);
//        btnConiuous.setOnClickListener(this);
//        btnLoopAll.setOnClickListener(this);
//        btnLoopOne.setOnClickListener(this);
//        btnNoLoop.setOnClickListener(this);
//        btnNext.setOnClickListener(this);
//        btnPrevious.setOnClickListener(this);
//        btnPlay.setOnClickListener(this);
//        btnPause.setOnClickListener(this);
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_continuous:
//                shuffle = false;
//                mainFragmentActivity.getMyMusicService().getMediaController().setShuffle(shuffle);
//                sharedPreferences.edit().putBoolean(CommonValue.KEY_SHUFFLE, shuffle).commit();
//                btnConiuous.setVisibility(View.VISIBLE);
//                btnShuffle.setVisibility(View.INVISIBLE);
//                break;
//            case R.id.iv_no_continuous:
//                shuffle = true;
//                mainFragmentActivity.getMyMusicService().getMediaController().setShuffle(shuffle);
//                sharedPreferences.edit().putBoolean(CommonValue.KEY_SHUFFLE, shuffle).commit();
//                btnConiuous.setVisibility(View.INVISIBLE);
//                btnShuffle.setVisibility(View.VISIBLE);
//                break;
//            case R.id.iv_play:
//                Intent intentPlay = new Intent(CommonValue.ACTION_PAUSE_SONG);
//                mainFragmentActivity.sendBroadcast(intentPlay);
//                btnPlay.setVisibility(View.INVISIBLE);
//                btnPause.setVisibility(View.VISIBLE);
//                break;
//            case R.id.iv_pause:
//                Intent intentPause = new Intent(CommonValue.ACTION_PAUSE_SONG);
//                mainFragmentActivity.sendBroadcast(intentPause);
//                btnPause.setVisibility(View.INVISIBLE);
//                btnPlay.setVisibility(View.VISIBLE);
//                break;
//            case R.id.iv_previous:
//                Intent intentPrevious = new Intent(CommonValue.ACTION_PREVIOUS);
//                mainFragmentActivity.sendBroadcast(intentPrevious);
//                break;
//            case R.id.iv_next:
//                Intent intentNext = new Intent(CommonValue.ACTION_NEXT);
//                mainFragmentActivity.sendBroadcast(intentNext);
//                break;
//            case R.id.iv_loop_all:
//                loopMusic = 1;
//                mainFragmentActivity.getMyMusicService().getMediaController().setLoop(loopMusic);
//                sharedPreferences.edit().putInt(CommonValue.KEY_LOOP_MUSIC, loopMusic).commit();
//                btnLoopAll.setVisibility(View.INVISIBLE);
//                btnLoopOne.setVisibility(View.INVISIBLE);
//                btnNoLoop.setVisibility(View.VISIBLE);
//                break;
//            case R.id.iv_loop_one:
//                loopMusic = 3;
//                mainFragmentActivity.getMyMusicService().getMediaController().setLoop(loopMusic);
//                sharedPreferences.edit().putInt(CommonValue.KEY_LOOP_MUSIC, loopMusic).commit();
//                btnLoopAll.setVisibility(View.VISIBLE);
//                btnLoopOne.setVisibility(View.INVISIBLE);
//                btnNoLoop.setVisibility(View.INVISIBLE);
//                break;
//            case R.id.iv_no_loop:
//                loopMusic = 2;
//                sharedPreferences.edit().putInt(CommonValue.KEY_LOOP_MUSIC, loopMusic).commit();
//                mainFragmentActivity.getMyMusicService().getMediaController().setLoop(loopMusic);
//                btnLoopAll.setVisibility(View.INVISIBLE);
//                btnLoopOne.setVisibility(View.VISIBLE);
//                btnNoLoop.setVisibility(View.INVISIBLE);
//                break;
//        }
//    }
}
