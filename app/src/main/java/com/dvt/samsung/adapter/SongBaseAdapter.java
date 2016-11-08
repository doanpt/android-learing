package com.dvt.samsung.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.model.Song;

import java.util.ArrayList;

/**
 * Created by sev_user on 11/8/2016.
 */

public class SongBaseAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Song> arrSong;

    public SongBaseAdapter(Context context, ArrayList<Song> arr) {
        this.context = context;
        this.arrSong = arr;
        inflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_item_song_display, parent, false);
            viewHolder.ivSong = (ImageView) convertView.findViewById(R.id.iv_icon_song);
            viewHolder.tvArtist = (TextView) convertView.findViewById(R.id.tv_artist_song);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.tv_name_song);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Song song=arrSong.get(position);
        viewHolder.txtName.setText(song.getName());
        viewHolder.tvArtist.setText(song.getArtist());
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.music_icon, opts);
        viewHolder.ivSong.setImageBitmap(bitmap);
        return convertView;
    }

    private class ViewHolder {
        ImageView ivSong;
        TextView txtName;
        TextView tvArtist;
    }
}
