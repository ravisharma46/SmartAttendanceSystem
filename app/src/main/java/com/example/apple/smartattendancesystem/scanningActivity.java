package com.example.apple.smartattendancesystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class scanningActivity extends AppCompatActivity {

    private ListView attendance_marked_list;
    private ArrayList<String>attendance_list;
    private  FloatingActionButton fab,fab1,fab2;
    private Animation fabOpen,fabClose, rotateForward,rotateBackward;
    private Button add,submit;
    private TextView total;


    Boolean isOpen=false;
    private static final String TAG = scanningActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    private URL url;
    private String roll_no="";
    private String rollno="";



    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText) || result.getText().length()!= 11)
            {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();

            if(!attendance_list.contains(result.getText() )){
                attendance_list.add(result.getText());
            }
           else{
                Toast.makeText(getApplicationContext(), "Already Done..!!", Toast.LENGTH_SHORT).show();

            }


            //onPause();
            update_adapter_list();

           // onResume();
            send_attendance();






            //Added preview of scanned barcode
           // ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
           // imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));



        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }


    };

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        //attendance Arraylist create
        attendance_list = new ArrayList<>();


//////////////////////////////////////////////////////////////////////////////////


        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
        //  barcodeView.decodeSingle(callback);
        beepManager = new BeepManager(this);


        /////////////////////////////////////////////////////////////////////////////////

        fab = findViewById(R.id.add_fab1);
        add= (Button)findViewById(R.id.add_rollno);
        submit=(Button) findViewById(R.id.submit_att);




        fabOpen= AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose= AnimationUtils.loadAnimation(this,R.anim.fab_close);

        rotateForward=AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward=AnimationUtils.loadAnimation(this,R.anim.rotate_backward);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             animateFab();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                //Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                //     .setAction("Action", null).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(scanningActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertdialog_custom_view, null);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);

                // Get the custom alert dialog view widgets reference
                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                final EditText et_name = (EditText) dialogView.findViewById(R.id.et_name);

                // Create the alert dialog
                final AlertDialog dialog = builder.create();

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss the alert dialog
                        dialog.cancel();
                        rollno = et_name.getText().toString();
                        //Toast.makeText(getApplication(),
                        //      rollno, Toast.LENGTH_SHORT).show();
                        // Say hello to the submitter

                        if (rollno.length() == 11) {
                            if (!attendance_list.contains(rollno)) {

                                attendance_list.add(rollno);
                                update_adapter_list();
                                send_attendance();

                            } else {
                                Toast.makeText(getApplicationContext(), "Already Done..!!", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid roll no.", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                // Set negative/no button click listener
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss/cancel the alert dialog
                        //dialog.cancel();
                        dialog.dismiss();
                        Toast.makeText(getApplication(),
                                "Cancel button clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();



            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();

                AlertDialog.Builder builder= new AlertDialog.Builder(scanningActivity.this);
                LayoutInflater inflater =getLayoutInflater();
                View dialog_endScan =inflater.inflate(R.layout.end_scan_dailogalert,null);



                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialog_endScan);

                // Get the custom alert dialog view widgets reference
                Button btn_positive = (Button) dialog_endScan.findViewById(R.id.yes_action);
                Button btn_negative = (Button) dialog_endScan.findViewById(R.id.no_action);
                total=(TextView)dialog_endScan.findViewById(R.id.total_present);

                // Create the alert dialog
                final AlertDialog dialog = builder.create();

                int total_prs=attendance_list.size();
                String toal_prst=Integer.toString(total_prs);
                total.setText(toal_prst);

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss the alert dialog
                        dialog.cancel();




                    }
                });

                // Set negative/no button click listener
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss/cancel the alert dialog
                        //dialog.cancel();
                        dialog.dismiss();
                        Toast.makeText(getApplication(),
                                "NO button clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();

            }
        });



    }

    public void animateFab(){

        if(isOpen){
            fab.startAnimation(rotateForward);
            add.startAnimation(fabClose);
            submit.startAnimation(fabClose);
            add.setClickable(false);
            submit.setClickable(false);
            isOpen=false;
        }
        else{
            fab.startAnimation(rotateBackward);
            add.startAnimation(fabOpen);
            submit.startAnimation(fabOpen);
            add.setClickable(true);
            submit.setClickable(true);
            isOpen=true;

        }

    }









    public void send_attendance(){
        try {

            url = new URL("http://192.168.43.212:7000/home/attendance/demo");
        } catch (MalformedURLException e) {
            Log.e("assign", "problem");
            Toast.makeText(scanningActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        new SendPostRequest().execute();
    }








  //////attendance fetch into database...

    public class SendPostRequest extends AsyncTask<String, Void, String> {



        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try {




                //username = user_id.getText().toString();
              //  username=user_id.getText().toString();
              //  password = user_password.getText().toString();
                JSONObject postDataParams = new JSONObject();
                //postDataParams.put("username", username);

                for(int i=0;i<attendance_list.size();i++){

                    roll_no=attendance_list.get(i);
                    postDataParams.put("at",roll_no);
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
                attendance_list.remove(roll_no);
                Toast.makeText(getApplicationContext(), "Roll no. doesn't exit ", Toast.LENGTH_SHORT).show();

            }
            else if( y.equals("0")){
               // user_id.setText("");
              //  user_password.setText("");
                Toast.makeText(getApplicationContext(), "attendance done", Toast.LENGTH_SHORT).show();
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

//////////////////////////////////






    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();


    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();

    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {

        barcodeView.decodeSingle(callback);

    }


    public void update_adapter_list() {

       attendance_marked_list=(ListView)findViewById(R.id.show_rollno);
        // create Arrayadapter..... updating the roll no. on every scan in listview
        ArrayAdapter<String>arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,attendance_list);

        attendance_marked_list.setAdapter(arrayAdapter);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }







}
