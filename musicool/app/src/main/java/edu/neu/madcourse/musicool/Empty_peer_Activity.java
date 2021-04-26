package edu.neu.madcourse.musicool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Empty_peer_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_peer);

        // initialize bottom navigation bar
        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);
        //set home selected
        bottom_navigation.setSelectedItemId(R.id.ic_groups);
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

                    case R.id.ic_groups:
                        return true;

                    case R.id.ic_face:
                        // xinglu: tmp commented. ContactsContract.Profile.class haven't been initialized
                        // startActivity(new Intent(getApplicationContext(), ContactsContract.Profile.class));

                        if(Helper.loggedIn == true) {
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        } else {
                            startActivity(new Intent(getApplicationContext(), Empty_profileActivity.class));
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
}