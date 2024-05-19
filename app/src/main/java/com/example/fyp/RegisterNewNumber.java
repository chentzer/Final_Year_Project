package com.example.fyp;

// Programmer Name: Liew Chen Tzer
// Program Name: RegisterNewNumber.java
//Description: To register new contact
//First Written on: Tuesday, 1-Feb-2022
//Edited on: Thursday, 27-March-2022

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;

public class RegisterNewNumber extends AppCompatActivity {

    EditText name;
    EditText number;
    Button RNNSaveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_number);

        RNNSaveButton = findViewById(R.id.RNNSaveButton);

        RNNSaveButton.setOnClickListener(this::RegisterNumber);
    }



    @SuppressLint("Recycle")
    public void RegisterNumber(View view) {
        this.name = (EditText) findViewById(R.id.SetEmergencyLine);
        this.number = (EditText) findViewById(R.id.editText2);
        String obj = this.name.getText().toString();
        String obj2 = this.number.getText().toString();
        if (obj.length() == 0 && obj2.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter the details", Toast.LENGTH_SHORT).show();
        } else if (obj.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter the name", Toast.LENGTH_SHORT).show();
        } else if (obj2.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter the contact number", Toast.LENGTH_SHORT).show();
        } else if (obj2.length() < 7) {
            Toast.makeText(getApplicationContext(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        }else if (obj2.length() > 15) {
            Toast.makeText(getApplicationContext(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        }else {
            SQLiteDatabase openOrCreateDatabase = openOrCreateDatabase("NumDB", 0, (SQLiteDatabase.CursorFactory) null);
            openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS details(name VARCHAR,number VARCHAR);");
            if (openOrCreateDatabase.rawQuery("SELECT * FROM details", (String[]) null).getCount() < 5) {
                try {
                    openOrCreateDatabase.execSQL("INSERT INTO details VALUES('" + obj + "','" + obj2 + "');");
                    finish();
                    Toast.makeText(getApplicationContext(), "SAVED", Toast.LENGTH_SHORT).show();
                } catch (SQLiteException | NullPointerException unused) {
                    showMessageError("Error", "Sorry, an unexpected error has occurred\nPlease try again");
                    return;
                }
            } else {
                showMessage("Maximum limit reached", "You can only save up to 5 contact numbers");
            }
            openOrCreateDatabase.close();
        }
    }

    public void showMessage(String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.show();
    }

    public void showMessageError(String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setPositiveButton("OK", (dialogInterface, i) -> RegisterNewNumber.this.finish());
        builder.show();
    }
}