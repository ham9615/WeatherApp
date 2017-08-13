package com.example.adi.weathermap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static com.example.adi.weathermap.R.id.humidity_field;
import static com.example.adi.weathermap.R.id.pressure_field;

public class MainActivity extends AppCompatActivity implements LocationListener{

    Typeface weatherFont;

    TextView cityField,detailsField,currentTemperatureField,humidity_field,pressure_field,weatherIcon,updateField;
    ImageButton locationButton;
    LocationManager locationManager;
    Function.placeidtask asyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this,"Click on Location Button to retrieve current information",Toast.LENGTH_LONG).show();

        weatherFont = Typeface.createFromAsset(getAssets(),"fonts/weathericons-regular-webfont.ttf");
        LocationManager locationManager;
        cityField = (TextView)findViewById(R.id.city_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        humidity_field = (TextView)findViewById(R.id.humidity_field);
        pressure_field = (TextView)findViewById(R.id.pressure_field);
        updateField = (TextView)findViewById(R.id.updated_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);
        locationButton = (ImageButton)findViewById(R.id.location_icon);

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},101);

        }

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationButton.setVisibility(View.GONE);
                getLocation();
            }


        });


        asyncTask = new Function.placeidtask(new Function.AsyncResponse() {
           @Override
           public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {
               cityField.setText(weather_city);
               updateField.setText(weather_updatedOn);
               detailsField.setText(weather_description);
               currentTemperatureField.setText(weather_temperature);
               humidity_field.setText("Humidity: "+weather_humidity);
               pressure_field.setText("Pressure: "+weather_pressure);
               weatherIcon.setText(Html.fromHtml(weather_iconText));
           }
       });


    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }catch (SecurityException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {

            asyncTask.execute(location.getLatitude(),location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(),"GPS and Internet is enabled",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(),"Please Enable GPS and Internet",Toast.LENGTH_SHORT).show();

    }
}
