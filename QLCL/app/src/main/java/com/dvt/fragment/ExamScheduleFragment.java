package com.dvt.fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.dvt.adapter.ExamScheduleAdapter;
import com.dvt.item.ExamScheduleItem;
import com.dvt.jsoup.HtmlParse;
import com.dvt.qlcl.R;
import com.dvt.util.CommonMethod;
import com.dvt.util.CommonValue;

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
        Bundle bundle = getArguments();
        code = bundle.getString(CommonValue.KEY_CODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_exam_schedule, container, false);
        lvExamSchedule = (ListView) myFragmentView.findViewById(R.id.lv_exam_schedule);
        initView();
        setHasOptionsMenu(true);
        return myFragmentView;
    }

    private void initView() {
        htmlParse = new HtmlParse(CommonMethod.getInstance().getFile(getContext(), "lichthi.txt"));
        htmlParse.setCode(code);
        new ExamScheduleTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
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
                Toast.makeText(getContext(), "Search schedule" + query, Toast.LENGTH_SHORT).show();
                //TODO
                htmlParse.setCode(query);
                new ExamScheduleTask().execute();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reload) {
            new ExamScheduleTask().execute();
        }
        return true;
    }

    private class ExamScheduleTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("Exam Schedule!");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            htmlParse.getExamSchedule();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            arrExamSchedule = htmlParse.getArrExamSchedule();
            mProgressDialog.dismiss();
            Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            Collections.reverse(arrExamSchedule);
            adapter = new ExamScheduleAdapter(getActivity(), arrExamSchedule);
            lvExamSchedule.setAdapter(adapter);
        }
    }
}