package edu.neu.madcourse.musicool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Update_age_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;
    private FirebaseUser currentUser;
    private Button updateAge;
    private EditText newAgeEditText;
    private TextView currentAgeTextView;
    //private DatabaseReference reference;
    String  age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_age);

        updateAge = findViewById(R.id.confirm);
        newAgeEditText = findViewById(R.id.updateAge);
        currentAgeTextView = findViewById(R.id.age);

        // try display name using Helper class
        currentAgeTextView.setText(Helper.age);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // get user information from database and show original age (not working??)
        mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userprofile = snapshot.getValue(User.class);

                if (userprofile != null) {

                    age = userprofile.age;
                    if(age != null){
                        currentAgeTextView.setText(age);
                    } else {
                        currentAgeTextView.setText("");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Update_age_Activity.this, "lol!!! what is wrong here!", Toast.LENGTH_LONG).show();
            }
        });




//
//
//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        //todo: Peng Hao add comment if user != null
//        if(currentUser != null){
//            reference = FirebaseDatabase.getInstance().getReference("Users");
//            uid = currentUser.getUid();
//
//            reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    User userprofile = snapshot.getValue(User.class);
//
//                    username = userprofile.username;
//                    age = userprofile.age;
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(Update_age_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//
//        if(age != null){
//            currentAgeTextView.setText(age);
//        } else {
//            currentAgeTextView.setText("");
//        }

//        mDatabase.child("Users").child(uid).child("age").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                currentAgeTextView.setText(dataSnapshot.getValue(String.class));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

        // Update age
        updateAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAge.setEnabled(false);
                updateAge.setText("Updating...");
                final String newAge = newAgeEditText.getText().toString();
                Helper.age = newAge;
                if (newAge.trim().equals("")) {
                    updateAge.setText("Submit");
                    updateAge.setEnabled(true);
//                    Toast.makeText(Update_age_Activity.this, "New nick name must be at least 1 character with no Leading and trailing white spaces.",
//                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mDatabase.child("Users").child(uid).child("age").setValue(newAge);
                Snackbar.make(findViewById(android.R.id.content), "Awesome! Age changed successfully.", Snackbar.LENGTH_SHORT).show();
                updateAge.setText("Submit");
                newAgeEditText.setText("");
                updateAge.setEnabled(true);
            }
        });
    }

    public void back(View view){
        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
    }
}