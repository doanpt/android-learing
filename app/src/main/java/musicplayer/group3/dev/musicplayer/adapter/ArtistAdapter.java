package musicplayer.group3.dev.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.item.ItemArtist;
import musicplayer.group3.dev.musicplayer.listener.OnPlayMusic;

/**
 * Created by sev_user on 12/19/2016.
 */

public class ArtistAdapter extends BaseAdapter {

    private ArrayList<ItemArtist> arrItemArtist = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    public ArtistAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrItemArtist.size();
    }

    @Override
    public ItemArtist getItem(int position) {
        return arrItemArtist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_song, null);
            holder = new ViewHolder();
            holder.imvImageArtist = (ImageView) view.findViewById(R.id.imv_item_image_song);
            holder.tvNameArtist = (TextView) view.findViewById(R.id.tv_item_name_song);
            holder.tvInforArtist = (TextView) view.findViewById(R.id.tv_item_artist_song);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ItemArtist item = arrItemArtist.get(position);
        // Bitmap bitmap = item.getImageSong(context);
//        if (bitmap == null) {
//            holder.imvImageArtist.setImageResource(R.drawable.ic_item_image_song);
//        } else {
//            holder.imvImageArtist.setImageBitmap(bitmap);
//        }

        holder.tvNameArtist.setText(item.getArtistName());
        holder.tvInforArtist.setText(item.getNumOfAlbums() + " Albums   " + item.getNumofTracks() + " bài hát");
        return view;
    }

    private class ViewHolder {
        ImageView imvImageArtist;
        TextView tvNameArtist;
        TextView tvInforArtist;
    }

    public void setArrItemArtist(ArrayList<ItemArtist> arrItemArtist) {
        this.arrItemArtist = arrItemArtist;
    }
}
