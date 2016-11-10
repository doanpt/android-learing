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
    private ImageView ivSearch;
    private TextView tvName;
    private SearchView editSearch;

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
//                editSearch.requestFocus();
                editSearch.setActivated(true);
                editSearch.setIconifiedByDefault(true);
            }
        });
        tvName = (TextView) mainFragmentActivity.findViewById(R.id.tv_name_viewpager);
        editSearch = (SearchView) mainFragmentActivity.findViewById(R.id.edt_search);
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
        return view;
    }
}
