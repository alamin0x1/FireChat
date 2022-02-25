package com.developeralamin.firechat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.developeralamin.firechat.R;
import com.developeralamin.firechat.adapter.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.installations.Utils;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;

    FirebaseAuth auth;

    FragmentManager fragmentManager;
    TabLayout tabLayout;
    FragmentAdapter adapter;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        fragmentManager = getSupportFragmentManager();
        adapter = new FragmentAdapter(fragmentManager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_group_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_chat_24);


        auth = FirebaseAuth.getInstance();


        if (!isConnected()) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.wifi)
                    .setTitle("Internet Connection Please")
                    .setMessage("Please Check your Internet Connection?")
                    .setCancelable(false)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
                break;

            case R.id.logout:
                Logut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Logut() {
        auth.signOut();
        startActivity(new Intent(MainActivity.this, StartActivity.class));
        finish();
    }

}