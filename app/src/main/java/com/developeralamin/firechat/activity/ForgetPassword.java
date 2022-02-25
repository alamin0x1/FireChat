package com.developeralamin.firechat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developeralamin.firechat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class ForgetPassword extends AppCompatActivity {

    EditText forgetEmail;
    Button forgetBtn;
    TextView signInTextView;

    ProgressDialog progressDialog;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forgetEmail = findViewById(R.id.forgetEmail);
        signInTextView = findViewById(R.id.signInTextView);

        forgetBtn = findViewById(R.id.forgetBtn);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);


        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgetPassword.this, SignInActivity.class));
                finish();
            }
        });

        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userforgetEmail();
            }
        });
    }

    private void userforgetEmail() {

        String email = forgetEmail.getText().toString();

        if (email.isEmpty()) {
            forgetEmail.setError("Please Enter your Email");
            forgetEmail.requestFocus();
            return;
        } else {
            progressDialog.setMessage("Please Wait");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toasty.warning(ForgetPassword.this, "Check your Email!", Toasty.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgetPassword.this, SignInActivity.class));
                                finish();
                            } else {
                                Toasty.error(ForgetPassword.this, "Error : " + task.getException().getMessage(), Toasty.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }

    }
}