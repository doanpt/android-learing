package com.dvt.samsung.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dvt.samsung.finalapp.MainFragmentActivity;
import com.dvt.samsung.finalapp.R;
import com.dvt.samsung.model.TypeItem;
import com.dvt.samsung.utils.CommonValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sev_user on 11/7/2016.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<TypeItem> arrType;
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TypeItem item = arrType.get(position);
        String name = item.getNameType();
        String number = item.getNumber();
        holder.tvTitle.setText(name);
        holder.tvNumber.setText(number);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.music_icon, opts);
        holder.ivType.setImageBitmap(bitmap);
//        Picasso.with(context).load(bitmap).into(holder.ivType);
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainFragmentActivity.class);
                intent.putExtra(CommonValue.KEY_MAIN_CLICK_ITEM, position);
                context.startActivity(intent);
            }
        });

    }

    public MainAdapter(Context context, ArrayList<TypeItem> arrType) {
        this.arrType = arrType;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return arrType.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvNumber;
        private ImageView ivType;
        private LinearLayout llMain;

        public ViewHolder(View view) {
            super(view);
            tvNumber = (TextView) view.findViewById(R.id.tv_number_type);
            tvTitle = (TextView) view.findViewById(R.id.tv_name_type);
            ivType = (ImageView) view.findViewById(R.id.iv_icon_type);
            llMain = (LinearLayout) view.findViewById(R.id.ll_main_type);
        }
    }
}
