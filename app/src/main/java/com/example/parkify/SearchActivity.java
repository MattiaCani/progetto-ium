package com.example.parkify;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Rating;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.search.SearchBar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    SearchView searchView;
    RecyclerView searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Cambia il colore della statusbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_primary));
        }

        //Chiamata della actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Find the BottomNavigationView and set up the listener
        navigationView = findViewById(R.id.navBar);
        navigationView.setSelectedItemId(R.id.nav_search);

        searchView = findViewById(R.id.searchView);
        searchResult = findViewById(R.id.searchResults);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchResult.removeAllViews();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("parcheggio")
                        .whereEqualTo("nomeParcheggio", "Piazza Unione Sarda")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.parcheggio_ricerca, searchResult, false);
                                    searchResult.addView(view);

                                    TextView nome = view.findViewById(R.id.text_view_name);
                                    RatingBar ratingBar = view.findViewById(R.id.raating);

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Parcheggio p = document.toObject(Parcheggio.class);

                                        nome.setText(p.getNomeParcheggio());
                                        ratingBar.setRating(p.getRatingSicurezza());
                                    }

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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