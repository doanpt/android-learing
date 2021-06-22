package musicplayer.group3.dev.musicplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import musicplayer.group3.dev.musicplayer.common.Const;
import musicplayer.group3.dev.musicplayer.fragment.AlbumFragment;
import musicplayer.group3.dev.musicplayer.fragment.ArtistFragment;
import musicplayer.group3.dev.musicplayer.fragment.SongFragment;

/**
 * Created by sev_user on 12/15/2016.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private final int numOfTab;

    public PagerAdapter(FragmentManager fm, int numOfTab) {
        super(fm);
        this.numOfTab = numOfTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Const.DEFAULT_VALUE_INT_0:
                return SongFragment.getInstance();
            case Const.DEFAULT_VALUE_INT_1:
                return AlbumFragment.getInstance();
            case Const.DEFAULT_VALUE_INT_2:
                return ArtistFragment.getInstance();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
