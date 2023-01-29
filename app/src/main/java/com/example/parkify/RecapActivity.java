package com.example.parkify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;

public class RecapActivity extends AppCompatActivity {

    RelativeLayout sMattina, sSera, sNotte;
    RelativeLayout fsMattina, fsSera, fsNotte;

    TextView spinner_sMattina, spinner_sSera, spinner_sNotte;
    TextView spinner_fsMattina, spinner_fsSera, spinner_fsNotte;

    RatingBar ratingSicurezzaResult;
    TextView commentoResult;

    Valutazione valutazioneUtente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        //Chiamata della actionbar
        ActionBar actionBar = getSupportActionBar();

        //Mostra un pulsante di ritorno nella actionbar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Cambia tra la disponibilità Settimana e Fine Settimana
        sMattina = findViewById(R.id.s_mattinaResult);
        fsMattina = findViewById(R.id.fs_mattinaResult);

        sSera = findViewById(R.id.s_seraResult);
        fsSera = findViewById(R.id.fs_seraResult);

        sNotte = findViewById(R.id.s_notteResult);
        fsNotte = findViewById(R.id.fs_notteResult);

        RadioGroup radioGroup = findViewById(R.id.toggleResult);
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

        //Recupero dei dati
        spinner_sMattina = findViewById(R.id.s_dispMattinaResult);
        spinner_fsMattina = findViewById(R.id.fs_dispMattinaResult);

        spinner_sSera = findViewById(R.id.s_dispSeraResult);
        spinner_fsSera = findViewById(R.id.fs_dispSeraResult);

        spinner_sNotte = findViewById(R.id.s_dispNotteResult);
        spinner_fsNotte = findViewById(R.id.fs_dispNotteResult);

        ratingSicurezzaResult = findViewById(R.id.ratingSicurezzaResult);
        commentoResult = findViewById(R.id.commentoResult);

        //Visualizzazione dei dati
        Intent intent = getIntent();
        Serializable obj = intent.getSerializableExtra(ValutazioneActivity.VALUTAZIONE_EXTRA);

        if(obj instanceof Valutazione)
            valutazioneUtente = (Valutazione) obj;
        else
            valutazioneUtente = new Valutazione();

        updateTextViews();
    }

    //Tasto di ritorno
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void updateTextViews(){
        spinner_sMattina.setText(valutazioneUtente.getsMattina());
        spinner_fsMattina.setText(valutazioneUtente.getFsMattina());

        spinner_sSera.setText(valutazioneUtente.getsSera());
        spinner_fsSera.setText(valutazioneUtente.getFsSera());

        spinner_sNotte.setText(valutazioneUtente.getsNotte());
        spinner_fsNotte.setText(valutazioneUtente.getFsNotte());

        ratingSicurezzaResult.setRating(valutazioneUtente.getRatingSicurezza());
        commentoResult.setText(valutazioneUtente.getCommento());
    }
}