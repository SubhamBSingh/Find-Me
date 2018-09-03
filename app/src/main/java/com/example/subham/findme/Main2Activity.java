package com.example.subham.findme;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.speech.tts.TextToSpeech;
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

public class Main2Activity extends AppCompatActivity
{
    double Latitude;
    double Longitude;
    String LocationInfo;
    TextView tvLocation;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tvLocation = (TextView) findViewById(R.id.tvLocation);

        Button btnFind = (Button) findViewById(R.id.btnFind);
        Button btnSpeak = (Button) findViewById(R.id.btnSpeak);
        Button btnMap = (Button) findViewById(R.id.btnMap);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5,new MyLocation());

        btnFind.setOnClickListener(new MyClick());
        btnSpeak.setOnClickListener(new MyClick());
        btnMap.setOnClickListener(new MyClick());

        tts = new TextToSpeech(Main2Activity.this,new MySpeak());
    }

    class MyClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if(v.getId() == R.id.btnFind)
            {
                tvLocation.setText(LocationInfo);

                try
                {
                    Intent I = new Intent(Main2Activity.this, Notification.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            Main2Activity.this, 111, I, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, 3000, pendingIntent);
                }

                catch(Exception E)
                {
                    Toast.makeText(Main2Activity.this, "Error : "+E, Toast.LENGTH_SHORT).show();
                }
            }

            else if(v.getId() == R.id.btnSpeak)
            {
                tts.speak(LocationInfo,TextToSpeech.QUEUE_FLUSH,null);
            }

            else if(v.getId() == R.id.btnMap)
            {
                Intent i = new Intent(Main2Activity.this,MapsActivity.class);
                startActivity(i);
            }
        }
    }

    class MyLocation implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location)
        {
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();

            try
            {
                Geocoder geocoder = new Geocoder(Main2Activity.this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(Latitude,Longitude,1);
                Address a = addresses.get(0);

                LocationInfo = a.getAddressLine(0)+"\n"+a.getSubLocality()+"\n"+a.getLocality()+"\n"+a.getSubAdminArea()+"\n"+a.getAdminArea();
            }
            catch(Exception E)
            {
                Toast.makeText(Main2Activity.this, "Error", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onProviderEnabled(String provider)
        {

        }

        @Override
        public void onProviderDisabled(String provider)
        {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    }

    class MySpeak implements TextToSpeech.OnInitListener
    {
        @Override
        public void onInit(int status)
        {
            tts.setLanguage(Locale.UK);
        }
    }
}
