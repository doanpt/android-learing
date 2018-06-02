package com.cnc.hcm.cnctrack.fragment;


import android.app.Activity;
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
import com.cnc.hcm.cnctrack.adapter.ServicesAdapter;
import com.cnc.hcm.cnctrack.api.ApiUtils;
import com.cnc.hcm.cnctrack.api.MHead;
import com.cnc.hcm.cnctrack.base.BaseFragment;
import com.cnc.hcm.cnctrack.customeview.MyRecyclerView;
import com.cnc.hcm.cnctrack.event.OnItemInputClickListener;
import com.cnc.hcm.cnctrack.model.SearchServiceModel;
import com.cnc.hcm.cnctrack.model.Services;
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
public class ListServiceFragment extends BaseFragment implements
        OnItemInputClickListener, ListProductAndServiceActivity.OnTextChangeServiceListener {

    private static final String TAGG = ListServiceFragment.class.getSimpleName();
    private MyRecyclerView rcServices;
    private ListProductAndServiceActivity activity;
    private ServicesAdapter adapter;

    public ListServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initObjects();
        getListService();
        activity = (ListProductAndServiceActivity) getActivity();
        if (activity != null) {
            activity.setOnTextChangeServiceListener(this);
        }
    }

    private void getListService() {
        String accessToken = UserInfo.getInstance(getContext()).getAccessToken();
        List<MHead> headList = new ArrayList<>();
        headList.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        ApiUtils.getAPIService(headList).getServices().enqueue(new Callback<Services>() {
            @Override
            public void onResponse(Call<Services> call, Response<Services> response) {
                int statusCode = response.body().getStatusCode();
                Log.e(TAGG, "getListService.onResponse(), statusCode: " + statusCode);
                if (response.isSuccessful()) {
                    if (statusCode == Conts.RESPONSE_STATUS_OK) {
                        Log.e(TAGG, "getListService.onResponse(), --> response: " + response.toString());
                        Services services = response.body();
                        if (services != null) {
                            List<Services.Result> result = services.getResult();
                            if (adapter != null) {
                                adapter.notiData(result);
                            }
                        }
                    } else if (statusCode == Conts.RESPONSE_STATUS_TOKEN_WRONG) {
                        actionLogout();
                    }
                }
            }

            @Override
            public void onFailure(Call<Services> call, Throwable t) {

            }
        });
    }

    private void initObjects() {
        adapter = new ServicesAdapter(getContext());
        adapter.setOnItemInputClickListener(this);
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_list_service;
    }

    @Override
    public void onViewReadly(View view) {
        rcServices = (MyRecyclerView) view.findViewById(R.id.rc_services);
        rcServices.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcServices.setAdapter(adapter);
    }

    @Override
    public void onClickInput(int position) {
        Services.Result result = adapter.getItem(position);
        Intent intentResult = new Intent();
        intentResult.putExtra(Conts.KEY_SERVICE_PRODUCT_RESULT, result);
        intentResult.putExtra(Conts.KEY_CHECK_TYPE_RESULT, Conts.KEY_SERVICE);
        activity.setResult(Activity.RESULT_OK, intentResult);
        activity.finish();
    }

    @Override
    public void onTextChange(SearchServiceModel model) {
        if (adapter != null) {
            adapter.filter(model);
        }
    }
}
