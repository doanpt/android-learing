package musicplayer.group3.dev.musicplayer.fragment;


import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.activity.DetailArtistActivity;
import musicplayer.group3.dev.musicplayer.activity.MainActivity;
import musicplayer.group3.dev.musicplayer.adapter.AlbumAdapter;
import musicplayer.group3.dev.musicplayer.media.MediaManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumOfAritstFragment extends Fragment {


    private static final String TAG = "AlbumFragment";
    private static AlbumOfAritstFragment albumOfAritstFragment;

    //initView
    private ListView listView;
    private LinearLayout llAlbumEmpty;


    private DetailArtistActivity artistActivity;
    private AlbumAdapter albumAdapter;

    public static AlbumOfAritstFragment getInstance() {
        if (albumOfAritstFragment == null) {
            return new AlbumOfAritstFragment();
        }
        return albumOfAritstFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_of_artist, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.lv_album_of_artist);
        listView.setAdapter(albumAdapter);

        llAlbumEmpty = (LinearLayout) view.findViewById(R.id.ll_list_album_empty);
        int size = albumAdapter.getCount();
        if (size == 0) {
            llAlbumEmpty.setVisibility(View.VISIBLE);
        } else {
            llAlbumEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.artistActivity = (DetailArtistActivity) getActivity();
        albumAdapter = new AlbumAdapter(artistActivity, R.layout.item_album_of_artist);
        albumAdapter.setArrItemAlbum(MediaManager.getInstance(artistActivity)
                .getAllAlbums(MediaStore.Audio.Albums.ARTIST + " =?",
                        new String[]{artistActivity.getNameArtist() + ""}));
    }

}
