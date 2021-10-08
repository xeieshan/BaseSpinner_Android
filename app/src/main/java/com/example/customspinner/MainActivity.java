package com.example.customspinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<GeneralModel> listCountry;
    BaseSpinner spinnerCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCountry =  findViewById(R.id.spinner_country);

        listCountry = new ArrayList<GeneralModel>();
        listCountry.add(new GeneralModel("1231132","United States of America"));
        listCountry.add(new GeneralModel("4451132","United Kingdom"));


        spinnerCountry.setPlaceholder(R.string.spinner_placeholder);
        spinnerCountry.setData(listCountry);
        spinnerCountry.setSpinnerBackground(R.drawable.spinner_bg_transparent);
        spinnerCountry.setOnValueChangedListener(new BaseSpinner.OnValueChangedListener() {
            @Override
            public void onValueChanged() {
                if(spinnerCountry.getSelectedData().size() == 0) {
                    //Please select something
                }
                if(spinnerCountry.getSelectedData().size() > 0) {
                    if (spinnerCountry.getSelectedData().get(0).getName().equalsIgnoreCase("United States of America")) {
                        //Do something
                    } else if (spinnerCountry.getSelectedData().get(0).getName().equalsIgnoreCase("United Kingdom")) {
                        //Do something
                    }
                }
            }
        });
    }
}