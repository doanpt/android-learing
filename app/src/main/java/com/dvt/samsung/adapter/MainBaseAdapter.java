package com.dvt.samsung.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.model.TypeItem;

import java.util.ArrayList;

/**
 * Created by sev_user on 11/8/2016.
 */

public class MainBaseAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<TypeItem> arrSong;

    public MainBaseAdapter(Context context, ArrayList<TypeItem> arr) {
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
            convertView = inflater.inflate(R.layout.layout_item_main, parent, false);
//            viewHolder.ivSong = (ImageView) convertView.findViewById(R.id.iv_icon_type);
            viewHolder.tvArtist = (TextView) convertView.findViewById(R.id.tv_name_type);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.tv_number_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TypeItem song=arrSong.get(position);
        viewHolder.txtName.setText(song.getNameType());
        viewHolder.tvArtist.setText(song.getNumber());
        return convertView;
    }

    private class ViewHolder {
        ImageView ivSong;
        TextView txtName;
        TextView tvArtist;
    }
}
