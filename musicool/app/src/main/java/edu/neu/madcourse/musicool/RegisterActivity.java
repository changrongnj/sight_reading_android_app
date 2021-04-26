package edu.neu.madcourse.musicool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView logo, already;
    private Button registeruser;
    private EditText editUsername, editEmail, editPassword, editAge;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    String slogan, city, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        mAuth = FirebaseAuth.getInstance();

        editUsername = (EditText) findViewById(R.id.username);
        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);
        editAge = (EditText) findViewById(R.id.age);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // set ClickListener
        logo = (TextView) findViewById(R.id.musicoollogo);
        logo.setOnClickListener(this);
        registeruser = (Button) findViewById(R.id.register);
        registeruser.setOnClickListener(this);
        already = (TextView) findViewById(R.id.already);
        already.setOnClickListener(this);


//        // initialize bottom navigation bar
//        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);
//        // perform itemselected listener;
//        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.ic_home:
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                    case R.id.ic_grading:
//                        startActivity(new Intent(getApplicationContext(), ScoreActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                    case R.id.ic_face:
//                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                    case R.id.ic_groups:
//                        startActivity(new Intent(getApplicationContext(), PeerActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                }
//                return false;
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.musicoollogo:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                userRegister();
                break;
            case R.id.already:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private void userRegister() {

        // default
        slogan = "";
        city = "";
        state = "";

        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String username = editUsername.getText().toString().trim();
        String age = editAge.getText().toString().trim();

        // email validation
        if (email.isEmpty()) {
            editEmail.setError("Email address is required!");
            editEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please enter valid email!");
            editEmail.requestFocus();
            return;
        }

        // username validation
        if (username.isEmpty()) {
            editUsername.setError("Username is required!");
            editUsername.requestFocus();
            return;
        }

        // password validation
        if (password.isEmpty()) {
            editPassword.setError("Password is required!");
            editPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editPassword.setError("Password length should be at least 6 characters!");
            editPassword.requestFocus();
            return;
        }

        // age is optional
        if (age.isEmpty()) {
            editAge.setText("");
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            User user = new User(username, age, email, slogan, city, state);
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                            reference
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        // redirect to login layout
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register,try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register,try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

    }
}

