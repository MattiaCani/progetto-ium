package com.example.parkify;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecapActivity extends AppCompatActivity {

    //Widget
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

        //Collegamento al database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("Parkify", MODE_PRIVATE);

        //Chiamata della actionbar
        ActionBar actionBar = getSupportActionBar();

        //Cambia il testo e il colore della actionbar
        if (actionBar != null) {
            actionBar.setTitle("Riassunto valutazione");
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.blue_primary)));
        }

        //Cambia il colore della barra di notifiche
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_primary));
        }

        //Mostra un pulsante di ritorno nella actionbar
        actionBar.setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogThemeSuccess);

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

        String loggedUser = sharedPreferences.getString("USER", "");

        DocumentReference docRef = db.collection("utente").document(loggedUser);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Person utente = documentSnapshot.toObject(Person.class);

                valutazioneUtente.setUtente(utente);
            }
        });

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

                DocumentReference docRef = db.collection("parcheggio").document(String.valueOf(valutazioneUtente.getIdParcheggio()));
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Parcheggio pV = documentSnapshot.toObject(Parcheggio.class);

                        //Ricalcola i dati del parcheggio
                        Float sum = pV.getRatingSicurezza() * pV.getNumValutazioni() + valutazioneUtente.getRatingSicurezza();
                        int num = pV.getNumValutazioni() + 1;

                        List<String> commenti = new ArrayList<>(pV.getCommenti());
                        commenti.add(valutazioneUtente.getCommento());

                        //Aggiorna i dati relativi al parcheggio
                        DocumentReference nuovo = db.collection("parcheggio").document(String.valueOf(valutazioneUtente.getIdParcheggio()));
                        nuovo.update("ratingSicurezza", sum/num,
                                        "commenti", commenti,
                                        "sMattina", updateDisp(pV.getsMattina().getMedia(), pV.getsMattina().getNumVal(), valutazioneUtente.getsMattina()),
                                        "sSera", updateDisp(pV.getsSera().getMedia(), pV.getsSera().getNumVal(), valutazioneUtente.getsSera()),
                                        "sNotte", updateDisp(pV.getsNotte().getMedia(), pV.getsNotte().getNumVal(), valutazioneUtente.getsNotte()),
                                        "fsMattina", updateDisp(pV.getFsMattina().getMedia(), pV.getFsMattina().getNumVal(), valutazioneUtente.getFsMattina()),
                                        "fsSera", updateDisp(pV.getFsSera().getMedia(), pV.getFsSera().getNumVal(), valutazioneUtente.getFsSera()),
                                        "fsNotte", updateDisp(pV.getFsNotte().getMedia(), pV.getFsNotte().getNumVal(), valutazioneUtente.getFsNotte()),
                                        "numValutazioni", num)
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
                    }
                });

                //Reindirizza a quale activity ????
                builder.setMessage("Verrai reindirizzato alla Mappa")
                        .setTitle("Valutazione inserita con successo");

                builder.setNeutralButton("Ho capito", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent paginaHome = new Intent(RecapActivity.this, HomepageActivity.class);
                        paginaHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        paginaHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        paginaHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        paginaHome.putExtra("EXIT", true);
                        startActivity(paginaHome);
                        finish();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
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
        spinner_sMattina.setText(chooseSpinnerText(valutazioneUtente.getsMattina()));
        spinner_fsMattina.setText(chooseSpinnerText(valutazioneUtente.getFsMattina()));

        spinner_sSera.setText(chooseSpinnerText(valutazioneUtente.getsSera()));
        spinner_fsSera.setText(chooseSpinnerText(valutazioneUtente.getFsSera()));

        spinner_sNotte.setText(chooseSpinnerText(valutazioneUtente.getsNotte()));
        spinner_fsNotte.setText(chooseSpinnerText(valutazioneUtente.getFsNotte()));

        ratingSicurezzaResult.setStepSize((float) 0.5);
        ratingSicurezzaResult.setRating(valutazioneUtente.getRatingSicurezza());
        commentoResult.setText(valutazioneUtente.getCommento());
    }

    //Aggiorna i dati della disponibilità
    Disp updateDisp(Float value1, int num, int value2){
        Float media = value1;

        if(value2 != 0) {
            media = (value1 * num + value2) / (num + 1);
            num = num + 1;
        }

        return new Disp(media, num);
    }

    //Setta la stringa in base al numero
    String chooseSpinnerText(Integer media){
        String text;

        if(media <= 0)
            text = "Non lo so";
        else if(media <= 1)
            text = "Zero";
        else if (media <=2)
            text = "Poca";
        else if (media <=3)
            text = "Variabile";
        else if (media <= 4)
            text = "Molta";
        else
            text = "Piena";

        return text;
    }
}