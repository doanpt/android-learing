package com.dvt.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dvt.adapters.ItemImageLocal;
import com.dvt.adapters.ListViewLocalAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by doantrung on 11/2/15.
 */
public class LocalSearchActivity extends Activity implements View.OnClickListener {
    public static final String KEY_ARRAY_IMAGE_LOCAL = "key_array_image_l";
    public static final String KEY_POSITION_LOCAL = "key_position_local";
    private EditText edtContentSearchLocal;
    private Button btnLocalSearch;
    private ListView lvImageLocal;
    private ArrayList<ItemImageLocal> arrImage;
    private ListViewLocalAdapter adapter;
    private Activity activity;
    private ArrayList<ItemImageLocal> arrImageResultSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_search);
        initView();
    }

    private void initView() {
        activity = this;
        edtContentSearchLocal = (EditText) findViewById(R.id.edt_content_search_local);
        btnLocalSearch = (Button) findViewById(R.id.btn_search_local);
        btnLocalSearch.setOnClickListener(this);
        lvImageLocal = (ListView) findViewById(R.id.lv_image_local);
        arrImageResultSearch = new ArrayList<>();
        arrImage = new ArrayList<>();
        if (getAllImageLocal() == null) {

        } else {
            arrImage = getAllImageLocal();
            adapter = new ListViewLocalAdapter(activity, arrImage);
            lvImageLocal.setAdapter(adapter);
            lvImageLocal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showDetailImage(position);
                }
            });
        }
    }

    private void showDetailImage(int position) {
        Intent intentDetail = new Intent(LocalSearchActivity.this, DetailImageLocalActivity.class);
        intentDetail.putExtra(KEY_ARRAY_IMAGE_LOCAL, arrImage);
        intentDetail.putExtra(KEY_POSITION_LOCAL, position + "");
        startActivity(intentDetail);
    }

    private ArrayList<ItemImageLocal> getAllImageLocal() {
        ArrayList<ItemImageLocal> listImage = new ArrayList<>();
        String path = Environment.getExternalStoragePublicDirectory(DownloadDialog.MY_FOLDER).toString();
        File file = new File(path);
        if (file.exists()) {
            File listFile[] = file.listFiles();
            if (listFile.length == 0) {
                arrImage=new ArrayList<>();
                return arrImage;
            }
            int length = listFile.length;
            for (int i = 0; i < length; i++) {
                listImage.add(i, new ItemImageLocal(listFile[i].getName(), listFile[i].getPath()));
            }
        } else {
            file.mkdir();
            arrImage=new ArrayList<>();
            return arrImage;
        }
        return listImage;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_local:
                searchLocal();
                break;
        }
    }

    private void searchLocal() {
        arrImageResultSearch.clear();
        arrImage = getAllImageLocal();
        if (edtContentSearchLocal.getText().equals("")) {
            arrImageResultSearch = arrImage;
            displayListImage(arrImageResultSearch);
        } else {
            String key = edtContentSearchLocal.getText().toString();
            key = key.replaceAll("[^a-zA-Z]+", "").trim();
            if (arrImage.size() > 0) {
                int size = arrImage.size();
                String imageName = "";
                for (int i = 0; i < size; i++) {
                    imageName = arrImage.get(i).getImageName().replaceAll("[^a-zA-Z]+", "").trim();
                    if (imageName.indexOf(key) != -1) {
                        arrImageResultSearch.add(arrImage.get(i));
                    }
                }
                if (arrImageResultSearch.size() > 0) {
                    arrImage = arrImageResultSearch;
                    displayListImage(arrImageResultSearch);
                } else {
                    displayListImage(arrImage);
                }
            }else{
                Toast.makeText(activity, "No images to display! please download images.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayListImage(ArrayList<ItemImageLocal> listImage) {
        lvImageLocal.setAdapter(new ListViewLocalAdapter(this, listImage));
        adapter.notifyDataSetChanged();
    }
}