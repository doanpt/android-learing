package com.dvt.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.dvt.adapter.LearningResultAdapter;
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
public class LearingResultFragment extends Fragment {
    String code;
    private ListView lvLearningResult;
    private LearningResultAdapter adapter;
    ProgressDialog mProgressDialog;
    private ArrayList<LearningResultItem> arrLearningResult = new ArrayList<>();
    private HtmlParse htmlParse;
    View myFragmentView;

    public LearingResultFragment() {

    }

    private void initView() {
        htmlParse = new HtmlParse(CommonMethod.getInstance().getFile(getContext(),"kqhoctap.txt"));
        htmlParse.setCode(code);
        new LearningResult().execute();
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
        myFragmentView = inflater.inflate(R.layout.fragment_learing_result, container, false);
        lvLearningResult = (ListView) myFragmentView.findViewById(R.id.lv_learning_result);
        initView();
        return myFragmentView;
    }

    private class LearningResult extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("Learning Result!");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            htmlParse.getResultLearning();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            arrLearningResult = htmlParse.getArrLearnResult();
            mProgressDialog.dismiss();
            Toast.makeText(getContext(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
            Collections.reverse(arrLearningResult);
            adapter = new LearningResultAdapter(getActivity(), arrLearningResult);
            lvLearningResult.setAdapter(adapter);
        }
    }
}