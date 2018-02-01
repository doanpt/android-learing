package com.cnc.hcm.cnctracking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.event.OnItemInputClickListener;
import com.cnc.hcm.cnctracking.model.Services;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giapmn on 1/30/18.
 */

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Services.Result> arr = new ArrayList<>();
    private OnItemInputClickListener onItemInputClickListener;

    public ServicesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tradding_product, parent, false);
        return new ViewHolder(view);
    }
    public Services.Result getItem(int pos){
        return arr.get(pos);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Services.Result result = arr.get(position);
        String urlPhoto = Conts.URL_BASE + result.getPhoto();
        if (!urlPhoto.equals(Conts.BLANK))
            Picasso.with(context).load(urlPhoto).into(holder.imvIcon);

        holder.tvTitle.setText(result.getName());

        holder.tvPriceUnit.setText("Giá: " + CommonMethod.formatMoney(Integer.parseInt(String.valueOf(result.getPrice()))) + " đ");
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public void notiData(List<Services.Result> result) {
        arr.clear();
        arr.addAll(result);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imvIcon, imvInput;
        private TextView tvTitle, tvTypeManufacture, tvPriceUnit;


        public ViewHolder(View itemView) {
            super(itemView);
            imvIcon = (ImageView) itemView.findViewById(R.id.imv_icon_product);
            imvInput = (ImageView) itemView.findViewById(R.id.imv_input);
            imvInput.setOnClickListener(this);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_tite_trading_product);
            tvTypeManufacture = (TextView) itemView.findViewById(R.id.tv_type_manufacture);
            tvTypeManufacture.setVisibility(View.GONE);
            tvPriceUnit = (TextView) itemView.findViewById(R.id.tv_price_unit);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imv_input:
                    if (onItemInputClickListener != null) {
                        onItemInputClickListener.onClickInput(getAdapterPosition());
                    }
                    break;
            }
        }
    }


    public void setOnItemInputClickListener(OnItemInputClickListener onItemInputClickListener) {
        this.onItemInputClickListener = onItemInputClickListener;
    }
}
