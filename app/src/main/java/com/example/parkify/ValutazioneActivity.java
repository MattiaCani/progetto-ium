package com.example.parkify;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ValutazioneActivity extends AppCompatActivity {

    RelativeLayout sMattina, sSera, sNotte;
    RelativeLayout fsMattina, fsSera, fsNotte;

    Spinner spinner_sMattina, spinner_sSera, spinner_sNotte;
    Spinner spinner_fsMattina, spinner_fsSera, spinner_fsNotte;

    RatingBar ratingSicurezza;
    EditText commento;
    Button bottoneConferma;

    Valutazione valutazioneUtente;

    public static final String VALUTAZIONE_EXTRA = "com.example.parkify.Valutazione";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Imposta di default la modalità giorno
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valutazione);

        valutazioneUtente = new Valutazione();

        //Chiamata della actionbar
        ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        //Mostra un pulsante di ritorno nella actionbar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Cambia tra la disponibilità Settimana e Fine Settimana
        sMattina = findViewById(R.id.s_mattina);
        fsMattina = findViewById(R.id.fs_mattina);

        sSera = findViewById(R.id.s_sera);
        fsSera = findViewById(R.id.fs_sera);

        sNotte = findViewById(R.id.s_notte);
        fsNotte = findViewById(R.id.fs_notte);

        RadioGroup radioGroup = findViewById(R.id.toggle);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.settimana:
                        sMattina.setVisibility(View.VISIBLE);
                        fsMattina.setVisibility(View.GONE);

                        sSera.setVisibility(View.VISIBLE);
                        fsSera.setVisibility(View.GONE);

                        sNotte.setVisibility(View.VISIBLE);
                        fsNotte.setVisibility(View.GONE);
                        break;
                    case R.id.fineSettimana:
                        sMattina.setVisibility(View.GONE);
                        fsMattina.setVisibility(View.VISIBLE);

                        sSera.setVisibility(View.GONE);
                        fsSera.setVisibility(View.VISIBLE);

                        sNotte.setVisibility(View.GONE);
                        fsNotte.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        //Inserimento dei dati nello spinner e specifica del layout
        spinner_sMattina = findViewById(R.id.s_dispMattina);
        spinner_fsMattina = findViewById(R.id.fs_dispMattina);

        spinner_sSera = findViewById(R.id.s_dispSera);
        spinner_fsSera = findViewById(R.id.fs_dispSera);

        spinner_sNotte = findViewById(R.id.s_dispNotte);
        spinner_fsNotte = findViewById(R.id.fs_dispNotte);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_disponibilita, R.layout.custom_spinner_layout);

        //Specifica il layout da usare per la lista dello spinner
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);

        //Applica le modifiche allo spinner
        spinner_sMattina.setAdapter(adapter);
        spinner_fsMattina.setAdapter(adapter);

        spinner_sSera.setAdapter(adapter);
        spinner_fsSera.setAdapter(adapter);

        spinner_sNotte.setAdapter(adapter);
        spinner_fsNotte.setAdapter(adapter);

        //Ulteriori dati per la valutazione
        ratingSicurezza = findViewById(R.id.ratingSicurezza);
        commento = findViewById(R.id.commento);
        bottoneConferma = findViewById(R.id.bottoneConferma);

        //Salva i dati inseriti e passa alla activity di recap
        bottoneConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateValutazione();

                Intent paginaRecap = new Intent(ValutazioneActivity.this, RecapActivity.class);
                paginaRecap.putExtra(VALUTAZIONE_EXTRA, valutazioneUtente);
                startActivity(paginaRecap);
            }
        });

    }

    //Meccanismo per il pulsante di ritorno
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void updateValutazione (){
        String spinner_sMattina = this.spinner_sMattina.getSelectedItem().toString();
        this.valutazioneUtente.setsMattina(spinner_sMattina);

        String spinner_fsMattina = this.spinner_fsMattina.getSelectedItem().toString();
        this.valutazioneUtente.setFsMattina(spinner_fsMattina);

        String spinner_sSera = this.spinner_sSera.getSelectedItem().toString();
        this.valutazioneUtente.setsSera(spinner_sSera);

        String spinner_fsSera = this.spinner_fsSera.getSelectedItem().toString();
        this.valutazioneUtente.setFsSera(spinner_fsSera);

        String spinner_sNotte = this.spinner_sNotte.getSelectedItem().toString();
        this.valutazioneUtente.setsNotte(spinner_sNotte);

        String spinner_fsNotte = this.spinner_fsNotte.getSelectedItem().toString();
        this.valutazioneUtente.setFsNotte(spinner_fsNotte);

        String commento = this.commento.getText().toString();
        this.valutazioneUtente.setCommento(commento);

        Float rating = this.ratingSicurezza.getRating();
        this.valutazioneUtente.setRatingSicurezza(rating);
    }
}