package com.example.progettoIUMcorretto;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ParkAdapter parkAdapter;
    RecyclerView.LayoutManager layoutManager;
    BottomNavigationView navigationView;

    List<Parcheggi> searchList = new ArrayList<>();
    SearchView searchView;

    List<Parcheggi> parcheggiList = new ArrayList<>();

    public static final String PARK = "com.example.progettoIUMcorretto.Parcheggi";

    ActionBar actionBar;
    SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchView = findViewById(R.id.searchView);
        searchView.setFocusable(false);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

/****************************** Lista Parcheggi **********************************/
        parcheggiList.add(new Parcheggi("Parcheggio - Via Manzoni", R.drawable.parcheggio_via_manzoni, "4.5", "4.0", R.drawable.dot_green));
        parcheggiList.add(new Parcheggi("Parcheggio - Via Cagliari", R.drawable.parcheggio_via_cagliari, "3.4", "3.0", R.drawable.dot_orange));
        parcheggiList.add(new Parcheggi("Parcheggio - Via Manzoni", R.drawable.parcheggio_via_manzoni, "2.4", "3.5"));
        parcheggiList.add(new Parcheggi("Parcheggio - Via Roma", R.drawable.parcheggio_via_roma, "4.0", "4.0"));
        parcheggiList.add(new Parcheggi("Parcheggio - Via XX Settembre", R.drawable.parcheggio_via_xx_settembre, "1.5", "2.0"));

        for (Parcheggi parcheggio : parcheggiList) {
            parcheggio.setColorSecurity();
        }
/*********************************************************************************/

        parkAdapter = new ParkAdapter(MainActivity.this, parcheggiList);
        recyclerView.setAdapter(parkAdapter);

/*************++++++++++++++++++++++++ Navbar **********************************/
        // Find the BottomNavigationView and set up the listener
        navigationView = findViewById(R.id.navBar);
        navigationView.setSelectedItemId(R.id.nav_search);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent ;
                switch (item.getItemId()) {
                    case R.id.nav_map:
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_search:

                        intent = new Intent(MainActivity.this, SearchActivity.class);
                        intent.putExtra(PARK, (Serializable) parcheggiList);
                        startActivity(intent);

//                        // calling this activity's function to
//                        // use ActionBar utility methods
//                        actionBar = getSupportActionBar();
//
//                        // providing title for the ActionBar
//                        actionBar.setTitle("Parkify");
//
//                        // providing subtitle for the ActionBar
//                        actionBar.setSubtitle("Sezione ricerca parcheggio");
//
//                        // adding icon in the ActionBar
//                        actionBar.setIcon(R.drawable.logo);
//
//
//                        // methods to display the icon in the ActionBar
//                        actionBar.setDisplayUseLogoEnabled(true);
//                        actionBar.setDisplayShowHomeEnabled(true);
                        break;
                    case R.id.nav_home:
                        intent = new Intent(MainActivity.this, UserHomeActivity.class);
                        startActivity(intent);
                    default:
                        return false;
                }
                return true; //true to display the item as the selected item
            }
        });
/***************************************************************************/

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "Parcheggi in ordine per numero di stelle", Toast.LENGTH_LONG).show();
                if (query.length() > 0) {
                    for (Parcheggi park : parcheggiList) {
                        if (park.getParkingName().toLowerCase().contains(query.toLowerCase())) {
                            //Parcheggi parcheggio = new Parcheggi(park.getParkingName(),park.getImageResource(),park.getSecurity(),park.getgRatings());
                            searchList.add(park);

                        }
                    }
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(layoutManager);

                    ParkAdapter parkAdapter = new ParkAdapter(MainActivity.this, searchList);
                    recyclerView.setAdapter(parkAdapter);
                } else {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(layoutManager);

                    ParkAdapter parkAdapter = new ParkAdapter(MainActivity.this, searchList);
                    recyclerView.setAdapter(parkAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 0) {
                    searchList.clear();
                    for (Parcheggi park : parcheggiList) {
                        if (park.getParkingName().toLowerCase().contains(query.toLowerCase())) {
                            searchList.add(park);
                        }
                    }
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(layoutManager);

                    ParkAdapter parkAdapter = new ParkAdapter(MainActivity.this, searchList);
                    recyclerView.setAdapter(parkAdapter);
                } else {
                    searchList.clear();
                    searchList.addAll(parcheggiList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(layoutManager);

                    ParkAdapter parkAdapter = new ParkAdapter(MainActivity.this, searchList);
                    recyclerView.setAdapter(parkAdapter);
                }
                return false;
            }
        });
    }

    /**
     * @param new_fragment sostituisce il fragment attuale con "new_fragment"
     * @replaceFragment
     */
    private void replaceFragment(Fragment new_fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout2, new_fragment).commit();
    }

    @Nullable
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }


}


