package com.dvt.imagesearch;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by doantrung on 10/26/15.
 */
public class ServiceConnect {
    public String getConnect(String link) {
        URL url;
        StringBuilder builder = null;
        try {
            url = new URL(link);
            URLConnection connection = url.openConnection();
            String line;
            builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
    public void downloadImage(Context context,String link,String imageName){
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Toast.makeText(context, "connect to fail", Toast.LENGTH_SHORT).show();
            }
            input = connection.getInputStream();
            File file = new File(Environment.getExternalStoragePublicDirectory(DownloadDialog.MY_FOLDER).toString());
            if (!file.exists()) {
                file.mkdir();
            }
            output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(DownloadDialog.MY_FOLDER)+File.separator+imageName);
            byte data[] = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }
            if (connection != null)
                connection.disconnect();
        }
    }
}