package com.example.progettoIUMcorretto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    ParkAdapter  parkAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Parcheggi> parcheggiList = new ArrayList<>();
    List<Parcheggi> searchList = new ArrayList<>();

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_search, container,false);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        parkAdapter = new ParkAdapter(SearchActivity.this, parcheggiList);
        recyclerView.setAdapter(parkAdapter);
        layoutManager = new LinearLayoutManager(SearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        return v;
    }
    
    //Questo può essere utile per un possibile menù per filtrare i risultati non c'è
    //i Collection.sort(....) funzionano ma nell'app non è stato implementato un menù
    //che effettivamente permette di utilizzarli
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Context context;
        context = SearchActivity.this;
        switch(item.getItemId()) {
            case R.id.filter_A_Z:
                //sort a to z
                Collections.sort(parcheggiList, Parcheggi.Parks_a_To_Z);
                Toast.makeText(context.getApplicationContext(), "Parcheggi in ordine alfabetico", Toast.LENGTH_SHORT).show();
                parkAdapter.notifyDataSetChanged();
                return true;
            case R.id.filter_Z_A:
                //sort z to a
                Collections.sort(parcheggiList, Parcheggi.Parks_z_To_A);
                Toast.makeText(context.getApplicationContext(), "Parcheggi in ordine alfabetico inverso", Toast.LENGTH_SHORT).show(); //provare con LENGTH_LONG
                parkAdapter.notifyDataSetChanged();
                return true;
            case R.id.filter_most_secure:
                //sort from most secure to least secure
                Collections.sort(parcheggiList, Parcheggi.Parks_mostSec);
                Toast.makeText(context.getApplicationContext(), "Parcheggi in ordine di sicurezza", Toast.LENGTH_SHORT).show();
                parkAdapter.notifyDataSetChanged();
                return true;
            case R.id.filter_most_voted:
                //sort from most voted to least voted
                Collections.sort(parcheggiList, Parcheggi.Parks_mostGrate);
                parkAdapter.notifyDataSetChanged();
                Toast.makeText(context.getApplicationContext(), "Parcheggi in ordine per numero di stelle", Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);
        Intent intent = getIntent();

        // Find the BottomNavigationView and set up the listener
        BottomNavigationView navigationView;

        Object serializable = getIntent().getSerializableExtra(MainActivity.PARK);
        if (serializable instanceof List) {
            parcheggiList = (List<Parcheggi>) serializable;
            if(parcheggiList.isEmpty()) {
                Toast.makeText(this, "Lista Parcheggi vuota", Toast.LENGTH_LONG).show();
            }
        }else {
            parcheggiList.add(new Parcheggi("Parcheggio - Via Manzoni", R.drawable.parcheggio_via_manzoni, "4.5", "4.0", R.drawable.dot_green));
            parcheggiList.add(new Parcheggi("Parcheggio - Via Cagliari", R.drawable.parcheggio_via_cagliari, "3.4", "3.0", R.drawable.dot_orange));
            parcheggiList.add(new Parcheggi("Parcheggio - Via Manzoni", R.drawable.parcheggio_via_manzoni, "2.4", "3.5"));
            parcheggiList.add(new Parcheggi("Parcheggio - Via Roma", R.drawable.parcheggio_via_roma, "4.0", "4.0"));
            parcheggiList.add(new Parcheggi("Parcheggio - Via XX Settembre", R.drawable.parcheggio_via_xx_settembre, "1.5", "2.0"));
            
            //Questo ciclo dovrebbe essere inutile perché in teoria i colori vanno calcolati facendo la media
            //di tutte le recensioni, volendo si può lasciare così perché effettivamente non vanno a controllare
            //questo aspetto nei dettagli
            for (Parcheggi parcheggio : parcheggiList) {
                parcheggio.setColorSecurity();
            }
        }

        // Find the BottomNavigationView and set up the listener
        navigationView = findViewById(R.id.navBar);
        navigationView.setSelectedItemId(R.id.nav_search);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    for (Parcheggi park : parcheggiList) {
                        if (park.getParkingName().toLowerCase().contains(query.toLowerCase())) {
                            //Parcheggi parcheggio = new Parcheggi(park.getParkingName(),park.getImageResource(),park.getSecurity(),park.getgRatings());
                            searchList.add(park);
                        }
                    }
                    //ridimesiona la box di RecyclerView per farci stare tutti i parcheggi trovati
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    //aggiorna la lista dei parcheggi che verranno mostrati
                    ParkAdapter parkAdapter = new ParkAdapter(SearchActivity.this, searchList);
                    recyclerView.setAdapter(parkAdapter);
                } else {
                    //ridimesiona la box di RecyclerView per farci stare tutti i parcheggi trovati
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    //aggiorna la lista dei parcheggi che verranno mostrati
                    ParkAdapter parkAdapter = new ParkAdapter(SearchActivity.this, searchList);
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
                    //ridimesiona la box di RecyclerView per farci stare tutti i parcheggi trovati
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    //aggiorna la lista dei parcheggi che verranno mostrati
                    ParkAdapter parkAdapter = new ParkAdapter(SearchActivity.this, searchList);
                    recyclerView.setAdapter(parkAdapter);
                } else {
                    searchList.clear();
                    searchList.addAll(parcheggiList);
                    //ridimesiona la box di RecyclerView per farci stare tutti i parcheggi trovati
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    //aggiorna la lista dei parcheggi che verranno mostrati
                    ParkAdapter parkAdapter = new ParkAdapter(SearchActivity.this, searchList);
                    recyclerView.setAdapter(parkAdapter);
                }
                return false;
            }
        });

        /************************************* Navbar **********************************/
        // Find the BottomNavigationView and set up the listener
        navigationView.setSelectedItemId(R.id.nav_search);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent ;
                switch (item.getItemId()) {
                    case R.id.nav_map:
                        intent = new Intent(SearchActivity.this, MappaActivity.class);
                        intent.putExtra(MainActivity.PARK, (Serializable) parcheggiList);
                        startActivity(intent);

                        break;
                    case R.id.nav_search:                        
                        break;
                    case R.id.nav_home:
                        intent = new Intent(SearchActivity.this, UserHomeActivity.class);
                        intent.putExtra(MainActivity.PARK, (Serializable) parcheggiList);
                        startActivity(intent);
                        break;
                    default:
                        return false;
                }
                return true; //true to display the item as the selected item
            }
        });
      /***************************************************************************/
    }
}
