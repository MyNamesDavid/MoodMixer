package com.example.moodmixer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Settings extends AppCompatActivity {
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    String SelectedServer;

    String [] genre = {"R&B","Rap","Country","Alternative","Pop","Rock","Jazz",};
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.fragment_settings);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_dropdown_item_1line,genre);
        MaterialBetterSpinner betterSpinnerOne = (MaterialBetterSpinner)findViewById(R.id.genre_select_one);
        MaterialBetterSpinner betterSpinnerTwo = (MaterialBetterSpinner)findViewById(R.id.genre_select_two);
        MaterialBetterSpinner betterSpinnerThree = (MaterialBetterSpinner)findViewById(R.id.genre_select_three);
        MaterialBetterSpinner betterSpinnerFour = (MaterialBetterSpinner)findViewById(R.id.genre_select_four);
        betterSpinnerOne.setAdapter(arrayAdapter);
        betterSpinnerTwo.setAdapter(arrayAdapter);
        betterSpinnerThree.setAdapter(arrayAdapter);
        betterSpinnerFour.setAdapter(arrayAdapter);
        betterSpinnerOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedServer = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void saveGenreSettings(View view) {
        File txtFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MyFolder/");
        if (!txtFolder.exists()) {
            txtFolder.mkdir();
        }
        File file = new File(txtFolder, "setting.txt");
        String.valueOf(SelectedServer.getBytes());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(SelectedServer.getBytes());
            fos.close();
            Toast.makeText(getApplicationContext(),"Setting Saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadSavedGenreSettings(){

    }



}
