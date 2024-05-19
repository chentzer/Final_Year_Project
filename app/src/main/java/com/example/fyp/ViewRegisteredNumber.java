package com.example.fyp;

// Programmer Name: Liew Chen Tzer
// Program Name: ViewRegisteredNumber.java
//Description: To view the registered contacts
//First Written on: Tuesday, 1-Feb-2022
//Edited on: Thursday, 27-Mar-2022

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewRegisteredNumber extends AppCompatActivity {

    SQLiteDatabase database;
    TextView nameid1, nameid2, nameid3, nameid4, nameid5;
    TextView numid1, numid2, numid3, numid4, numid5;
    LinearLayout l1, l2, l3, l4, l5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registered_number);
        SQLiteDatabase openOrCreateDatabase = openOrCreateDatabase("NumDB", 0, (SQLiteDatabase.CursorFactory) null);
        this.database = openOrCreateDatabase;
        openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS details(name VARCHAR,number VARCHAR);");
        try {
            Cursor rawQuery = this.database.rawQuery("SELECT * FROM details", (String[]) null);
            if (rawQuery.getCount() == 0) {
                showMessage("Error", "No records found.");
                return;
            }
            int i = 0;
            while (rawQuery.moveToNext()) {
                if (i == 0) {
                    this.nameid1 = (TextView) findViewById(R.id.name1);
                    this.numid1 = (TextView) findViewById(R.id.num1);
                    this.nameid1.setText(rawQuery.getString(0));
                    this.numid1.setText(rawQuery.getString(1));
                } else if (i == 1) {
                    this.nameid2 = (TextView) findViewById(R.id.name2);
                    this.numid2 = (TextView) findViewById(R.id.num2);
                    this.nameid2.setText(rawQuery.getString(0));
                    this.numid2.setText(rawQuery.getString(1));
                } else if (i == 2) {
                    this.nameid3 = (TextView) findViewById(R.id.name3);
                    this.numid3 = (TextView) findViewById(R.id.num3);
                    this.nameid3.setText(rawQuery.getString(0));
                    this.numid3.setText(rawQuery.getString(1));
                } else if (i == 3) {
                    this.nameid4 = (TextView) findViewById(R.id.name4);
                    this.numid4 = (TextView) findViewById(R.id.num4);
                    this.nameid4.setText(rawQuery.getString(0));
                    this.numid4.setText(rawQuery.getString(1));
                } else if (i == 4) {
                    this.nameid5 = (TextView) findViewById(R.id.name5);
                    this.numid5 = (TextView) findViewById(R.id.num5);
                    this.nameid5.setText(rawQuery.getString(0));
                    this.numid5.setText(rawQuery.getString(1));
                }
                i++;
            }

            l1 = findViewById(R.id.l1);
            l2 = findViewById(R.id.l2);
            l3 = findViewById(R.id.l3);
            l4 = findViewById(R.id.l4);
            l5 = findViewById(R.id.l5);

            l1.setVisibility(rawQuery.getCount() >= 1 ? View.VISIBLE : View.INVISIBLE);
            l2.setVisibility(rawQuery.getCount() >= 2 ? View.VISIBLE : View.INVISIBLE);
            l3.setVisibility(rawQuery.getCount() >= 3 ? View.VISIBLE : View.INVISIBLE);
            l4.setVisibility(rawQuery.getCount() >= 4 ? View.VISIBLE : View.INVISIBLE);
            l5.setVisibility(rawQuery.getCount() >= 5 ? View.VISIBLE : View.INVISIBLE);

        } catch (SQLiteException unused) {
            showMessage("Error", "No records found.");
        }
    }

    public void showMessage(String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.show();
    }
}