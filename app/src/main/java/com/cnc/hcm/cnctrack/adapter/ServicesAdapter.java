package com.cnc.hcm.cnctrack.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.event.OnItemInputClickListener;
import com.cnc.hcm.cnctrack.model.SearchServiceModel;
import com.cnc.hcm.cnctrack.model.Services;
import com.cnc.hcm.cnctrack.model.detailproduct.Service;
import com.cnc.hcm.cnctrack.util.CommonMethod;
import com.cnc.hcm.cnctrack.util.Conts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by giapmn on 1/30/18.
 */

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Service> arr = new ArrayList<>();
    private ArrayList<Service> arrTemp = new ArrayList<>();
    private OnItemInputClickListener onItemInputClickListener;

    public ServicesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tradding_product, parent, false);
        return new ViewHolder(view);
    }

    public Service getItem(int pos) {
        return arr.get(pos);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Service result = arr.get(position);
        String photo=result.getPhoto();
        if(photo==null || photo.isEmpty()){
            photo=result.getCategory().getPhoto();
        }
        String urlPhoto = Conts.URL_BASE + result.getPhoto();
        if (!urlPhoto.equals(Conts.BLANK))
            Picasso.with(context).load(urlPhoto).placeholder(R.drawable.ic_bg_place_error).into(holder.imvIcon);

        holder.tvTitle.setText(result.getName());
        holder.tvPriceUnit.setText("Giá: " + CommonMethod.formatMoney(Integer.parseInt(String.valueOf(result.getPrice()))) + " đ    Đvt: " + result.getUnit().getTitle());
        holder.tvTypeManufacture.setText("Loại: " + result.getCategory().getTitle());
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public void notiData(List<Service> result) {
        if (result != null) {
            if (arr != null) {
                arr.clear();
                arr.addAll(result);
            }
            if (arrTemp != null) {
                arrTemp.clear();
                arrTemp.addAll(result);
            }
            notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imvIcon, imvInput;
        private TextView tvTitle, tvTypeManufacture, tvPriceUnit;


        public ViewHolder(View itemView) {
            super(itemView);
            imvIcon = itemView.findViewById(R.id.imv_icon_product);
            imvInput = itemView.findViewById(R.id.imv_input);
            imvInput.setOnClickListener(this);
            tvTitle = itemView.findViewById(R.id.tv_tite_trading_product);
            tvTypeManufacture = itemView.findViewById(R.id.tv_type_manufacture);
            tvPriceUnit = itemView.findViewById(R.id.tv_price_unit);
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

    public void filter(SearchServiceModel model) {
        arr.clear();
        String text, category;
        text = model.getName().toLowerCase();
        category = model.getCategory().toLowerCase();
        boolean isText = false;
        boolean isCategory = false;
        isText = "".equals(model.getName()) ? false : true;
        isCategory = category.equals(context.getResources().getString(R.string.spn_category_service_default).toLowerCase()) ? false : true;

        if (isText) {
            ArrayList<Service> arrTemp2 = new ArrayList<>();
            arrTemp2.addAll(arr);
            arr.clear();
            for (int i = 0; i < arrTemp.size(); i++) {
                if (arrTemp.get(i).getName().toLowerCase(Locale.getDefault()).contains(text)) {
                    arr.add(arrTemp.get(i));
                }
            }
//            for (int i = 0; i < arrTemp2.size(); i++) {
//                if (arrTemp2.get(i).getName().toLowerCase().equals(text)) {
//                    arr.add(arrTemp2.get(i));
//                }
//            }
        } else {
            arr.addAll(arrTemp);
        }
        if (isCategory) {
            ArrayList<Service> arrTemp3 = new ArrayList<>();
            arrTemp3.addAll(arr);
            arr.clear();
            for (int i = 0; i < arrTemp3.size(); i++) {
                if (arrTemp3.get(i).getCategory().getTitle().toLowerCase().equals(category)) {
                    arr.add(arrTemp3.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setOnItemInputClickListener(OnItemInputClickListener onItemInputClickListener) {
        this.onItemInputClickListener = onItemInputClickListener;
    }
}
