package musicplayer.group3.dev.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.activity.DetailAlbumActivity;
import musicplayer.group3.dev.musicplayer.activity.MainActivity;
import musicplayer.group3.dev.musicplayer.common.Const;
import musicplayer.group3.dev.musicplayer.item.ItemAlbums;

/**
 * Created by sev_user on 12/19/2016.
 */

public class AlbumAdapter extends BaseAdapter {
    private final int resource;
    private ArrayList<ItemAlbums> arrItemAlbum = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Context context;

    public AlbumAdapter(Context context, int resource) {
        this.resource = resource;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrItemAlbum.size();
    }

    @Override
    public ItemAlbums getItem(int position) {
        return arrItemAlbum.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.imvImage = (ImageView) view.findViewById(R.id.imv_image_album);
            holder.tvTitleAlbum = (TextView) view.findViewById(R.id.tv_title_album);
            holder.tvTitleNameArtist = (TextView) view.findViewById(R.id.tv_title_name_artist);
            holder.tvTitleAlbum.setSelected(true);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final ItemAlbums item = arrItemAlbum.get(position);
//        Bitmap bitmap = item.getImageSong(context);
//        if (bitmap == null) {
//            holder.imvImage.setImageResource(R.drawable.ic_album);
//        } else {
//            holder.imvImage.setImageBitmap(bitmap);
//            holder.imvImage.postInvalidate();
//        }
        holder.tvTitleNameArtist.setText(item.getAlbumArtist());
        holder.tvTitleAlbum.setText(item.getAlbumName() + " (" + item.getNumOfSong() + ")");
        holder.tvTitleAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + item, Toast.LENGTH_LONG).show();
                //actionShowDetailAlbums(item.getAlbumID(), item.getAlbumName(), item.getAlbumArtist());
            }
        });
        holder.imvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionShowDetailAlbums(item.getAlbumID(), item.getAlbumName(), item.getAlbumArtist());
            }
        });
        return view;
    }

    private void actionShowDetailAlbums(int albumID, String albumName, String albumArtist) {
        Intent intent = new Intent(context, DetailAlbumActivity.class);
        intent.putExtra(Const.KEY_ALBUM_ID, albumID);
        intent.putExtra(Const.KEY_ALBUM_NAME, albumName);
        intent.putExtra(Const.KEY_ALBUM_ARTIST, albumArtist);
        context.startActivity(intent);
    }

    private class ViewHolder {
        ImageView imvImage;
        TextView tvTitleAlbum;
        TextView tvTitleNameArtist;
    }

    public void setArrItemAlbum(ArrayList<ItemAlbums> arrItemAlbum) {
        this.arrItemAlbum = arrItemAlbum;
    }
}
