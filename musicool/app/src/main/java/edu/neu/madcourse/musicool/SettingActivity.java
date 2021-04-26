package edu.neu.madcourse.musicool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.grpc.Context;

public class SettingActivity extends AppCompatActivity {

    ImageView changeProfilePhoto;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference(); // storage for image

        changeProfilePhoto = findViewById(R.id.changeProfilePhoto);
        changeProfilePhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // open gallery locally
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000); // overwrite startactivityForResult below
            }
        });
        
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK){
                // image data is passing into
                Uri imageUri = data.getData();
                changeProfilePhoto.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        FirebaseUser currentUser = fAuth.getCurrentUser();
        String uid = currentUser.getUid();
        StorageReference fileRef = storageReference.child(uid + ".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("setting", "File upload");
                Toast.makeText(SettingActivity.this, "Image successfully uploaded.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("setting", "File error");
                Toast.makeText(SettingActivity.this, "Failed due to error.getMessage().", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void backToProfile(View view){
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }

    public void updateName(View view){
        startActivity(new Intent(getApplicationContext(), Update_nameActivity.class));
    }

    public void updateAge(View view){
        startActivity(new Intent(getApplicationContext(), Update_age_Activity.class));
    }

    public void updateSlogan(View view){
        startActivity(new Intent(getApplicationContext(), Update_slogan_Activity.class));
    }
}