package com.dvt.samsung.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.utils.OnPlayMusic;

import java.util.ArrayList;

/**
 * Created by sev_user on 11/8/2016.
 */

public class SongBaseAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Song> arrSong;
    private OnPlayMusic onPlayMusic;

    public SongBaseAdapter(Context context, ArrayList<Song> arr, OnPlayMusic onPlayMusic) {
        this.context = context;
        this.arrSong = arr;
        inflater = LayoutInflater.from(context);
        this.onPlayMusic = onPlayMusic;
    }

    @Override
    public int getCount() {
        return arrSong.size();
    }

    @Override
    public Object getItem(int position) {
        return arrSong.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_item_song_display, parent, false);
            viewHolder.ivSong = (ImageView) convertView.findViewById(R.id.iv_icon_song);
            viewHolder.tvArtist = (TextView) convertView.findViewById(R.id.tv_artist_song);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.tv_name_song);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_main_song);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Song song = arrSong.get(position);
        viewHolder.txtName.setText(song.getName());
        viewHolder.tvArtist.setText(song.getArtist());
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.music_icon, opts);
        viewHolder.ivSong.setImageBitmap(bitmap);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayMusic.playSong(arrSong, position);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView ivSong;
        TextView txtName;
        TextView tvArtist;
        LinearLayout linearLayout;
    }
}
