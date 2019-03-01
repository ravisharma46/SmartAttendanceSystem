package com.example.apple.smartattendancesystem;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class barcode_generator extends AppCompatActivity {

    private ImageView barcodeView;
    private Button generate_barcode;
    private EditText rollNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_generator);

        barcodeView= (ImageView) findViewById(R.id.set_barcode);
        generate_barcode=(Button) findViewById(R.id.generate_barcode);
        rollNo= (EditText) findViewById(R.id.rollNo);


        generate_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=rollNo.getText().toString();
                MultiFormatWriter multiFormatWriter=new MultiFormatWriter();

                try{
                    if(text.length()!=11){
                        Toast.makeText(getApplicationContext(),"Enter 11 digit roll no.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        BitMatrix bitMatrix= multiFormatWriter.encode(text,BarcodeFormat.CODE_39,550,100);
                        BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                        Bitmap bitmap= barcodeEncoder.createBitmap(bitMatrix);
                        barcodeView.setImageBitmap(bitmap);
                    }


                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        });



    }
}
