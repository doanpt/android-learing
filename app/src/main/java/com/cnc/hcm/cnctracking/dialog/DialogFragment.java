package com.cnc.hcm.cnctracking.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.FragmentAdapter;
import com.cnc.hcm.cnctracking.fragment.ProductRepairFragment;
import com.cnc.hcm.cnctracking.fragment.TaskDetailFragment;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;

import biz.laenger.android.vpbs.BottomSheetUtils;
import biz.laenger.android.vpbs.ViewPagerBottomSheetDialogFragment;

@SuppressLint("ValidFragment")
public class DialogFragment extends ViewPagerBottomSheetDialogFragment {

    private ViewPager viewPager;
    private FragmentAdapter fragmentAdapter;
    private String idTask = Conts.BLANK;

    @SuppressLint("ValidFragment")
    public DialogFragment(String idTask) {
        this.idTask = idTask;
    }

    public DialogFragment() {
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
//        final View contentView = View.inflate(getContext(), R.layout.dialog_bottom_sheet, null);
        dialog.setContentView(R.layout.dialog_bottom_sheet);
        CommonMethod.makeToast(getContext(), "ID: " + idTask);
        iniObject();
        initViews();
    }

    private void iniObject() {
        Fragment[] listFragment = new Fragment[]{new TaskDetailFragment(), new ProductRepairFragment()};
        fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), listFragment);
    }

    private void initViews() {
        TextView tvaa = (TextView) getDialog().findViewById(R.id.tv_aaa);
        tvaa.setText("AAAAAA");
//        viewPager = (ViewPager) getDialog().findViewById(R.id.view_pager);
//        viewPager.setAdapter(fragmentAdapter);
//        BottomSheetUtils.setupViewPager(viewPager);
    }


}
