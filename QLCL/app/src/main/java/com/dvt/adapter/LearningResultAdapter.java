package com.dvt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dvt.qlcl.R;

import java.util.ArrayList;

/**
 * Created by Android on 12/14/2015.
 */
public class LearningResultAdapter extends BaseAdapter {
    private ArrayList<LearningResultItem> arrayLearn;
    private LayoutInflater mInflater;
    private Context mContext;

    public LearningResultAdapter(Context context,ArrayList<LearningResultItem> arrItem) {
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
        View view=null;
        if(view==null){
            view=mInflater.inflate(R.layout.layout_learning_result,null);
            viewHolder=new ViewHolder();
            viewHolder.tvName= (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tvPoint1= (TextView) view.findViewById(R.id.tv_point1);
            viewHolder.tvPoint2= (TextView) view.findViewById(R.id.tv_point2);
            viewHolder.tvPoint3= (TextView) view.findViewById(R.id.tv_point3);
            viewHolder.tvPointGPA= (TextView) view.findViewById(R.id.tv_point_gpa);
            view.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) view.getTag();
        LearningResultItem item=arrayLearn.get(position);
        viewHolder.tvName.setText(item.getSubjectName());
        viewHolder.tvPoint1.setText("Điểm L1:"+item.getPoint1());
        viewHolder.tvPoint2.setText("Điểm L2:"+item.getPoint2());
        viewHolder.tvPoint3.setText("Điểm L3:"+item.getPoint3());
        viewHolder.tvPointGPA.setText("ĐTB\n "+item.getMediumScore());
        return view;
    }
    class ViewHolder{
        TextView tvName;
        TextView tvPoint1;
        TextView tvPoint2;
        TextView tvPoint3;
        TextView tvPointGPA;
    }
}
