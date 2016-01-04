package com.dvt.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dvt.imagesearch.DownloadDialog;
import com.dvt.imagesearch.ImageLoader;
import com.dvt.imagesearch.R;
import com.dvt.imagesearch.TouchImageView;

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
    private DownloadDialog downloadDialog;

    public ViewPagerAdapter(Activity activity, ArrayList<ItemImage> listImage) {
        this.activity = activity;
        arrPage = listImage;
        mLayoutInflate = LayoutInflater.from(activity);
        imageLoader = new ImageLoader(this.activity.getApplicationContext());
        downloadDialog = new DownloadDialog(activity);
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
        ivDetail.setTag(arrPage.get(position).getLinkImageFull());
        imageLoader.DisplayImage(arrPage.get(position).getLinkImageFull(), activity, ivDetail);
        ivDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadDialog.setLinkDownload(arrPage.get(position).getLinkImageFull());
                downloadDialog.setContext(activity);
                downloadDialog.show();
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}