package com.dvt.customview.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by Android on 19/08/2018.
 */

public class Dot {
    private Paint paint;
    private PointF center;
    private int radius;

    public Dot() {
        paint = new Paint();
        paint.setAntiAlias(true);
        center = new PointF();
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public PointF getCenter() {
        return center;
    }

    public void setCenter(PointF center) {
        this.center = center;
    }

    public void drawDot(Canvas canvas) {
        canvas.drawCircle(center.x, center.y, radius, paint);
    }

    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }
}
