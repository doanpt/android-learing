package com.cnc.hcm.cnctrack.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.activity.ListProductAndServiceActivity;
import com.cnc.hcm.cnctrack.adapter.TraddingProductAdapter;
import com.cnc.hcm.cnctrack.api.ApiUtils;
import com.cnc.hcm.cnctrack.api.MHead;
import com.cnc.hcm.cnctrack.base.BaseFragment;
import com.cnc.hcm.cnctrack.customeview.MyRecyclerView;
import com.cnc.hcm.cnctrack.event.OnItemInputClickListener;
import com.cnc.hcm.cnctrack.model.SearchModel;
import com.cnc.hcm.cnctrack.model.common.Product;
import com.cnc.hcm.cnctrack.model.common.Product_Products;
import com.cnc.hcm.cnctrack.util.Conts;
import com.cnc.hcm.cnctrack.util.UserInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListProductFragment extends BaseFragment implements ListProductAndServiceActivity.OnTextChangeProductListener,
        OnItemInputClickListener {

    private static final String TAGG = ListProductFragment.class.getSimpleName();
    private ListProductAndServiceActivity activity;
    private TraddingProductAdapter adapter;


    private MyRecyclerView recyclerView;

    public ListProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initObject();
        getListTraddingProduct();
        activity = (ListProductAndServiceActivity) getActivity();
        if (activity != null) {
            activity.setOnTextChangeProductListener(this);
        }
    }

    private void initObject() {
        adapter = new TraddingProductAdapter(getContext());
        adapter.setOnItemInputClickListener(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_list_product;
    }

    @Override
    public void onViewReady(View view) {
        recyclerView = (MyRecyclerView) view.findViewById(R.id.rc_tradding_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void getListTraddingProduct() {
        showProgressLoading();
        String accessToken = UserInfo.getInstance(getContext()).getAccessToken();
        List<MHead> arrHeader = new ArrayList<>();
        arrHeader.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        ApiUtils.getAPIService(arrHeader).getListTraddingProduct().enqueue(new Callback<Product_Products>() {
            @Override
            public void onResponse(Call<Product_Products> call, Response<Product_Products> response) {
                dismisProgressLoading();
                Log.e(TAGG, "getListTraddingProduct.onResponse(), statusCode: " + response.code());
                int statusCode = response.body().getStatusCode();
                if (response.isSuccessful()) {
                    if (statusCode == Conts.RESPONSE_STATUS_OK) {
                        Log.e(TAGG, "getListTraddingProduct.onResponse(), --> response: " + response.toString());
                        Product_Products tradingProduct = response.body();
                        if (tradingProduct != null) {
                            List<Product> result = tradingProduct.getListProduct();
                            adapter.notiData(result);
                        }
                    } else if (statusCode == Conts.RESPONSE_STATUS_TOKEN_WRONG) {
                        actionLogout();
                    }
                }
            }

            @Override
            public void onFailure(Call<Product_Products> call, Throwable t) {
                Log.e(TAGG, "getListTraddingProduct.onFailure(), " + t.getMessage());
                dismisProgressLoading();
            }
        });
    }

    @Override
    public void onClickInput(int position) {
        Product result = adapter.getItem(position);
        result.setQuantity(1);
        Intent intentResult = new Intent();
        intentResult.putExtra(Conts.KEY_SERVICE_PRODUCT_RESULT, result);
        intentResult.putExtra(Conts.KEY_CHECK_TYPE_RESULT, Conts.KEY_PRODUCT);
        activity.setResult(Activity.RESULT_OK, intentResult);
        activity.finish();
    }

    @Override
    public void onTextChange(SearchModel searchModel) {
        if (adapter != null) {
            adapter.filter(searchModel);
        }
    }
}
