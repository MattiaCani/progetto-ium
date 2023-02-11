package com.example.parkify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PaginaPersonaleActivity extends AppCompatActivity {

    Button logoutButton;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_personale);

        getSupportActionBar().hide();

        logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(PaginaPersonaleActivity.this, LoginActivity.class);
                loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                loginActivity.putExtra("EXIT", true);
                startActivity(loginActivity);
                finish();
            }
        });

        // Find the BottomNavigationView and set up the listener
        navigationView = findViewById(R.id.navBar);
        navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_map:
                        intent = new Intent(PaginaPersonaleActivity.this, HomepageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        finish();
                        break;
                    case R.id.nav_search:
                        intent = new Intent(PaginaPersonaleActivity.this, SearchActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        finish();
                        break;
                    case R.id.nav_home:
                        break;
                    default:
                        return false;
                }
                return true; //true to display the item as the selected item
            }
        });
    }
}