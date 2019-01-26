package com.example.apple.smartattendancesystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class scanningActivity extends AppCompatActivity {

    private ListView attendance_marked_list;
    private ArrayList<String>attendance_list;

    private static final String TAG = scanningActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            //if(result.getText() == null || result.getText().equals(lastText))
            if(result.getText() == null )
            {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();
            attendance_list.add(result.getText());
            onPause();

            onResume();






            //Added preview of scanned barcode
           // ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
           // imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));



        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }


    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        //attendance Arraylist create
        attendance_list=new ArrayList<>();


//////////////////////////////////////////////////////////////////////////////////


        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
      //  barcodeView.decodeSingle(callback);
        beepManager = new BeepManager(this);



 /////////////////////////////////////////////////////////////////////////////////


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
                                "Cancel button clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();


            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();


    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();

        attendance_marked_list=(ListView)findViewById(R.id.show_rollno);
        // create Arrayadapter..... updating the roll no. on every scan in listview
        ArrayAdapter<String>arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,attendance_list);

        attendance_marked_list.setAdapter(arrayAdapter);
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





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


}
