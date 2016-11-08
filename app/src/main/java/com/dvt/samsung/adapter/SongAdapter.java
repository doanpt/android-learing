package com.dvt.samsung.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dvt.samsung.finalapp.MainFragmentActivity;
import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sev_user on 11/3/2016.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private ArrayList<Song> arrSong;
    private Context context;

    public SongAdapter(ArrayList<Song> arrSong, Context context) {
        this.arrSong = arrSong;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SongAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_song_display, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Song item = arrSong.get(position);
        String name = item.getName();
        String artist = item.getArtist();
        holder.tvTitle.setText(name);
        holder.tvArtist.setText(artist);
//        Picasso.with(context).load(R.drawable.music_icon).into(holder.iv);
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainFragmentActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrSong.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvArtist;
        private ImageView ivSong;
        private LinearLayout llMain;

        public ViewHolder(View view) {
            super(view);
            tvArtist = (TextView) view.findViewById(R.id.tv_artist_song);
            tvTitle = (TextView) view.findViewById(R.id.tv_name_song);
//            ivSong = (ImageView) view.findViewById(R.id.iv_icon_song);
            llMain = (LinearLayout) view.findViewById(R.id.ll_main_song);
        }
    }

}
