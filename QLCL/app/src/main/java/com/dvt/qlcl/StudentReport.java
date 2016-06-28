package com.dvt.qlcl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.dvt.adapter.ExamResultItem;
import com.dvt.adapter.HtmlParse;
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
import java.util.Collections;
/**
 * Created by Doanp on 6/25/2016.
 */

public class StudentReport extends Activity {
    String code;
    ProgressDialog mProgressDialog;
    private ArrayList<ExamResultItem> examResultItems = new ArrayList<>();
    private HtmlParse htmlParse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_report);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mCode = preferences.getString(MainActivity.RESULT_CODE_STUDENT, "");
        if(!mCode.equalsIgnoreCase(""))
        {
            code = mCode;  /* Edit the value here*/
        }
        htmlParse = new HtmlParse(getFile("diemthi.txt"));
        htmlParse.setCode(code);
        new LearningResult().execute();
    }

    public String getFile(String filename) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(filename), "UTF-8"));
            String mLine;
            StringBuilder sb = new StringBuilder();
            while ((mLine = reader.readLine()) != null) {
                sb.append(mLine);
            }
            return sb.toString();
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return null;
    }

    private class LearningResult extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            htmlParse.getExamReport();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            examResultItems = htmlParse.getArrExamReport();
            int size = examResultItems.size();
            if (size == 0) {
                Toast.makeText(StudentReport.this, "Bạn chưa nộp đủ lệ phí thi", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < size; i++) {
                    if (i == size - 1) {

                    } else {
                        ExamResultItem examResultItem = examResultItems.get(i);
                        if (examResultItem.getPoint2().equals("**")) {
                            examResultItems.clear();
                            Toast.makeText(StudentReport.this, "Bạn chưa nộp đủ lệ phí thi", Toast.LENGTH_SHORT).show();
                            break;
                        } else if (examResultItem.getName().indexOf("Giáo dục thể chất") >= 0 || examResultItem.getPoint2().equals("I")) {
                            examResultItems.remove(examResultItem);
                            size = examResultItems.size();
                            i--;
                        } else {
                            for (int j = i + 1; j < size; j++) {
                                ExamResultItem item = examResultItems.get(j);
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
                for (ExamResultItem item : examResultItems) {
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
                PieChart pieChart = (PieChart) findViewById(R.id.chart);
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
