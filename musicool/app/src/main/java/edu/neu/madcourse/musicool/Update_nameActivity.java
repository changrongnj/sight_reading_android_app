package edu.neu.madcourse.musicool;

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

public class Update_nameActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;
    private FirebaseUser currentUser;
    private Button updateName;
    private EditText newNameEditText;
    private TextView currentNameTextView;
    //private DatabaseReference reference;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);

        updateName = findViewById(R.id.confirm);
        newNameEditText = findViewById(R.id.UpdateName);
        currentNameTextView = findViewById(R.id.name);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // try display name using Helper class
        currentNameTextView.setText(Helper.currentUserName);

        // get user information from database and show original name (not working??)
        mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);

                if (userprofile != null) {
                    name = userprofile.username;
                    currentNameTextView.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Update_nameActivity.this, "lol!!! what is wrong here!", Toast.LENGTH_LONG).show();
            }
        });

        // Show name
//        mDatabase.child("Users").child(uid).child("Name").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                currentNameTextView.setText(dataSnapshot.getValue(String.class));
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });


        // Update name
        updateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName.setEnabled(false);
                updateName.setText("Updating...");
                final String newName = newNameEditText.getText().toString();
                Helper.currentUserName = newName;
                if (newName.trim().equals("")) {
                    updateName.setText("Submit");
                    updateName.setEnabled(true);
                    Toast.makeText(Update_nameActivity.this, "New nick name must be at least 1 character with no Leading and trailing white spaces.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // here I was wrong, should be child("Username") (saved in database)
                mDatabase.child("Users").child(uid).child("username").setValue(newName);
                Snackbar.make(findViewById(android.R.id.content), "Cool! Name changed.", Snackbar.LENGTH_SHORT).show();
                updateName.setText("Submit");
                newNameEditText.setText("");
                updateName.setEnabled(true);
            }
        });
    }

    public void back(View view){
        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
    }
}