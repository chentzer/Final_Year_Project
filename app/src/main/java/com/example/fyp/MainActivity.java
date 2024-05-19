package com.example.fyp;

// Programmer Name: Liew Chen Tzer
// Program Name: MainActivity.java
//Description: To operate the SOS process and to navigate to Menu
//First Written on: Tuesday, 1-Feb-2022
//Edited on: Thursday, 7-April-2022

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    Button MenuButton, cancelButton;
    ImageButton SOSButton, cameraButton, phoneButton;
    TextView Timer, TimerText;
    String link, DefaultEmergencyLine = "999";
    CountDownTimer startCDT, threeSecCDT;
    RelativeLayout screen;
    Handler smsHandler;
    SQLiteDatabase database;
    private LocationRequest locationRequest;
    private String[] permissions;
    private String SMS;
    private double latitude, longitude;
    private boolean sevenSecCDTStatus =false, threeSecCDTStatus =false, widget = false;
    FirebaseDatabase firebase;


    //phone number = +1 555-521-5554

    private static final int REQUEST_CHECK_SETTING = 1001;
    private static final int VIDEO_RECORD_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Permission Check
        permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE
        };

        if(!hasPermissions(MainActivity.this,permissions)){
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }

        //Check for Device Location
        locationPermission();

        //create Emergency Line database
        SQLiteDatabase EmergencyDatabase = openOrCreateDatabase("EmergencyLine", 0, (SQLiteDatabase.CursorFactory) null);
        EmergencyDatabase.execSQL("CREATE TABLE IF NOT EXISTS details(EmergencyLine VARCHAR);");
        if(EmergencyDatabase.rawQuery("SELECT * FROM details", (String[]) null).getCount() <1){
            EmergencyDatabase.execSQL("INSERT INTO details VALUES('" + DefaultEmergencyLine + "');");
            finish();
        }
        this.database = EmergencyDatabase;

        //SOS Button
        SOSButton = findViewById(R.id.SOSButton);
        SOSButton.setOnClickListener(view -> {
            if(!hasPermissions(MainActivity.this,permissions)){
                ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
            }
            SOSButton.setEnabled(false);
            MenuButton.setEnabled(false);
            cancelButton.setVisibility(View.VISIBLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //keeps the screen on
            locationPermission(); //Check for Device Location

            startCDT = new CountDownTimer(7000, 200) {
                @Override
                public void onTick(long millisUntilFinished) {
                    sevenSecCDTStatus = true;
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        sevenSecCDTStatus = false;
                        startCDT.cancel();
                        cancelTimer();
                    }
                }

                @Override
                public void onFinish() {
                    sevenSecCDTStatus = false;
                    cancelTimer();
                }
            }.start(); //Timer to give time for user to grant permission

        });

        //Cancel Button
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            if(sevenSecCDTStatus==true){
                startCDT.cancel();
                cancelButton.setVisibility(View.INVISIBLE);
                cameraButton.setVisibility(View.INVISIBLE);
                phoneButton.setVisibility(View.INVISIBLE);
                MenuButton.setEnabled(true);
                SOSButton.setEnabled(true);
                sevenSecCDTStatus=false;
            }else if(threeSecCDTStatus==true){
                threeSecCDT.cancel();
                TimerText.setVisibility(View.INVISIBLE);
                Timer.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                cameraButton.setVisibility(View.INVISIBLE);
                phoneButton.setVisibility(View.INVISIBLE);
                MenuButton.setEnabled(true);
                SOSButton.setEnabled(true);
                threeSecCDTStatus=false;
            }else{
                screen.clearAnimation(); //clear animation
                smsHandler.removeCallbacksAndMessages(null);
                screen.setBackgroundResource(R.color.black);
                SOSButton.setBackgroundResource(R.color.black);
                cancelButton.setVisibility(View.INVISIBLE);
                cameraButton.setVisibility(View.INVISIBLE);
                phoneButton.setVisibility(View.INVISIBLE);
                MenuButton.setEnabled(true);
                SOSButton.setEnabled(true);
                sendSMSDone();
            }
        });

        //Camera Button
        cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(v -> recordVideo());

        //Phone Button
        phoneButton = findViewById(R.id.phoneButton);
        phoneButton.setOnClickListener(v -> callEmergencyLine());

        //Menu Button
        MenuButton = findViewById(R.id.MenuButton);
        MenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Menu.class);
            startActivity(intent);
        });

        //if widget is pressed
        this.widget = getIntent().getBooleanExtra("widget",false);
        if(widget==true){
            SOSButton.performClick();
        }
    }

    //Overall
    private void cancelTimer() {
        threeSecCDT = new CountDownTimer(3999, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                threeSecCDTStatus=true;
                TimerText = findViewById(R.id.TimerText);
                Timer = findViewById(R.id.Timer);
                TimerText.setVisibility(View.VISIBLE);
                Timer.setVisibility(View.VISIBLE);
                String seconds = String.valueOf(millisUntilFinished /1000);
                Timer.setText(seconds);
            }

            @Override
            public void onFinish() {
                TimerText.setVisibility(View.INVISIBLE);
                Timer.setVisibility(View.INVISIBLE);
                threeSecCDTStatus=false;
                helpProcess();
            }
        }.start(); //timer for user to cancel Help
    }

    private void helpProcess(){
        //screen, camera, location, sms
        cameraButton.setVisibility(View.VISIBLE);
        phoneButton.setVisibility(View.VISIBLE);
        screenBlink();
        helpHandler();
    }

    private void helpHandler(){
        getLocation();
        smsHandler = new Handler();
        smsHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLocation();
                sendSMS();
                firebase();
                smsHandler.postDelayed(this, 120000); //repeats every 2 minutes
            }
        }, 10000);
    }

    //Location
    private void locationPermission() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());
        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes
                            .RESOLUTION_REQUIRED:

                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTING);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            break;
                        }

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    } //location services

    private void getLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CHECK_SETTING);
        }
        else {
            if(!isGPSEnabled())
            {
                link = "ERROR, user did not turn on GPS";
                Toast.makeText(MainActivity.this, link, Toast.LENGTH_LONG).show();
            }
            else
            {
                LocationServices.getFusedLocationProviderClient(MainActivity.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                        .removeLocationUpdates(this);

                                if (locationResult.getLocations().size() > 0) {
                                    int index = locationResult.getLocations().size() - 1;
                                    latitude = locationResult.getLocations().get(index).getLatitude();
                                    longitude = locationResult.getLocations().get(index).getLongitude();
                                    link = "http://maps.google.com/?q=" + MainActivity.this.latitude + "," + MainActivity.this.longitude;
                                }
                            }
                        }, Looper.getMainLooper());
            }
        }

    }//getting current location

    private boolean isGPSEnabled() {
        LocationManager locationManager;
        boolean isEnabled;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //get Location Service
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //Checking if GPS is turned on
        return isEnabled;
    }

    //Camera
    private void recordVideo(){
        try {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            startActivity(intent);
        }catch(Exception e){
            Toast.makeText(MainActivity.this,"Error, cannot start camera",Toast.LENGTH_SHORT).show();
        }
    }


    //Screen
    private void screenBlink(){
        screen = findViewById(R.id.flashingScreen);
        Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        screen.setBackgroundResource(R.color.white);
        SOSButton.setBackgroundResource(R.color.white);
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the layout will fade back in
        screen.startAnimation(animation);
    }


    //SMS
    private void sendSMS(){
        final Cursor rawQuery = openOrCreateDatabase("NumDB", 0, (SQLiteDatabase.CursorFactory) null).rawQuery("SELECT * FROM details", (String[]) null);
        if(link == null){
            SMS = "HELP! I am in an EMERGENCY situation!";
        }else{
            SMS = "HELP! I am in an EMERGENCY situation! I am currently in this location: "+link;

        }
        SmsManager smsManager = SmsManager.getDefault();
        try {
            if (rawQuery.getCount() == 0) {
                Toast.makeText(this, "You have no registered number, SMS not sent", Toast.LENGTH_SHORT).show();
            }else{
                while (rawQuery.moveToNext()){
                smsManager.sendTextMessage(rawQuery.getString(1), null, SMS, null, null);
                }
                Toast.makeText(this, "SMS Sent", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "message failed to sent", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMSDone(){
        final Cursor rawQuery = openOrCreateDatabase("NumDB", 0, (SQLiteDatabase.CursorFactory) null).rawQuery("SELECT * FROM details", (String[]) null);
        SMS = "I am safe now, thank you!";
        SmsManager smsManager = SmsManager.getDefault();
        try {
            if (rawQuery.getCount() == 0) {
                Toast.makeText(this, "You have no registered number, SMS not sent", Toast.LENGTH_SHORT).show();
            }else{
                while (rawQuery.moveToNext()){
                smsManager.sendTextMessage(rawQuery.getString(1), null, SMS, null, null);
                }
                Toast.makeText(this, "Safe SMS Sent", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "message failed to sent", Toast.LENGTH_SHORT).show();
        }
    }

    //Firebase
    private void firebase(){
        firebase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebase.getReference("location");

        Date timeOfIncident = Calendar.getInstance().getTime();

        FirebaseHelper firebaseHelper = new FirebaseHelper(String.valueOf(longitude),String.valueOf(latitude),timeOfIncident);
        String uniqueID = UUID.randomUUID().toString();

        reference.child(String.valueOf(uniqueID)).setValue(firebaseHelper);
    }

    //Phone
    private void callEmergencyLine(){
        String EmergencyLine = null;
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM details", (String[]) null);
        while(rawQuery.moveToNext()){
            EmergencyLine = rawQuery.getString(0);
        }
        database.close();

        if(EmergencyLine.trim().length()>0){
            if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CALL_PHONE) !=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
            }else{
                String dial = "tel: "+EmergencyLine;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }else{
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    //Phone Settings
    private boolean hasPermissions(Context context, String...permissions){

        if(context != null && permissions !=null){
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context, permission)!=PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTING) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(MainActivity.this, "Location Permission granted", Toast.LENGTH_SHORT).show();
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(MainActivity.this, "Location Permission denied", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        if(requestCode == VIDEO_RECORD_CODE){
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(MainActivity.this, "Video Saved", Toast.LENGTH_SHORT).show();
                    break;

                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    } //provide Results based on Activity Action

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"location granted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"location denied",Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==1){
            if(grantResults[1]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"sms granted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"sms denied",Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==1){
            if(grantResults[2]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"camera granted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"camera denied",Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==1){
            if(grantResults[3]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"phone call granted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"phone call denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        this.widget = getIntent().getBooleanExtra("widget", false);
        if(widget==true){
            SOSButton.performClick();
        }
    }
}