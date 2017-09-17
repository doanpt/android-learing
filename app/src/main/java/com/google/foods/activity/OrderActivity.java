package com.google.foods.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.foods.R;
import com.google.foods.adapter.OrderListAdapter;
import com.google.foods.customeview.DialogNotification;
import com.google.foods.dialog.DialogNetworkConnection;
import com.google.foods.models.ItemFood;
import com.google.foods.models.ItemOrder2;
import com.google.foods.utils.CommonMethod;
import com.google.foods.utils.CommonValue;
import com.google.foods.utils.Keys;
import com.google.foods.utils.UserInfor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Android on 7/27/2017.
 */

public class OrderActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = OrderActivity.class.getSimpleName();

    private RecyclerView mRcvOrder;
    private TextView mTxtPrice;
    private TextView mTxtPriceOff;
    private TextView mTxtFinalPrice;
    private EditText mEdtUserName;
    private EditText mEdtPhoneNo;
    private EditText mEdtAddress;
    private EditText mLabelAddress;
    private RadioGroup mRgDeliverType;
    private Button mBtnBookNow;

    private String mCurrentTable;
    private int mTotalPrice;
    private int mComboPrice;
    private int mTotalDrinkInCombo;

    private OrderListAdapter mOrderListAdapter;

    private DialogNetworkConnection dialogNetworkConnection;
    private boolean isNetworkConnected;

    private OrderListAdapter.OnOrderQuantityChangeListener mOnOrderQuantityChangeListener = new OrderListAdapter.OnOrderQuantityChangeListener() {
        @Override
        public void onOrderQuantityChange(int position, int quantity) {
            if (!isFinishing()) {
                Log.e(TAG, "onOrderQuantityChange, position:" + position + "; quantity: " + quantity);
                if (quantity < 1) {
                    String itemName = mOrderListAdapter.getListFood().get(position).getName();
                    mOrderListAdapter.removeItemAtPosition(position);
                    if (mOrderListAdapter.getItemCount() == 0) {
                        DialogNotification dialogNotification = new DialogNotification(OrderActivity.this);
                        dialogNotification.setHiddenBtnExit();
                        dialogNotification.setContentMessage(getString(R.string.order_activity_remove_all_food_msg));
                        dialogNotification.setCancelable(false);
                        dialogNotification.setOnClickButtonOKDialogListener(new DialogNotification.OnClickButtonOKDialogListener() {
                            @Override
                            public void onClickButtonOK() {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                OrderActivity.this.finish();
                            }
                        });
                        dialogNotification.show();
                    } else {
                        DialogNotification dialogNotification = new DialogNotification(OrderActivity.this);
                        dialogNotification.setHiddenBtnExit();
                        dialogNotification.setContentMessage(String.format(getString(R.string.order_activity_remove_item_msg), itemName));
                        dialogNotification.setCancelable(true);
                        dialogNotification.show();
                    }
                }
            }
            showTotalPrice(mOrderListAdapter.getListFood());
        }
    };
    private OrderListAdapter.OnFoodItemQuantityChangeListenner mOnFoodItemQuantityChangeListenner = new OrderListAdapter.OnFoodItemQuantityChangeListenner() {
        @Override
        public void onRemainingQuantityChange(int position, int quantity) {
            if (!isFinishing()) {
                Log.e(TAG, "onRemainingQuantityChange, position:" + position + "; quantity: " + quantity);
                ItemFood itemFood = mOrderListAdapter.getListFood().get(position);
                DialogNotification dialogNotification = new DialogNotification(OrderActivity.this);
                dialogNotification.setHiddenBtnExit();
                dialogNotification.setContentMessage(String.format(getString(R.string.order_activity_reduce_remaining_quantity_msg), itemFood.getName(), itemFood.getOrderQuantity()));
                dialogNotification.setCancelable(true);
                dialogNotification.show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcastReciver();
        setContentView(R.layout.activity_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindView();

        Intent intent = getIntent();
        if (intent != null) {
            List<ItemFood> orderList = intent.getParcelableArrayListExtra(Keys.KEY_ORDER_LIST);
            showOrderList(orderList);
            showTotalPrice(orderList);
        }

        showUserInformation();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void registerBroadcastReciver() {
        dialogNetworkConnection = new DialogNetworkConnection(this);
        dialogNetworkConnection.setCancelable(false);

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

    private void bindView() {
        mTxtPrice = (TextView) findViewById(R.id.tv_price);
        mTxtPriceOff = (TextView) findViewById(R.id.tv_price_off);
        mTxtFinalPrice = (TextView) findViewById(R.id.tv_final_price);
        mRcvOrder = (RecyclerView) findViewById(R.id.rcv_order);
        mEdtUserName = (EditText) findViewById(R.id.edt_username);
        mEdtPhoneNo = (EditText) findViewById(R.id.edt_phone);
        mEdtAddress = (EditText) findViewById(R.id.edt_address);
        mLabelAddress = (EditText) findViewById(R.id.label_address);

        mRgDeliverType = (RadioGroup) findViewById(R.id.rg_delever_type);
        mRgDeliverType.setOnCheckedChangeListener(this);

        mBtnBookNow = (Button) findViewById(R.id.btn_booknow);
        mBtnBookNow.setOnClickListener(this);
    }

    private void showUserInformation() {
        mEdtUserName.setText(UserInfor.getInstance(this).getUserName());
        mEdtPhoneNo.setText(UserInfor.getInstance(this).getPhoneNumber());
        mEdtAddress.setText(UserInfor.getInstance(this).getAddress());
    }

    private void showTotalPrice(List<ItemFood> orderList) {
        mTotalPrice = 0;
        mTotalDrinkInCombo = 0;

        int totalFood = 0;
        for (ItemFood itemFood : orderList) {
            mTotalPrice += CommonMethod.getSinglePrice(itemFood.getType(), itemFood.getOrderQuantity());
            if (itemFood.getType() == CommonValue.VALUE_FOOD_10_VND) {
                if (!itemFood.getName().contains(CommonValue.PREFIX_FOOD_10_VND_NOT_IN_COMBO)) {
                    mTotalDrinkInCombo += itemFood.getOrderQuantity();
                }
            } else {
                totalFood += itemFood.getOrderQuantity();
            }
        }
        Log.e(TAG, "totalFood: " + totalFood + "; totalCombo: " + mTotalDrinkInCombo);
        mTotalDrinkInCombo = (mTotalDrinkInCombo > totalFood ? totalFood : mTotalDrinkInCombo);
        mComboPrice = mTotalDrinkInCombo * CommonValue.VALUE_FOOD_10_VND_IN_COMBO;

        mTxtPrice.setText(CommonMethod.getFormatedPrice(mTotalPrice));
        mTxtPriceOff.setText(CommonMethod.getFormatedPrice(mComboPrice));
        mTxtFinalPrice.setText(CommonMethod.getFormatedPrice(mTotalPrice - mComboPrice));
    }

    private void showOrderList(List<ItemFood> orderList) {
        mRcvOrder.setHasFixedSize(true);

        LinearLayoutManager llManager = new LinearLayoutManager(this);
        mRcvOrder.setLayoutManager(llManager);

        mOrderListAdapter = new OrderListAdapter(OrderActivity.this, orderList, mOnOrderQuantityChangeListener, mOnFoodItemQuantityChangeListenner);
        mRcvOrder.setAdapter(mOrderListAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_booknow:
                bookNow();
                break;
        }
    }

    private void bookNow() {

        String userName = mEdtUserName.getText().toString();
        String userPhone = mEdtPhoneNo.getText().toString();
        String userAddress = mEdtAddress.getText().toString();

        int deliveryType;

        int checkedRadioButtonId = mRgDeliverType.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.rdb_ship) {
            deliveryType = CommonValue.ORDER_NEED_SHIP;
            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPhone) || TextUtils.isEmpty(userAddress)) {
                Toast.makeText(this, R.string.order_activity_request_user_full_infor_msg, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            deliveryType = CommonValue.ORDER_AT_RESTAURANT;
            userAddress = getString(R.string.order_activity_prefix_banso) + " " + userAddress;
            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userAddress)) {
                Toast.makeText(this, R.string.order_activity_request_name_table_msg, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        final ProgressDialog progressDialog = new ProgressDialog(OrderActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.order_activity_booking_in_progress));
        progressDialog.show();

        final DatabaseReference newOrderRef = FirebaseDatabase.getInstance().getReference().child(CommonValue.DATABASE_TABLE_ORDER).push();
        String orderKey = newOrderRef.getKey();

        Date datetime = Calendar.getInstance().getTime();
        ItemOrder2 itemOrder2 = new ItemOrder2(orderKey, userName, userPhone, userAddress, deliveryType, mTotalPrice, mComboPrice, mTotalPrice - mComboPrice, datetime, CommonValue.STATUS_ORDER_NOT_HANDL, mOrderListAdapter.getListFood());
        newOrderRef.setValue(itemOrder2, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                progressDialog.dismiss();
                if (databaseError == null) {
                    showSuccessNotification();
                    updateOrderQuantityOfFood();
                } else {
                    Toast.makeText(OrderActivity.this, R.string.order_activity_booking_is_fail_msg, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Update food orderQuantity fail " + databaseError.toString());
                }
            }

            private void showSuccessNotification() {
                if (!isFinishing()) {
                    final DialogNotification dialogNotification = new DialogNotification(OrderActivity.this);
                    dialogNotification.setHiddenBtnExit();
                    String message;
                    if (mTotalDrinkInCombo > 0) {
                        message = String.format(getString(R.string.order_activity_booking_success_with_combo_msg), CommonMethod.getFormatedPrice(mComboPrice), CommonMethod.getFormatedPrice(mTotalPrice - mComboPrice));
                    } else {
                        message = getString(R.string.order_activity_booking_success_msg);
                    }
                    dialogNotification.setContentMessage(message);
                    dialogNotification.setCancelable(false);
                    dialogNotification.setOnClickButtonOKDialogListener(new DialogNotification.OnClickButtonOKDialogListener() {
                        @Override
                        public void onClickButtonOK() {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            OrderActivity.this.finish();
                        }
                    });
                    dialogNotification.show();
                }
            }

            private void updateOrderQuantityOfFood() {
                for (final ItemFood food : mOrderListAdapter.getListFood()) {
                    Log.e(TAG, "food: " + food);
                    final DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference().child(CommonValue.DATABASE_TABLE_FOOD).child(food.getIdFood()).child(CommonValue.DATABASE_TABLE_ORDER_FIELD_ORDER_QUANTITY).getRef();
                    foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int orderQuantity = snapshot.getValue(Integer.class);
                            int totalQuantity = food.getTotalQuantity();

                            Log.e(TAG, "orderQuantity: " + orderQuantity + "; totalCombo: " + totalQuantity);
                            orderQuantity += food.getOrderQuantity();
                            orderQuantity = orderQuantity > totalQuantity ? totalQuantity : orderQuantity;
                            foodRef.setValue(orderQuantity);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "Update food orderQuantity fail " + databaseError);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rdb_ship:
                mLabelAddress.setText(R.string.address);

                mCurrentTable = mEdtAddress.getText().toString();

                mEdtAddress.setInputType(InputType.TYPE_CLASS_TEXT);
                mEdtAddress.setText(UserInfor.getInstance(this).getAddress());
                break;
            case R.id.rdb_onShop:
                mLabelAddress.setText(R.string.activity_order_content_label_table);

                mEdtAddress.setInputType(InputType.TYPE_CLASS_NUMBER);
                mEdtAddress.setText(TextUtils.isEmpty(mCurrentTable) ? CommonValue.BLANK : mCurrentTable);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
