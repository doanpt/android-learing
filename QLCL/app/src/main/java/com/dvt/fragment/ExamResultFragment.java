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

import com.dvt.adapter.ExamResultAdapter;
import com.dvt.item.LearningResultItem;
import com.dvt.jsoup.HtmlParse;
import com.dvt.qlcl.R;
import com.dvt.util.CommonMethod;
import com.dvt.util.CommonValue;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by DoanPT1 on 6/23/2016.
 */
public class ExamResultFragment extends Fragment {
    String code;
    private ListView lvLearningResult;
    private ExamResultAdapter adapter;
    ProgressDialog mProgressDialog;
    private ArrayList<LearningResultItem> arrLearningResult = new ArrayList<>();
    private HtmlParse htmlParse;
    View myFragmentView;

    private void initView() {
        htmlParse = new HtmlParse(CommonMethod.getInstance().getFile(getContext(), "diemthi.txt"));
        htmlParse.setCode(code);
        new ExamResult().execute();
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
        myFragmentView = inflater.inflate(R.layout.fragment_exam_result, container, false);
        lvLearningResult = (ListView) myFragmentView.findViewById(R.id.lv_exam_result);
        initView();
        setHasOptionsMenu(true);
        return myFragmentView;
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
                Toast.makeText(getContext(), "Search exam result" + query, Toast.LENGTH_SHORT).show();
                htmlParse.setCode(query);
                new ExamResult().execute();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reload) {
            new ExamResult().execute();
        }
        return true;
    }

    private class ExamResult extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("ExamResult!");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            htmlParse.getExamResult();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            arrLearningResult = htmlParse.getArrExamResult();
            mProgressDialog.dismiss();
            Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            Collections.reverse(arrLearningResult);
            adapter = new ExamResultAdapter(getActivity(), arrLearningResult);
            lvLearningResult.setAdapter(adapter);
        }
    }

    public ExamResultFragment() {
    }


}