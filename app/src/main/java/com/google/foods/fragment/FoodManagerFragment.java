package com.google.foods.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.foods.R;
import com.google.foods.activity.OrderFoodManagerActivity;
import com.google.foods.adapter.FoodManagerAdapter;
import com.google.foods.adapter.OrderManagerAdapter;
import com.google.foods.models.ItemFood;
import com.google.foods.utils.CommonValue;

import java.util.ArrayList;

/**
 * Created by sev_user on 08/14/2017.
 */

public class FoodManagerFragment extends Fragment implements FoodManagerAdapter.OnClickMoreOptionListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private static final String TAGG = "FoodManagerFragment";
    String arr[] = new String[]{
            "Tất cả xuất ăn",
            "30.000 VND",
            "35.000 VND",
            "40.000 VND",
            "5.000 VND",
            "10.000 VND",
            "15.000 VND",
    };
    private int foodType;

    private Spinner spinTypeFood;
    private CheckBox cbxHideFood;
    private RecyclerView recyclerViewFood;
    private Button btnResetData;
    private LinearLayout llListEmpty;

    private FoodManagerAdapter foodManagerAdapter;
    private DatabaseReference mData;

    private ArrayList<ItemFood> arrItemFood = new ArrayList<>();
    private ProgressDialog progressDialog;


    private static FoodManagerFragment foodManagerFragment;
    private OrderFoodManagerActivity orderFoodManagerActivity;

    public static FoodManagerFragment getInstance() {
        if (foodManagerFragment == null) {
            foodManagerFragment = new FoodManagerFragment();
        }
        return foodManagerFragment;
    }

    public FoodManagerFragment(){

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderFoodManagerActivity = (OrderFoodManagerActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAGG, "onCreateView FoodManagerFragment");
        showDumyLoadingData();

        View view = inflater.inflate(R.layout.fragment_food_manager, container, false);

        btnResetData = (Button) view.findViewById(R.id.btn_reset_data_food);
        btnResetData.setOnClickListener(this);
        llListEmpty = (LinearLayout) view.findViewById(R.id.ll_list_food_empty);
        cbxHideFood = (CheckBox) view.findViewById(R.id.cbx_hide_food_out_of_stock);
        cbxHideFood.setOnCheckedChangeListener(this);
        spinTypeFood = (Spinner) view.findViewById(R.id.spinner_type_food);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTypeFood.setAdapter(adapter);
        spinTypeFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int foodType = CommonValue.TYPE_FOOD_ALL;
                if (spinTypeFood.isEnabled()) {
                    switch (position) {
                        case CommonValue.DEFAULT_VALUE_INT_1:
                            foodType = CommonValue.VALUE_FOOD_30_VND;
                            break;
                        case CommonValue.DEFAULT_VALUE_INT_2:
                            foodType = CommonValue.VALUE_FOOD_35_VND;
                            break;
                        case CommonValue.DEFAULT_VALUE_INT_3:
                            foodType = CommonValue.VALUE_FOOD_40_VND;
                            break;
                        case CommonValue.DEFAULT_VALUE_INT_4:
                            foodType = CommonValue.VALUE_FOOD_5_VND;
                            break;
                        case CommonValue.DEFAULT_VALUE_INT_5:
                            foodType = CommonValue.VALUE_FOOD_10_VND;
                            break;
                        case CommonValue.DEFAULT_VALUE_INT_6:
                            foodType = CommonValue.VALUE_FOOD_15_VND;
                            break;

                    }
                    FoodManagerFragment.this.foodType = foodType;
                    foodManagerAdapter.filterByFoodType(foodType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        foodType = CommonValue.TYPE_FOOD_ALL;
        foodManagerAdapter = new FoodManagerAdapter(getActivity());
        foodManagerAdapter.setFoodManagerFragment(this);
        foodManagerAdapter.setOnClickMoreOptionListener(this);
        recyclerViewFood = (RecyclerView) view.findViewById(R.id.recycler_view_mgr_food);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewFood.setLayoutManager(layoutManager);
        recyclerViewFood.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFood.setAdapter(foodManagerAdapter);

        return view;
    }

    @Override
    public void onClickMoreOption(View view, int position) {
        foodManagerAdapter.showMenuPopup(view, position);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        foodManagerAdapter.setFoodOutOfStock(isChecked);
        foodManagerAdapter.filterFoodOutOfStock(isChecked);
    }

    private void showDumyLoadingData() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();

    }

    public void hideDumyLoadingData(){
        progressDialog.dismiss();
    }

    public int getFoodType() {
        return foodType;
    }

    public void hideLayoutListEmpty() {
        llListEmpty.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reset_data_food:
                orderFoodManagerActivity.resetDataFood();
                break;
        }
    }
}
