package com.cnc.hcm.cnctracking.fragment;


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

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.ListProductAndServiceActivity;
import com.cnc.hcm.cnctracking.adapter.ServicesAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.customeview.MyRecyclerView;
import com.cnc.hcm.cnctracking.event.OnItemInputClickListener;
import com.cnc.hcm.cnctracking.model.SearchServiceModel;
import com.cnc.hcm.cnctracking.model.Services;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListServiceFragment extends Fragment implements
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


                Log.e(TAGG, "getListService.onResponse(), statusCode: " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAGG, "getListService.onResponse(), --> response: " + response.toString());
                    Services services = response.body();
                    if (services != null) {
                        List<Services.Result> result = services.getResult();
                        if (adapter != null) {
                            adapter.notiData(result);
                        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_service, container, false);
        rcServices = (MyRecyclerView) view.findViewById(R.id.rc_services);
        rcServices.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcServices.setAdapter(adapter);
        return view;
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
