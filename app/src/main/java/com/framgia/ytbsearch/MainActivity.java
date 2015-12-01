package com.framgia.ytbsearch;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.framgia.adapter.SearchAdapter;
import com.framgia.adapter.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 2;
    private static final int REQUEST_CODE_FROM_SEARCH_PERMISSIONS = 3;
    public static final String ID_VIDEO = "id_video";
    public static final String KEY_DEVELOP = "AIzaSyD1kdTvRqw1OndpiuC7_HS4T4aSk81FrIA";
    private Utils utils;
    private EditText edtSearchInput;
    private ListView lvVideosFound;
    private Button btnSearch;
    private Handler handler;
    private SearchAdapter searchAdapter;
    private ArrayList<VideoItem> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkYourPermissions();
        initView();
    }

    private void initView() {
        handler = new Handler();
        edtSearchInput = (EditText) findViewById(R.id.edt_content_search);
        lvVideosFound = (ListView) findViewById(R.id.lv_result);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
        utils=new Utils();
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkYourPermissions() {
        int hasInternetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int hasAccessNetworkPesmission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        List<String> permissions = new ArrayList<String>();
        if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.INTERNET);
        }
        if (hasAccessNetworkPesmission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_FROM_SEARCH_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        doSearch();
                        break;
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public void doSearch() {
        new Thread() {
            public void run() {
                YoutubeConnector yc = new YoutubeConnector(MainActivity.this);
                searchResults = yc.search(edtSearchInput.getText().toString());
                handler.post(new Runnable() {
                    public void run() {
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    private void searchOnYoutube() {
        if (utils.netCheckin(MainActivity.this)) {
            int hasInternetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
            int hasAccessNetworkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
            List<String> permissions = new ArrayList<>();
            if (hasInternetPermission != PackageManager.PERMISSION_GRANTED)
                permissions.add(Manifest.permission.INTERNET);
            if (hasAccessNetworkPermission != PackageManager.PERMISSION_GRANTED)
                permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            if (!permissions.isEmpty()) {
                requestPermission(this, permissions.toArray(new String[permissions.size()]), REQUEST_CODE_FROM_SEARCH_PERMISSIONS);
            } else {
                doSearch();
            }
        } else {
            Toast.makeText(MainActivity.this, "Please enable network!", Toast.LENGTH_SHORT).show();
        }
    }

    public void requestPermission(Activity activity, String[] permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, permission, requestCode);
    }

    private void updateVideosFound() {
        searchAdapter = new SearchAdapter(MainActivity.this, searchResults);
        lvVideosFound.setAdapter(searchAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                searchOnYoutube();
                break;
        }
    }
}
