package com.cnc.hcm.cnctracking.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.FragmentAdapter;
import com.cnc.hcm.cnctracking.fragment.ProductRepairFragment;
import com.cnc.hcm.cnctracking.fragment.TaskDetailFragment;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;

import biz.laenger.android.vpbs.ViewPagerBottomSheetDialogFragment;

@SuppressLint("ValidFragment")
public class DialogDetailTaskFragment extends ViewPagerBottomSheetDialogFragment implements View.OnClickListener {

    private ViewPager viewPager;
    private LinearLayout llImageService, llFindWay;
    private RelativeLayout rlDetail;
    private ImageView imvBack, imvImageService, imvTime, imvDistance;
    private TextView tvDetailTask;


    private FragmentAdapter fragmentAdapter;
    private String idTask = Conts.BLANK;

    public DialogDetailTaskFragment() {
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        dialog.setContentView(R.layout.dialog_bottom_sheet);
        iniObject();
        initViews();
    }

    private void iniObject() {
        Fragment[] listFragment = new Fragment[]{new TaskDetailFragment(), new ProductRepairFragment()};
        fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), listFragment);

        CommonMethod.makeToast(getContext(), "ID: " + idTask);

    }

    private void initViews() {
        llImageService = (LinearLayout) getDialog().findViewById(R.id.ll_image_service);
        imvBack = (ImageView) getDialog().findViewById(R.id.imv_back_detail_task_dialog_bottom);
        imvBack.setOnClickListener(this);
        imvImageService = (ImageView) getDialog().findViewById(R.id.imv_image_service);
        imvTime = (ImageView) getDialog().findViewById(R.id.imv_time);
        imvDistance = (ImageView) getDialog().findViewById(R.id.imv_distance);
        rlDetail = (RelativeLayout) getDialog().findViewById(R.id.rl_detail_task);

        tvDetailTask = (TextView) getDialog().findViewById(R.id.tv_detail_task);
        tvDetailTask.setOnClickListener(this);
        llFindWay = (LinearLayout) getDialog().findViewById(R.id.ll_find_way);
        llFindWay.setOnClickListener(this);


        viewPager = (ViewPager) getDialog().findViewById(R.id.view_pager);
//        viewPager.setAdapter(new FragmentAdapter(getFragmentManager()));
//        BottomSheetUtils.setupViewPager(viewPager);
    }

    //TODO edit them at here
    @Override
    public int getTheme() {
        return super.getTheme();
    }

    @Override
    public void onSlideBottomSheet(@NonNull View bottomSheet, float slideOffset) {
        if (slideOffset > 0) {
            llImageService.setVisibility(View.GONE);
            rlDetail.setVisibility(View.GONE);
            imvTime.setVisibility(View.GONE);
            imvDistance.setVisibility(View.GONE);
        } else {
            llImageService.setVisibility(View.VISIBLE);
            rlDetail.setVisibility(View.VISIBLE);
            imvTime.setVisibility(View.VISIBLE);
            imvDistance.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStateChangedBottomSheet(@NonNull View bottomSheet, int newState) {

    }

    @Override
    public void setVisibilityView() {
        Log.d("setVisibilityView", "setVisibilityView: ");
        llImageService.setVisibility(View.GONE);
        rlDetail.setVisibility(View.GONE);
        imvTime.setVisibility(View.GONE);
        imvDistance.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back_detail_task_dialog_bottom:
                dismiss();
                break;
            case R.id.tv_detail_task:
                showExpaned();
                break;
            case R.id.ll_find_way:
                CommonMethod.actionFindWayInMapApp(getContext(), 0, 0, 0, 0);
                break;
        }
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }
}
