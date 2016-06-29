package com.dvt.qlcl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.dvt.fragment.ExamResultFragment;
import com.dvt.fragment.ExamScheduleFragment;
import com.dvt.fragment.InformationDeveloperFragment;
import com.dvt.fragment.LearingResultFragment;
import com.dvt.item.ExamResultForReportItem;
import com.dvt.jsoup.HtmlParse;
import com.dvt.util.CommonMethod;
import com.dvt.util.CommonValue;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Doanp on 6/25/2016.
 */

public class StudentReport extends Fragment {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    String code;
    View myFragmentView;
    ProgressDialog mProgressDialog;
    private ArrayList<ExamResultForReportItem> examResultItems = new ArrayList<>();
    private HtmlParse htmlParse;
    Bundle bundle=new Bundle();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.activity_student_report, container, false);
        code= CommonMethod.getCode(getContext());
        bundle.putString("Code",code);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String mCode = preferences.getString(CommonValue.RESULT_CODE_STUDENT, "");
        if(!mCode.equalsIgnoreCase(""))
        {
            code = mCode;
        }
        htmlParse = new HtmlParse(CommonMethod.getInstance().getFile(getContext(),"diemthi.txt"));
        htmlParse.setCode(code);
        new LearningResultForReportTask().execute();
        return myFragmentView;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_report);
//
//    }

    private class LearningResultForReportTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setTitle("GPA!");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            htmlParse.getExamReport();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            examResultItems = htmlParse.getArrExamReport();
            int size = examResultItems.size();
            if (size == 0) {
                Toast.makeText(getContext(), "Bạn chưa nộp đủ lệ phí thi", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < size; i++) {
                    if (i == size - 1) {

                    } else {
                        ExamResultForReportItem examResultItem = examResultItems.get(i);
                        if (examResultItem.getPoint2().equals("**")) {
                            examResultItems.clear();
                            Toast.makeText(getContext(), "Bạn chưa nộp đủ lệ phí thi", Toast.LENGTH_SHORT).show();
                            break;
                        } else if (examResultItem.getName().indexOf("Giáo dục thể chất") >= 0 || examResultItem.getPoint2().equals("I")) {
                            examResultItems.remove(examResultItem);
                            size = examResultItems.size();
                            i--;
                        } else {
                            for (int j = i + 1; j < size; j++) {
                                ExamResultForReportItem item = examResultItems.get(j);
                                if (examResultItem.getName().equals(item.getName())) {
                                    if (Float.parseFloat(examResultItem.getPoint1()) < Float.parseFloat(item.getPoint1())) {
                                        examResultItems.remove(examResultItem);
                                        i--;
                                        size = examResultItems.size();
                                        break;
                                    } else {
                                        examResultItems.remove(item);
                                        i--;
                                        size = examResultItems.size();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                for(int i=0;i<examResultItems.size();i++){
                    Log.d("Item",examResultItems.get(i).getName()+"--"+examResultItems.get(i).getInchi()+"--"+examResultItems.get(i).getPoint2());
                }
                Log.d("SizeF",examResultItems.size()+"--");
                int a = 0, b = 0, c = 0, d = 0, f = 0;
                int tinchi = 0, tinchia = 0, tinchib = 0, tinchic = 0, tinchid = 0, tinchif = 0;
                for (ExamResultForReportItem item : examResultItems) {
                    if (item.getPoint2().equals("A")) {
                        a++;
                        Log.d("TCA", item.getName() + "-" + item.getPoint1() + "-" + item.getPoint2() + "-" + item.getInchi());
                        tinchia += Integer.parseInt(item.getInchi());
                        tinchi += Integer.parseInt(item.getInchi());
                    } else if (item.getPoint2().equals("B")) {
                        b++;
                        Log.d("TCB", item.getName() + "-" + item.getPoint1() + "-" + item.getPoint2() + "-" + item.getInchi());
                        tinchib += Integer.parseInt(item.getInchi());
                        tinchi += Integer.parseInt(item.getInchi());
                    } else if (item.getPoint2().equals("C")) {
                        c++;
                        Log.d("TCC", item.getName() + "-" + item.getPoint1() + "-" + item.getPoint2() + "-" + item.getInchi());
                        tinchic += Integer.parseInt(item.getInchi());
                        tinchi += Integer.parseInt(item.getInchi());
                    } else if (item.getPoint2().equals("D")) {
                        d++;
                        Log.d("TCD", item.getName() + "-" + item.getPoint1() + "-" + item.getPoint2() + "-" + item.getInchi());
                        tinchid += Integer.parseInt(item.getInchi());
                        tinchi += Integer.parseInt(item.getInchi());
                    } else if (item.getPoint2().equals("I")) {
                        Log.d("TCI", item.getName() + "-" + item.getPoint1() + "-" + item.getPoint2() + "-" + item.getInchi());
                        continue;

                    } else {
                        Log.d("TCF", item.getName() + "-" + item.getPoint1() + "-" + item.getPoint2() + "-" + item.getInchi());
                        f++;
                    }
                }
                PieChart pieChart = (PieChart) myFragmentView.findViewById(R.id.chart);
                ArrayList<Entry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<String>();
                DecimalFormat df = new DecimalFormat("#,##");
                int dem = 0;
                if (a != 0) {
                    float pTA = ((float) tinchia / tinchi) * 100;
                    entries.add(new Entry(Float.parseFloat(df.format(pTA)), dem));
                    labels.add("% A");
                    dem++;
                }
                if (b != 0) {
                    float pTB = ((float) tinchib / tinchi) * 100;
                    entries.add(new Entry(Float.parseFloat(df.format(pTB)), dem));
                    labels.add("% B");
                    dem++;
                }
                if (c != 0) {
                    float pTC = ((float) tinchic / tinchi) * 100;
                    entries.add(new Entry(Float.parseFloat(df.format(pTC)), dem));
                    labels.add("% C");
                    dem++;
                }
                if (d != 0) {
                    float pTD = ((float) tinchid / tinchi) * 100;
                    entries.add(new Entry(Float.parseFloat(df.format(pTD)), dem));
                    labels.add("% D");
                    dem++;
                }
                if (f != 0) {
                    float pTF = ((float) tinchif / tinchi) * 100;
                    entries.add(new Entry(Float.parseFloat(df.format(pTF)), dem));
                    labels.add("% F");
                    dem++;
                }
                Float DTB = (float) (tinchia * 4 + tinchib * 3 + tinchic * 2 + tinchid * 1) / tinchi;
                Log.d("ABC",tinchia+"--"+tinchib+"--"+tinchic+"--"+tinchid+"--"+tinchi);
                String DTBText = df.format(DTB);
                PieDataSet dataset = new PieDataSet(entries, "# Type Point");
                PieData data = new PieData(labels, dataset);
                dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
                pieChart.setDescription("Description");
                pieChart.setData(data);
                pieChart.setCenterText(DTBText);
                pieChart.setCenterTextColor(Color.RED);
                pieChart.setCenterTextSize(40);
                pieChart.setCenterTextSizePixels(50);
                pieChart.animateY(5000);
                pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image
            }
        }
    }

}
