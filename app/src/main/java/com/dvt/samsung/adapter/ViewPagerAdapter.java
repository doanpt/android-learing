package com.dvt.samsung.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dvt.samsung.fragment.AlbumFragment;
import com.dvt.samsung.fragment.ArtistFragment;
import com.dvt.samsung.fragment.DownloadFragment;
import com.dvt.samsung.fragment.PlaylistFragment;
import com.dvt.samsung.fragment.SongFragment;

import java.util.ArrayList;

/**
 * Created by Android on 11/7/2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
//    private String[] titles = new String[]{"Bài hát", "Album", "Nghệ sĩ", "Playlist", "Download"};
    private String[] titles = new String[]{"Bài hát", "Album", "Nghệ sĩ"};

    public ViewPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments = new ArrayList<>();
        fragments.add(new SongFragment(context));
        fragments.add(new AlbumFragment(context));
        fragments.add(new ArtistFragment(context));
//        fragments.add(new PlaylistFragment(context));
//        fragments.add(new DownloadFragment(context));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
