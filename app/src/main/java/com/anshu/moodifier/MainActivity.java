package com.anshu.moodifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
EditText name;
Button startBtn;
SharedPreferences sharedPreferences;
SharedPreferences.Editor editor;
Spinner gender;
String[] genders={"<Select Gender>","Male","Female"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=findViewById(R.id.name);
        startBtn=findViewById(R.id.moodifyBtn);
        gender=findViewById(R.id.genderSelect);
        ArrayAdapter ad =new ArrayAdapter(this,android.R.layout.simple_spinner_item,genders);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(ad);
        gender.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString("Gender",genders[position]);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        sharedPreferences=getSharedPreferences("Moodifier", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtnClicked();
            }
        });

    }
    private void startBtnClicked() {
        String nameStr=name.getText().toString();
        editor.putString("Name",nameStr);
        editor.putBoolean("Logged",true);
        editor.apply();
        startActivity(new Intent(MainActivity.this,Dashboard.class));
        finish();
    }
}