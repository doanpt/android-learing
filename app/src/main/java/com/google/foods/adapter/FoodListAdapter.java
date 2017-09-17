package com.google.foods.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.foods.R;
import com.google.foods.activity.MainActivity;
import com.google.foods.customeview.SelectOrderView;
import com.google.foods.models.ItemFood;
import com.google.foods.utils.CommonValue;
import com.google.foods.utils.UserInfor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 7/27/2017.
 */

public class FoodListAdapter extends RecyclerView.Adapter {

    public static final int TYPE_FOOTER_RECYCLER_VIEW = 99;
    public static final int TYPE_RECYCLER_VIEW = 1;


    private List<ItemFood> arrFoodItem = new ArrayList<>();
    private Context mcontext;
    private ViewHolder vh;
    //    private OnFoodClickItem onFoodClickItem;
    private int valueOrder, minOrder;
    private MainActivity mActivity;

    // Provide a suitable constructor (depends on the kind of dataset)
    public FoodListAdapter(Context context, List<ItemFood> arrFoodSet) {
        this.arrFoodItem.addAll(arrFoodSet);
        this.mcontext = context;
        mActivity= (MainActivity) context;
        valueOrder = CommonValue.DEFAULT_VALUE_INT_0;
        minOrder = CommonValue.DEFAULT_VALUE_INT_0;
//        this.onFoodClickItem=onFoodClickItem;
    }

    public FoodListAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view

        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_RECYCLER_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_display, parent, false);
            viewHolder = new ViewHolder(view);
        } else if (viewType == TYPE_FOOTER_RECYCLER_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_recycler_view_food, parent, false);
            viewHolder = new FooterViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            ViewHolder holder = (ViewHolder) viewHolder;
            if (arrFoodItem.size() > 0) {
                if (position < (getItemCount() - 1)) {
                    ItemFood foodItem = arrFoodItem.get(position);
                    holder.txtFoodName.setText(foodItem.getName());
                    int remainingFood = foodItem.getTotalQuantity() - foodItem.getOrderQuantity();
                    if (remainingFood == 0) {
                        holder.txtRemaining.setTextColor(mcontext.getResources().getColor(android.R.color.holo_red_light));
                        holder.txtRemaining.setText(mcontext.getResources().getString(R.string.out_of_stock));
                    } else {
                        holder.txtRemaining.setTextColor(mcontext.getResources().getColor(R.color.color_item_food));
                        holder.txtRemaining.setText(remainingFood + CommonValue.BLANK);
                    }
                    holder.selectOrderView.setMaxValue(remainingFood);
                    if (holder.selectOrderView.getValue() > remainingFood) {
                        holder.selectOrderView.setValue(remainingFood);
                        foodItem.setOrderQuantity(remainingFood);
                        mActivity.onItemClick(foodItem);
                    }
                    Picasso.with(mcontext)
                            .load(foodItem.getImage()).placeholder(R.drawable.ic_loading).error(R.drawable.ic_load_fail)
                            .into(holder.imgFood);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER_RECYCLER_VIEW;
        }
        return TYPE_RECYCLER_VIEW;
    }

    private boolean isPositionFooter(int position) {
        return position == (getItemCount() - 1);
    }


    @Override
    public int getItemCount() {
        return arrFoodItem.size() + 1;
    }

    public void add(int position, ItemFood item) {
        arrFoodItem.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        arrFoodItem.remove(position);
        notifyItemRemoved(position);
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtFoodName;
        private TextView txtRemaining;
        private SelectOrderView selectOrderView;
        private View layout;
        private ImageView imgFood;


        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtFoodName = (TextView) v.findViewById(R.id.tv_food_name);
            txtRemaining = (TextView) v.findViewById(R.id.tv_remaining_count);
            selectOrderView = (SelectOrderView) v.findViewById(R.id.choose_quantity_order);
            selectOrderView.setValue(valueOrder);
            selectOrderView.setMinValue(minOrder);
            //listen onlick plus button and get position in array and value which user selected
            selectOrderView.setOnButtonPlusClickListener(new SelectOrderView.OnButtonPlusClickListener() {
                @Override
                public void onClickButtonPlus() {
                    if (onClickButtonPlusAdapter != null) {
                        int position = getAdapterPosition();
                        onClickButtonPlusAdapter.onClickButtonPlusAdapter(position, selectOrderView.getValue());
                    }
                }
            });
            //listen onlick minus button and get position in array and value which user selected
            selectOrderView.setOnButtonMinusClickListener(new SelectOrderView.OnButtonMinusClickListener() {
                @Override
                public void onClickButtonMinus() {
                    if (onClickButtonMinusAdapter != null) {
                        int position = getAdapterPosition();
                        onClickButtonMinusAdapter.onClickButtonMinusAdapter(position, selectOrderView.getValue());
                    }
                }
            });
            imgFood = (ImageView) v.findViewById(R.id.img_food);

        }


    }

    public int getValueOrder() {
        return valueOrder;
    }

    public void setValueOrder(int valueOrder) {
        this.valueOrder = valueOrder;
    }

    public int getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(int minOrder) {
        this.minOrder = minOrder;
    }

    private OnClickButtonPlusAdapter onClickButtonPlusAdapter;
    private OnClickButtonMinusAdapter onClickButtonMinusAdapter;

    public interface OnClickButtonPlusAdapter {
        void onClickButtonPlusAdapter(int position, int value);
    }

    public interface OnClickButtonMinusAdapter {
        void onClickButtonMinusAdapter(int position, int value);
    }

    public void setOnClickButtonPlusAdapter(OnClickButtonPlusAdapter onClickButtonPlusAdapter) {
        this.onClickButtonPlusAdapter = onClickButtonPlusAdapter;
    }

    public void setOnClickButtonMinusAdapter(OnClickButtonMinusAdapter onClickButtonMinusAdapter) {
        this.onClickButtonMinusAdapter = onClickButtonMinusAdapter;
    }

    public void setArrFoodItem(List<ItemFood> arrFoodItem) {
        this.arrFoodItem = arrFoodItem;
    }
}

