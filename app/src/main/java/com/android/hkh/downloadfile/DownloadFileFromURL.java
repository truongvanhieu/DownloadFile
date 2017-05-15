package com.android.hkh.downloadfile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hieu.truongvan on 5/14/2017.
 */

public class DownloadFileFromURL extends AsyncTask<String, Integer, Integer> {
    private final int STATUS_OK = 1;
    private final int STATUS_ERROR = 2;

    private DownloadFileFromURLListener downloadFileFromURLListener = null;

    public void SetListener(DownloadFileFromURLListener _downloadFileFromURLListener){
        downloadFileFromURLListener =_downloadFileFromURLListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(downloadFileFromURLListener != null){
            downloadFileFromURLListener.OnStartDownload();
        }
    }

    @Override
    protected Integer doInBackground(String... urls) {
        String dataUrl = urls[0];
        String savePath = urls[1];
        Log.i("DownloadFileDemo",savePath);
        int count;
        try{
            URL url = new URL(dataUrl);
            URLConnection urlconnect = url.openConnection();
            urlconnect.connect();

            //input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream ouput = new FileOutputStream(savePath);
            /* Download & read file by read line

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line="";
            StringBuilder stringBuilder = new StringBuilder();
            while((line = reader.readLine()) != null) {
                Log.i("DownloadFileDemo", line);
                stringBuilder.append(line +"\n");
            }
            ouput.write(stringBuilder.toString().getBytes());
            */
            int lengthOfFile = urlconnect.getContentLength();

            byte data[] = new byte[10];//[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress( (int) (total*100/lengthOfFile));//publishing the progress
                ouput.write(data, 0, count);
                SystemClock.sleep(200);
            }
            ouput.flush();
            ouput.close();
            input.close();
        }catch(Exception e){
            e.printStackTrace();
            Log.i("DownloadFileDemo", "doInBackground: ->download Failed!!!");
            return STATUS_ERROR;
        }
        return STATUS_OK;
    }

    @Override
    protected void onPostExecute(Integer s) {
        super.onPostExecute(s);
        Log.i("DownloadFileDemo", "Finished");
        if(downloadFileFromURLListener != null){
            downloadFileFromURLListener.OnFinishedDownload(s);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if(downloadFileFromURLListener != null){
            downloadFileFromURLListener.OnDownloading(values);
        }
    }
}
