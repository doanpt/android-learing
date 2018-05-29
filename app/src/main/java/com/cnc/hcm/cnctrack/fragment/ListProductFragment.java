package com.cnc.hcm.cnctrack.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.activity.ListProductAndServiceActivity;
import com.cnc.hcm.cnctrack.adapter.TraddingProductAdapter;
import com.cnc.hcm.cnctrack.api.ApiUtils;
import com.cnc.hcm.cnctrack.api.MHead;
import com.cnc.hcm.cnctrack.customeview.MyRecyclerView;
import com.cnc.hcm.cnctrack.event.OnItemInputClickListener;
import com.cnc.hcm.cnctrack.model.SearchModel;
import com.cnc.hcm.cnctrack.model.TraddingProduct;
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
public class ListProductFragment extends Fragment implements ListProductAndServiceActivity.OnTextChangeProductListener,
        OnItemInputClickListener {

    private static final String TAGG = ListProductFragment.class.getSimpleName();
    private ListProductAndServiceActivity activity;
    private TraddingProductAdapter adapter;


    private MyRecyclerView recyclerView;
    private ProgressDialog mProgressDialog;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_product, container, false);
        recyclerView = (MyRecyclerView) view.findViewById(R.id.rc_tradding_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void getListTraddingProduct() {
        showProgressLoadding();
        String accessToken = UserInfo.getInstance(getContext()).getAccessToken();
        List<MHead> arrHeader = new ArrayList<>();
        arrHeader.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        ApiUtils.getAPIService(arrHeader).getListTraddingProduct().enqueue(new Callback<TraddingProduct>() {
            @Override
            public void onResponse(Call<TraddingProduct> call, Response<TraddingProduct> response) {
                Log.e(TAGG, "getListTraddingProduct.onResponse(), statusCode: " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAGG, "getListTraddingProduct.onResponse(), --> response: " + response.toString());
                    TraddingProduct tradingProduct = response.body();
                    if (tradingProduct != null) {
                        List<TraddingProduct.Result> result = tradingProduct.getResult();
                        adapter.notiData(result);
                    }
                }
                dismisProgressLoading();
            }

            @Override
            public void onFailure(Call<TraddingProduct> call, Throwable t) {
                Log.e(TAGG, "getListTraddingProduct.onFailure(), " + t.getMessage());
                dismisProgressLoading();
            }
        });
    }

    private void showProgressLoadding() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loadding));
        mProgressDialog.show();
    }

    private void dismisProgressLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClickInput(int position) {
        TraddingProduct.Result result = adapter.getItem(position);
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