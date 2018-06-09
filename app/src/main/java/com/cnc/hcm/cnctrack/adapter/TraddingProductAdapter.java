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
import com.cnc.hcm.cnctrack.model.SearchModel;
import com.cnc.hcm.cnctrack.model.detailproduct.Product;
import com.cnc.hcm.cnctrack.util.CommonMethod;
import com.cnc.hcm.cnctrack.util.Conts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by giapmn on 1/30/18.
 */

public class TraddingProductAdapter extends RecyclerView.Adapter<TraddingProductAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Product> arr = new ArrayList<>();
    private ArrayList<Product> arrTemp = new ArrayList<>();

    private OnItemInputClickListener onItemInputClickListener;


    public TraddingProductAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tradding_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product result = arr.get(position);
        String urlPhoto = Conts.URL_BASE + result.getPhoto();
        if (!urlPhoto.equals(Conts.BLANK))
            Picasso.with(context).load(urlPhoto).into(holder.imvIcon);

        holder.tvTitle.setText(result.getName());
        holder.tvTypeManufacture.setText("Loại: " + result.getCategory().getTitle() + "    NSX: " + result.getBrand().getName());
        holder.tvTypeManufacture.setSelected(true);
        holder.tvPriceUnit.setText("Giá: " + CommonMethod.formatMoney(result.getPrice()) + " đ    Đvt: " + result.getUnit().getTitle());
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public Product getItem(int position) {
        return arr.get(position);
    }

    public void notiData(List<Product> result) {
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
            imvIcon = (ImageView) itemView.findViewById(R.id.imv_icon_product);
            imvInput = (ImageView) itemView.findViewById(R.id.imv_input);
            imvInput.setOnClickListener(this);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_tite_trading_product);
            tvTypeManufacture = (TextView) itemView.findViewById(R.id.tv_type_manufacture);
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


    public void filter(SearchModel model) {
        arr.clear();
        String text, branch, category;
        text = model.getText().toLowerCase();
        branch = model.getBranch().toLowerCase();
        category = model.getCategory().toLowerCase();
        text = text.toLowerCase(Locale.getDefault());
        boolean isText = false;
        boolean isBranch = false;
        boolean isCategory = false;
        isText = "".equals(model.getText()) ? false : true;
        isBranch = branch.equals(context.getResources().getString(R.string.spn_branch_default).toLowerCase()) ? false : true;
        isCategory = category.equals(context.getResources().getString(R.string.spn_category_default).toLowerCase()) ? false : true;

        if (isText) {
            ArrayList<Product> arrTemp2 = new ArrayList<>();
            arrTemp2.addAll(arr);
            arr.clear();
            for (int i = 0; i < arrTemp.size(); i++) {
                if (arrTemp.get(i).getName().toLowerCase(Locale.getDefault()).contains(text)) {
                    arr.add(arrTemp.get(i));
                }
            }
//            for (int i = 0; i < arrTemp2.size(); i++) {
//                if (arrTemp2.get(i).getBrand().getName().toLowerCase().equals(branch)) {
//                    arr.add(arrTemp2.get(i));
//                }
//            }
        } else {
            arr.addAll(arrTemp);
        }
        if (isBranch) {
            ArrayList<Product> arrTemp2 = new ArrayList<>();
            arrTemp2.addAll(arr);
            arr.clear();
            for (int i = 0; i < arrTemp2.size(); i++) {
                if (arrTemp2.get(i).getBrand().getName().toLowerCase().equals(branch)) {
                    arr.add(arrTemp2.get(i));
                }
            }
        }
        if (isCategory) {
            ArrayList<Product> arrTemp3 = new ArrayList<>();
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
