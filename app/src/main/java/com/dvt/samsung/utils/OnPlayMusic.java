package com.dvt.samsung.utils;

import com.dvt.samsung.model.Song;

import java.util.List;

/**
 * Created by Android on 11/8/2016.
 */

public interface OnPlayMusic {
    void playSong(List<Song> paths, int postion);
}
