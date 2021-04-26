package edu.neu.madcourse.musicool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import static edu.neu.madcourse.musicool.R.id.bottom_navigation;


public class Empty_profileActivity extends AppCompatActivity implements LocationListener{

    private static final int REQUEST_LOCATION = 1;
    TextView showLocation;
    //LocationManager locationManager;
    Double latitude, longitude;
   // String countryCode, detailAddress;
    LocationManager locManager;
    //String address, city, country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_profile);

        showLocation = findViewById(R.id.location);

        if(!Helper.currentCity.equals("") && !Helper.currentState.equals("")){
            showLocation.setText("Location "+ Helper.currentCity + ", " + Helper.currentState);
        } else {
            showLocation.setText("Location: not shared yet");
        }

        // initialize bottom navigation bar
        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);
        //set face selected
        bottom_navigation.setSelectedItemId(R.id.ic_face);
        // perform itemselected listener;
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ic_grading:
                        if(Helper.loggedIn == true) {

                            startActivity(new Intent(getApplicationContext(), ScoreActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        } else {
                            startActivity(new Intent(getApplicationContext(), Empty_scoreActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        }
                    case R.id.ic_face:
                        return true;
                    case R.id.ic_groups:
                        if(Helper.loggedIn == true) {
                            startActivity(new Intent(getApplicationContext(), PeerActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        } else {
                            startActivity(new Intent(getApplicationContext(), Empty_peer_Activity.class));
                            overridePendingTransition(0,0);
                            return true;
                        }

                }
                return false;
            }
        });
    }

    public void goToLogin(View view){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }


    public void getCurrentCity(View view){

        if(!Helper.currentCity.equals("") && !Helper.currentState.equals("")){
            showLocation.setText("Location: " + Helper.currentCity + ", " + Helper.currentState + Helper.currentCountry);
            Toast.makeText(this, "Hi you still here at " + Helper.currentCity + " " + Helper.currentState, Toast.LENGTH_SHORT).show();
            return;
        } else {
            getLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        showLocation.setText("Getting current location..(Forgive me being slow🥺)");

        //Runtime Permission: for get access for location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        else {
            updateLocation();
        }
    }
    @SuppressLint("MissingPermission")
    private void updateLocation() {
        // permission allowed
        try{
            // location manager class provides the facility to get latitude and longitude coor of current location
            locManager= (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locManager.requestLocationUpdates(locManager.GPS_PROVIDER, 1000, 1, (LocationListener) Empty_profileActivity.this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {

        if(location != null){

            latitude = (double) location.getLatitude();
            longitude = (double) location.getLongitude();

            //Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
//            Geocoder geocoder;
//            List<Address> addresses;

            Geocoder geoCoder = new Geocoder(this, Locale.getDefault());


            try {

                List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(),   location.getLongitude(), 5);

                if (addresses.size() > 0) {

                    Helper.currentCity = addresses.get(0).getLocality();
                    Helper.currentState = addresses.get(0).getAdminArea();
                    Helper.currentCountry = addresses.get(0).getCountryCode();
                //  detailAddress =  addresses.get(0).getAddressLine(0);
                }
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
            showLocation.setText("Location: " + Helper.currentCity + ", " + Helper.currentState
                    + ", "+  Helper.currentCountry);

        } else {

            Toast.makeText(this, "Ops! Unable to find location.", Toast.LENGTH_SHORT).show();
            showLocation.setText("Location: not available");
            }
        }


//        if (location != null) {
//            showLocation.setText("Current Location: \nLatitude: " + location.getLatitude() + ",\n" + "Longitude: " + location.getLongitude());
//        } else {
//            showLocation.setText("Not able to locate, please move around or get to an open space.");
//        }
//    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(@NonNull String provider) { }

    @Override
    public void onProviderDisabled(@NonNull String provider) { }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1]== PackageManager.PERMISSION_GRANTED)) {
                updateLocation();
            } else {
                Snackbar.make(showLocation, "The access to the location information was denied.", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        }
    }





//    public void getCurrentCity(View view){
//
//        if(!Helper.currentCity.equals("") && !Helper.currentState.equals("")){
//            showLocation.setText(Helper.currentCity + " " + Helper.currentState);
//            Toast.makeText(this, "Hi you still here at " + Helper.currentCity + " " + Helper.currentState, Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        ActivityCompat.requestPermissions( this,
//                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            OnGPS();
//        } else {
//            getLocation();
//        }
//    }

//    private void OnGPS() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(
//                        Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            }
//        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
//
//    private void getLocation() {
//
//        if (ActivityCompat.checkSelfPermission(
//
//                Empty_profileActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                Empty_profileActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//
//        } else {
//
//            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//            if (locationGPS != null) {
//
//                latitude = locationGPS.getLatitude();
//                longitude = locationGPS.getLongitude();
//
//                Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
//
//                try {
//                    List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
//                    if (addresses.size() > 0) {
//
//                        Helper.currentCity = addresses.get(0).getLocality();
//                        Helper.currentState = addresses.get(0).getAdminArea();
//                        countryCode = addresses.get(0).getCountryCode();
//                        test =  addresses.get(0).getAddressLine(0);
//                    }
//                }
//                catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//                // don't know whether GPS is wrong or me. I tested I'm at Mountain View LOL (Xinglu)
//                showLocation.setText("Location: " +  Helper.currentCity + ", " + Helper.currentState + " "+ countryCode + " " + test );
//            } else {
//
//                Toast.makeText(this, "Ops! Unable to find location.", Toast.LENGTH_SHORT).show();
//                showLocation.setText("Location: not available");
//            }
//        }
//    }
}