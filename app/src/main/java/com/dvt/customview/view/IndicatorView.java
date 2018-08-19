package com.dvt.customview.view;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.dvt.customview.R;
import com.dvt.customview.listener.IndicatorAction;
import com.dvt.customview.model.Dot;

/**
 * Created by Android on 19/08/2018.
 */

public class IndicatorView extends View implements IndicatorAction, ViewPager.OnPageChangeListener {

    private static final long DEFAULT_ANIMATE_DURATION = 200;

    private static final int DEFAULT_RADIUS_SELECTED = 20;

    private static final int DEFAULT_RADIUS_UNSELECTED = 15;

    private static final int DEFAULT_DISTANCE = 40;
    private ViewPager viewPager;
    private Dot[] arrDots;
    private long animateDuration = DEFAULT_ANIMATE_DURATION;
    private int radiusSelected = DEFAULT_RADIUS_SELECTED;
    private int radiusUnSelected = DEFAULT_RADIUS_UNSELECTED;
    private int distance = DEFAULT_DISTANCE;

    private int colorSelected;
    private int colorUnSelected;
    private int currentPosition;
    private int beforePosition;
    private ValueAnimator animatorZoomIn;
    private ValueAnimator animationZoomOut;

    public IndicatorView(Context context) {
        super(context);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        this.radiusSelected = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_dt_color_selected, DEFAULT_RADIUS_SELECTED);
        this.radiusUnSelected = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_dt_color_unselected, DEFAULT_RADIUS_UNSELECTED);
        this.distance = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_dt_distance, DEFAULT_DISTANCE);
        this.colorSelected = typedArray.getColor(R.styleable.IndicatorView_dt_color_selected, Color.parseColor("#ffffff"));
        this.colorUnSelected = typedArray.getColor(R.styleable.IndicatorView_dt_color_unselected, Color.parseColor("#ffffff"));
        typedArray.recycle();
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setViewPager(ViewPager viewPager) throws Exception {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        initDot(viewPager.getAdapter().getCount());
        onPageSelected(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desiredHeight = 2 * radiusSelected;
        int width;
        int height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            width = withSize;
        } else if (widthMode == MeasureSpec.EXACTLY) {
            width = withSize;
        } else {
            width = 0;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = desiredHeight;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        float yCenter = getHeight() / 2;

        int d = distance + 2 * radiusUnSelected;
        float firstXCenter = (getWidth() / 2) - ((arrDots.length - 1) * d / 2);
        for (int i = 0; i < arrDots.length; i++) {
            arrDots[i].setCenter(new PointF(firstXCenter + d * i, yCenter));
            arrDots[i].setRadius(i == currentPosition ? radiusSelected : radiusUnSelected);
            arrDots[i].setColor(i == currentPosition ? colorSelected : colorUnSelected);
            arrDots[i].setAlpha(i == currentPosition ? 255 : radiusUnSelected * 255 / radiusSelected);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < arrDots.length; i++) {
            arrDots[i].drawDot(canvas);
        }
    }

    private void initDot(int count) throws Exception {
        if (count < 2) throw new Exception();
        arrDots = new Dot[count];
        for (int i = 0; i < count; i++) {
            arrDots[i] = new Dot();
        }
    }

    @Override
    public void setRadiusSelected(int radius) {
        this.radiusSelected = radius;
    }

    @Override
    public void setRadiusUnselected(int radius) {
        this.radiusUnSelected = radius;
    }

    @Override
    public void setDotDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(final int position) {
        beforePosition = currentPosition;
        currentPosition = position;
        if (beforePosition == currentPosition) {
            beforePosition = currentPosition + 1;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(animateDuration);
        animatorZoomIn = ValueAnimator.ofInt(radiusUnSelected, radiusSelected);
        animatorZoomIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int positionPerform = currentPosition;

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int newRadius = (int) valueAnimator.getAnimatedValue();
                changeNewRadius(positionPerform, newRadius);
            }
        });
        animationZoomOut = new ValueAnimator().ofInt(radiusSelected, radiusUnSelected);
        animationZoomOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int positionPerform = beforePosition;

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int newRadius = (int) valueAnimator.getAnimatedValue();
                changeNewRadius(positionPerform, newRadius);
            }
        });
        animatorSet.play(animatorZoomIn).with(animationZoomOut);
        animatorSet.start();
    }

    private void changeNewRadius(int positionPerform, int newRadius) {
        if (arrDots[positionPerform].getRadius() != newRadius) {
            arrDots[positionPerform].setRadius(newRadius);
            arrDots[positionPerform].setAlpha(newRadius * 255 / radiusSelected);
            invalidate();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
