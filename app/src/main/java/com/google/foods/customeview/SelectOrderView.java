package com.google.foods.customeview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.foods.R;

/**
 * View to allow the selection of a numeric value by pressing plus/minus buttons.  Pressing and holding
 * a button will update the value repeatedly.
 * <p>
 * This view can be configured with a minimum and maximum value.  There is also a label that will
 * display below the current value.
 * </p>
 */
public class SelectOrderView extends RelativeLayout {

    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;

    private boolean plusButtonIsPressed = false;
    private boolean minusButtonIsPressed = false;
    private final long REPEAT_INTERVAL_MS = 100l;

    View rootView;
    TextView valueTextView;
    View minusButton;
    View plusButton;
    LinearLayout llMain;


    Handler handler = new Handler();

    public SelectOrderView(Context context) {
        super(context);
        init(context);
    }

    public SelectOrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectOrderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * Get the current minimum value that is allowed
     *
     * @return
     */
    public int getMinValue() {
        return minValue;
    }

    /**
     * Set the minimum value that will be allowed
     *
     * @param minValue
     */
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    /**
     * Get the current maximum value that is allowed
     *
     * @return
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * Set the maximum value that will be allowed
     *
     * @param maxValue
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Get the current value
     *
     * @return the current value
     */
    public int getValue() {
        return Integer.valueOf(valueTextView.getText().toString());
    }

    /**
     * Set the current value.  If the passed in value exceeds the current min or max, the value
     * will be set to the respective min/max.
     *
     * @param newValue new value
     */
    public void setValue(int newValue) {
        int value = newValue;
        if (newValue < minValue) {
            value = minValue;
        } else if (newValue > maxValue) {
            value = maxValue;
        }

        valueTextView.setText(String.valueOf(value));

        if (mOnValueChangeListener != null) {
            mOnValueChangeListener.onValueChange(value);
        }
    }

    public void setBackgroundColor(int color) {
        llMain.setBackgroundColor(color);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.value_selector, this);
        llMain = (LinearLayout) rootView.findViewById(R.id.ll_main);
        valueTextView = (TextView) rootView.findViewById(R.id.valueTextView);

        minusButton = rootView.findViewById(R.id.minusButton);
        plusButton = rootView.findViewById(R.id.plusButton);

        minusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementValue();
            }
        });
        minusButton.setOnLongClickListener(
                new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View arg0) {
                        minusButtonIsPressed = true;
                        handler.post(new AutoDecrementer());
                        return false;
                    }
                }
        );
        minusButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    minusButtonIsPressed = false;
                }
                return false;
            }
        });

        plusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementValue();
            }
        });
        plusButton.setOnLongClickListener(
                new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View arg0) {
                        plusButtonIsPressed = true;
                        handler.post(new AutoIncrementer());
                        return false;
                    }
                }
        );

        plusButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    plusButtonIsPressed = false;
                }
                return false;
            }
        });
    }

    private void incrementValue() {
        int currentVal = Integer.valueOf(valueTextView.getText().toString());
        if (currentVal < maxValue) {
            valueTextView.setText(String.valueOf(currentVal + 1));
            if (mOnValueChangeListener != null) {
                mOnValueChangeListener.onValueChange(currentVal + 1);
            }
            if (onButtonPlusClickListener != null){
                onButtonPlusClickListener.onClickButtonPlus();
            }
        }
    }

    private void decrementValue() {
        int currentVal = Integer.valueOf(valueTextView.getText().toString());
        if (currentVal > minValue) {
            valueTextView.setText(String.valueOf(currentVal - 1));
            if (mOnValueChangeListener != null) {
                mOnValueChangeListener.onValueChange(currentVal - 1);
            }
            if (onButtonMinusClickListener != null){
                onButtonMinusClickListener.onClickButtonMinus();
            }
        }
    }

    private class AutoIncrementer implements Runnable {
        @Override
        public void run() {
            if (plusButtonIsPressed) {
                incrementValue();
                handler.postDelayed(new AutoIncrementer(), REPEAT_INTERVAL_MS);
            }
        }
    }

    private class AutoDecrementer implements Runnable {
        @Override
        public void run() {
            if (minusButtonIsPressed) {
                decrementValue();
                handler.postDelayed(new AutoDecrementer(), REPEAT_INTERVAL_MS);
            }
        }
    }

    public void setOnValueChangeListener(OnValueChangeListener mOnValueChangeListener) {
        this.mOnValueChangeListener = mOnValueChangeListener;
    }

    private OnValueChangeListener mOnValueChangeListener;

    public interface OnValueChangeListener {
        void onValueChange(int value);
    }

    private OnButtonPlusClickListener onButtonPlusClickListener;
    private OnButtonMinusClickListener onButtonMinusClickListener;

    public interface OnButtonPlusClickListener{
        void onClickButtonPlus();
    }

    public interface OnButtonMinusClickListener{
        void onClickButtonMinus();
    }

    public void setOnButtonPlusClickListener(OnButtonPlusClickListener onButtonPlusClickListener) {
        this.onButtonPlusClickListener = onButtonPlusClickListener;
    }

    public void setOnButtonMinusClickListener(OnButtonMinusClickListener onButtonMinusClickListener) {
        this.onButtonMinusClickListener = onButtonMinusClickListener;
    }
}

