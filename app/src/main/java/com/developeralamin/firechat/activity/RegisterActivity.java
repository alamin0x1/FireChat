package com.developeralamin.firechat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developeralamin.firechat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private CircleImageView profile_image;

    EditText inputName, inputNumber, inputEmail, inputPassword;
    TextView backButton;
    AppCompatButton registerButton;
    private ProgressDialog progressDialog;
    private Uri resultUri;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        profile_image = findViewById(R.id.profile_image);
        inputName = findViewById(R.id.inputName);
        inputNumber = findViewById(R.id.inputNumber);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        backButton = findViewById(R.id.backButton);
        registerButton = findViewById(R.id.registerButton);

        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegistion();
            }
        });

    }

    private void userRegistion() {
        String name = inputName.getText().toString();
        String number = inputNumber.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (name.isEmpty()) {
            inputName.setError("Name is Empty");
            inputName.requestFocus();
            return;
        }
        if (number.isEmpty()) {
            inputNumber.setError("Number is Empty");
            inputNumber.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            inputEmail.setError("Email is Empty");
            inputEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            inputPassword.setError("Password is Empty");
            inputPassword.requestFocus();
            return;
        } else {
            progressDialog.setMessage("Please Wait......");
            progressDialog.show();


            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        String error = task.getException().toString();
                        Toast.makeText(RegisterActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                    } else {
                        String currentUserId = auth.getCurrentUser().getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference()
                                .child("FireChartUser").child(currentUserId);

                        HashMap userInfo = new HashMap();
                        userInfo.put("id", currentUserId);
                        userInfo.put("name", name);
                        userInfo.put("number", number);
                        userInfo.put("email", email);
                        userInfo.put("password", password);
                        userInfo.put("profilepictureurl", "no_pic_uploaded");

                        databaseReference.updateChildren(userInfo)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Registion Successful", Toast.LENGTH_SHORT).show();


                                            TaskStackBuilder.create(RegisterActivity.this)
                                                    .addNextIntentWithParentStack(new Intent(getApplicationContext(), MainActivity.class))
                                                    .addNextIntent(new Intent(RegisterActivity.this, IntroActivity.class))
                                                    .startActivities();


                                        } else {
                                            Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        finish();

                                    }
                                });


                        if (resultUri != null) {
                            final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                    .child("FireChart_Profile_Images").child(currentUserId);

                            Bitmap bitmap = null;

                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                            byte[] data = byteArrayOutputStream.toByteArray();
                            UploadTask uploadTask = filePath.putBytes(data);

                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageUrl = uri.toString();
                                                Map newImageMap = new HashMap();
                                                newImageMap.put("profilepictureurl", imageUrl);

                                                databaseReference.updateChildren(newImageMap)
                                                        .addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(RegisterActivity.this, "Image url added to database successfully", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                finish();
                                            }
                                        });
                                    }
                                }
                            });
//                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                            TaskStackBuilder.create(RegisterActivity.this)
                                    .addNextIntentWithParentStack(new Intent(getApplicationContext(), MainActivity.class))
                                    .addNextIntent(new Intent(RegisterActivity.this, IntroActivity.class))
                                    .startActivities();
                            progressDialog.dismiss();

                        }

                    }
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            resultUri = data.getData();
            profile_image.setImageURI(resultUri);

        }
    }
}