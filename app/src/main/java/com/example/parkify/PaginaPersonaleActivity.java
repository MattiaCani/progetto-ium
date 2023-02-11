package com.example.parkify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaginaPersonaleActivity extends AppCompatActivity {

    Button logoutButton;
    BottomNavigationView navigationView;
    ImageView userPic;
    TextView usernameField, emailField, carField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_personale);

        SharedPreferences sharedPreferences = getSharedPreferences("Parkify", MODE_PRIVATE);

        getSupportActionBar().hide();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userPic = findViewById(R.id.userPic);
        usernameField = findViewById(R.id.usernameField);
        emailField = findViewById(R.id.emailField);
        carField = findViewById(R.id.carField);

        logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(PaginaPersonaleActivity.this, LoginActivity.class);
                loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                loginActivity.putExtra("EXIT", true);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("IS_LOGGED_IN", false);
                editor.putString("USER", "");
                editor.apply();

                startActivity(loginActivity);
                finish();
            }
        });

        String loggedUser = sharedPreferences.getString("USER", "");

        DocumentReference docRef = db.collection("utente").document(loggedUser);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Person utente = documentSnapshot.toObject(Person.class);

                if(utente.getPicId() == 1){
                    userPic.setImageResource(R.drawable.avatar1);
                } else if(utente.getPicId() == 2){
                    userPic.setImageResource(R.drawable.avatar2);
                }

                usernameField.setText(utente.getUsername());
                emailField.setText(utente.getEmail());
                carField.setText(utente.getVehicle());
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