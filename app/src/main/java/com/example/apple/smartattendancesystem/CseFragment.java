package com.example.apple.smartattendancesystem;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CseFragment extends Fragment {


    private Spinner spinner_group,spinner_year,spinner_class;
    private  String select_cse_group,select_cse_year,select_cse_classMode;
    private TextView date,time;
    private DatePickerDialog datePickerDialog;
    private String format = "";
    private Button scan;
    private String date_1;
    private String time_1;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_cse, container, false);

        spinner_group = (Spinner) view.findViewById(R.id.spinner_select_group);
        spinner_year=(Spinner)view.findViewById(R.id.spinner_select_year);
        spinner_class=(Spinner)view.findViewById(R.id.spinner_select_class);

        date=(TextView) view.findViewById(R.id.date_picker);
        time=(TextView) view.findViewById(R.id.time_picker);
        scan=(Button)view.findViewById(R.id.start_scan_ece);

        final String Theory_class[]={"C123","C456","C789","C10 11 12"};
        final String lab_class[]={"C1","C2","C3","C4","C5","C6","C7","C8","C9","C11","C12","C13"};



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

                select_cse_year=parentView.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(),"You have selected "+select_cse_year,Toast.LENGTH_SHORT).show();


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



                select_cse_classMode=parentView.getItemAtPosition(position).toString();

                Toast.makeText(getActivity(),"You have selected "+select_cse_classMode+" Class",Toast.LENGTH_SHORT).show();




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

                select_cse_group=parentView.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(),"You have selected "+select_cse_group+" group",Toast.LENGTH_SHORT).show();


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
                    }
                }, hour, minute, false);// 12 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date_1==null){
                    Toast.makeText(getActivity(),"Please Select Date",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(time_1==null){
                    Toast.makeText(getActivity(),"Please Select Time",Toast.LENGTH_SHORT).show();
                    return;
                }

                final String[] items = {"Year    "+select_cse_year, "Class    "+select_cse_classMode,
                        "Group    "+select_cse_group, "Date    "+date_1,"Time    "+time_1};
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
                //   builder.setPositiveButtonIcon(getResources().getDrawable(android.R.drawable.ic_menu_call, getTheme()));
                //  builder.setIcon(getResources().getDrawable(R.drawable.jd, getTheme()));

                AlertDialog alertDialog = builder.create();

                alertDialog.show();

                Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                // button.setBackgroundColor(Color.BLACK);
                button.setPadding(0, 0, 20, 0);
                // button.setTextColor(Color.WHITE);
            }
        });

/// update actionbar title
        getActivity().setTitle("CSE branch");

        return view;
    }

    public void startScan(){

        Intent intent=new Intent(getActivity(),scanningActivity.class);
        startActivity(intent);

    }
}
