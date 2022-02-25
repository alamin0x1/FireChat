package com.developeralamin.firechat.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developeralamin.firechat.R;
import com.developeralamin.firechat.model.ProfileData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView profile_image;
    EditText profileName, profileNumber, profileEmail, profilePassword;
    AppCompatButton updateButton;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseAuth auth;

    Uri selectedImageUri;

    String CoverImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_image = findViewById(R.id.pro_image);
        profileName = findViewById(R.id.profileName);
        profileNumber = findViewById(R.id.profileNumber);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        updateButton = findViewById(R.id.updateButton);

        auth = FirebaseAuth.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("FireChartUser").child(auth.getUid());

        storageReference = FirebaseStorage.getInstance().getReference()
                .child("FireChart_Profile_Images").child(auth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ProfileData profileData = snapshot.getValue(ProfileData.class);

                profileName.setText(profileData.getName());
                profileNumber.setText(profileData.getNumber());
                profileEmail.setText(profileData.getEmail());
                profilePassword.setText(profileData.getPassword());
                Glide.with(getApplicationContext()).load(profileData.getProfilepictureurl()).into(profile_image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name_text = profileName.getText().toString();
                String phone_text = profileNumber.getText().toString();
                String email_text = profileEmail.getText().toString();
                String password_text = profilePassword.getText().toString();

                if (selectedImageUri != null) {
                    storageReference.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    CoverImage = String.valueOf(uri);

                                    HashMap hashMap = new HashMap();
                                    hashMap.put("name", name_text);
                                    hashMap.put("number", phone_text);
                                    hashMap.put("email", email_text);
                                    hashMap.put("password", password_text);
                                    hashMap.put("profilepictureurl", profile_image);

                                    databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                        }
                                    });
                                }
                            });
                        }
                    });
                }else {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            CoverImage = String.valueOf(uri);

                            HashMap hashMap = new HashMap();
                            hashMap.put("name", name_text);
                            hashMap.put("number", phone_text);
                            hashMap.put("email", email_text);
                            hashMap.put("password", password_text);
                            hashMap.put("profilepictureurl", profile_image);

                            databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                    
                                }
                            });
                        }
                    });
                }

            }
        });

//                HashMap hashMap = new HashMap();
//                hashMap.put("name", name_text);
//                hashMap.put("number", phone_text);
//                hashMap.put("email", email_text);
//                hashMap.put("password", password_text);
//                hashMap.put("profilepictureurl", profile_image);
//
//
//                databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ProfileActivity.this, "Failed :" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                    }
////                });
//            }
//        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 10);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                selectedImageUri = data.getData();
                profile_image.setImageURI(selectedImageUri);
            }
        }
    }
}