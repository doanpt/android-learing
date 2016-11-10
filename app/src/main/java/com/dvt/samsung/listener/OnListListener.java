package com.dvt.samsung.listener;

import com.dvt.samsung.model.Song;

import java.util.List;

/**
 * Created by Android on 11/8/2016.
 */

public interface OnListListener {
    void onItemClick(int pos);

    void onItemClick(List<Song> songs);
}
