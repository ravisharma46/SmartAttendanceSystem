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
import android.widget.Toast;

import org.json.JSONException;
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


    private Button download,email;
    private  URL url;
    DownloadManager downloadManager;
    private long downloadID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_download_attendance_pdf, container, false);

         download= (Button) view.findViewById(R.id.downlnoad);
         email =(Button) view.findViewById(R.id.email_attendance);

         download.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {


                 start_download();


             }
         });


         email.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 email_attendance();
             }
         });




        getActivity().setTitle("Download Attendance");

        return  view;
    }





    public void start_download(){

        File file= new File(getActivity().getExternalFilesDir(null),"pro.pdf");


       // Uri uri = Uri.parse("http://192.168.43.212:8080/ravip.pdf");
        Uri uri = Uri.parse("http://b6094894.ngrok.io/ravip.pdf");

       // Uri uri = Uri.parse("http://192.168.43.212:7000/ravi/template/ravip.pdf");
        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle("atten File")
                .setDescription("Downloading")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);


        DownloadManager downloadManager= (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);


    }


    public void email_attendance(){

        try {

            url = new URL("http://192.168.43.212:7000/home/email/attendance");
        } catch (MalformedURLException e) {
            Log.e("assign", "problem");
           // Toast.makeText(download_attendance_pdf.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        new SendPostRequest().execute();
    }


    //////attendance fetch into database...

    public class SendPostRequest extends AsyncTask<String, Void, String> {



        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try {




                JSONObject postDataParams = new JSONObject();

                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = null;
                try {

                    conn = (HttpURLConnection) url.openConnection();
                    Log.e("cp1","connectionin");
                    //Toast.makeText(LoginActivity.this,"connevtion success",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    //Toast.makeText(LoginActivity.this,"connevtion failed",Toast.LENGTH_SHORT).show();
                    Log.e("connectionError", e.getMessage());
                }
                Log.i("TAG","connection built here");
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
////////////////////////////////////////////////////////////
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                Log.i("TAG","connection reached 1here");
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    Log.i("TAG","connection reached 2here");
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }
                    Log.i("TAG","connection reached 3here");

                    in.close();
                    return sb.toString();
                } else {
                    return new String("false : " + responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {



        }
    }


    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }



}
