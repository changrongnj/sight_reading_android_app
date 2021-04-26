package edu.neu.madcourse.musicool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
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

public class Update_slogan_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;
    private FirebaseUser currentUser;
    private Button confirm;
    private EditText newSloganEditText;
    private TextView currentSloganTextView;
    String slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_slogan);

        confirm = findViewById(R.id.confirm);
        newSloganEditText = findViewById(R.id.updateSlogan);
        currentSloganTextView = findViewById(R.id.slogan);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // try display name using Helper class
       currentSloganTextView.setText(Helper.slogan);

        // get user information from database and show original name (not working??)
        mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userprofile = snapshot.getValue(User.class);

                if (userprofile != null) {
                    slogan = userprofile.slogan;
                    if(slogan != null){
                        currentSloganTextView.setText(slogan);
                    } else {
                        currentSloganTextView.setText("");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Update_slogan_Activity.this, "lol!!! what is wrong here!", Toast.LENGTH_LONG).show();
            }
        });


        // Update slogan
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm.setEnabled(false);
                confirm.setText("Submiting...");
                final String newSlogan = newSloganEditText.getText().toString();
                Helper.slogan = newSlogan;

                mDatabase.child("Users").child(uid).child("slogan").setValue(newSlogan);

                Snackbar.make(findViewById(android.R.id.content), "Cool! New slogan set.", Snackbar.LENGTH_SHORT).show();
                confirm.setText("Submit");
                newSloganEditText.setText("");
                confirm.setEnabled(true);
            }
        });
    }


    public void back(View view){
        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
    }
}