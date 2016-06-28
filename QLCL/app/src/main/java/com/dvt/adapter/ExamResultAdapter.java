package com.dvt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dvt.qlcl.R;

import java.util.ArrayList;

/**
 * Created by Android on 12/14/2015.
 */
public class ExamResultAdapter extends BaseAdapter {
    private ArrayList<LearningResultItem> arrayLearn;
    private LayoutInflater mInflater;
    private Context mContext;

    public ExamResultAdapter(Context context, ArrayList<LearningResultItem> arrItem) {
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
            viewHolder.imgStart= (ImageView) view.findViewById(R.id.img_star);
            view.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) view.getTag();
        LearningResultItem item=arrayLearn.get(position);
        viewHolder.tvName.setText(item.getSubjectName());
        viewHolder.tvPoint1.setText("Điểm TL:"+item.getPoint1());
        viewHolder.tvPoint2.setText("Điểm Thi:"+item.getPoint2());
        viewHolder.tvPoint3.setText("Điểm TB:"+item.getPoint3());
        viewHolder.tvPointGPA.setText(item.getMediumScore());
        if(item.getMediumScore().equals("**")){
            viewHolder.imgStart.setImageResource(R.drawable.ic_star_border_white_low);
        }else{
            if(item.getMediumScore().equals("A") || item.getMediumScore().equals("B")){
                viewHolder.imgStart.setImageResource(R.drawable.ic_star_whitedp);
            }else if(item.getMediumScore().equals("C")){
                viewHolder.imgStart.setImageResource(R.drawable.ic_star_half_white);
            }else{
                viewHolder.imgStart.setImageResource(R.drawable.ic_star_border_white_low);
            }
        }
        return view;
    }
    class ViewHolder{
        TextView tvName;
        TextView tvPoint1;
        TextView tvPoint2;
        TextView tvPoint3;
        TextView tvPointGPA;
        ImageView imgStart;
    }
}
