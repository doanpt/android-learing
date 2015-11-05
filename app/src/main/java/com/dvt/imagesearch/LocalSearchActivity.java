package com.dvt.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.dvt.adapters.ItemImageLocal;
import com.dvt.adapters.ListViewImageAdapter;
import com.dvt.adapters.ListViewLocalAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by doantrung on 11/2/15.
 */
public class LocalSearchActivity extends Activity implements View.OnClickListener{
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
        arrImage = getAllImageLocal();
        adapter = new ListViewLocalAdapter(activity, arrImage);
        arrImageResultSearch=new ArrayList<>();
        lvImageLocal.setAdapter(adapter);
        lvImageLocal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetailImage(position);
            }
        });
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
        File listFile[] = file.listFiles();
        int length=listFile.length;
        for (int i = 0; i < length; i++) {
            listImage.add(i, new ItemImageLocal(listFile[i].getName(), listFile[i].getPath()));
        }
        return listImage;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search_local:
                searchLocal();
                break;
        }
    }

    private void searchLocal() {
        arrImageResultSearch.clear();
        if(edtContentSearchLocal.getText().equals("")){
            arrImage=getAllImageLocal();
            arrImageResultSearch=arrImage;
            displayListImage(arrImageResultSearch);
        }else{
            String key=edtContentSearchLocal.getText().toString();
            searchLocalImage(key);
        }
    }

    private void searchLocalImage(String key) {
        int size=arrImage.size();
        for(int i=0;i<size;i++){
            if(arrImage.get(i).getImageName().contains(key)){
                arrImageResultSearch.add(arrImage.get(i));
            }
        }
        if(arrImageResultSearch.size()>0){
            arrImage=arrImageResultSearch;
            displayListImage(arrImageResultSearch);
        }else{
            displayListImage(arrImage);
        }
    }

    private void displayListImage(ArrayList<ItemImageLocal> listImage) {
        lvImageLocal.setAdapter(new ListViewLocalAdapter(this,listImage));
        adapter.notifyDataSetChanged();
    }
}
