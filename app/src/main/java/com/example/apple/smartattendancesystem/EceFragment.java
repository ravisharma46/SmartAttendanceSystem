package com.example.apple.smartattendancesystem;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class EceFragment extends Fragment {


    private Spinner spinner_group,spinner_year,spinner_class;
    private  String select_ece_group,select_ece_year,select_ece_classMode;
    private TextView date,time;
    private DatePickerDialog datePickerDialog;
    private String format = "";
    private Button scan;
    private String date_1;
    private String time_1;
    private URL url;
    private ArrayList<String>attribute_arr;

    private String scanContent;

    public EceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_ece, container, false);

         spinner_group = (Spinner) view.findViewById(R.id.spinner_select_group);
         spinner_year=(Spinner)view.findViewById(R.id.spinner_select_year);
         spinner_class=(Spinner)view.findViewById(R.id.spinner_select_class);

         date=(TextView) view.findViewById(R.id.date_picker);
         time=(TextView) view.findViewById(R.id.time_picker);
         scan=(Button)view.findViewById(R.id.start_scan_ece);

        attribute_arr=new ArrayList<>();

       // data.setText("Content: " + scanContent);

         final String Theory_class[]={"E123","E456","E789"};
         final String lab_class[]={"E1","E2","E3","E4","E5","E6","E7","E8","E9"};






         // create adapter array for select year
        ArrayAdapter<String> adapter_year= new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.ece_select_year));


        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(adapter_year);



        // create adapter array for select class mode
        ArrayAdapter<String> adapter_class= new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.ece_select_classMode));


        adapter_class.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_class.setAdapter(adapter_class);




        //select year spinner
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                select_ece_year=parentView.getItemAtPosition(position).toString();
                attribute_arr.add(select_ece_year);
                Toast.makeText(getActivity(),"You have selected "+select_ece_year,Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Log.i("TAG","chooose nothing");
            }

        });




        //select class spinner
        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                if(spinner_class.getSelectedItemPosition()==0){

                    ArrayAdapter<String> adapter_group= new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1,Theory_class);
                    adapter_group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                     spinner_group.setAdapter(adapter_group);
                }
              else  {
                    ArrayAdapter<String> adapter_group= new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1,lab_class);
                    adapter_group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_group.setAdapter(adapter_group);

                }



                select_ece_classMode=parentView.getItemAtPosition(position).toString();
                attribute_arr.add(select_ece_classMode);

                Toast.makeText(getActivity(),"You have selected "+select_ece_classMode+" Class",Toast.LENGTH_SHORT).show();




            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Log.i("TAG","chooose nothing");
            }

        });





        //select group spinner
        spinner_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                select_ece_group=parentView.getItemAtPosition(position).toString();
                attribute_arr.add(select_ece_group);
                Toast.makeText(getActivity(),"You have selected "+select_ece_group+" group",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Log.i("TAG","chooose nothing");
            }

        });



        //select date

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c =  Calendar.getInstance();
                int mYear=c.get(Calendar.YEAR);
                int mMonth=c.get(Calendar.MONTH);
                int mDay =c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog =new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        date.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);
                        date_1=date.getText().toString();
                        attribute_arr.add(date_1);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });


        //select time
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);
             //  final   int str=mcurrentTime.get(Calendar.AM_PM);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour == 0) {
                            selectedHour += 12;
                            format = "AM";
                        } else if (selectedHour == 12) {
                            format = "PM";
                        } else if (selectedHour > 12) {
                            selectedHour -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        if(selectedMinute == 0 ) {
                            time.setText(selectedHour + ":00"+" "+format);
                        }
                        else if(selectedMinute < 10){
                            time.setText(selectedHour + ":0" + selectedMinute+" "+format);
                        }
                        else {
                            time.setText(selectedHour + ":" + selectedMinute+" "+format);
                        }

                        time_1=time.getText().toString();
                        attribute_arr.add(time_1);


                    }
                }, hour, minute, false);// 12 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        //attribute String array...////







        ///////////////////////////////



        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(date_1==null){
                    Toast.makeText(getActivity(),"Please Select Date",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(time_1==null){
                    Toast.makeText(getActivity(),"Please Select Time",Toast.LENGTH_SHORT).show();
                    return;
                }
                final String[] items = {"Year    "+select_ece_year, "Class    "+select_ece_classMode,
                        "Group    "+select_ece_group, "Date    "+date_1,"Time    "+time_1};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Selected attributes")

                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), items[which] + " is clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setPositiveButton("Start Scan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startScan();
                    }
                });
                builder.setNegativeButton("Edit", null);
                builder.setNeutralButton("CANCEL", null);
              //  builder.setNeutralButton("CANCEL", null);builder.setPositiveButtonIcon(getResources().getDrawable(android.R.drawable.ic_menu_call, getTheme()));
              //  builder.setIcon(getResources().getDrawable(R.drawable.jd, getTheme()));


                AlertDialog alertDialog = builder.create();

                alertDialog.show();

                Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
               // button.setBackgroundColor(Color.BLACK);
                button.setPadding(0, 0, 20, 0);
               // button.setTextColor(Color.WHITE);
            }
        });

       // update action bar title
        getActivity().setTitle("ECE branch");

        return view;

    }




    public void startScan(){

        send_attributes();
        Intent intent=new Intent(getActivity(),scanningActivity.class);

        startActivity(intent);

    }

    public void send_attributes(){
        try {

            url = new URL("http://192.168.43.212:7000/home/cseattendance/theory");
        } catch (MalformedURLException e) {
            Log.e("assign", "problem");
            Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        new SendPostRequest().execute();
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {



        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try {


                attribute_arr.add("texh12");


                //username = user_id.getText().toString();
                //  username=user_id.getText().toString();
                //  password = user_password.getText().toString();

                JSONObject postDataParams = new JSONObject();
                //postDataParams.put("username", username);

                Log.i("ARRAY:",attribute_arr.toString());
                for(int i=0;i<=attribute_arr.size();i++){

                    String data=attribute_arr.get(i);
                       if(i==0){
                           postDataParams.put("year",data);
                       }
                       else if(i==1){
                           postDataParams.put("class_mode",data);
                       }
                       else if(i==2){
                           postDataParams.put("group",data);
                       }
                       else if(i==3){
                           postDataParams.put("date",data);
                       }
                       else if(i==4){
                           postDataParams.put("time",data);
                       }
                       else if(i==5){
                           postDataParams.put("teacher_id",data);
                       }
                   // postDataParams.put(keys,data);
                    //postDataParams.put("abc",data);
                }




                // postDataParams.put("password", password);
                Log.e("params", postDataParams.toString());
                /// editor= sharedPreferences.edit();
                // SharedPreferences.Editor editor = getSharedPreferences("Teachers", MODE_PRIVATE).edit();
                // editor.putString("Key","0");

                // editor.putString("sharedTname", roll_no);
                //editor.apply();
                // editor.commit();
                // Log.i("TAG:___",editor.toString());
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
            String y="";
            String d="";
            try {
                JSONObject json = new JSONObject(s);
                y=json.getString("error");
                d=json.getString("data");

                Log.e("value of y",y);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("servers",s);


            if(y.equals("1")){

                // user_password.setText("");
               // attendance_list.remove(roll_no);
                Toast.makeText(getActivity(), "There is problem", Toast.LENGTH_SHORT).show();

            }
            else if( y.equals("0")){
                // user_id.setText("");
                //  user_password.setText("");
                Toast.makeText(getActivity(), "data sending to server", Toast.LENGTH_SHORT).show();
                Log.i("TAG","connection reached here");
                //  Intent intent=new Intent (MainActivity.this,select_branch.class);
                // startActivity(intent);
            }
            else{

            }


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
