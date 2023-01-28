package com.example.parkify;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ValutazioneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Imposta di default la modalità giorno
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valutazione);

        //Chiamata della actionbar
        ActionBar actionBar = getSupportActionBar();

        //Nasconde la actionbar (la riga in alto con il nome dell'applicazione)
        //actionBar.hide();

        //Modifica l'aspetto del pulsante, i valori sono sul file drawable
        //actionBar.setHomeAsUpIndicator(R.drawable.bottone_return);

        //Mostra un pulsante di ritorno nella actionbar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Inserimento dei dati nello spinner e specifica del layout
        Spinner spinnerMattina = (Spinner) findViewById(R.id.s_dispMattina);
        Spinner spinnerSera = (Spinner) findViewById(R.id.s_dispSera);
        Spinner spinnerNotte = (Spinner) findViewById(R.id.s_dispNotte);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_disponibilita, R.layout.custom_spinner_layout);

        //Specifica il layout da usare per la lista dello spinner
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);

        //Applica le modifiche allo spinner
        spinnerMattina.setAdapter(adapter);
        spinnerSera.setAdapter(adapter);
        spinnerNotte.setAdapter(adapter);
    }
}