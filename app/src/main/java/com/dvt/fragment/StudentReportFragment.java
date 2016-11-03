package com.dvt.fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dvt.item.ExamResultForReportItem;
import com.dvt.jsoup.HtmlParse;
import com.dvt.qlcl.R;
import com.dvt.util.CommonMethod;
import com.dvt.util.CommonValue;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Doanp on 6/25/2016.
 */

public class StudentReportFragment extends Fragment {
    String code;
    View myFragmentView;
    ProgressDialog mProgressDialog;
    private ArrayList<ExamResultForReportItem> examResultItems = new ArrayList<>();
    private HtmlParse htmlParse;
    Bundle bundle = new Bundle();
    private TextView mTVName, mTVCode, mTVClass;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.activity_student_report, container, false);
        code = CommonMethod.getCode(getContext());
        bundle.putString(CommonValue.KEY_CODE, code);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String mCode = preferences.getString(CommonValue.RESULT_CODE_STUDENT, "");
        if (!mCode.equalsIgnoreCase("")) {
            code = mCode;
        }
        setHasOptionsMenu(true);
        mTVName = (TextView) myFragmentView.findViewById(R.id.tv_name_s);
        mTVCode = (TextView) myFragmentView.findViewById(R.id.tv_code_s);
        mTVClass = (TextView) myFragmentView.findViewById(R.id.tv_class_s);
        htmlParse = new HtmlParse();
        new LearningResultForReportTask().execute("2!nocode");
        setHasOptionsMenu(true);
        return myFragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_student, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getContext().SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (CommonMethod.getInstance().isValid(query)) {
                    new LearningResultForReportTask().execute("1!" + query);
                    searchView.clearFocus();
                } else {
                    Toast.makeText(getContext(), "Mã sinh viên có 10 chữ số", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private class LearningResultForReportTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setTitle("Điểm tích lũy!");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] arrParam = params[0].split("!");
            int type = Integer.parseInt(arrParam[0]);
            String ttsv = htmlParse.getExamReport(type, arrParam[1], getContext());
            return ttsv;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                mProgressDialog.dismiss();
                String[] ttsv = result.split("-!!");
                mTVName.setText(ttsv[0].toString() + "");
                mTVCode.setText(ttsv[1].toString() + "");
                mTVClass.setText(ttsv[2].toString() + "");
                examResultItems = htmlParse.getArrExamReport();
                PieChart pieChart = (PieChart) myFragmentView.findViewById(R.id.chart);
                ArrayList<Entry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<String>();
                DecimalFormat df = new DecimalFormat("#,##");

                int size = examResultItems.size();
                if (size == 0) {
                    setReportError(pieChart);
                    Toast.makeText(getContext(), getString(R.string.toast_chua_nop_le_phi), Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < size; i++) {
                        if (i == size - 1) {
                        } else {
                            ExamResultForReportItem examResultItem = examResultItems.get(i);
                            if (examResultItem.getPoint2().equals("**")) {
                                examResultItems.clear();
                                setReportError(pieChart);
                                Toast.makeText(getContext(), getString(R.string.toast_chua_nop_le_phi), Toast.LENGTH_SHORT).show();
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
                    int a = 0, b = 0, c = 0, d = 0, f = 0, bCong = 0, cCong = 0, dCong = 0;
                    int tinchi = 0, tinchia = 0, tinchib = 0, tinchic = 0, tinchid = 0, tinchif = 0, tinchiBCong = 0, tinchiCCong = 0, tinchiDCong = 0;
                    for (ExamResultForReportItem item : examResultItems) {
                        if (item.getPoint2().equals("A")) {
                            a++;
                            tinchia += Integer.parseInt(item.getInchi());
                            tinchi += Integer.parseInt(item.getInchi());
                        } else if (item.getPoint2().equals("B+")) {
                            bCong++;
                            tinchiBCong += Integer.parseInt(item.getInchi());
                            tinchi += Integer.parseInt(item.getInchi());
                        } else if (item.getPoint2().equals("B")) {
                            b++;
                            tinchib += Integer.parseInt(item.getInchi());
                            tinchi += Integer.parseInt(item.getInchi());
                        } else if (item.getPoint2().equals("C")) {
                            c++;
                            tinchic += Integer.parseInt(item.getInchi());
                            tinchi += Integer.parseInt(item.getInchi());
                        } else if (item.getPoint2().equals("C+")) {
                            cCong++;
                            tinchiCCong += Integer.parseInt(item.getInchi());
                            tinchi += Integer.parseInt(item.getInchi());
                        } else if (item.getPoint2().equals("D+")) {
                            dCong++;
                            tinchiDCong += Integer.parseInt(item.getInchi());
                            tinchi += Integer.parseInt(item.getInchi());
                        } else if (item.getPoint2().equals("D")) {
                            d++;
                            tinchid += Integer.parseInt(item.getInchi());
                            tinchi += Integer.parseInt(item.getInchi());
                        } else if (item.getPoint2().equals("I")) {
                            continue;

                        } else {
                            f++;
                        }
                    }
                    int dem = 0;
                    if (a != 0) {
                        float pTA = ((float) tinchia / tinchi) * 100;
                        entries.add(new Entry(Float.parseFloat(df.format(pTA)), dem));
                        labels.add("% A");
                        dem++;
                    }
                    if (bCong != 0) {
                        float pTBCong = ((float) tinchiBCong / tinchi) * 100;
                        entries.add(new Entry(Float.parseFloat(df.format(pTBCong)), dem));
                        labels.add("% B+");
                        dem++;
                    }
                    if (b != 0) {
                        float pTB = ((float) tinchib / tinchi) * 100;
                        entries.add(new Entry(Float.parseFloat(df.format(pTB)), dem));
                        labels.add("% B");
                        dem++;
                    }
                    if (cCong != 0) {
                        float pTC = ((float) tinchiCCong / tinchi) * 100;
                        entries.add(new Entry(Float.parseFloat(df.format(pTC)), dem));
                        labels.add("% C+");
                        dem++;
                    }
                    if (c != 0) {
                        float pTC = ((float) tinchic / tinchi) * 100;
                        entries.add(new Entry(Float.parseFloat(df.format(pTC)), dem));
                        labels.add("% C");
                        dem++;
                    }
                    if (dCong != 0) {
                        float pTD = ((float) tinchiDCong / tinchi) * 100;
                        entries.add(new Entry(Float.parseFloat(df.format(pTD)), dem));
                        labels.add("% D+");
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

                    Float DTB = (float) (tinchia * 4 + tinchiBCong * 3.5 + tinchiCCong * 2.5 + tinchiDCong * 1.5 + tinchib * 3 + tinchic * 2 + tinchid * 1) / tinchi;
                    String DTBText = String.format("%.2f", DTB);
                    if(DTBText.equals("NaN")){
                        setReportError(pieChart);
                    }else {
                        PieDataSet dataset = new PieDataSet(entries, "# Type Point");
                        PieData data = new PieData(labels, dataset);
                        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                        data.setValueTextSize(13f);

                        pieChart.setDescription("Tích lũy:  " + DTBText);
                        pieChart.setData(data);
                        pieChart.setCenterText(DTBText);
                        pieChart.setCenterTextColor(Color.RED);
                        pieChart.setCenterTextSize(50);
                        pieChart.animateY(3000);

                        pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Load dữ liệu lỗi. Làm ơn thử lại!", Toast.LENGTH_SHORT).show();
            }
        }

        private void setReportError(PieChart pieChart) {
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<String>();
            PieDataSet dataset = new PieDataSet(entries, "# Type Point");
            PieData data = new PieData(labels, dataset);
            dataset.setColors(ColorTemplate.COLORFUL_COLORS);
            data.setValueTextSize(13f);
            pieChart.setDescription("Tích lũy:  *.*");
            pieChart.setData(data);
            pieChart.setCenterText(getString(R.string.toast_chua_nop_le_phi));
            pieChart.setCenterTextColor(Color.RED);
            pieChart.setCenterTextSize(20);
            pieChart.animateY(3000);
            pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image
        }
    }


}
