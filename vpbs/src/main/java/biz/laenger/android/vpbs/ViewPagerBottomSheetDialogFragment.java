package biz.laenger.android.vpbs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

public class ViewPagerBottomSheetDialogFragment extends BottomSheetDialogFragment implements ViewPagerBottomSheetDialog.OnSlideListener {

    private ViewPagerBottomSheetDialog viewPagerBottomSheetDialog;
    private boolean isExpaned;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewPagerBottomSheetDialog = new ViewPagerBottomSheetDialog(getContext(), getTheme());
        viewPagerBottomSheetDialog.setOnSlideBottomSheet(this);
        if (isExpaned) {
            viewPagerBottomSheetDialog.showExpanded();
        }
        return viewPagerBottomSheetDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isExpaned && viewPagerBottomSheetDialog != null) {
            viewPagerBottomSheetDialog.showExpanded();
        }
    }

    public void showExpaned() {
        if (isExpaned && viewPagerBottomSheetDialog != null) {
            viewPagerBottomSheetDialog.showExpanded();
        }
    }


    @Override
    public void onSlideBottomSheet(@NonNull View bottomSheet, float slideOffset) {
    }

    @Override
    public void onStateChangedBottomSheet(@NonNull View bottomSheet, int newState) {

    }

    @Override
    public void setVisibilityView() {

    }

    public void setExpaned(boolean expaned) {
        isExpaned = expaned;
    }
}
