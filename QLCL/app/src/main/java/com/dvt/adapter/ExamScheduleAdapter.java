package com.dvt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dvt.item.ExamScheduleItem;
import com.dvt.qlcl.R;

import java.util.ArrayList;

/**
 * Created by Android on 12/14/2015.
 */
public class ExamScheduleAdapter extends BaseAdapter {
    private ArrayList<ExamScheduleItem> arrayLearn;
    private LayoutInflater mInflater;
    private Context mContext;

    public ExamScheduleAdapter(Context context, ArrayList<ExamScheduleItem> arrItem) {
        this.mContext=context;
        this.arrayLearn=arrItem;
        this.mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayLearn.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayLearn.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = null;
        if(view==null){
            view=mInflater.inflate(R.layout.layout_exam_schedule_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tvName= (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tvNgayThi= (TextView) view.findViewById(R.id.tv_point1);
            viewHolder.tvGioThi= (TextView) view.findViewById(R.id.tv_point2);
            viewHolder.tvSoBD= (TextView) view.findViewById(R.id.tv_point3);
            viewHolder.tvPhongthi= (TextView) view.findViewById(R.id.tv_point4);
            view.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) view.getTag();
        ExamScheduleItem item=arrayLearn.get(position);
        viewHolder.tvName.setText(item.getTenMon());
        viewHolder.tvNgayThi.setText(item.getNgayThi());
        viewHolder.tvGioThi.setText(item.getCaThi());
        viewHolder.tvSoBD.setText(item.getSoBaoDanh());
        viewHolder.tvPhongthi.setText(item.getPhongThi());
        return view;
    }
    class ViewHolder{
        TextView tvName;
        TextView tvNgayThi;
        TextView tvGioThi;
        TextView tvSoBD;
        TextView tvPhongthi;
    }
}
