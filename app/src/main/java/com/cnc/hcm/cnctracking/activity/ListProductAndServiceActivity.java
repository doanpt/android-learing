package com.cnc.hcm.cnctracking.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.FragmentAdapter;
import com.cnc.hcm.cnctracking.fragment.ListProductFragment;
import com.cnc.hcm.cnctracking.fragment.ListServiceFragment;
import com.cnc.hcm.cnctracking.util.Conts;

public class ListProductAndServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imvBack;
    private EditText edtSeach;
    private ViewPager viewPager;
    private TextView tvClear;
    private OnTextChangeProductListener onTextChangeProductListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_and_service);
        initViews();
    }

    private void initViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvBack.setOnClickListener(this);
        tvClear = (TextView) findViewById(R.id.tv_clear_text_search);
        tvClear.setOnClickListener(this);
        edtSeach = (EditText) findViewById(R.id.edt_search);
        edtSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (onTextChangeProductListener != null) {
                    onTextChangeProductListener.onTextChange(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Fragment[] list = new Fragment[]{new ListProductFragment(), new ListServiceFragment()};
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), list);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.tv_clear_text_search:
                edtSeach.setText(Conts.BLANK);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public interface OnTextChangeProductListener {
        void onTextChange(CharSequence str);
    }

    public void setOnTextChangeProductListener(OnTextChangeProductListener onTextChangeProductListener) {
        this.onTextChangeProductListener = onTextChangeProductListener;
    }
}
