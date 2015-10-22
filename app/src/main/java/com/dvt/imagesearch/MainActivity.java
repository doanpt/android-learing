package com.dvt.imagesearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.dvt.adapters.ItemImage;
import com.dvt.adapters.ListViewImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {
    private String contentSearch = "";
    private EditText edtSearch;
    private Button btnSearch;
    private ListView lvImage;
    private ListViewImageAdapter adapter;
    private ArrayList<Object> arrImage;
    private Activity activity;

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
        activity = this;
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
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            URL url;
            try {
                url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" + "v=1.0&q=" + contentSearch + "&rsz=8");
                URLConnection connection = url.openConnection();
                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                json = new JSONObject(builder.toString());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                JSONObject responseObject = json.getJSONObject("responseData");
                JSONArray resultArray = responseObject.getJSONArray("results");
                arrImage = getImageList(resultArray);
                SetListViewAdapter(arrImage);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public ArrayList<Object> getImageList(JSONArray resultArray) {
        ArrayList<Object> listImages = new ArrayList<Object>();
        ItemImage itemImage;

        try {
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject obj;
                obj = resultArray.getJSONObject(i);
                itemImage = new ItemImage();

                itemImage.setTitleImage(obj.getString("title"));
                itemImage.setThumbUrl(obj.getString("tbUrl"));
                listImages.add(itemImage);
            }
            return listImages;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public void SetListViewAdapter(ArrayList<Object> images) {
        adapter = new ListViewImageAdapter(activity, arrImage);
        lvImage.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
