package com.dvt.imagesearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.dvt.adapters.ItemImage;
import com.dvt.adapters.ListViewImageAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {
    public static final String KEY_ARRAY_IMAGE = "key_array_image";
    public static final String KEY_POSITION = "key_position";
    private String contentSearch = "";
    private EditText edtSearch;
    private Button btnSearch;
    private ListView lvImage;
    private ListViewImageAdapter adapter;
    private ArrayList<Object> arrImage;
    private Activity activity;
    private ImageConnection imageConnection;
    private ServiceConnect serviceConnect;
    private DownloadDialog downloadDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        edtSearch = (EditText) findViewById(R.id.edt_content_search);
        btnSearch = (Button) findViewById(R.id.btn_search);
        lvImage = (ListView) findViewById(R.id.lv_image);
        btnSearch.setOnClickListener(this);
        imageConnection = new ImageConnection();
        serviceConnect = new ServiceConnect();
        downloadDialog=new DownloadDialog(this);
        activity = this;
        lvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetailImage(position);
            }
        });
        lvImage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ItemImage itemImage = (ItemImage) arrImage.get(position);
                downloadDialog.setLinkDownload(itemImage.getLinkImageFull());
                downloadDialog.setContext(MainActivity.this);
                downloadDialog.show();
                return true;
            }
        });
    }

    private void showDetailImage(int position) {
        Intent intentDetail = new Intent(MainActivity.this, DetailActivity.class);
        intentDetail.putExtra(KEY_ARRAY_IMAGE, arrImage);
        intentDetail.putExtra(KEY_POSITION, position + "");
        startActivity(intentDetail);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_search:
                searchImage();
                break;
            default:
                break;
        }
    }

    private void searchImage() {
        contentSearch = edtSearch.getText().toString();
        contentSearch = Uri.encode(contentSearch);
        new getImagesTask().execute();
    }

    class getImagesTask extends AsyncTask<Void, Void, Void> {
        JSONObject json;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            String link = "https://ajax.googleapis.com/ajax/services/search/images?" + "v=1.0&q=" + contentSearch + "&rsz=8";
            try {
                json = new JSONObject(serviceConnect.getConnect(link));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            arrImage = imageConnection.getImageListFromJsonObj(json);
            SetListViewAdapter(arrImage);
        }
    }

    public void SetListViewAdapter(ArrayList<Object> images) {
        adapter = new ListViewImageAdapter(activity, arrImage);
        lvImage.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}