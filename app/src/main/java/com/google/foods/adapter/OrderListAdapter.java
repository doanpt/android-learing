package com.google.foods.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.foods.R;
import com.google.foods.customeview.SelectOrderView;
import com.google.foods.models.ItemFood;
import com.google.foods.utils.CommonMethod;
import com.google.foods.utils.CommonValue;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ngova on 11-Aug-17.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private static final String TAG = OrderListAdapter.class.getSimpleName();
    private Context mContext;

    private List<ItemFood> mListFood;

    private OnOrderQuantityChangeListener mOnOrderQuantityChangeListener;

    private OnFoodItemQuantityChangeListenner mOnFoodItemQuantityChangeListenner;

    public List<ItemFood> getListFood() {
        return mListFood;
    }

    public void setListFood(List<ItemFood> mListFood) {
        this.mListFood = mListFood;
    }

    private int valueOrder, minOrder, maxOrder;

    public OrderListAdapter(Context context, List<ItemFood> listOrder, OnOrderQuantityChangeListener onOrderQuantityChangeListener, OnFoodItemQuantityChangeListenner onFoodItemQuantityChangeListenner) {
        mContext = context;
        mListFood = listOrder;
        mOnOrderQuantityChangeListener = onOrderQuantityChangeListener;
        mOnFoodItemQuantityChangeListenner = onFoodItemQuantityChangeListenner;

        valueOrder = CommonValue.DEFAULT_VALUE_INT_1;
        minOrder = CommonValue.DEFAULT_VALUE_INT_0;
        maxOrder = CommonValue.DEFAULT_VALUE_INT_99;
    }

    public void removeItemAtPosition(int position) {
        mListFood.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_order_confirm, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemFood itemFood = mListFood.get(position);
        Picasso.with(mContext).load(itemFood.getImage()).placeholder(R.drawable.ic_loading).error(R.drawable.ic_load_fail).into(holder.imgFoodImage);
        holder.txtFoodName.setText(itemFood.getName());
        holder.selectOrderView.setValue(itemFood.getOrderQuantity());
        holder.selectOrderView.setMaxValue(itemFood.getTotalQuantity() - itemFood.getOrderQuantity());

        holder.txtSinglePrice.setText(CommonMethod.getFormatedPrice(itemFood.getType()));

        holder.selectOrderView.setOnValueChangeListener(new SelectOrderView.OnValueChangeListener() {
            @Override
            public void onValueChange(int value) {
                Log.i(TAG, "Item " + itemFood.getName() + " has been changed quantity to " + value);
                itemFood.setOrderQuantity(value);
                int singlePrice = CommonMethod.getSinglePrice(itemFood.getType(), value);
                holder.txtSinglePrice.setText(CommonMethod.getFormatedPrice(singlePrice));
                if (mOnOrderQuantityChangeListener != null) {
                    mOnOrderQuantityChangeListener.onOrderQuantityChange(position, value);
                }
            }
        });

        final DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference().child(CommonValue.DATABASE_TABLE_FOOD).child(itemFood.getIdFood()).child(CommonValue.DATABASE_TABLE_ORDER_FIELD_ORDER_QUANTITY).getRef();
        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int totalOrderQuantity = snapshot.getValue(Integer.class);
                int remainQuantity = itemFood.getTotalQuantity() - totalOrderQuantity;
                Log.e(TAG, "remainQuantity: " + remainQuantity + ", orderQuantity: " + itemFood.getOrderQuantity());
                if (remainQuantity < itemFood.getOrderQuantity()) {
                    itemFood.setOrderQuantity(remainQuantity);
                    holder.selectOrderView.setValue(remainQuantity);
                    if (remainQuantity < 1) {
                        foodRef.removeEventListener(this);
                    }
                    if (mOnFoodItemQuantityChangeListenner != null && itemFood.getOrderQuantity() > 0) {
                        mOnFoodItemQuantityChangeListenner.onRemainingQuantityChange(position, remainQuantity);
                    }
                }

                holder.selectOrderView.setMaxValue(remainQuantity);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "UpdateFoodItemListenner onCancelled, " + databaseError);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListFood.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgFoodImage;
        private TextView txtFoodName;
        private TextView txtSinglePrice;
        private SelectOrderView selectOrderView;

        public ViewHolder(View v) {
            super(v);
            imgFoodImage = (ImageView) v.findViewById(R.id.iv_food_item);
            txtFoodName = (TextView) v.findViewById(R.id.tv_food_name);
            txtSinglePrice = (TextView) v.findViewById(R.id.tv_single_price);
            selectOrderView = (SelectOrderView) v.findViewById(R.id.choose_quantity_order);
            selectOrderView.setValue(valueOrder);
            selectOrderView.setMinValue(minOrder);
            selectOrderView.setMaxValue(maxOrder);
        }
    }

    public interface OnOrderQuantityChangeListener {
        void onOrderQuantityChange(int position, int quantity);
    }

    public interface OnFoodItemQuantityChangeListenner {
        void onRemainingQuantityChange(int position, int quantity);
    }
}
