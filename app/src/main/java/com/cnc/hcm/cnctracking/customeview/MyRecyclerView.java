package com.cnc.hcm.cnctracking.customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.cnc.hcm.cnctracking.R;

/**
 * Created by giapmn on 1/18/18.
 */

public class MyRecyclerView extends RecyclerView {

    private Paint paint = new Paint();
    private String msg;
    private float x, y;

    public MyRecyclerView(Context context) {
        super(context);
        init();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        msg = getResources().getString(R.string.no_data);
        paint.setTextSize(40);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        Rect rect = new Rect();
        paint.getTextBounds(msg, 0, msg.length(), rect);
        x = getResources().getDisplayMetrics().widthPixels;
        y = getResources().getDisplayMetrics().heightPixels;
        x = (x - rect.width()) / 2;
        y = (y - rect.height()) / 3;

    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (getChildCount() == 0) {
            c.drawText(msg, x, y, paint);
        }
    }
}
