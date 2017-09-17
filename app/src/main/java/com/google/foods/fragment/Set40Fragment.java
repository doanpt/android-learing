package com.google.foods.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.foods.R;
import com.google.foods.activity.MainActivity;
import com.google.foods.adapter.FoodListAdapter;
import com.google.foods.listenner.OnFoodClickItem;
import com.google.foods.models.ItemFood;
import com.google.foods.utils.CommonValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Android on 7/27/2017.
 */

@SuppressLint("ValidFragment")
public class Set40Fragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private LinearLayout llListFoodEmpty;
    private FoodListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ItemFood> foodList;
    private MainActivity mActivity;
    private DatabaseReference mDatabase;
    private DatabaseReference food40;
    private static Set40Fragment set40Fragment;

    public static Set40Fragment getInstance(Context context) {
        if (set40Fragment == null) {
            set40Fragment = new Set40Fragment(context);
        }
        return set40Fragment;
    }

    public static void releaseInstance() {
        set40Fragment = null;
    }

    public Set40Fragment() {
        mActivity = (MainActivity) getContext();
    }

    private Set40Fragment(Context context) {
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        food40 = mDatabase.child(CommonValue.DATABASE_TABLE_FOOD);
        setRetainInstance(true);
    }

    private void initComponent() {
        llListFoodEmpty = (LinearLayout) view.findViewById(R.id.ll_list_food_empty);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_set40);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        resetAdapter();
    }

    private void resetAdapter() {
        foodList = new ArrayList<>();
        mAdapter = new FoodListAdapter(getContext(), foodList);
        recyclerView.setAdapter(mAdapter);
        //listen onlick plus button and get position in array and value which user selected
        mAdapter.setOnClickButtonPlusAdapter(new FoodListAdapter.OnClickButtonPlusAdapter() {
            @Override
            public void onClickButtonPlusAdapter(int position, int value) {
                ItemFood itemFood = foodList.get(position);
                itemFood.setOrderQuantity(value);
                mActivity.onItemClick(itemFood);
            }
        });
        //listen onlick minus button and get position in array and value which user selected
        mAdapter.setOnClickButtonMinusAdapter(new FoodListAdapter.OnClickButtonMinusAdapter() {
            @Override
            public void onClickButtonMinusAdapter(int position, int value) {
                ItemFood itemFood = foodList.get(position);
                itemFood.setOrderQuantity(value);
                mActivity.onItemClick(itemFood);
            }
        });
        //listen change of food node and get all food with cost = 40000VND to display on recyclerView
        food40.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                foodList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ItemFood itemFood = postSnapshot.getValue(ItemFood.class);
                    if (itemFood.getType() == CommonValue.VALUE_FOOD_40_VND) {
                        foodList.add(itemFood);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                if (foodList.size() > 0) {
                    mAdapter.setArrFoodItem(foodList);
                    mAdapter.notifyDataSetChanged();
                    hideLayoutListEmpty();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //If have any error in progress=> handler at here
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.getActivity());
        view = layoutInflater.inflate(R.layout.fragment_set40, null);
        initComponent();
        return view;
    }

    public void hideLayoutListEmpty() {
        llListFoodEmpty.setVisibility(View.INVISIBLE);
    }
}