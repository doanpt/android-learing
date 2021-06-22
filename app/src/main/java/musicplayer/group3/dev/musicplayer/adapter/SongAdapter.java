package musicplayer.group3.dev.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.item.ItemSong;
import musicplayer.group3.dev.musicplayer.listener.OnPlayMusic;
import musicplayer.group3.dev.musicplayer.media.MediaManager;

/**
 * Created by sev_user on 12/16/2016.
 */

public class SongAdapter extends BaseAdapter{
    private static final String TAG = "SongAdapter";
    private ArrayList<ItemSong> arrItemSong = new ArrayList<>();
    private ArrayList<ItemSong> arrSong = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private OnPlayMusic onPlayMusic;

    public SongAdapter(Context context,OnPlayMusic onPlayMusic) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        arrItemSong = pushData();
        MediaManager.getInstance(context).setArrItemSong(arrItemSong);
        this.onPlayMusic = onPlayMusic;
    }

    @Override
    public int getCount() {
        return arrItemSong.size();
    }

    @Override
    public ItemSong getItem(int position) {
        return arrItemSong.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_song, null);
            holder = new ViewHolder();
            holder.imvImageSong = (ImageView) view.findViewById(R.id.imv_item_image_song);
            holder.imvAnimation = (ImageView) view.findViewById(R.id.imv_animation);
            holder.tvArtist = (TextView) view.findViewById(R.id.tv_item_artist_song);
            holder.tvNameSong = (TextView) view.findViewById(R.id.tv_item_name_song);
            holder.llMain= (LinearLayout) view.findViewById(R.id.ll_item_song);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ItemSong item = arrItemSong.get(position);

        String nameSong = item.getDisplayName();
        if (nameSong.indexOf("-") > 0) {
            nameSong = nameSong.substring(0, nameSong.indexOf("-"));
        } else if (nameSong.indexOf("_") > 0) {
            nameSong = nameSong.substring(0, nameSong.indexOf("_"));
        }
        holder.tvNameSong.setText(nameSong);
        holder.tvArtist.setText(item.getArtist());
        if (holder.llMain.isSelected()) {
            holder.imvAnimation.setBackgroundResource(R.drawable.anim_animaton);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.imvAnimation.getBackground();
            animationDrawable.start();
        } else {
            holder.imvAnimation.setBackgroundColor(Color.blue(R.color.colorPrimary));
        }
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayMusic.playSong(arrItemSong,position);
            }
        });
        return view;
    }

    private class ViewHolder {
        ImageView imvImageSong;
        ImageView imvAnimation;
        TextView tvNameSong;
        TextView tvArtist;
        LinearLayout llMain;
    }

    public void setArrItemSong(ArrayList<ItemSong> arrItemSong) {
        this.arrItemSong = arrItemSong;
    }

    public void setArrSong(ArrayList<ItemSong> arrSong) {
        this.arrSong = arrSong;
    }

    public void updateAnimItem(int position) {
        for (int i = 0; i < arrItemSong.size(); i++) {
            if (arrItemSong.get(i).isSelected()) {
                arrItemSong.get(i).setSelected(false);
            }
        }
        arrItemSong.get(position).setSelected(true);
        notifyDataSetChanged();
    }

    // Filter Class
    public boolean filter(String charText) {
        boolean show = false;
        ArrayList<ItemSong> arrItemSearchTemp = new ArrayList<>();
        arrItemSearchTemp = arrSong;
        charText = charText.toLowerCase(Locale.getDefault());
        arrItemSong.clear();
        if (charText.length() == 0) {
            arrItemSong=pushData();
            show = false;
        } else {
            for (ItemSong item : arrItemSearchTemp) {
                if (item.getDisplayName().toLowerCase(Locale.getDefault())
                        .contains(charText) || item.getArtist().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    arrItemSong.add(item);
                    show = true;
                }
            }
        }
        notifyDataSetChanged();
        return show;
    }

    public ArrayList<ItemSong> pushData() {
        return MediaManager.getInstance(context).getSongList(null, null);
    }

    public void clearArr() {
        arrSong.clear();
    }
}
