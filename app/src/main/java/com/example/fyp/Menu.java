package com.example.fyp;

// Programmer Name: Liew Chen Tzer
// Program Name: Menu.java
//Description: To navigate to other options
//First Written on: Tuesday, 1-Feb-2022
//Edited on: Thursday, 21-Mar-2022

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    Button InstructionButton, VRNButton, RNNButton, DRNButton, SELButton ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        InstructionButton = findViewById(R.id.InstructionsButton);
        InstructionButton.setOnClickListener(v -> showI());

        VRNButton = findViewById(R.id.VRNButton);
        VRNButton.setOnClickListener(v -> showVRN());

        RNNButton = findViewById(R.id.RNNButton);
        RNNButton.setOnClickListener(v -> showRNN());

        DRNButton = findViewById(R.id.DRNButton);
        DRNButton.setOnClickListener(v -> showDRN());

        SELButton = findViewById(R.id.SELButton);
        SELButton.setOnClickListener(v -> showSEL());

    }

    public void showI(){
        Intent intent = new Intent(this, Instruction.class);
        startActivity(intent);
    }

    public void showRNN(){
        Intent intent = new Intent(this, RegisterNewNumber.class);
        startActivity(intent);
    }

    public void showVRN(){
        Intent intent = new Intent(this, ViewRegisteredNumber.class);
        startActivity(intent);
    }

    public void showDRN(){
        Intent intent = new Intent(this, DeleteRegisteredNumber.class);
        startActivity(intent);
    }

    public void showSEL(){
        Intent intent = new Intent(this, SetEmergencyLine.class);
        startActivity(intent);
    }

}