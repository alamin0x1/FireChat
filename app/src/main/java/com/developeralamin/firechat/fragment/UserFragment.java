package com.developeralamin.firechat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.developeralamin.firechat.R;
import com.developeralamin.firechat.adapter.UserAdapter;
import com.developeralamin.firechat.model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;

    private List<UserData> list;
    private DatabaseReference reference;

    private String auth = FirebaseAuth.getInstance().getUid();
    FirebaseUser firebaseUser;
    String currentUser;


    UserAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView = view.findViewById(R.id.reclerViewId);
        progressBar = view.findViewById(R.id.progressBar);

        list = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser!=null){
            currentUser = firebaseUser.getUid();
        }


        reference = FirebaseDatabase.getInstance().getReference().child("FireChartUser");

        getData();

        return view;
    }

    private void getData() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserData userData = dataSnapshot.getValue(UserData.class);

                    assert userData != null;
                    if(!userData.getId().equals(currentUser)){
                        list.add(userData);
                    }


                }

                adapter = new UserAdapter(list, getContext());
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}