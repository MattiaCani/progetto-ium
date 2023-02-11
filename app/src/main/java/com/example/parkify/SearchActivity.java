package com.example.parkify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SearchActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Find the BottomNavigationView and set up the listener
        navigationView = findViewById(R.id.navBar);
        navigationView.setSelectedItemId(R.id.nav_search);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_map:
                        intent = new Intent(SearchActivity.this, HomepageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        finish();
                        break;
                    case R.id.nav_search:
                        break;
                    case R.id.nav_home:
                        intent = new Intent(SearchActivity.this, PaginaPersonaleActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        finish();
                        break;
                    default:
                        return false;
                }
                return true; //true to display the item as the selected item
            }
        });
    }
}