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
import com.dvt.samsung.utils.OnListListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by sev_user on 11/8/2016.
 */

public class AlbumBaseAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> names = new ArrayList<>();
    private HashMap<String, List<Song>> albums;
    private OnListListener listListener;

    public AlbumBaseAdapter(Context context, HashMap<String, List<Song>> albums, OnListListener listListener) {
        this.context = context;
        this.albums = albums;
        Set<String> setnames = albums.keySet();
        Iterator<String> iterator = setnames.iterator();
        while (iterator.hasNext()) {
            names.add(iterator.next());
        }
        inflater = LayoutInflater.from(context);
        this.listListener = listListener;
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position);
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
            convertView = inflater.inflate(R.layout.layout_item_album_display, parent, false);
            viewHolder.ivAlbum = (ImageView) convertView.findViewById(R.id.iv_album_icon);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name_album);
            viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number_song_album);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_item_album);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        List<Song> list = albums.get(names.get(position));
        viewHolder.tvName.setText(names.get(position));
        viewHolder.tvNumber.setText(String.valueOf(list.size()) + " bài hát");
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.music_icon, opts);
        viewHolder.ivAlbum.setImageBitmap(bitmap);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listListener.onItemClick(albums.get(names.get(position)));

            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView ivAlbum;
        TextView tvName;
        TextView tvNumber;
        LinearLayout linearLayout;
    }
}
