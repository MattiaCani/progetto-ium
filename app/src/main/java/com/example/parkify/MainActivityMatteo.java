package com.example.progettoIUMcorretto;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    BottomNavigationView navigationView;
    List<Parcheggi> searchList;
    SearchView searchView;
    RecyclerView recyclerView;
    ParkAdapter parkAdapter;
    List<Parcheggi> parcheggiList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        searchView = findViewById(R.id.searchView);

        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

       recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
       recyclerView.setHasFixedSize(true);
       RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);


/****************************** Lista Parcheggi **********************************/
        parcheggiList.add(new Parcheggi("Parcheggio - Via Manzoni", R.drawable.parcheggio_via_manzoni, "4.5", "4.0", R.drawable.dot_green));
        parcheggiList.add(new Parcheggi("Parcheggio - Via Cagliari",R.drawable.parcheggio_via_cagliari, "3.4","3.0", R.drawable.dot_orange));
        parcheggiList.add(new Parcheggi("Parcheggio - Via Manzoni", R.drawable.parcheggio_via_manzoni, "3.0", "3.5"));
        parcheggiList.add(new Parcheggi("Parcheggio - Via Roma", R.drawable.parcheggio_via_roma, "4.0", "4.0"));
        parcheggiList.add(new Parcheggi("Parcheggio - Via XX Settembre", R.drawable.parcheggio_via_xx_settembre, "1.5", "2.0"));

        for (Parcheggi parcheggio : parcheggiList) {
            parcheggio.setColorSecurity();
        }
/*********************************************************************************/


        recyclerView.setLayoutManager(layoutManager);

        ParkAdapter parkAdapter = new ParkAdapter(MainActivity.this, parcheggiList);
        recyclerView.setAdapter(parkAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                searchList = new ArrayList<>();
//
//                if(query.length()>0){
//                    for (Parcheggi parcheggio : parcheggiList) {
//                        if(parcheggio.getParkingName().toLowerCase().contains(query.toLowerCase())){
//                            Parcheggi parks = parcheggio;
//                            searchList.add(parks);
//
//                        }
//                    }
//
//                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                filterList(newText);
                return true;
            }
        });
/********************************* Navbar **********************************/
//        navigationView = findViewById(R.id.navBar);
//        Fragment fragment = new Fragment();
//        navigationView.setSelectedItemId(R.id.nav_search);
//        replaceFragment(new SearchFragment());
//
//
//        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.nav_map:
//                        replaceFragment(new UserHomeFragment());
//                        break;
//                    case R.id.nav_search:
//                        replaceFragment(new SearchFragment());
//                        break;
//                    case R.id.nav_home:
//                        replaceFragment(new EditPwdFragment());
//                        break;
//                    default: break;
//
//                }
//                return true; //true to display the item as the selected item
//            }
//
//
//        });

    }
//    private void replaceFragment(Fragment new_fragment){
//            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout2, new_fragment).commit();
//    }
/*************************************************************************/
}
