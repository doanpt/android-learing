package com.dvt.imagesearch;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by doantrung on 10/29/15.
 */
public class DownloadDialog extends Dialog implements View.OnClickListener {
    private Button btnDownload;
    private String linkDownload = "";
    private ProgressDialog mProgressDialog;
    private Context context;
    private String imageName = "";
    private ServiceConnect serviceConnect;
    public static final String MY_FOLDER = "MyImage";

    public DownloadDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_dowload);
        setCanceledOnTouchOutside(true);
        initView(context);
    }

    private void initView(Context context) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Download Image...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        btnDownload = (Button) findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(this);
        serviceConnect = new ServiceConnect();
    }

    @Override
    public void onClick(View v) {
        dismiss();
        imageName = linkDownload.substring(linkDownload.lastIndexOf("/") + 1);
        File file = new File(File.separator + Environment.getExternalStoragePublicDirectory(MY_FOLDER) + File.separator + imageName);
        if (file.exists()) {
            Toast.makeText(context, "Image already exists", Toast.LENGTH_SHORT).show();
        } else {
            new ImageDownloader().execute(linkDownload, imageName);
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setLinkDownload(String linkDownload) {
        this.linkDownload = linkDownload;
    }

    class ImageDownloader extends AsyncTask<String, Integer, Void> {
        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            serviceConnect.downloadImage(context, params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(context, "Download Success", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }
}