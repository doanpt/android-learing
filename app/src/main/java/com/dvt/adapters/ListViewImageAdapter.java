package com.dvt.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dvt.imagesearch.DetailActivity;
import com.dvt.imagesearch.ImageLoader;
import com.dvt.imagesearch.MainActivity;
import com.dvt.imagesearch.R;

import java.util.ArrayList;

/**
 * Created by doantrung on 10/22/15.
 */
public class ListViewImageAdapter extends BaseAdapter {
    private Activity activity;
    public ArrayList<Object> listImages;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;

    public ListViewImageAdapter(Activity a, ArrayList<Object> listImages) {
        activity = a;
        this.listImages = listImages;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return listImages.size();
    }

    public Object getItem(int position) {
        return listImages.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public ImageView imgViewImage;
        public TextView txtViewTitle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.layout_item_listview, null);
            holder = new ViewHolder();
            holder.imgViewImage = (ImageView) view.findViewById(R.id.iv_item_image);
            holder.txtViewTitle = (TextView) view.findViewById(R.id.tv_item_title);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        ItemImage imageBean = (ItemImage) listImages.get(position);
        holder.imgViewImage.setTag(imageBean.getThumbUrl());
        imageLoader.DisplayImage(imageBean.getThumbUrl(), activity, holder.imgViewImage);
        holder.txtViewTitle.setText(Html.fromHtml(imageBean.getTitleImage()));
        return view;
    }
}