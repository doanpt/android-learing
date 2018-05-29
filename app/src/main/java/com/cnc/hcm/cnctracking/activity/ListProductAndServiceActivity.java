package com.cnc.hcm.cnctracking.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.FragmentAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.base.BaseActivity;
import com.cnc.hcm.cnctracking.dialog.DialogNotification;
import com.cnc.hcm.cnctracking.fragment.ListProductFragment;
import com.cnc.hcm.cnctracking.fragment.ListServiceFragment;
import com.cnc.hcm.cnctracking.model.CategoryListResult;
import com.cnc.hcm.cnctracking.model.ProductListResult;
import com.cnc.hcm.cnctracking.model.SearchModel;
import com.cnc.hcm.cnctracking.model.SearchServiceModel;
import com.cnc.hcm.cnctracking.model.Services;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProductAndServiceActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imvBack, imgSearch, imgExitProductSearch, imgExitServiceSearch;
    private LinearLayout llTitle, llProductSearch, llServiceSearch;
    private EditText edtProductSearch, edtServiceSearch;
    private ViewPager viewPager;
    private TextView tvProductClear, tvServiceClear;
    private OnTextChangeProductListener onTextChangeProductListener;
    private OnTextChangeServiceListener onTextChangeServiceListener;
    private ArrayAdapter manufactureAdapter;
    private ArrayAdapter categoryAdapter;
    private ArrayAdapter servicesAdapter;
    private ArrayList<String> arrManufactures;
    private ArrayList<String> arrCategory;
    private ArrayList<String> arrServices;
    private List<MHead> arrHeads;
    private String accessToken;
    private ArrayList<Services.Result> listServices;
    private ArrayList<ProductListResult.Product> listProduct;
    private ArrayList<CategoryListResult.Category> listCategory;
    private Spinner spnManufacuture, spnCategory, spnServiceCategory;
    private DialogNotification dialogNotification;


    @Override
    public void onViewReady(@Nullable Bundle savedInstanceState) {
        initObjects();
        initViews();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_list_product_and_service;
    }

    private void initObjects() {
        arrHeads = new ArrayList<>();
        arrManufactures = new ArrayList<>();
        arrCategory = new ArrayList<>();
        arrServices = new ArrayList<>();
        manufactureAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrManufactures);
        manufactureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrCategory);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrServices);
        servicesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accessToken = UserInfo.getInstance(getApplicationContext()).getAccessToken();
        arrHeads.clear();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));

        dialogNotification = new DialogNotification(this);
        dialogNotification.setTitle(getString(R.string.error_occurred));
        dialogNotification.setTextBtnOK(getString(R.string.try_again));
        dialogNotification.setOnClickDialogNotificationListener(new DialogNotification.OnClickDialogNotificationListener() {
            @Override
            public void onClickButtonOK() {
                showLoadingDialog();
                getListServices();
            }

            @Override
            public void onClickButtonExit() {

            }
        });

    }

    private void showLoadingDialog() {
        showProgressLoadding();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismisProgressLoading();
            }
        }, 10000);
    }

    private void initViews() {
        imvBack = findViewById(R.id.imv_back);
        imgSearch = findViewById(R.id.img_search);

        llTitle = findViewById(R.id.ll_title_visible);
        llServiceSearch = findViewById(R.id.ll_search_service_visible);
        llProductSearch = findViewById(R.id.ll_product_search_visible);
        llTitle.setVisibility(View.VISIBLE);
        llServiceSearch.setVisibility(View.INVISIBLE);
        llProductSearch.setVisibility(View.INVISIBLE);

        tvServiceClear = findViewById(R.id.tv_service_clear_text_search);
        tvProductClear = findViewById(R.id.tv_product_clear_text_search);

        edtProductSearch = findViewById(R.id.edt_product_search);
        edtServiceSearch = findViewById(R.id.edt_service_search);

        imgExitProductSearch = findViewById(R.id.img_product_back_to_list);
        imgExitServiceSearch = findViewById(R.id.img_service_back_to_list);
        showLoadingDialog();
        ApiUtils.getAPIService(arrHeads).getListManufactures().enqueue(new Callback<ProductListResult>() {
            @Override
            public void onResponse(Call<ProductListResult> call, Response<ProductListResult> response) {
                Long status = response.body().getStatusCode();
                if (status != null && status == 200) {
                    arrManufactures.clear();
                    listProduct = (ArrayList<ProductListResult.Product>) response.body().getResult();
                    arrManufactures.add("Chọn nhà sản xuất");
                    if (listProduct != null) {
                        for (ProductListResult.Product p : listProduct) {
                            arrManufactures.add(p.getName());
                            manufactureAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("ListProductAndServiceAc", "FATA ListProductAndServiceAc, initViews(), listProduct = null");
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductListResult> call, Throwable t) {
                dismisProgressLoading();
                Toast.makeText(ListProductAndServiceActivity.this, "Get lise product  for search fail", Toast.LENGTH_SHORT).show();
            }
        });
        ApiUtils.getAPIService(arrHeads).getListCategory().enqueue(new Callback<CategoryListResult>() {
            @Override
            public void onResponse(Call<CategoryListResult> call, Response<CategoryListResult> response) {
                Long status = response.body().getStatusCode();
                if (status != null && status == 200) {
                    arrCategory.clear();
                    arrCategory.add("Chọn loại thiết bị");
                    listCategory = (ArrayList<CategoryListResult.Category>) response.body().getResult();
                    if (listCategory != null) {
                        for (CategoryListResult.Category category : listCategory) {
                            arrCategory.add(category.getTitle());
                            categoryAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("ListProductAndServiceAc", "FATA ListProductAndServiceAc, initViews(), listCategory = null");
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryListResult> call, Throwable t) {
                dismisProgressLoading();
                Toast.makeText(ListProductAndServiceActivity.this, "Get lise category  for search fail", Toast.LENGTH_SHORT).show();
            }
        });

        //Get list services
        getListServices();


        spnCategory = findViewById(R.id.spn_product_product_type);
        spnManufacuture = findViewById(R.id.spn_product_manufacture);
        spnServiceCategory = findViewById(R.id.spn_service_product_type);
        spnManufacuture.setAdapter(manufactureAdapter);
        spnCategory.setAdapter(categoryAdapter);
        spnServiceCategory.setAdapter(servicesAdapter);

        imgSearch.setOnClickListener(this);
        imvBack.setOnClickListener(this);
        tvProductClear.setOnClickListener(this);
        tvServiceClear.setOnClickListener(this);
        imgExitProductSearch.setOnClickListener(this);
        imgExitServiceSearch.setOnClickListener(this);
        edtProductSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (onTextChangeProductListener != null) {
                    SearchModel searchModel = new SearchModel();
                    searchModel.setText(charSequence.toString());
                    searchModel.setBranch(spnManufacuture.getSelectedItem().toString());
                    searchModel.setCategory(spnCategory.getSelectedItem().toString());
                    onTextChangeProductListener.onTextChange(searchModel);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtServiceSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (onTextChangeServiceListener != null) {
                    SearchServiceModel searchModel = new SearchServiceModel();
                    searchModel.setName(charSequence.toString());
                    searchModel.setCategory(spnServiceCategory.getSelectedItem().toString());
                    onTextChangeServiceListener.onTextChange(searchModel);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spnManufacuture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnServiceCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchServiceCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Fragment[] list = new Fragment[]{new ListProductFragment(), new ListServiceFragment()};
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), list);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    if (llProductSearch.getVisibility() != View.VISIBLE && llTitle.getVisibility() != View.VISIBLE) {
                        llProductSearch.setVisibility(View.VISIBLE);
                        llServiceSearch.setVisibility(View.GONE);
                        llTitle.setVisibility(View.GONE);
                    }
                } else if (position == 1) {
                    if (llServiceSearch.getVisibility() != View.VISIBLE && llTitle.getVisibility() != View.VISIBLE) {
                        llProductSearch.setVisibility(View.GONE);
                        llServiceSearch.setVisibility(View.VISIBLE);
                        llTitle.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        dismisProgressLoading();
    }

    private void getListServices() {
        ApiUtils.getAPIService(arrHeads).getServices().enqueue(new Callback<Services>() {
            @Override
            public void onResponse(Call<Services> call, Response<Services> response) {
                Integer status = response.body().getStatusCode();
                if (status != null && status == 200) {
                    arrServices.clear();
                    arrServices.add("Chọn loại dịch vụ");
                    listServices = (ArrayList<Services.Result>) response.body().getResult();
                    if (listServices != null) {
                        for (Services.Result service : listServices) {
                            arrServices.add(service.getCategory().getTitle());
                            servicesAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("ListProductAndServiceAc", "FATA ListProductAndServiceAc, initViews(), listServices = null");
                    }
                }
            }

            @Override
            public void onFailure(Call<Services> call, Throwable t) {
                dismisProgressLoading();
                if (dialogNotification != null) {
                    dialogNotification.setContentMessage("getServices().onFailure()\n" + t.getMessage());
                    dialogNotification.show();
                }
            }
        });
    }

    private void searchServiceCategory() {
        if (onTextChangeServiceListener != null) {
            SearchServiceModel searchModel = new SearchServiceModel();
            searchModel.setName(edtServiceSearch.getText().toString());
            searchModel.setCategory(spnServiceCategory.getSelectedItem().toString());
            onTextChangeServiceListener.onTextChange(searchModel);
        }
    }

    private void search() {
        if (onTextChangeProductListener != null && spnManufacuture.getSelectedItem() != null && spnCategory.getSelectedItem() != null) {
            SearchModel searchModel = new SearchModel();
            searchModel.setText(edtProductSearch.getText().toString());
            searchModel.setBranch(spnManufacuture.getSelectedItem().toString());
            searchModel.setCategory(spnCategory.getSelectedItem().toString());
            onTextChangeProductListener.onTextChange(searchModel);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.tv_service_clear_text_search:
                edtServiceSearch.setText(Conts.BLANK);
                break;
            case R.id.tv_product_clear_text_search:
                edtProductSearch.setText(Conts.BLANK);
                break;
            case R.id.img_search:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (viewPager.getCurrentItem() == 0) {
                        revealTextSeach(llProductSearch);
                    } else {
                        revealTextSeach(llServiceSearch);
                    }
                } else {
                    if (viewPager.getCurrentItem() == 0) {
                        llProductSearch.setVisibility(View.VISIBLE);
                        llServiceSearch.setVisibility(View.GONE);
                        llTitle.setVisibility(View.GONE);
                    } else {
                        llProductSearch.setVisibility(View.GONE);
                        llServiceSearch.setVisibility(View.VISIBLE);
                        llTitle.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.img_product_back_to_list:
            case R.id.img_service_back_to_list:
                backpressLayoutSearch();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (llServiceSearch.getVisibility() == View.VISIBLE || llProductSearch.getVisibility() == View.VISIBLE) {
            backpressLayoutSearch();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void backpressLayoutSearch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (llProductSearch.getVisibility() == View.VISIBLE) {
                hideTextSeach(llProductSearch);
            }

            if (llServiceSearch.getVisibility() == View.VISIBLE) {
                hideTextSeach(llServiceSearch);
            }

        } else {
            llProductSearch.setVisibility(View.GONE);
            llServiceSearch.setVisibility(View.GONE);
            llTitle.setVisibility(View.VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealTextSeach(View view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;

        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void hideTextSeach(final View view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int initialRadius = view.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });

        anim.start();

    }

    public interface OnTextChangeProductListener {
        void onTextChange(SearchModel str);
    }

    public interface OnTextChangeServiceListener {
        void onTextChange(SearchServiceModel str);
    }

    public void setOnTextChangeProductListener(OnTextChangeProductListener onTextChangeProductListener) {
        this.onTextChangeProductListener = onTextChangeProductListener;
    }

    public void setOnTextChangeServiceListener(OnTextChangeServiceListener onTextChangeServiceListener) {
        this.onTextChangeServiceListener = onTextChangeServiceListener;
    }
}
