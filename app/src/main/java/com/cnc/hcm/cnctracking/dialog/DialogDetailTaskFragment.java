package com.cnc.hcm.cnctracking.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private LinearLayout llImageService, llFindWay;
    private RelativeLayout rlDetail;
    private ImageView imvBack, imvImageService, imvTime, imvDistance;
    private TextView tvDetailTask;
    private ViewPager viewPager;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom_sheet, container, false);
        initViews(view);
        return view;
    }

    private void iniObject() {

        Fragment[] listFragment = new Fragment[]{new TaskDetailFragment(), new ProductRepairFragment()};
        fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), listFragment);

        CommonMethod.makeToast(getContext(), "ID: " + idTask);

    }

    private void initViews(View view) {
        llImageService = (LinearLayout) view.findViewById(R.id.ll_image_service);
        imvBack = (ImageView) view.findViewById(R.id.imv_back_detail_task_dialog_bottom);
        imvBack.setOnClickListener(this);
        imvImageService = (ImageView) view.findViewById(R.id.imv_image_service);
        imvTime = (ImageView) view.findViewById(R.id.imv_time);
        imvDistance = (ImageView) view.findViewById(R.id.imv_distance);
        rlDetail = (RelativeLayout) view.findViewById(R.id.rl_detail_task);

        tvDetailTask = (TextView) view.findViewById(R.id.tv_detail_task);
        tvDetailTask.setOnClickListener(this);
        llFindWay = (LinearLayout) view.findViewById(R.id.ll_find_way);
        llFindWay.setOnClickListener(this);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
//        viewPager.setAdapter(new SimpleAdapter(fragmentManager));
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
                setExpaned(true);
                showExpaned();
                setExpaned(false);
                break;
            case R.id.ll_find_way:
                CommonMethod.actionFindWayInMapApp(getContext(), 0, 0, 0, 0);
                break;
        }
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public class SimpleAdapter extends FragmentPagerAdapter {

        public SimpleAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new TaskDetailFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}
