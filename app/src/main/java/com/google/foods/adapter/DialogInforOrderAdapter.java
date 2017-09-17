package com.google.foods.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.foods.R;
import com.google.foods.models.ItemFood;
import com.google.foods.utils.CommonMethod;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by giapmn on 8/21/17.
 */

public class DialogInforOrderAdapter extends RecyclerView.Adapter<DialogInforOrderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ItemFood> arrItemFood = new ArrayList<>();

    public DialogInforOrderAdapter(Context context, ArrayList<ItemFood> arrItemFood) {
        this.context = context;
        this.arrItemFood = arrItemFood;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_dialog_infor_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemFood itemFood = arrItemFood.get(position);
        holder.tvFoodName.setText(itemFood.getName());
        holder.tvFoodType.setText(CommonMethod.convertMoneyToVND(itemFood.getType()));
        holder.tvFoodQuantity.setText(itemFood.getOrderQuantity() +"");
        Picasso.with(context).load(itemFood.getImage()).placeholder(R.drawable.ic_loading).error(R.drawable.ic_load_fail).into(holder.imvImageFood);
    }

    @Override
    public int getItemCount() {
        return arrItemFood.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imvImageFood;
        private TextView tvFoodName;
        private TextView tvFoodType;
        private TextView tvFoodQuantity;

        public ViewHolder(View view) {
            super(view);
            imvImageFood = (ImageView) itemView.findViewById(R.id.itemfood_dialog_imv_food);
            tvFoodName = (TextView) itemView.findViewById(R.id.itemfood_dialog_tv_title_food_name);
            tvFoodType = (TextView) itemView.findViewById(R.id.itemfood_dialog_tv_title_type_food);
            tvFoodQuantity = (TextView) itemView.findViewById(R.id.itemfood_dialog_tv_title_quantity);
        }
    }
}
