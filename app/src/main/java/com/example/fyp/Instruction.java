package com.example.fyp;

// Programmer Name: Liew Chen Tzer
// Program Name: Instruction.java
//Description: To display instructions to users
//First Written on: Tuesday, 1-Feb-2022
//Edited on: Thursday, 17-Feb-2022

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Instruction extends AppCompatActivity {

    Button BackButton;
    TextView textview1,textview2,textview3,textview4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        textview1 = (TextView) findViewById(R.id.instruction1);
        textview2 = (TextView) findViewById(R.id.instruction2);
        textview3 = (TextView) findViewById(R.id.instruction3);
        textview4 = (TextView) findViewById(R.id.instruction4);

        String instruction1="Please register your emergency contact numbers under MENU>REGISTER NUMBER; Please register your emergency line under MENU>SET EMERGENCY LINE (default number is 999)";
        String instruction2="Press the SOS ButtonSOS Widget or on the homepage in case of an EMERGENCY. Your Current Location will be sent as an SMS to your registered contacts every 5 minutes";
        String instruction3="When SOS is activated, 2 buttons will show. The camera button will open a video camera; The phone button will call the Emergency Line";
        String instruction4="If required, please mute your phone so that incoming call will not ring to the public in order to keep your location unknown";


        textview1.setText(instruction1);
        textview2.setText(instruction2);
        textview3.setText(instruction3);
        textview4.setText(instruction4);

        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(v -> showMenu());

    }

    public void showMenu(){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}