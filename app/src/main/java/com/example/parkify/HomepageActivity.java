package com.example.parkify;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.transition.Scene;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionManager;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.mapsforge.map.android.layers.MyLocationOverlay;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomepageActivity extends AppCompatActivity {

    //Permessi per l'utilizzo della mappa
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    //Per il passaggio a un altra activity
    public static final String HOMEPAGE_EXTRA0 = "com.example.parkify.HomepageActivity.idParcheggio";
    public static final String HOMEPAGE_EXTRA1 = "com.example.parkify.HomepageActivity.nomeParcheggio";

    //Mappa Osmdroid
    private static CustomMapView map = null;

    //Widget
    LinearLayout infobox;
    LinearLayout tutorialBox;
    LinearLayout commentBox;

    TextView benvenuto;
    TextView nomeParcheggio;

    TextView spinner_sMattina, spinner_sSera, spinner_sNotte;
    TextView spinner_fsMattina, spinner_fsSera, spinner_fsNotte;

    private static Integer idParcheggio = null;

    RatingBar ratingSicurezzaResult;
    Button buttonAggiungiValutazione;
    Button commentiButton;

    BottomNavigationView navigationView;

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
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_primary));
        }

        SharedPreferences sharedPreferences = getSharedPreferences("Parkify", MODE_PRIVATE);

        //Cambia il colore del testo della statusbar
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Chiamata della actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

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
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        map.setMultiTouchControls(true);

        //Calcolo posizione tramite GPS, altrimenti zomma direttamente su Cagliari
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location luogoGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(luogoGps != null)
            map.getController().animateTo(new GeoPoint(luogoGps.getLatitude(), luogoGps.getLongitude()));
        else
            map.getController().animateTo(new GeoPoint(39.23054d, 9.11917d));

        map.getController().setZoom(13.0);

        //Recupero dei dati
        infobox = findViewById(R.id.infoBox);
        tutorialBox = findViewById(R.id.tutorialBox);
        commentBox = findViewById(R.id.commentBox);

        benvenuto = findViewById(R.id.benvenuto);
        nomeParcheggio = findViewById(R.id.nomeParcheggio);

        spinner_sMattina = findViewById(R.id.s_dispMattinaResult);
        spinner_fsMattina = findViewById(R.id.fs_dispMattinaResult);

        spinner_sSera = findViewById(R.id.s_dispSeraResult);
        spinner_fsSera = findViewById(R.id.fs_dispSeraResult);

        spinner_sNotte = findViewById(R.id.s_dispNotteResult);
        spinner_fsNotte = findViewById(R.id.fs_dispNotteResult);

        ratingSicurezzaResult = findViewById(R.id.ratingSicurezzaResult);
        buttonAggiungiValutazione = findViewById(R.id.buttonAggiungiValutazione);

        commentiButton = findViewById(R.id.caricaCommenti);

        String loggedUser = sharedPreferences.getString("USER", "");
        benvenuto.setText("Benvenuto " + loggedUser + "!");

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
        newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(newMarker);

        if(parcheggio.isFree())
            newMarker.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.location_free));
        else
            newMarker.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.location_pay));

        //Al click su un Marker mostra le informazioni del parcheggio relativo a quel marker
        newMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                commentiButton.setClickable(true);
                commentiButton.setBackgroundColor(getResources().getColor(R.color.blue_transparent));
                commentiButton.setTextColor(getResources().getColor(R.color.white));
                commentBox.setVisibility(View.INVISIBLE);
                commentBox.removeAllViews();

                tutorialBox.setVisibility(View.GONE);
                map.getController().animateTo(luogo, 15.50, 500L);

                Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                infobox.startAnimation(slideDown);

                slideDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        //TransitionManager.beginDelayedTransition(infobox);
                        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                        infobox.setVisibility(View.VISIBLE);
                        infobox.startAnimation(slideUp);

                        if(parcheggio.isFree()) {
                            infobox.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.infobox_free));
                            nomeParcheggio.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corners_top_free));
                        }
                        else {
                            infobox.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.infobox_pay));
                            nomeParcheggio.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corners_top_pay));
                        }

                        nomeParcheggio.setText(parcheggio.getNomeParcheggio());
                        idParcheggio = parcheggio.getIdParcheggio();

                        chooseSpinnerText(spinner_sMattina, parcheggio.getsMattina().getMedia());
                        chooseSpinnerText(spinner_fsMattina, parcheggio.getFsMattina().getMedia());

                        chooseSpinnerText(spinner_sSera, parcheggio.getsSera().getMedia());
                        chooseSpinnerText(spinner_fsSera, parcheggio.getFsSera().getMedia());

                        chooseSpinnerText(spinner_sNotte, parcheggio.getsNotte().getMedia());
                        chooseSpinnerText(spinner_fsNotte, parcheggio.getFsNotte().getMedia());

                        ratingSicurezzaResult.setRating(parcheggio.getRatingSicurezza());
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                return false;
            }
        });

        //Al click sul bottone di valutazione reindirizza alla activity per effettuare una valutazione
        buttonAggiungiValutazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paginaValutazione = new Intent(HomepageActivity.this, ValutazioneActivity.class);
                paginaValutazione.putExtra(HOMEPAGE_EXTRA0, idParcheggio);
                paginaValutazione.putExtra(HOMEPAGE_EXTRA1, nomeParcheggio.getText().toString());
                startActivity(paginaValutazione);
            }
        });

        //Al click del bottone dei commenti apre i commenti
        commentiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentiButton.setClickable(false);
                commentiButton.setBackgroundColor(getResources().getColor(R.color.grey_transparent));
                commentiButton.setTextColor(getResources().getColor(R.color.black));
                commentBox.setVisibility(View.VISIBLE);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("valutazione")
                        .whereEqualTo("idParcheggio", idParcheggio)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Valutazione p = document.toObject(Valutazione.class);

                                        if(p.getCommento().length() > 0) {
                                            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.commento, commentBox, false);
                                            commentBox.addView(view);

                                            ImageView immagine = view.findViewById(R.id.image_view);
                                            TextView utente = view.findViewById(R.id.text_view_name);
                                            TextView commento = view.findViewById(R.id.text_view_text);

                                            if(p.getUtente().getPicId() == 1)
                                                immagine.setImageResource(R.drawable.avatar1);
                                            else
                                                immagine.setImageResource(R.drawable.avatar2);

                                            utente.setText(p.getUtente().getUsername());
                                            commento.setText(p.getCommento());
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });


            }
        });

        // Find the BottomNavigationView and set up the listener
        navigationView = findViewById(R.id.navBar);
        navigationView.setSelectedItemId(R.id.nav_map);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_map:
                        break;
                    case R.id.nav_search:
                        intent = new Intent(HomepageActivity.this, SearchActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        finish();
                        break;
                    case R.id.nav_home:
                        intent = new Intent(HomepageActivity.this, PaginaPersonaleActivity.class);
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

    //Sceglie il testo dello spinner in base alla media
    void chooseSpinnerText(TextView spinner, Float media){
        String text;

        if(media <= 1)
            text = "Zero";
        else if (media <=2)
            text = "Poca";
        else if (media <=3)
            text = "Variabile";
        else if (media <= 4)
            text = "Molta";
        else
            text = "Piena";

        spinner.setText(text);
    }
}