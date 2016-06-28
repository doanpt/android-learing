package com.dvt.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.dvt.adapter.ExamScheduleAdapter;
import com.dvt.adapter.ExamScheduleItem;
import com.dvt.adapter.HtmlParse;
import com.dvt.qlcl.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_exam_schedule, container, false);
        lvExamSchedule = (ListView) myFragmentView.findViewById(R.id.lv_exam_schedule);
        initView();
        return myFragmentView;


    }
    private void initView() {
        htmlParse = new HtmlParse(getFile("lichthi.txt"));
        htmlParse.setCode(code);
        new ExamScheduleTask().execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        code = bundle.getString("Code");
        Toast.makeText(getActivity(), "Lịch thi" + code, Toast.LENGTH_SHORT).show();
    }

    public String getFile(String filename) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getActivity().getAssets().open(filename), "UTF-8"));
            String mLine;
            StringBuilder sb = new StringBuilder();
            while ((mLine = reader.readLine()) != null) {
                sb.append(mLine);
            }
            return sb.toString();
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }


    private class ExamScheduleTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressDialog = new ProgressDialog(getActivity());
//            mProgressDialog.setTitle("Exam Schedule!");
//            mProgressDialog.setMessage("Loading...");
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            htmlParse.getExamSchedule();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            arrExamSchedule = htmlParse.getArrExamSchedule();
 //           mProgressDialog.dismiss();
            Collections.reverse(arrExamSchedule);
            adapter = new ExamScheduleAdapter(getActivity(), arrExamSchedule);
            lvExamSchedule.setAdapter(adapter);
        }
    }


}