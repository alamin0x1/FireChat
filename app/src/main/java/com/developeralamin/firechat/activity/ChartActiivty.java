package com.developeralamin.firechat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developeralamin.firechat.R;
import com.developeralamin.firechat.model.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChartActiivty extends AppCompatActivity {

    Intent intent;
    String userId,userName,userProfile;

    Toolbar toolbar;
    CircleImageView profile_image;
    TextView reciverName;
    RecyclerView recyclerViewChat;
    EditText editTextMessage;
    CardView sendBtn;

    String senderRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_actiivty);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        userProfile = intent.getStringExtra("userProfile");


        profile_image = findViewById(R.id.profile_image);
        reciverName = findViewById(R.id.reciverName);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        sendBtn = findViewById(R.id.sendBtn);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("FireChartUser").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserData userData = snapshot.getValue(UserData.class);

                Glide.with(getApplicationContext()).load(userData.getProfilepictureurl()).into(profile_image);
                reciverName.setText(userData.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}