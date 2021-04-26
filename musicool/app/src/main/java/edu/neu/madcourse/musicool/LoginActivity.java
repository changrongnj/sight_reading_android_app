package edu.neu.madcourse.musicool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView logo, register, forgotPassword;
    private EditText editEmail, editPassword;
    private Button login;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    TextView showLocation;
    Double latitude, longitude;
    LocationManager locManager;
    TextView name;
    ImageView profilePhoto;
    TextView age;
    TextView score;
    TextView userSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Xinglu: move from main activity
        // get user information to display


        user = FirebaseAuth.getInstance().getCurrentUser();
        //todo: Peng Hao add comment if user != null
        if(user != null){
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

//            final Button bt_login = (Button) findViewById(R.id.bt_login);
//            final Button bt_logout = (Button) findViewById(R.id.bt_logout);
//            final TextView greeting = (TextView) findViewById(R.id.greeting);

            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User userprofile = snapshot.getValue(User.class);

                    if (userprofile != null) {
//                        bt_logout.setVisibility((View.VISIBLE));
                        Helper.loggedIn = true;
                      //  if(userprofile.username!= null)
                        Helper.currentUserName = userprofile.username;
//                            greeting.setText("Welcome, " + username + "!");
                        // if(userprofile.age!= null)
                        Helper.age = userprofile.age;

                    } else {
//                        bt_login.setVisibility((View.VISIBLE));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        mAuth = FirebaseAuth.getInstance();

        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // set ClickListener
        logo = (TextView) findViewById(R.id.musicoollogo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        forgotPassword = (TextView) findViewById((R.id.forgotPassword));
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your email to receive reset link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Reset link fails to sent due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });

    }

    private void openMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void openRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void userLogin() {

        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // email validation
        if (email.isEmpty()) {
            editEmail.setError("Email address is required.");
            editEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please enter valid email.");
            editEmail.requestFocus();
            return;
        }

        // password validation
        if (password.isEmpty()) {
            editPassword.setError("Password is required.");
            editPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editPassword.setError("Password length should be at least 6 characters.");
            editPassword.requestFocus();
            return;
        }


        progressBar.setVisibility((View.VISIBLE));

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            // firsttime verify account in email
                            if (user.isEmailVerified()) {
                                // redirect to profile activity(xinglu)
                                Helper.loggedIn = true;
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                            } else {
                                user.sendEmailVerification();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Please check your email to verify your account.", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Fail to login. Please check the email and password.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
