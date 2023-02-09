package com.example.parkify;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RecapActivity extends AppCompatActivity {

    //Widget
    TextView spinner_sMattina, spinner_sSera, spinner_sNotte;
    TextView spinner_fsMattina, spinner_fsSera, spinner_fsNotte;

    RatingBar ratingSicurezzaResult;
    TextView commentoResult;
    Button bottoneConfermaRecap;

    Valutazione valutazioneUtente;
    Parcheggio parcheggioValutato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        //Collegamento al database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        //Recupero delle info sulla valutazione
        Intent intent0 = getIntent();
        Serializable obj0 = intent0.getSerializableExtra(ValutazioneActivity.VALUTAZIONE_EXTRA0);

        if(obj0 instanceof Valutazione)
            valutazioneUtente = (Valutazione) obj0;
        else
            valutazioneUtente = new Valutazione();

        //Recupero delle info sul parcheggio
        Intent intent1 = getIntent();
        Serializable obj1 = intent1.getSerializableExtra(ValutazioneActivity.VALUTAZIONE_EXTRA1);

        if(obj1 instanceof Parcheggio)
            parcheggioValutato = (Parcheggio) obj1;
        else
            parcheggioValutato = new Parcheggio();

        updateTextViews();

        //Conferma i dati inseriti e passa alla activity di recap
        bottoneConfermaRecap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Aggiunge un documento delle valutazioni
                db.collection("valutazione")
                        .add(valutazioneUtente)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                //Ricalcola i dati del parcheggio
                Float palle = parcheggioValutato.getRatingSicurezza() + valutazioneUtente.getRatingSicurezza();
                int palle33 = parcheggioValutato.getNumValutazioni() + 1;
                parcheggioValutato.setRatingSicurezza(palle / palle33);

                //Aggiorna i dati relativi al parcheggio
                DocumentReference nuovo = db.collection("parcheggio").document(String.valueOf(valutazioneUtente.getIdParcheggio()));
                nuovo.update("ratingSicurezza", parcheggioValutato.getRatingSicurezza(),
                                "commenti", parcheggioValutato.getCommenti(),
                                "sMattina", parcheggioValutato.getsMattina(),
                                "sSera", parcheggioValutato.getsSera(),
                                "sNotte", parcheggioValutato.getsNotte(),
                                "fsMattina", parcheggioValutato.getFsMattina(),
                                "fsSera", parcheggioValutato.getFsSera(),
                                "fsNotte", parcheggioValutato.getFsNotte())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });

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

    //Aggiorna i dati delle textview di recap
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