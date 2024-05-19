package com.example.fyp;

// Programmer Name: Liew Chen Tzer
// Program Name: DeleteRegisteredNumber.java
//Description: To delete registered contact(s)
//First Written on: Tuesday, 1-Feb-2022
//Edited on: Thursday, 27-March-2022

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteRegisteredNumber extends AppCompatActivity {

    private SQLiteDatabase database;
    TextView nameid1, nameid2, nameid3, nameid4, nameid5;
    TextView numid1, numid2, numid3, numid4, numid5;
    LinearLayout l1, l2, l3, l4, l5;
    ImageButton del1,del2,del3,del4,del5;

    private boolean oneEntry = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_registered_number);


        SQLiteDatabase openOrCreateDatabase = openOrCreateDatabase("NumDB", 0, null);
        this.database = openOrCreateDatabase;
        openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS details(name VARCHAR,number VARCHAR);");
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM details", null);
        try {
            if (rawQuery.getCount() == 1) {
                this.oneEntry = true;
            } else if (rawQuery.getCount() == 0) {
                showMessage("Error", "No records found.");
                return;
            }
            int i = 0;
            while (rawQuery.moveToNext()) {
                if (i == 0) {
                    this.nameid1 = findViewById(R.id.name1);
                    this.numid1 = findViewById(R.id.num1);
                    this.nameid1.setText(rawQuery.getString(0));
                    this.numid1.setText(rawQuery.getString(1));
                } else if (i == 1) {
                    this.nameid2 = findViewById(R.id.name2);
                    this.numid2 = findViewById(R.id.num2);
                    this.nameid2.setText(rawQuery.getString(0));
                    this.numid2.setText(rawQuery.getString(1));
                } else if (i == 2) {
                    this.nameid3 = findViewById(R.id.name3);
                    this.numid3 = findViewById(R.id.num3);
                    this.nameid3.setText(rawQuery.getString(0));
                    this.numid3.setText(rawQuery.getString(1));
                } else if (i == 3) {
                    this.nameid4 = findViewById(R.id.name4);
                    this.numid4 = findViewById(R.id.num4);
                    this.nameid4.setText(rawQuery.getString(0));
                    this.numid4.setText(rawQuery.getString(1));
                } else if (i == 4) {
                    this.nameid5 = findViewById(R.id.name5);
                    this.numid5 = findViewById(R.id.num5);
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

        del1 = (ImageButton) findViewById(R.id.del1);
        del1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                del(view);
            }
        });

        del2 = (ImageButton) findViewById(R.id.del2);
        del2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                del(view);
            }
        });

        del3 = (ImageButton) findViewById(R.id.del3);
        del3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                del(view);
            }
        });

        del4 = (ImageButton) findViewById(R.id.del4);
        del4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                del(view);
            }
        });

        del5 = (ImageButton) findViewById(R.id.del5);
        del5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                del(view);
            }
        });
    }

    public void del(View view) {
        if (this.oneEntry) {
            try {
                this.database.rawQuery("DELETE FROM details WHERE number = ?", new String[]{this.numid1.getText().toString()}).moveToFirst();
                finish();
                Toast.makeText(getApplicationContext(), "DELETED ", Toast.LENGTH_SHORT).show();
            } catch (NullPointerException unused) {
                showMessageError("Error", "Sorry, an unexpected error has occurred\nPlease try again");
            }
        } else {
            try {
                switch (view.getId()) {
                    case R.id.del1 /*2131165272*/:
                        this.database.rawQuery("DELETE FROM details WHERE number = ?", new String[]{this.numid1.getText().toString()}).moveToFirst();
                        break;
                    case R.id.del2 /*2131165273*/:
                        this.database.rawQuery("DELETE FROM details WHERE number = ?", new String[]{this.numid2.getText().toString()}).moveToFirst();
                        break;
                    case R.id.del3 /*2131165274*/:
                        this.database.rawQuery("DELETE FROM details WHERE number = ?", new String[]{this.numid3.getText().toString()}).moveToFirst();
                        break;
                    case R.id.del4 /*2131165275*/:
                        this.database.rawQuery("DELETE FROM details WHERE number = ?", new String[]{this.numid4.getText().toString()}).moveToFirst();
                        break;
                    case R.id.del5 /*2131165276*/:
                        this.database.rawQuery("DELETE FROM details WHERE number = ?", new String[]{this.numid5.getText().toString()}).moveToFirst();
                        break;
                }
                this.database.close();
                finish();
                startActivity(getIntent());
            } catch (NullPointerException unused2) {
                showMessageError("Error", "Sorry, an unexpected error has occurred\nPlease try again");
            }
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
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteRegisteredNumber.this.finish();
            }
        });
        builder.show();
    }
}