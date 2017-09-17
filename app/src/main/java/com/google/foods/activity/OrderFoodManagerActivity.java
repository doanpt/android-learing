package com.google.foods.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.foods.R;
import com.google.foods.customeview.DialogNotification;
import com.google.foods.dialog.DialogNetworkConnection;
import com.google.foods.dialog.DialogResetDataFood;
import com.google.foods.fragment.FoodManagerFragment;
import com.google.foods.fragment.OrderManagerFrament;
import com.google.foods.models.ItemFood;
import com.google.foods.models.ItemOrder2;
import com.google.foods.utils.CommonValue;
import com.google.foods.utils.Keys;
import com.google.foods.utils.UserInfor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 8/13/2017.
 */

public class OrderFoodManagerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, DialogResetDataFood.OnClickButtonOKDialogResetDataFoodListener {

    private static final String SETTING_SCREEN_WHEN_ROTATE_SCREEN = "setting_rotate_screen";
    private ImageView imvMenu;
    private ImageView imvAddFood;
    private TextView tvTitleActionBar;
    private DrawerLayout drawer;

    private boolean isShowFragmentOrderMgr;
    private String inforAdmin;

    private DialogNetworkConnection dialogNetworkConnection;
    private DialogResetDataFood dialogResetDataFood;
    private boolean isNetworkConnected;

