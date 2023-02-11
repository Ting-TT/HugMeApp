package com.example.hugme;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.location.LocationListenerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class LocationActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    TextView locationText;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // change action bar colour
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().
                getColor(R.color.navy)));

        locationText =(TextView)findViewById(R.id.locationText);
        locationManager =(LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener =new LocationListener(){

            @Override
            public void onLocationChanged(@NonNull Location location) {
                locationText.setText(location.getLatitude()+","+location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >+ Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
             ||checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
             ||checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
                 requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},10);
            }
        }

    }
    @SuppressLint("MissingPermission")
    public void getLocation(View v){
        locationManager.requestLocationUpdates("gps",1000,0, locationListener);
    }
}