package edu.neu.madcourse.musicool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    TextView greeting;
    Button login, logout;
    Button modeButton;
    String duration = "10 Seconds";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), ChallengeActivity.class));
                Intent challengeIntent = new Intent(MainActivity.this, ChallengeActivity.class);

                challengeIntent.putExtra("duration", duration);
                startActivity(challengeIntent);
            }
        });

        modeButton = findViewById(R.id.modeButton);
        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mode = new Intent(MainActivity.this, ModeActivity.class);
                startActivityForResult(mode, 1);

            }
        });

//        greeting = findViewById(R.id.greeting);
//        greeting.setText("Welcome " + Helper.currentUserName);

//        login = findViewById(R.id.bt_login);
//        logout = findViewById(R.id.bt_logout);


        // initialize bottom navigation bar
        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);
        //set home selected
        bottom_navigation.setSelectedItemId(R.id.ic_home);
        // perform itemselected listener;
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
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
//                        startActivity(new Intent(getApplicationContext(), ScoreActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
                    case R.id.ic_face:

                        // check if user is logged in or not
//                        user = FirebaseAuth.getInstance().getCurrentUser();
//                        reference = FirebaseDatabase.getInstance().getReference("Users");

                        if(Helper.loggedIn == true) {
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        } else {
                            startActivity(new Intent(getApplicationContext(), Empty_profileActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        }
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

//        // get user information to display
//
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        //todo: Peng Hao add comment if user != null
//        if(user != null){
//            reference = FirebaseDatabase.getInstance().getReference("Users");
//            userID = user.getUid();
//
//            final Button bt_login = (Button) findViewById(R.id.bt_login);
//            final Button bt_logout = (Button) findViewById(R.id.bt_logout);
//            final TextView greeting = (TextView) findViewById(R.id.greeting);
//
//            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    User userprofile = snapshot.getValue(User.class);
//
//                    if (userprofile != null) {
//                        bt_logout.setVisibility((View.VISIBLE));
//                        String username = userprofile.username;
//                        greeting.setText("Welcome, " + username + "!");
//
//                    } else {
//                        bt_login.setVisibility((View.VISIBLE));
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            });
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                duration =data.getStringExtra("duration");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
    }//onActivityResult
}