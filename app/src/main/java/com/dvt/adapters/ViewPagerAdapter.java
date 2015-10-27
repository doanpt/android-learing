package com.dvt.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dvt.imagesearch.ImageLoader;
import com.dvt.imagesearch.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by doantrung on 10/26/15.
 */
public class ViewPagerAdapter extends PagerAdapter implements Serializable {
    private ArrayList<ItemImage> arrPage;
    private LayoutInflater mLayoutInflate;
    private Activity activity;
    private ImageLoader imageLoader;

    public ViewPagerAdapter(Activity activity, ArrayList<ItemImage> listImage) {
        this.activity = activity;
        arrPage = listImage;
        mLayoutInflate = LayoutInflater.from(activity);
        imageLoader = new ImageLoader(this.activity.getApplicationContext());
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
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflate.inflate(R.layout.layout_item_detail_image, container, false);
        ImageView ivDetail = (ImageView) view.findViewById(R.id.iv_detail_image);
        ivDetail.setTag(arrPage.get(position).getLinkImageFull());
        imageLoader.DisplayImage(arrPage.get(position).getLinkImageFull(), activity, ivDetail);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
