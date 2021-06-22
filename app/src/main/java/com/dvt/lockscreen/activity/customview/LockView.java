package com.dvt.lockscreen.activity.customview;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dvt.lockscreen.activity.R;


/**
 * Created by sev_user on 10/10/2017.
 */

public class LockView extends RelativeLayout {
    private Context mContext;
    private WindowManager windowManager;

    public LockView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = inflate(mContext, R.layout.my_view_layout, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Button btnUnLock = (Button) view.findViewById(R.id.btn_unlock);
        btnUnLock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                offLockScreen();
            }
        });
        addView(view);
    }

    public void onLockScreen() {
        windowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        if (!this.isShown()) {
            windowManager.addView(this, getLayoutParams());
            setSystemUiVisibility(getSystemUiVisibility());
        }
    }

    public void offLockScreen() {
        if (this.isShown())
            windowManager.removeView(this);
    }

    public WindowManager.LayoutParams getLayoutParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        //   params.type = WindowManager.LayoutParams.TYPE_PHONE;
        //params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        params.flags = WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
        params.flags &= -WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        params.flags |= WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

        params.flags &= -WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.flags |= WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

//        TRANSLUCENT System chooses a format that supports translucency (many alpha bits) hỗ trợ mờ
//        TRANSLUCENT System chooses a format that supports transparency

        params.format = PixelFormat.TRANSPARENT;
        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        return params;

        //flags quy định những thuộc tính của view như focus. có thể ra ngoài màn hình không. có cho sử dụng bàn phím cứng khi dùng màn hình này không.
//      FLAG_LAYOUT_NO_LIMITS: khong bi gioi han boi man hinh
//      FLAG_NOT_FOCUSABLE Su duoc nut home, k cần focus lên view
//      FLAG_WATCH_OUTSIDE_TOUCH quy định view có lắng nghe sự kiện touch bên ngoài view không
//      FLAG_NOT_TOUCH_MODAL: cho phép bất kỳ sự kiện touch nào bên ngoài cửa sổ được gửi đến các cửa sổ phía sau cửa sổ này. Nếu không set cờ này, nó sẽ tiêu thụ tất cả các sự kiện con trỏ, bất kể touch trong cửa sổ hay không.
//      FLAG_NOT_TOUCHABLE:cửa sổ này không bao giờ có thể nhận sự kiện touch.
//      FLAG_SHOW_WHEN_LOCKED:cho phép hiển thị kể cả khi màn hình khóa
//      FLAG_HARDWARE_ACCELERATED sử dùng phần cứng

//        TYPE_APPLICATION:Loại cửa sổ: một cửa sổ ứng dụng bình thường.
//        TYPE_PHONE :Deprecated API 26. đối với các ứng dụng không thuộc hệ thống. Sử dụng TYPE_APPLICATION_OVERLAY thay thế.Các cửa sổ này thường được đặt phía trên tất cả các ứng dụng, nhưng phía sau thanh trạng thái.
//        TYPE_SYSTEM_ALERT: Deprecate in API 26. ,đối với các ứng dụng không thuộc hệ thống. Sử dụng TYPE_APPLICATION_OVERLAYthay thế.
//        Loại cửa sổ: cửa sổ hệ thống, Các cửa sổ này luôn ở trên cùng của các cửa sổ ứng dụng
//        TYPE_SYSTEM_ERROR:Deprecated API 26 Loại cửa sổ: cửa sổ lỗi hệ thống nội bộ, xuất hiện trên tất cả mọi thứ có thể.
//        TYPE_SYSTEM_OVERLAY:Deprecated API 26 Loại cửa sổ: cửa sổ lớp phủ hệ thống, cần được hiển thị trên mọi thứ khác.
//        TYPE_TOAST:Deprecated API 26 Loại cửa sổ: thông báo tức thời
//        TYPE_APPLICATION_MEDIA:Loại cửa sổ: cửa sổ hiển thị phương tiện (chẳng hạn như video).


//        return new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT, 0, 0,
//                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
//                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
//                PixelFormat.TRANSPARENT);


    }

    public int getSystemUiVisibility() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }
}