package com.cnc.hcm.cnctracking.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.FragmentAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.fragment.ListProductFragment;
import com.cnc.hcm.cnctracking.fragment.ListServiceFragment;
import com.cnc.hcm.cnctracking.model.CategoryListResult;
import com.cnc.hcm.cnctracking.model.ProductListResult;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProductAndServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imvBack, imgSearch;
    private LinearLayout llSearchVisiable, llSearchInVisiable;
    private EditText edtSearch;
    private ViewPager viewPager;
    private TextView tvClear;
    private OnTextChangeProductListener onTextChangeProductListener;
    private ArrayAdapter manufactureAdapter;
    private ArrayAdapter categoryAdapter;
    private ArrayList<String> arrManufactures;
    private ArrayList<String> arrCategory;
    private List<MHead> arrHeads;
    private String accessToken;
    private ArrayList<ProductListResult.Product> listProduct;
    private ArrayList<CategoryListResult.Category> listCategory;
    private Spinner spnManufacuture, spnCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_and_service);
        arrHeads = new ArrayList<>();
        arrManufactures = new ArrayList<>();
        arrCategory = new ArrayList<>();
        manufactureAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrManufactures);
        manufactureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrCategory);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accessToken = UserInfo.getInstance(getApplicationContext()).getAccessToken();
        arrHeads.clear();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        initViews();
    }

    private void initViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imgSearch = findViewById(R.id.img_search);
        llSearchInVisiable = findViewById(R.id.ll_search_invisible);
        llSearchVisiable = findViewById(R.id.ll_search_visible);
        tvClear = (TextView) findViewById(R.id.tv_clear_text_search);
        edtSearch = (EditText) findViewById(R.id.edt_search);
        ApiUtils.getAPIService(arrHeads).getListManufactures().enqueue(new Callback<ProductListResult>() {
            @Override
            public void onResponse(Call<ProductListResult> call, Response<ProductListResult> response) {
                Long status = response.body().getStatusCode();
                if (status == 200) {
                    arrManufactures.clear();
                    listProduct = (ArrayList<ProductListResult.Product>) response.body().getResult();
                    for (ProductListResult.Product p : listProduct) {
                        arrManufactures.add(p.getName());
                        manufactureAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductListResult> call, Throwable t) {

            }
        });
        ApiUtils.getAPIService(arrHeads).getListCategory().enqueue(new Callback<CategoryListResult>() {
            @Override
            public void onResponse(Call<CategoryListResult> call, Response<CategoryListResult> response) {
                Long status = response.body().getStatusCode();
                if (status == 200) {
                    arrCategory.clear();
                    listCategory = (ArrayList<CategoryListResult.Category>) response.body().getResult();
                    for (CategoryListResult.Category category : listCategory) {
                        arrCategory.add(category.getTitle());
                        categoryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryListResult> call, Throwable t) {

            }
        });
        spnCategory = findViewById(R.id.spn_product_type);
        spnManufacuture = findViewById(R.id.spn_manufacture);
        spnManufacuture.setAdapter(manufactureAdapter);
        spnCategory.setAdapter(categoryAdapter);

        imgSearch.setOnClickListener(this);
        imvBack.setOnClickListener(this);
        tvClear.setOnClickListener(this);

        edtSearch.addTextChangedListener(new TextWatcher() {
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
                edtSearch.setText(Conts.BLANK);
                break;
            case R.id.img_search:
                llSearchVisiable.setVisibility(View.VISIBLE);
                llSearchInVisiable.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (llSearchVisiable.getVisibility() == View.VISIBLE) {
            llSearchVisiable.setVisibility(View.GONE);
            llSearchInVisiable.setVisibility(View.VISIBLE);
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    public interface OnTextChangeProductListener {
        void onTextChange(CharSequence str);
    }

    public void setOnTextChangeProductListener(OnTextChangeProductListener onTextChangeProductListener) {
        this.onTextChangeProductListener = onTextChangeProductListener;
    }
}
