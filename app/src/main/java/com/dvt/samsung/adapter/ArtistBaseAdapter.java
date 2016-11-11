package com.dvt.samsung.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.model.Song;
import com.dvt.samsung.listener.OnListListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by sev_user on 11/8/2016.
 */

public class ArtistBaseAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> names = new ArrayList<>();
    private HashMap<String, List<Song>> artist;
    private OnListListener listListener;

    public ArtistBaseAdapter(Context context, HashMap<String, List<Song>> artist, OnListListener listListener) {
        this.context = context;
        this.artist = artist;
        Set<String> setNames = artist.keySet();
        Iterator<String> iterator = setNames.iterator();
        while (iterator.hasNext()) {
            names.add(iterator.next());
        }
        inflater = LayoutInflater.from(context);
        this.listListener = listListener;
    }

    @Override
    public int getCount() {
        return artist.size();
    }

    @Override
    public Object getItem(int position) {
        return artist.get(position);
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
            convertView = inflater.inflate(R.layout.layout_item_artist_display, parent, false);
            viewHolder.ivArtist = (ImageView) convertView.findViewById(R.id.iv_artist_icon);
            viewHolder.tvArtist = (TextView) convertView.findViewById(R.id.tv_artist_song);
            viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number_song_artist);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_item_artist);
            viewHolder.linearLayout.setBackgroundResource(R.drawable.bkg_listview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        List<Song> list = artist.get(names.get(position));
        viewHolder.tvArtist.setText(names.get(position).toString() + " ");
        viewHolder.tvNumber.setText(String.valueOf(list.size()) + " bài hát");
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inSampleSize = 4;
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.music_song_icon, opts);
//        viewHolder.ivArtist.setImageBitmap(bitmap);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listListener.onItemClick(artist.get(names.get(position)));
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView ivArtist;
        TextView tvArtist;
        TextView tvNumber;
        LinearLayout linearLayout;
    }
}
