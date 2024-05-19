package com.example.fyp;

// Programmer Name: Liew Chen Tzer
// Program Name: SetEmergencyLine.java
//Description: To change the Emergency Number
//First Written on: Tuesday, 1-Feb-2022
//Edited on: Thursday, 22-March-2022

import androidx.appcompat.app.AppCompatActivity;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SetEmergencyLine extends AppCompatActivity {

    String tempEL, PreviousEmergencyLine;
    Button SELSaveButton;
    EditText SetEmergencyLine;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_emergency_line);

        SQLiteDatabase EmergencyDatabase = openOrCreateDatabase("EmergencyLine", 0, (SQLiteDatabase.CursorFactory) null);
        this.database = EmergencyDatabase;

        SetEmergencyLine = findViewById(R.id.SetEmergencyLine);
        SELSaveButton = findViewById(R.id.SELSaveButton);
        SELSaveButton.setOnClickListener(v -> {
            tempEL = String.valueOf(SetEmergencyLine.getText());
            if(isNumber(tempEL)){
                if (tempEL.length() != 3 && tempEL.length()!=2) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid Emergency Number", Toast.LENGTH_SHORT).show();
                }else{
                    Cursor rawQuery = this.database.rawQuery("SELECT * FROM details", (String[]) null);
                    while (rawQuery.moveToNext()) {
                        PreviousEmergencyLine = rawQuery.getString(0);
                    }
                    this.database.rawQuery("DELETE FROM details WHERE EmergencyLine = ?", new String[]{this.PreviousEmergencyLine.toString()}).moveToFirst();
                    this.database.execSQL("INSERT INTO details VALUES('" + tempEL + "');");
                    finish();
//                    database.close();
                    Toast.makeText(getApplicationContext(), "Emergency Number Saved", Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getApplicationContext(), "Please enter a valid Emergency Number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNumber(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}