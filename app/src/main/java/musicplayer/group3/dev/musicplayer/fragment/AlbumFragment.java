package musicplayer.group3.dev.musicplayer.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.activity.MainActivity;
import musicplayer.group3.dev.musicplayer.adapter.AlbumAdapter;
import musicplayer.group3.dev.musicplayer.media.MediaManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {


    private static final String TAG = "AlbumFragment";
    private static AlbumFragment albumFragment;

    //initView
    private GridView gridView;
    private LinearLayout llDetailAlbums;


    private MainActivity mainActivity;
    private AlbumAdapter albumAdapter;

    public static AlbumFragment getInstance() {
        if (albumFragment == null) {
            return new AlbumFragment();
        }
        return albumFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() - " + TAG);
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        gridView = (GridView) view.findViewById(R.id.grid_view_album);
        gridView.setAdapter(albumAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() - " + TAG);
        this.mainActivity = (MainActivity) getActivity();
        albumAdapter = new AlbumAdapter(mainActivity, R.layout.item_album);
        albumAdapter.setArrItemAlbum(MediaManager.getInstance(mainActivity).getAllAlbums(null, null));
    }

}
