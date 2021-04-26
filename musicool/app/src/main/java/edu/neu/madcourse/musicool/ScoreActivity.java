package edu.neu.madcourse.musicool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.neu.madcourse.musicool.Helper;

public class ScoreActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private List<DataPoint> dataPointList = new ArrayList<>();
    volatile boolean notReceived = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        getUserInfo();
        // initialize bottom navigation bar
        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);
        //set home selected
        bottom_navigation.setSelectedItemId(R.id.ic_grading);
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



        final GraphView graph = findViewById(R.id.graph);
        graph.setVisibility(View.VISIBLE);


        databaseReference.child(userID).child("scores").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    dataPointList.add(new DataPoint(0,0));
                    int minX = 0, maxX = 0;
                    int minY = 0, maxY = 0;
                    for(DataSnapshot ds : task.getResult().getChildren()){
                        int x = Integer.parseInt(ds.getKey());
                        minX = Math.min(x, minX);
                        maxX = Math.max(x, maxX);
                        int y = ds.getValue(Long.class).intValue();
                        minY = Math.min(y, minY);
                        maxY = Math.max(y, maxY);
                        dataPointList.add(new DataPoint(x, y));
                    }
                    DataPoint[] arr = new DataPoint[dataPointList.size()];
                    for(int i = 0; i < arr.length; i++){
                        arr[i] = dataPointList.get(i);
                    }

                    try {
                        LineGraphSeries <DataPoint> series = new LineGraphSeries <> (arr);
                        series.setTitle("Progress");
                        // set manual X bounds
                        graph.getViewport().setXAxisBoundsManual(true);
                        graph.getViewport().setMinX(0);
                        graph.getViewport().setMaxX(maxX+1);

                        // set manual Y bounds
                        graph.getViewport().setYAxisBoundsManual(true);
                        graph.getViewport().setMinY(0);
                        graph.getViewport().setMaxY(maxY+1);

                        //
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("Challenge time");
                        graph.getGridLabelRenderer().setVerticalAxisTitle("Score");
                        graph.addSeries(series);
                    }catch (IllegalArgumentException e){

                    }
                }
            }
        });


    }

    private void getUserInfo(){
        // get user information to display
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        if(firebaseUser != null) {
            userID = firebaseUser.getUid();
        }
        DatabaseReference userRef = databaseReference.child(userID).child("scores");
    }
}