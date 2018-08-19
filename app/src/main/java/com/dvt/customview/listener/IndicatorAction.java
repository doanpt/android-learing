package com.dvt.customview.listener;

import android.support.v4.view.ViewPager;

/**
 * Created by Android on 19/08/2018.
 */

public interface IndicatorAction {
    void setViewPager(ViewPager viewPager) throws Exception;

    void setRadiusSelected(int radius);

    void setRadiusUnselected(int radius);

    void setDotDistance(int distance);

}
