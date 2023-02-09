package com.example.parkify;

import static android.content.ContentValues.TAG;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.text.HtmlCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;

public class ValutazioneActivity extends AppCompatActivity {

    //Per il passaggio a un altra activity
    public static final String VALUTAZIONE_EXTRA0 = "Valutazione";
    public static final String VALUTAZIONE_EXTRA1 = "Parcheggio";

    //Gestione spinners
    LinearLayout settimanaSpinners, fineSettimanaSpinners;

    //Widget
    Spinner spinner_sMattina, spinner_sSera, spinner_sNotte;
    Spinner spinner_fsMattina, spinner_fsSera, spinner_fsNotte;

    RatingBar ratingSicurezza;
    EditText commento;
    Button bottoneConferma;

    Valutazione valutazioneUtente;
    Parcheggio infoParcheggio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valutazione);

        //Imposta di default la modalità giorno
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        valutazioneUtente = new Valutazione();

        //Chiamata della actionbar
        ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        //Cambia il testo e il colore della actionbar
        if (actionBar != null) {
            actionBar.setTitle("Interrompi valutazione");
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

        //Cambia tra la disponibilità Settimana e Fine Settimana
        settimanaSpinners = findViewById(R.id.settimanaSpinners);
        fineSettimanaSpinners = findViewById(R.id.fineSettimanaSpinners);

        RadioGroup radioGroup = findViewById(R.id.toggle);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.settimana:
                        settimanaSpinners.setVisibility(View.VISIBLE);
                        fineSettimanaSpinners.setVisibility(View.GONE);
                        break;
                    case R.id.fineSettimana:
                        settimanaSpinners.setVisibility(View.GONE);
                        fineSettimanaSpinners.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        //Recupera i dati sul nome e id parcheggio
        Intent intent = getIntent();
        Serializable obj = intent.getSerializableExtra(HomepageActivity.HOMEPAGE_EXTRA);

        if(obj instanceof Parcheggio)
            infoParcheggio = (Parcheggio) obj;
        else
            infoParcheggio = new Parcheggio();

        valutazioneUtente.setNomeParcheggio(infoParcheggio.getNomeParcheggio());
        valutazioneUtente.setIdParcheggio(infoParcheggio.getIdParcheggio());

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
                paginaRecap.putExtra(VALUTAZIONE_EXTRA0, valutazioneUtente);
                paginaRecap.putExtra(VALUTAZIONE_EXTRA1, infoParcheggio);
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

    //Aggiorna i dati della valutazione
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