    private DatabaseReference mData;
    private ArrayList<ItemFood> arrFood = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcastReciver();
        setContentView(R.layout.activity_control_order);
        getDataInforAdmin();
        initView();
        initDataFood();
    }

    private void initDataFood() {
        mData = FirebaseDatabase.getInstance().getReference();
        mData.child(CommonValue.DATABASE_TABLE_FOOD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrFood.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ItemFood itemFood = postSnapshot.getValue(ItemFood.class);
                    arrFood.add(itemFood);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void registerBroadcastReciver() {
        dialogNetworkConnection = new DialogNetworkConnection(this);
        dialogNetworkConnection.setCancelable(false);

        dialogResetDataFood = new DialogResetDataFood(this);
        dialogResetDataFood.setOnClickButtonOKDialogResetDataFoodListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(CommonValue.ACTION_NETWORK_CHANGE);
        registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case CommonValue.ACTION_NETWORK_CHANGE:
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetInfor = connectivityManager.getActiveNetworkInfo();
                    isNetworkConnected = activeNetInfor != null && activeNetInfor.isConnectedOrConnecting();
                    if (isNetworkConnected) {
                        dialogNetworkConnection.dismiss();
                    } else {
                        dialogNetworkConnection.show();
                    }
                    break;
            }

        }
    };

    private void getDataInforAdmin() {
        inforAdmin = UserInfor.getInstance(this).getAdminUsername();
    }

    private void initView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = (View) navigationView.getHeaderView(0);
        TextView imvInforAdmin = (TextView) view.findViewById(R.id.tv_infor_admin);

        imvInforAdmin.setText("Admin - " + inforAdmin);
        tvTitleActionBar = (TextView) findViewById(R.id.title_fragment);

        isShowFragmentOrderMgr = true;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        imvMenu = (ImageView) findViewById(R.id.imv_menu);
        imvMenu.setOnClickListener(this);
        imvAddFood = (ImageView) findViewById(R.id.imv_add_food);
        imvAddFood.setOnClickListener(this);

        if (getIsHowOrderFragment()) {
            callFragmentByID(CommonValue.ID_FRAGMENT_ORDER_MANAGER);
        } else {
            callFragmentByID(CommonValue.ID_FRAGMENT_FOOD_MANAGER);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_food) {
            nextScreenAddFood();
        } else if (id == R.id.nav_control_menu) {
            if (isShowFragmentOrderMgr) {
                callFragmentByID(CommonValue.ID_FRAGMENT_FOOD_MANAGER);
            }
        } else if (id == R.id.nav_control_order) {
            if (!isShowFragmentOrderMgr) {
                callFragmentByID(CommonValue.ID_FRAGMENT_ORDER_MANAGER);
            }
        } else if (id == R.id.nav_log_out_admin) {
            final ProgressDialog progressDialog = new ProgressDialog(OrderFoodManagerActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.string_logouting));
            progressDialog.show();

            UserInfor.getInstance(this).setUserLogout();
            UserInfor.getInstance(this).setAminLogout();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            Intent intent = new Intent(OrderFoodManagerActivity.this, SplashActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callFragmentByID(int idFragment) {
        switch (idFragment) {
            case CommonValue.ID_FRAGMENT_FOOD_MANAGER:
                callFragment(FoodManagerFragment.getInstance());
                tvTitleActionBar.setText(getResources().getString(R.string.title_action_bar_food_manager));
                isShowFragmentOrderMgr = false;
                imvAddFood.setVisibility(View.VISIBLE);
                setIsShowOrderFragment(false);
                break;
            case CommonValue.ID_FRAGMENT_ORDER_MANAGER:
                callFragment(OrderManagerFrament.getInstance());
                tvTitleActionBar.setText(getResources().getString(R.string.title_action_bar_order_manager));
                isShowFragmentOrderMgr = true;
                imvAddFood.setVisibility(View.INVISIBLE);
                setIsShowOrderFragment(true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_menu:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.imv_add_food:
                nextScreenAddFood();
                break;
        }
    }


    public void callFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    private void nextScreenAddFood() {
        Intent intent = new Intent(OrderFoodManagerActivity.this, AddFoodActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    public boolean isNetworkConnected() {
        return isNetworkConnected;
    }

    private void setIsShowOrderFragment(boolean isShow) {
        SharedPreferences sharedPreferences = getSharedPreferences(SETTING_SCREEN_WHEN_ROTATE_SCREEN, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Keys.KEY_SETTING_SCREEN_WHEN_ROTATE_SCREEN, isShow);
        editor.commit();
    }

    private boolean getIsHowOrderFragment() {
        SharedPreferences sharedPreferences = getSharedPreferences(SETTING_SCREEN_WHEN_ROTATE_SCREEN, MODE_PRIVATE);
        return sharedPreferences.getBoolean(Keys.KEY_SETTING_SCREEN_WHEN_ROTATE_SCREEN, true);
    }

    public void updateTotalQuantityFood(final List<ItemFood> foodList) {
        for (ItemFood item : foodList) {
            for (ItemFood itemFood : arrFood) {
                if (itemFood.getIdFood().equals(item.getIdFood())) {
                    int quantity = itemFood.getOrderQuantity() - item.getOrderQuantity();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("orderQuantity", quantity);
                    mData.child(CommonValue.DATABASE_TABLE_FOOD).child(itemFood.getIdFood()).updateChildren(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        }
                    });
                    break;
                }

            }


        }
    }

    public void resetDataFood() {
        dialogResetDataFood.show();
    }

    @Override
    public void onClickButtonOK(int totalQuantity) {

        Map<String, Object> mapReset = new HashMap<String, Object>();
        mapReset.put("totalQuantity", totalQuantity);
        mapReset.put("orderQuantity", CommonValue.DEFAULT_VALUE_INT_0);
        final int size = arrFood.size();
        if (size == 0) {
            DialogNotification dialog = new DialogNotification(this);
            dialog.setHiddenBtnExit();
            dialog.setContentMessage(getResources().getString(R.string.no_data_food));
            dialog.show();
        } else if (size > 0) {
            for (int index = 0; index < size; index++) {
                final int finalIndex = index;
                mData.child(CommonValue.DATABASE_TABLE_FOOD).child(arrFood.get(index).getIdFood()).updateChildren(mapReset, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            if (finalIndex == (size - 1))
                                Toast.makeText(OrderFoodManagerActivity.this, getResources().getString(R.string.update_successfull), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            //mData.child(CommonValue.DATABASE_TABLE_ORDER).removeValue();
        }
    }

    @Override
    public void onClickButtonExit() {

    }
}