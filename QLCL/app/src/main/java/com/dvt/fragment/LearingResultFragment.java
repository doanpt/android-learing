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

import com.dvt.adapter.LearningResultAdapter;
import com.dvt.item.LearningResultItem;
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
public class LearingResultFragment extends Fragment {
    private ListView lvLearningResult;
    private LearningResultAdapter adapter;
    ProgressDialog mProgressDialog;
    private ArrayList<LearningResultItem> arrLearningResult = new ArrayList<>();
    private HtmlParse htmlParse;
    View myFragmentView;

    public LearingResultFragment() {

    }

    private void initView() {
        htmlParse = new HtmlParse();
        new LearningResult().execute("2!nocode");
        Log.d("LoadType", "offine");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_learing_result, container, false);
        initView();
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
                    new LearningResult().execute("1!" + query);
                    Log.d("LoadType", "online");
                    searchView.clearFocus();
                } else {
                    Toast.makeText(getContext(), "Mã sinh viên có 10 chữ số", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private class LearningResult extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("Kết quả học tập!");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] arrParam = params[0].split("!");
            int type = Integer.parseInt(arrParam[0]);
            String ttsv = htmlParse.getResultLearning(type, arrParam[1], getContext());
            return ttsv;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            try {
                String[] ttsv = result.split("-!!");
                lvLearningResult = (ListView) myFragmentView.findViewById(R.id.lv_learning_result);
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View header = inflater.inflate(R.layout.item_header_listview, lvLearningResult, false);
                header.setTag(this.getClass().getSimpleName() + "header");
                if (lvLearningResult.getHeaderViewsCount() > 0) {
                    View oldView = lvLearningResult.findViewWithTag(this.getClass().getSimpleName() + "header");
                    if (oldView != null) {
                        lvLearningResult.removeHeaderView(oldView);
                    }
                }
                TextView mTVName = (TextView) header.findViewById(R.id.tv_name_head);
                TextView mTVCode = (TextView) header.findViewById(R.id.tv_code_head);
                TextView mTVClass = (TextView) header.findViewById(R.id.tv_class_head);
                mTVName.setText(ttsv[0].toString() + "");
                mTVCode.setText(ttsv[1].toString() + "");
                mTVClass.setText(ttsv[2].toString() + "");
                lvLearningResult.addHeaderView(header, null, false);
                arrLearningResult = htmlParse.getArrLearnResult();
                Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                Collections.reverse(arrLearningResult);
                adapter = new LearningResultAdapter(getActivity(), arrLearningResult);
                lvLearningResult.setAdapter(adapter);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Load data error!please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}