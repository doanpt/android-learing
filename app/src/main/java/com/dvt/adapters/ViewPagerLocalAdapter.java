package com.dvt.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dvt.imagesearch.ImageLoader;
import com.dvt.imagesearch.R;
import com.dvt.imagesearch.TouchImageView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by doantrung on 11/2/15.
 */
public class ViewPagerLocalAdapter extends PagerAdapter implements Serializable {
    private ArrayList<ItemImageLocal> arrPage;
    private LayoutInflater mLayoutInflate;
    private ImageLoader imageLoader;
    private Activity activity;

    public ViewPagerLocalAdapter(Activity activity, ArrayList<ItemImageLocal> listImage) {
        arrPage = listImage;
        this.activity = activity;
        mLayoutInflate = LayoutInflater.from(activity);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    @Override
    public int getCount() {
        return arrPage.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mLayoutInflate.inflate(R.layout.layout_item_detail_image, container, false);
        TouchImageView ivDetail = (TouchImageView) view.findViewById(R.id.iv_detail_image);
        ivDetail.setTag(arrPage.get(position).getImagePath());
        imageLoader.DisplayImageFromPath(arrPage.get(position).getImagePath(), activity, ivDetail);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
