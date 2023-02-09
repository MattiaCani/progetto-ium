package com.example.parkify;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.mapsforge.map.android.layers.MyLocationOverlay;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomepageActivity extends AppCompatActivity {

    //Permessi per l'utilizzo della mappa
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    //Per il passaggio a un altra activity
    public static final String HOMEPAGE_EXTRA = "com.example.parkify.HomepageActivity";

    //Mappa Osmdroid
    private static CustomMapView map = null;

    //Widget
    TextView nomeParcheggio;

    TextView spinner_sMattina, spinner_sSera, spinner_sNotte;
    TextView spinner_fsMattina, spinner_fsSera, spinner_fsNotte;

    RatingBar ratingSicurezzaResult;
    Button buttonAggiungiValutazione;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //Collegamento al database di Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Imposta di default la modalità giorno
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //Cambia il colore della statusbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }

        //Cambia il colore del testo della statusbar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Chiamata della actionbar
        ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        //Cambia il colore della actionbar
        if (actionBar != null) {
            actionBar.setTitle(HtmlCompat.fromHtml("<font color='#000000'>Parkify</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        }

        //Inizializza Osmdroid
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //Crea la mappa
        map = (CustomMapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //Calcolo posizione tramite GPS, altrimenti zomma direttamente su Cagliari
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location luogoGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(luogoGps != null)
            map.getController().animateTo(new GeoPoint(luogoGps.getLatitude(), luogoGps.getLongitude()));
        else
            map.getController().animateTo(new GeoPoint(39.23054d, 9.11917d));

        map.getController().setZoom(10.0);

        //Recupero dei dati
        nomeParcheggio = findViewById(R.id.nomeParcheggio);

        spinner_sMattina = findViewById(R.id.s_dispMattinaResult);
        spinner_fsMattina = findViewById(R.id.fs_dispMattinaResult);

        spinner_sSera = findViewById(R.id.s_dispSeraResult);
        spinner_fsSera = findViewById(R.id.fs_dispSeraResult);

        spinner_sNotte = findViewById(R.id.s_dispNotteResult);
        spinner_fsNotte = findViewById(R.id.fs_dispNotteResult);

        ratingSicurezzaResult = findViewById(R.id.ratingSicurezzaResult);
        buttonAggiungiValutazione = findViewById(R.id.buttonAggiungiValutazione);

        //Recupero dei parcheggi dal database
        db.collection("parcheggio")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                Map<String, Object> data = document.getData();
                                Gson gson = new Gson();
                                Parcheggio p = gson.fromJson(gson.toJson(data), Parcheggio.class);

                                //Aggiunta dei parcheggi nella mappa attraverso dei Marker
                                addMarker(p, new GeoPoint(p.getLatitudine(), p.getLongitudine()));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //Gestione dell'aggiunta di un Marker e del comportamento se esso viene cliccato
    public void addMarker(Parcheggio parcheggio, GeoPoint luogo){
        Marker newMarker = new Marker(map);
        newMarker.setPosition(luogo);
        newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(newMarker);

        if(parcheggio.isFree())
            newMarker.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.map_marker_free));
        else
            newMarker.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.map_marker_pay));

        newMarker.setTitle(parcheggio.getNomeParcheggio());

        //Al click su un Marker mostra le informazioni del parcheggio relativo a quel marker
        newMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                nomeParcheggio.setText(parcheggio.getNomeParcheggio());

                spinner_sMattina.setText(parcheggio.getsMattina());
                spinner_fsMattina.setText(parcheggio.getFsMattina());

                spinner_sSera.setText(parcheggio.getsSera());
                spinner_fsSera.setText(parcheggio.getFsSera());

                spinner_sNotte.setText(parcheggio.getsNotte());
                spinner_fsNotte.setText(parcheggio.getFsNotte());

                ratingSicurezzaResult.setRating(parcheggio.getRatingSicurezza());
                return false;
            }
        });

        //Al click sul bottone di valutazione reindirizza alla activity per effettuare una valutazione
        buttonAggiungiValutazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paginaValutazione = new Intent(HomepageActivity.this, ValutazioneActivity.class);
                paginaValutazione.putExtra(HOMEPAGE_EXTRA, parcheggio);
                startActivity(paginaValutazione);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}