package com.dvt.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dvt.imagesearch.ImageLoader;
import com.dvt.imagesearch.R;

import java.util.ArrayList;

/**
 * Created by doantrung on 11/2/15.
 */
public class ListViewLocalAdapter extends BaseAdapter {
    ArrayList<ItemImageLocal> imageList;
    Context context;
    private static LayoutInflater inflater = null;
    private ImageLoader imageLoader;
    private Activity activity;
    public ListViewLocalAdapter(Activity activity, ArrayList<ItemImageLocal> imgList) {
        imageList = imgList;
        context = activity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        TextView imageName;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.layout_item_listview, null);
            holder = new ViewHolder();
            holder.imageName = (TextView) view.findViewById(R.id.tv_item_title);
            holder.imageView = (ImageView) view.findViewById(R.id.iv_item_image);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        ItemImageLocal itemImage = (ItemImageLocal) imageList.get(position);
        holder.imageView.setTag(itemImage.getImagePath());
        imageLoader.DisplayImageFromPath(itemImage.getImagePath(), activity, holder.imageView);
        holder.imageName.setText(Html.fromHtml(itemImage.getImageName()));
        return view;
    }
}
