package edu.neu.madcourse.musicool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class ProfileActivity extends AppCompatActivity implements LocationListener {

    private Button logout;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private static final int REQUEST_LOCATION = 1;
    TextView showLocation;
    Double latitude, longitude;
    LocationManager locManager;
    TextView name;
    ImageView profilePhoto;
    TextView age;
    //TextView score;
    TextView userSlogan;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;
    private FirebaseUser currentUser;

    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // logout user
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Helper.loggedIn = false;
                Helper.currentCity = "";
                Helper.currentState = "";
                Helper.currentCountry = "";
                Helper.currentUserName = "";
                Helper.age = "";
                Helper.slogan = "";
                Helper.score = 0;
               // startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        // get user information to display
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        if(user != null) {
            userID = user.getUid();
        } else {
//            userID = "NotLoginYet";
            Toast.makeText(this,"haven't login yet",
                    Toast.LENGTH_SHORT).show();
        }

//      final TextView greeting = (TextView) findViewById(R.id.greeting);
        //  previous: age. Just change to MySlogan (xinglu)
        age = findViewById(R.id.age);
        name =  findViewById(R.id.name);
        profilePhoto = findViewById(R.id.profilePhoto);
        //score = findViewById(R.id.score);
        userSlogan = findViewById(R.id.userSlogan);
        showLocation = findViewById(R.id.location);

        storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child(userID + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.d("MyActivity", "File exists, address is" + uri.toString());
                Picasso.get().load(uri).into(profilePhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("MyActivity", "File not exist");
                profilePhoto.setImageResource(R.drawable.samplephoto);
                Toast.makeText(ProfileActivity.this, "You may upload your profile picture in settings.", Toast.LENGTH_LONG).show();
                }
        });

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);

                if (userprofile != null) {

                    Helper.currentUserName = userprofile.username;
                    name.setText(Helper.currentUserName);

                    if(userprofile.age == null || userprofile.age.equals("")){
                        age.setText("Age: " +  "Mystery");
                    } else {
                        Helper.age = userprofile.age;
                        age.setText("Age: " +  Helper.age);
                    }

                    if(userprofile.slogan == null || userprofile.slogan.equals("")){
                        userSlogan.setText("Slogan: " +  "not set yet");
                    } else {
                        Helper.slogan = userprofile.slogan;
                        userSlogan.setText("Slogan: " +  Helper.slogan);
                    }

                    if(userprofile.city == null || userprofile.city.equals("")){
                        showLocation.setText("Location: not shared yet");
                    } else {
                        Helper.currentCity = userprofile.city;
                        Helper.currentState = userprofile.state;
                        showLocation.setText("Location: "+ Helper.currentCity + ", " + Helper.currentState);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "lol!!! what is wrong here!", Toast.LENGTH_LONG).show();
            }
        });


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
                            startActivity(new Intent(getApplicationContext(), ScoreActivity.class));
                            overridePendingTransition(0,0);
                            return true;
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


    public void getCurrentCity(View view){
        if(!Helper.currentCity.equals("") && !Helper.currentState.equals("")){
            showLocation.setText("Location: " + Helper.currentCity + ", " + Helper.currentState);
            Toast.makeText(this, "Hi! You are still here at " + Helper.currentCity + " " + Helper.currentState, Toast.LENGTH_SHORT).show();
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
            locManager.requestLocationUpdates(locManager.GPS_PROVIDER, 1000, 1, (LocationListener) ProfileActivity.this);
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

            Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

            try {

                List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);

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

            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            uid = currentUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child("Users").child(uid).child("city").setValue(Helper.currentCity);
            mDatabase.child("Users").child(uid).child("state").setValue(Helper.currentState);
            showLocation.setText("Location: " + Helper.currentCity + ", " + Helper.currentState);

        } else {
            Toast.makeText(this, "Ops! Unable to find current location.", Toast.LENGTH_SHORT).show();
            showLocation.setText("Location: not available");
        }
    }

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

    public void goToSetting(View view){
        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
    }
}