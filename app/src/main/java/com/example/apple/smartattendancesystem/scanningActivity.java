package com.example.apple.smartattendancesystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class scanningActivity extends AppCompatActivity {

    private Button scan;
    private ListView attendance_marked_list;
    private ArrayList<String>attendance_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);


        scan=(Button)findViewById(R.id.scan);
        attendance_marked_list=(ListView)findViewById(R.id.show_rollno);

        //attendance list view create
        attendance_list=new ArrayList<>();
        ArrayAdapter<String>arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,attendance_list);

                attendance_marked_list.setAdapter(arrayAdapter);



                //scan button listener initialize
        final Activity activity=this;
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.CODE_39);
                integrator.setPrompt("Scan a barcode");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });



        FloatingActionButton fab = findViewById(R.id.add_rollno);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                   //     .setAction("Action", null).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(scanningActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertdialog_custom_view,null);

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
                        String rollno = et_name.getText().toString();
                        Toast.makeText(getApplication(),
                                 rollno, Toast.LENGTH_SHORT).show();
                        // Say hello to the submitter

                        if(rollno.length()==11){
                            if(!attendance_list.contains(rollno)){
                                attendance_list.add(rollno);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Already Done..!!", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else{
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
                                "No button clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();


            }
        });



    }

    // scan button result activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();


            // checking list doen't contain duplicate roll no.
            if(!attendance_list.contains(scanContent)){
                attendance_list.add(scanContent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Already Done..!!", Toast.LENGTH_SHORT).show();

            }



        }else{
             Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
