package com.example.apple.smartattendancesystem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.security.PrivateKey;


/**
 * A simple {@link Fragment} subclass.
 */
public class EceFragment extends Fragment {


    private Spinner spinner;


    public EceFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_ece, container, false);

         spinner = (Spinner) view.findViewById(R.id.spinner);

         ArrayAdapter<String> adapter= new ArrayAdapter<String>(this.getActivity(),
                 android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.ece_theory_group_list));


         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(adapter);


        return view;

    }

}
