package musicplayer.group3.dev.musicplayer.listener;

import java.util.List;

import musicplayer.group3.dev.musicplayer.item.ItemSong;

/**
 * Created by sev_user on 4/13/2017.
 */

public interface OnPlayMusic {
    void playSong(List<ItemSong> list,int position);
}
