package com.example.apple.smartattendancesystem;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class download_attendance_pdf extends Fragment {


    private Button download;
    private  URL url;
    DownloadManager downloadManager;
    private long downloadID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_download_attendance_pdf, container, false);

         download= (Button) view.findViewById(R.id.downlnoad);

         download.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {


                 start_download();


             }
         });




         return  view;
    }





    public void start_download(){

        File file= new File(getActivity().getExternalFilesDir(null),"pro");

       // downloadManager= (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("http://428a9c8d.ngrok.io/rr.pdf");
        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle("Dummy File")
                .setDescription("Downloading")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);


        DownloadManager downloadManager= (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);


    }



}
