package com.example.apple.smartattendancesystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.MultiFormatWriter;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private Button register_button;
    private URL url;
    private EditText user_id,user_password;
    public   String username="";
    public  String password="";
    private CheckBox ch_teacher,ch_student;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register_button= (Button)findViewById(R.id.register_button);
        user_id=(EditText)findViewById(R.id.userid_text);
        user_password=(EditText)findViewById(R.id.pass_text);
        ch_student=(CheckBox)findViewById(R.id.ch_S);
        ch_teacher=(CheckBox) findViewById(R.id.ch_T);

        ch_teacher.setEnabled(true);
        ch_student.setEnabled(true);


        ch_student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ch_teacher.setEnabled(false);
                }
                else {
                    ch_teacher.setEnabled(true);
                }

            }
        });

        ch_teacher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ch_student.setEnabled(false);
                }
                else{
                    ch_student.setEnabled(true);
                }
            }
        });



        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /////////////////

                if(!ch_teacher.isChecked() && !ch_student.isChecked()){
                    Toast.makeText(MainActivity.this,"Select Teacher or Student..!",Toast.LENGTH_SHORT).show();
                    return;
               }



                if(ch_student.isChecked()){
                    ch_teacher.setClickable(false);
                    Intent intent1=new Intent (MainActivity.this,barcode_generator.class);

                    startActivity(intent1);

                }

               else if(ch_teacher.isChecked()){
                    ch_student.setClickable(false );
                    Intent intent=new Intent (MainActivity.this,select_branch.class);
                    startActivity(intent);
                }
                else{

                }


                ////////////




//                try {
//
//                    url = new URL("http://192.168.43.212:7000/home/Teacherlogin");
//                } catch (MalformedURLException e) {
//                    Log.e("assign","problem");
//                    // Toast.makeText(LoginActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//
//
//                new SendPostRequest().execute();
            }
        });




    }

//    public class SendPostRequest extends AsyncTask<String, Void, String> {
//
//
//
//        protected void onPreExecute(){}
//
//        protected String doInBackground(String... arg0) {
//            try {
//                //username = user_id.getText().toString();
//                username=user_id.getText().toString();
//                password = user_password.getText().toString();
//                JSONObject postDataParams = new JSONObject();
//                postDataParams.put("username", username);
//                postDataParams.put("password", password);
//                Log.e("params", postDataParams.toString());
//               /// editor= sharedPreferences.edit();
//               SharedPreferences.Editor editor = getSharedPreferences("Teachers", MODE_PRIVATE).edit();
//               // editor.putString("Key","0");
//
//                editor.putString("sharedTname", username);
//                editor.apply();
//               // editor.commit();
//                Log.i("TAG:___",editor.toString());
//                HttpURLConnection conn = null;
//                try {
//
//                    conn = (HttpURLConnection) url.openConnection();
//                    Log.e("cp1","connectionin");
//                    //Toast.makeText(LoginActivity.this,"connevtion success",Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    //Toast.makeText(LoginActivity.this,"connevtion failed",Toast.LENGTH_SHORT).show();
//                    Log.e("connectionError", e.getMessage());
//                }
//                Log.i("TAG","connection built here");
//                conn.setReadTimeout(15000 /* milliseconds */);
//                conn.setConnectTimeout(15000 /* milliseconds */);
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//////////////////////////////////////////////////////////////
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(getPostDataString(postDataParams));
//                Log.i("TAG","connection reached 1here");
//                writer.flush();
//                writer.close();
//                os.close();
//
//                int responseCode = conn.getResponseCode();
//
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//
//                    Log.i("TAG","connection reached 2here");
//                    BufferedReader in = new BufferedReader(
//                            new InputStreamReader(
//                                    conn.getInputStream()));
//                    StringBuffer sb = new StringBuffer("");
//                    String line = "";
//
//                    while ((line = in.readLine()) != null) {
//
//                        sb.append(line);
//                        break;
//                    }
//                    Log.i("TAG","connection reached 3here");
//
//                    in.close();
//                    return sb.toString();
//                } else {
//                    return new String("false : " + responseCode);
//                }
//            }
//            catch(Exception e){
//                return new String("Exception: " + e.getMessage());
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            String y="";
//            String d="";
//            try {
//                JSONObject json = new JSONObject(s);
//                y=json.getString("error");
//                d=json.getString("data");
//
//                Log.e("value of y",y);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Log.e("servers",s);
//
//
//            if(y.equals("1")){
//                user_id.setText("");
//                user_password.setText("");
//                Toast.makeText(getApplicationContext(), d, Toast.LENGTH_SHORT).show();
//
//            }
//            else if( y.equals("2")){
//               // user_id.setText("");
//              //  user_password.setText("");
//                Toast.makeText(getApplicationContext(), "Welcome Teacher", Toast.LENGTH_SHORT).show();
//                Log.i("TAG","connection reached here");
//                Intent intent=new Intent (MainActivity.this,select_branch.class);
//                startActivity(intent);
//            }
//            else{
//
//            }
//
//
//        }
//    }
//
//
//    public String getPostDataString(JSONObject params) throws Exception {
//
//        StringBuilder result = new StringBuilder();
//        boolean first = true;
//
//        Iterator<String> itr = params.keys();
//
//        while(itr.hasNext()){
//
//            String key= itr.next();
//            Object value = params.get(key);
//
//            if (first)
//                first = false;
//            else
//                result.append("&");
//
//            result.append(URLEncoder.encode(key, "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
//
//        }
//        return result.toString();
//    }
//
//
//
//
//    @Override
//    public void onBackPressed() {
//
//
//    }
//
//
//    @Override
//    protected void onStart() {
//
//
//
//        super.onStart();
//    }
}
