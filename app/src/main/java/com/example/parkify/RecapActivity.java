package com.example.parkify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;

public class RecapActivity extends AppCompatActivity {

    TextView spinner_sMattina, spinner_sSera, spinner_sNotte;
    TextView spinner_fsMattina, spinner_fsSera, spinner_fsNotte;

    RatingBar ratingSicurezzaResult;
    TextView commentoResult;
    Button bottoneConfermaRecap;

    Valutazione valutazioneUtente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        //Chiamata della actionbar
        ActionBar actionBar = getSupportActionBar();

        //Cambia il testo e il colore della actionbar
        if (actionBar != null) {
            actionBar.setTitle("Modifica valutazione");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF000000")));
        }

        //Cambia il colore della barra di notifiche
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF000000"));
        }

        //Mostra un pulsante di ritorno nella actionbar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Recupero dei dati
        spinner_sMattina = findViewById(R.id.s_dispMattinaResult);
        spinner_fsMattina = findViewById(R.id.fs_dispMattinaResult);

        spinner_sSera = findViewById(R.id.s_dispSeraResult);
        spinner_fsSera = findViewById(R.id.fs_dispSeraResult);

        spinner_sNotte = findViewById(R.id.s_dispNotteResult);
        spinner_fsNotte = findViewById(R.id.fs_dispNotteResult);

        ratingSicurezzaResult = findViewById(R.id.ratingSicurezzaResult);
        commentoResult = findViewById(R.id.commentoResult);
        bottoneConfermaRecap = findViewById(R.id.bottoneConfermaRecap);

        //Permette di scorrere il testo nella textview del commento
        commentoResult.setMovementMethod(new ScrollingMovementMethod());

        //Visualizzazione dei dati
        Intent intent = getIntent();
        Serializable obj = intent.getSerializableExtra(ValutazioneActivity.VALUTAZIONE_EXTRA);

        if(obj instanceof Valutazione)
            valutazioneUtente = (Valutazione) obj;
        else
            valutazioneUtente = new Valutazione();

        updateTextViews();

        //Conferma i dati inseriti e passa alla activity di recap
        bottoneConfermaRecap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Metodo per caricare la valutazione nel database/o dove ci sono le altre valutazioni

                //Reindirizza a quale activity ????
            }
        });
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