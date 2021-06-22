package musicplayer.group3.dev.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.item.ItemSong;
import musicplayer.group3.dev.musicplayer.listener.OnPlayMusic;

/**
 * Created by sev_user on 12/20/2016.
 */

public class TrackAdapter extends BaseAdapter {
    private ArrayList<ItemSong> arrItemTrack = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private OnPlayMusic onPlayMusic;
    public TrackAdapter(Context context,OnPlayMusic playMusic) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        onPlayMusic=playMusic;
    }

    @Override
    public int getCount() {
        return arrItemTrack.size();
    }

    @Override
    public ItemSong getItem(int position) {
        return arrItemTrack.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Holder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_tracks, null);
            holder = new Holder();
            holder.tvTracksName = (TextView) view.findViewById(R.id.tv_title_tracks);
            holder.tvDuration = (TextView) view.findViewById(R.id.tv_duration);
            holder.imvAnim = (ImageView) view.findViewById(R.id.imv_animation_tracks);
            holder.llMain= (LinearLayout) view.findViewById(R.id.ll_main_detail_album);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        ItemSong item = arrItemTrack.get(position);

        String nameSong = item.getDisplayName();
        if (nameSong.indexOf("-") > 0) {
            nameSong = nameSong.substring(0, nameSong.indexOf("-"));
        } else if (nameSong.indexOf("_") > 0) {
            nameSong = nameSong.substring(0, nameSong.indexOf("_"));
        }
        holder.tvTracksName.setText(nameSong);
        holder.tvDuration.setText(convertToDate(item.getDuration()));
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayMusic.playSong(arrItemTrack,position);
            }
        });
        return view;
    }

    private class Holder {
        ImageView imvAnim;
        TextView tvTracksName;
        TextView tvDuration;
        LinearLayout llMain;
    }

    public String convertToDate(Integer value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(new Date(value));
    }

    public void setArrItemTrack(ArrayList<ItemSong> arrItemTrack) {
        this.arrItemTrack = arrItemTrack;
    }
}
