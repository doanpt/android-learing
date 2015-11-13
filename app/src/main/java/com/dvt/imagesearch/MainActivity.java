package com.dvt.imagesearch;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dvt.adapters.ItemImage;
import com.dvt.adapters.ListViewImageAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    public static final String KEY_ARRAY_IMAGE = "key_array_image";
    public static final String KEY_POSITION = "key_position";
    private static final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 2;
    private static final int REQUEST_CODE_FROM_SEARCH_PERMISSIONS = 3;
    private static final int REQUEST_CODE_FROM_LOCAL_PERMISSIONS = 4;
    private static final String TAG = "MainActivity";
    private String contentSearch = "";
    private ImageView ivLocalSearch;
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
        checkYourPermissions();
        initView();
    }

    private boolean netCheckin(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.w("INTERNET:", String.valueOf(i));
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.w("INTERNET:", "connected!");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkYourPermissions() {
        int hasWriteExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasInternetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int hasAccessNetworkPesmission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        List<String> permissions = new ArrayList<String>();
        if (hasWriteExternalPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.INTERNET);
        }
        if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
            case REQUEST_CODE_SOME_FEATURES_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
            }
            case REQUEST_CODE_FROM_SEARCH_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                        contentSearch = edtSearch.getText().toString();
                        contentSearch = Uri.encode(contentSearch);
                        new getImagesTask().execute();
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
            }
            case REQUEST_CODE_FROM_LOCAL_PERMISSIONS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permissions", "Permission Granted: " + permissions[0]);
                    Intent intentLocal = new Intent(MainActivity.this, LocalSearchActivity.class);
                    startActivity(intentLocal);
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Log.d("Permissions", "Permission Denied: " + permissions[0]);
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void initView() {
        ivLocalSearch = (ImageView) findViewById(R.id.iv_search_local);
        ivLocalSearch.setOnClickListener(this);
        edtSearch = (EditText) findViewById(R.id.edt_content_search);
        btnSearch = (Button) findViewById(R.id.btn_search);
        lvImage = (ListView) findViewById(R.id.lv_image);
        btnSearch.setOnClickListener(this);
        imageConnection = new ImageConnection();
        serviceConnect = new ServiceConnect();
        downloadDialog = new DownloadDialog(this);
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
            case R.id.iv_search_local:
                localSearch();
                break;
            default:
                break;
        }
    }

    private void localSearch() {
        int hasWriteExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> permissions = new ArrayList<String>();
        if (hasWriteExternalPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), REQUEST_CODE_FROM_LOCAL_PERMISSIONS);
        } else {
            Intent intentLocal = new Intent(MainActivity.this, LocalSearchActivity.class);
            startActivity(intentLocal);
        }
    }

    private void searchImage() {
        if (netCheckin(MainActivity.this)) {
            int hasWriteExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasInternetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
            int hasAccessNetworkPesmission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
            List<String> permissions = new ArrayList<String>();
            if (hasWriteExternalPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.INTERNET);
            }
            if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (hasAccessNetworkPesmission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            }
            if (!permissions.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), REQUEST_CODE_FROM_SEARCH_PERMISSIONS);
            } else {
                contentSearch = edtSearch.getText().toString();
                contentSearch = Uri.encode(contentSearch);
                new getImagesTask().execute();
            }
        } else {
            Toast.makeText(MainActivity.this, "Please enable network!", Toast.LENGTH_SHORT).show();
        }
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