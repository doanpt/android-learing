package com.dvt.fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dvt.adapter.ExamScheduleAdapter;
import com.dvt.item.ExamScheduleItem;
import com.dvt.jsoup.HtmlParse;
import com.dvt.qlcl.R;
import com.dvt.util.CommonMethod;
import com.dvt.util.CommonValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by DoanPT1 on 6/23/2016.
 */
public class ExamScheduleFragment extends Fragment {
    String code;
    private ListView lvExamSchedule;
    private ExamScheduleAdapter adapter;
    ProgressDialog mProgressDialog;
    private ArrayList<ExamScheduleItem> arrExamSchedule = new ArrayList<>();
    private HtmlParse htmlParse;
    View myFragmentView;

    public ExamScheduleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_exam_schedule, container, false);
        initView();
        setHasOptionsMenu(true);
        return myFragmentView;
    }

    private void initView() {
        htmlParse = new HtmlParse();
        new ExamScheduleTask().execute("2!nocode");
        Log.d("LoadType", "offile");
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
                    new ExamScheduleTask().execute("1!" + query);
                    searchView.clearFocus();
                } else {
                    Toast.makeText(getContext(), "Mã sinh viên có 10 chữ số", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private class ExamScheduleTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("Lịch thi!");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] arrParam = params[0].split("!");
            int type = Integer.parseInt(arrParam[0]);
            String ttsv = htmlParse.getExamSchedule(type, arrParam[1], getContext());
            return ttsv;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            try {
                String[] ttsv = result.split("-!!");
                lvExamSchedule = (ListView) myFragmentView.findViewById(R.id.lv_exam_schedule);
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View header = inflater.inflate(R.layout.item_header_listview, lvExamSchedule, false);
                header.setTag(this.getClass().getSimpleName() + "header");
                if (lvExamSchedule.getHeaderViewsCount() > 0) {
                    View oldView = lvExamSchedule.findViewWithTag(this.getClass().getSimpleName() + "header");
                    if (oldView != null) {
                        lvExamSchedule.removeHeaderView(oldView);
                    }
                }
                TextView mTVName = (TextView) header.findViewById(R.id.tv_name_head);
                TextView mTVCode = (TextView) header.findViewById(R.id.tv_code_head);
                TextView mTVClass = (TextView) header.findViewById(R.id.tv_class_head);
                mTVName.setText(ttsv[0].toString() + "");
                mTVCode.setText(ttsv[1].toString() + "");
                mTVClass.setText(ttsv[2].toString() + "");
                lvExamSchedule.addHeaderView(header, null, false);
                arrExamSchedule = htmlParse.getArrExamSchedule();
                Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                Collections.reverse(arrExamSchedule);
                adapter = new ExamScheduleAdapter(getActivity(), arrExamSchedule);
                lvExamSchedule.setAdapter(adapter);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Load data error!please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}