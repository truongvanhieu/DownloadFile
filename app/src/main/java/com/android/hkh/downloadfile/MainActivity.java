package com.android.hkh.downloadfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements DownloadFileFromURLListener{
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        DownloadFileFromURL downloadFileFromURL = new DownloadFileFromURL();
        downloadFileFromURL.SetListener(this);

        downloadFileFromURL.execute("https://drive.google.com/uc?export=download&id=0B8tAQ0_sJKCSQlpwRFREQlZTNVk",
                getExternalFilesDir(null).getAbsolutePath() + "/data.txt");
    }


    @Override
    public void OnStartDownload() {

    }

    @Override
    public void OnDownloading(Integer... percent) {
        progressBar.setProgress(percent[0]);
    }

    @Override
    public void OnFinishedDownload(int status) {

    }
}