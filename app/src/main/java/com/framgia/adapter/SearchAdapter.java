package com.framgia.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.ytbsearch.MainActivity;
import com.framgia.ytbsearch.PlayVideoActivity;
import com.framgia.ytbsearch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by doantrung on 11/19/15.
 */
public class SearchAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<VideoItem> listVideos;
    private static LayoutInflater inflater = null;

    public SearchAdapter(Context context, ArrayList<VideoItem> listVideos) {
        this.context = context;
        this.listVideos = listVideos;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return listVideos.size();
    }

    public Object getItem(int position) {
        return listVideos.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView description;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.layout_video_item, parent, false);
            holder = new ViewHolder();
            holder.thumbnail = (ImageView) view.findViewById(R.id.video_thumbnail);
            holder.title = (TextView) view.findViewById(R.id.video_title);
            holder.description = (TextView) view.findViewById(R.id.video_description);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        final VideoItem videoItem = (VideoItem) listVideos.get(position);
        Picasso.with(context).load(videoItem.getThumbnailURL()).into(holder.thumbnail);
        holder.title.setText(videoItem.getTitle());
        holder.description.setText(videoItem.getChannelTitle() + "--" + videoItem.getPublishedAt());
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra(MainActivity.ID_VIDEO, videoItem.getId());
                context.startActivity(intent);
            }
        });
        return view;
    }
}