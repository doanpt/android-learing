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

public class AlbumFragment extends Fragment {
    private View view;
    private ListView lvAlbum;
    private Context context;
    private AlbumBaseAdapter albumBaseAdapter;
//    private ImageView ivSearch;
//    private TextView tvName;
//    private SearchView editSearch;

    @SuppressLint("ValidFragment")
    public AlbumFragment(Context context) {
        this.context = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.fragment_album, null);
    }

    public AlbumFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lvAlbum = (ListView) view.findViewById(R.id.lv_album);
        if (MainFragmentActivity.albums != null) {
            albumBaseAdapter = new AlbumBaseAdapter(context, MainFragmentActivity.albums, new OnListListener() {
                @Override
                public void onItemClick(int pos) {

                }

                @Override
                public void onItemClick(List<Song> songs) {
                    Intent intent = new Intent(getActivity(), ShowListSongActivity.class);
                    intent.putParcelableArrayListExtra(CommonValue.KEY_LIST_SONG_CLICK, (ArrayList<? extends Parcelable>) songs);
                    intent.putExtra(CommonValue.KEY_FROM_SHOW_LIST, 0);
                    startActivity(intent);
                }
            });
            lvAlbum.setAdapter(albumBaseAdapter);
        }
        //        ivSearch = (ImageView) mainFragmentActivity.findViewById(R.id.im_other_viewpager);
//        ivSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvName.setVisibility(View.INVISIBLE);
//                editSearch.setVisibility(View.VISIBLE);
//            }
//        });
//        tvName = (TextView) mainFragmentActivity.findViewById(R.id.tv_name_viewpager);
//        editSearch = (SearchView) mainFragmentActivity.findViewById(R.id.edt_search);
//        editSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                in.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
//                tvName.setVisibility(View.VISIBLE);
//                editSearch.setVisibility(View.INVISIBLE);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                albumsList = MainFragmentActivity.albums;
//                albumBaseAdapter = new AlbumBaseAdapter(context, albumsList, new OnListListener() {
//                    @Override
//                    public void onItemClick(int pos) {
//
//                    }
//
//                    @Override
//                    public void onItemClick(List<Song> songs) {
//                        Intent intent = new Intent(getActivity(), ShowListSongActivity.class);
//                        intent.putParcelableArrayListExtra(CommonValue.KEY_LIST_SONG_CICK, (ArrayList<? extends Parcelable>) songs);
//                        intent.putExtra(CommonValue.KEY_FROM_SHOW_LIST, 0);
//                        startActivity(intent);
//                    }
//                });
//                lvAlbum.setAdapter(albumBaseAdapter);
//                albumBaseAdapter.getFilter().filter(newText.toLowerCase().trim());
//                albumBaseAdapter.notifyDataSetChanged();
//                return true;
//            }
//        });
        return view;
    }

}