package com.framgia.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.ytbsearch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by doantrung on 12/8/15.
 */
public class ListPlayListPageAdapter extends PagerAdapter{
    private ArrayList<ItemPlayListPage> arrPage = new ArrayList<ItemPlayListPage>();
    private LayoutInflater mLayoutInflate;
    protected Context mContext;

    public ListPlayListPageAdapter(Context context, ArrayList<ItemPlayListPage> listPlayList) {
        mContext = context;
        mLayoutInflate = LayoutInflater.from(context);
        arrPage = listPlayList;
    }

    @Override
    public int getCount() {
        return arrPage.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflate.inflate(R.layout.layout_playlist_page, container, false);
        ImageView ivImagePlaylist = (ImageView) view.findViewById(R.id.iv_img_playlist);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title_playlist);
        TextView tvAuthor = (TextView) view.findViewById(R.id.tv_author_playlist);
        TextView tvDate = (TextView) view.findViewById(R.id.tv_date_playlist);
        TextView tvVideoNumber = (TextView) view.findViewById(R.id.tv_video_count_playlist);
        ItemPlayListPage item = arrPage.get(position);
        Picasso.with(mContext).load(item.getThumbnailURL()).into(ivImagePlaylist);
        tvTitle.setText(item.getTitle());
        tvAuthor.setText(item.getChannelTitle());
        tvDate.setText(item.getPublishedAt());
        tvVideoNumber.setText(item.getNumberVideo());
        container.addView(view);
        return view;
    }
}