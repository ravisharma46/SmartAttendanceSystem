package com.example.apple.smartattendancesystem;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.DOWNLOAD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class download_attendance_pdf extends Fragment {


    private Spinner spinner_group,spinner_year,spinner_class,spinner_branch,spinner_attendance;
    private Button download,email;
    private  URL url;
    private  DownloadManager downloadManager;
    private long downloadID;
    private ArrayList<String> attribute_arr;
    private ArrayList<String> string_data;
    private  String select_group,select_year,select_classMode,select_branch;





    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(getActivity(), "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };



    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(onDownloadComplete);
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_download_attendance_pdf, container, false);


        getActivity().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));



         download= (Button) view.findViewById(R.id.downlnoad);
         email =(Button) view.findViewById(R.id.email_attendance);

        spinner_group = (Spinner) view.findViewById(R.id.spinner_select_group);
        spinner_year=(Spinner)view.findViewById(R.id.spinner_select_year);
        spinner_class=(Spinner)view.findViewById(R.id.spinner_select_class);
        spinner_branch=(Spinner)view.findViewById(R.id.spinner_select_branch);
        spinner_attendance=(Spinner)view.findViewById(R.id.spinner_select_attendance);

        String UID= getActivity().getSharedPreferences("Teacher_id",Context.MODE_PRIVATE).getString("Teacher_key","gdhfhf");

        Log.i("TAG123",UID);


        attribute_arr=new ArrayList<>();
        string_data=new ArrayList<>();

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

     // ECE
        final String Th_class_ece[]={"E123","E456","E789"};
        final String la_class_ece[]={"E1","E2","E3","E4","E5","E6","E7","E8","E9"};

     //CSE
        final String Th_class_cse[]={"C123","C456","C789","C10 11 12"};
        final String la_class_cse[]={"C1","C2","C3","C4","C5","C6","C7","C8","C9","C11","C12","C13"};

    //MAE
        final String Th_class_mae[]={"M123","M456","M789"};
        final String la_class_mae[]={"M1","M2","M3","M4","M5","M6","M7","M8","M9"};

     //EEE
        final String Th_class_eee[]={"EEE123","EEE456","EEE789"};
        final String la_class_eee[]={"EEE1","EEE2","EEE3","EEE4","EEE5","EEE6","EEE7","EEE8","EEE9"};
     //IT
        final String Th_class_it[]={"IT123","IT456","IT789"};
        final String la_class_it[]={"IT1","IT2","IT3","IT4","IT5","IT6","IT7","IT8","IT9"};



        // create adapter array for select year
        ArrayAdapter<String> adapter_year= new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.ece_select_year));


        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(adapter_year);



        // create adapter array for select class mode
       final ArrayAdapter<String> adapter_class= new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.ece_select_classMode));


        adapter_class.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_class.setAdapter(adapter_class);


        // create adapter array for select branch
        ArrayAdapter<String> adapter_branch= new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.select_branch));


        adapter_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_branch.setAdapter(adapter_branch);

        // create adapter array for select attendance
        ArrayAdapter<String> adapter_attendance= new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.select_attendance));


        adapter_attendance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_attendance.setAdapter(adapter_attendance);





        spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(spinner_branch.getSelectedItemPosition()==0){
                    final ArrayAdapter<String> adapter_group_0= new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1,Th_class_cse);
                    adapter_group_0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_group.setAdapter(adapter_group_0);
                    spinner_class.setAdapter(adapter_class);
                    select_branch=adapterView.getItemAtPosition(i).toString();



                    spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            if(spinner_class.getSelectedItemPosition()==0){
                                spinner_group.setAdapter(adapter_group_0);
                                select_group=adapterView.getItemAtPosition(i).toString();



                            }
                            else if(spinner_class.getSelectedItemPosition()==1) {
                                ArrayAdapter<String> adapter_group= new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_list_item_1,la_class_cse);
                                adapter_group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_group.setAdapter(adapter_group);
                                select_group=adapterView.getItemAtPosition(i).toString();

                            }
                            else{

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }


                else if(spinner_branch.getSelectedItemPosition()==1){
                  final  ArrayAdapter<String> adapter_group_1= new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1,Th_class_it);
                    adapter_group_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_group.setAdapter(adapter_group_1);
                    spinner_class.setAdapter(adapter_class);



                    spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            if(spinner_class.getSelectedItemPosition()==0){
                                spinner_group.setAdapter(adapter_group_1);

                            }
                            else if(spinner_class.getSelectedItemPosition()==1)  {
                                ArrayAdapter<String> adapter_group= new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_list_item_1,la_class_it);
                                adapter_group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_group.setAdapter(adapter_group);

                            }
                            else{

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }

                else if(spinner_branch.getSelectedItemPosition()==2){
                   final ArrayAdapter<String> adapter_group_2= new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1,Th_class_ece);
                    adapter_group_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_group.setAdapter(adapter_group_2);
                    spinner_class.setAdapter(adapter_class);

                    spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            if(spinner_class.getSelectedItemPosition()==0){
                                spinner_group.setAdapter(adapter_group_2);


                            }
                            else if(spinner_class.getSelectedItemPosition()==1)  {
                                ArrayAdapter<String> adapter_group= new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_list_item_1,la_class_ece);
                                adapter_group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_group.setAdapter(adapter_group);

                            }
                            else{

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
                else if(spinner_branch.getSelectedItemPosition()==3){
                  final  ArrayAdapter<String> adapter_group_3= new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1,Th_class_eee);
                    adapter_group_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_group.setAdapter(adapter_group_3);
                    spinner_class.setAdapter(adapter_class);

                    spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            if(spinner_class.getSelectedItemPosition()==0){

                                spinner_group.setAdapter(adapter_group_3);
                            }
                            else if(spinner_class.getSelectedItemPosition()==1)  {
                                ArrayAdapter<String> adapter_group= new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_list_item_1,la_class_eee);
                                adapter_group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_group.setAdapter(adapter_group);

                            }
                            else{

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
                else if(spinner_branch.getSelectedItemPosition()==4) {
                   final ArrayAdapter<String> adapter_group_4= new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1,Th_class_mae);
                    adapter_group_4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_group.setAdapter(adapter_group_4);
                    spinner_class.setAdapter(adapter_class);


                    spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            if(spinner_class.getSelectedItemPosition()==0){
                                spinner_group.setAdapter(adapter_group_4);

                            }
                            else if(spinner_class.getSelectedItemPosition()==1)  {
                                ArrayAdapter<String> adapter_group= new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_list_item_1,la_class_mae);
                                adapter_group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_group.setAdapter(adapter_group);

                            }else{

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }





            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });








        getActivity().setTitle("Download Attendance");

        return  view;
    }








    public void start_download(){

        File file=new File(getActivity().getExternalFilesDir(null),"Dummy");
        /*
        Create a DownloadManager.Request with all the information necessary to start the download

        http://192.168.43.212:7000/home/download/cseattendance
         */
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse("http://ilabs.uw.edu/sites/default/files/sample_0.pdf"))
                .setTitle("Dummy File")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                .setRequiresCharging(false)// Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network

        DownloadManager downloadManager= (DownloadManager)getActivity().getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.

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
