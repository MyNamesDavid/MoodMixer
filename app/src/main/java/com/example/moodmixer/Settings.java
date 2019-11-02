package com.example.moodmixer;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class Settings extends AppCompatActivity {
    String [] genre = {"R&B","Rap","Country","Alternative","Pop","empty string"};
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.fragment_settings);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_dropdown_item_1line,genre);
        MaterialBetterSpinner betterSpinner = (MaterialBetterSpinner)findViewById(R.id.genre_select_one);
        betterSpinner.setAdapter(arrayAdapter);
    }



}
