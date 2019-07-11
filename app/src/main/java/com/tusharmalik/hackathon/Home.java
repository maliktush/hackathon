package com.tusharmalik.hackathon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import static android.view.View.VISIBLE;

public class Home extends AppCompatActivity  implements LocationListener {
    TextView hellotext;
    TextView locationText,weatherText;
    LocationManager locationManager;
    Button getweather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        hellotext=findViewById(R.id.hellotext);
        getweather=findViewById(R.id.getweather);
        weatherText=findViewById(R.id.weather);
        getweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherText.setVisibility(View.VISIBLE);
            }
        });
        Intent intent = getIntent();
        //String hello = intent.getExtras().getString("epuzzle");
        hellotext.setText("hello Tushar!");
        locationText = (TextView)findViewById(R.id.locationText);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }
        getLocation();
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(Home.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
}
