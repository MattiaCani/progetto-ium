package com.example.parkify;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {

    EditText username, password, email;
    String vehicle;
    ImageView pickavatar1, pickavatar2;

    TextView loglink, errorText;
    Spinner dropdown;
    Button regbutton;

    ProgressBar progressBar;

    Person person;

    public static final String USER_EXTRA = "com.example.progetto.Person";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //Imposta di default la modalità giorno
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //Inizializza la connessione al database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Chiamata della actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Cambia il colore della statusbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }

        //Cambia il colore del testo della statusbar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Gestione dello spinner
        dropdown = findViewById(R.id.spinnerVehicle);

        //lista di elementi per lo spinner
        String[] items = new String[]{"SUV", "Utilitaria", "Furgone",
                "Berlina", "Station Wagon", "A due ruote"};
        //Adapter per decidere come sono mostrati i dati dello spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        //Settare lo spinner
        dropdown.setAdapter(adapter);

        //Recupero widget
        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword);
        email = findViewById(R.id.inputEmail);

        pickavatar1 = findViewById(R.id.avatar1);
        pickavatar2 = findViewById(R.id.avatar2);

        pickavatar1.setClickable(true);
        pickavatar2.setClickable(true);

        //Prendo lo spinner e porto a string il selected item per avere il veicolo
        vehicle = dropdown.getSelectedItem().toString();

        loglink = findViewById(R.id.log_link);
        errorText = findViewById(R.id.error);
        regbutton = findViewById(R.id.regButton);

        progressBar = findViewById(R.id.progressBar);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        person = new Person();

        //link per tornare al login
        loglink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loglink.setClickable(false);
                finish();
            }
        });

        //Conferma registrazione
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput()){
                    //Finta barra di caricamento
                    progressBar.setVisibility(View.VISIBLE);

                    //Rende il bottone non cliccabile e aggiorna i dati dell'utente
                    regbutton.setClickable(false);
                    updatePerson();

                    //Controlla se l'utente è nel database
                    DocumentReference docRef = db.collection("utente").document(person.getUsername());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(!documentSnapshot.exists()) {
                                db.collection("utente").document(person.getUsername())
                                        .set(person)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                finish();
                                            }
                                        });
                            } else {
                                builder.setMessage("Perfavore inserire un username diverso")
                                        .setTitle("L'username \"" + person.getUsername() + "\" è già stato preso");

                                builder.setPositiveButton("Ho capito", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();

                                regbutton.setClickable(true);
                            }
                        }
                    });
                }
            }
        });

        //L'utente ha scelto l'avatar 1
        pickavatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person.setPicId(1);
                pickavatar1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.transparent_grey));
                pickavatar2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
            }
        });

        //L'utente ha scelto l'avatar 2
        pickavatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person.setPicId(2);
                pickavatar2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.transparent_grey));
                pickavatar1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
            }
        });

    }

    //Controlli dell'input
    private boolean checkInput(){
        int errors = 0;
        //editName ecc contengono i riferimenti all'edit text
        if(username.getText().toString().length() == 0){
            errors++;
            username.setError("Inserire l'username");
        }

        if(password.getText().toString().length() == 0){
            errors++;
            password.setError("Inserire una password");
        }


        if(email.getText().toString().length() == 0){
            errors++;
            email.setError("Inserire un indirizzo email");
        }

        switch(errors){
            case 0:
                errorText.setVisibility(View.GONE);
                break;
            case 1:
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Si è verificato un errore");
                break;
            default:
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Si sono verificati " + errors + " errori");
                break;
        }

        return errors == 0;
    }

    //Setta i dati relativi alla registrazione
    private void updatePerson(){
        String username = this.username.getText().toString();
        this.person.setUsername(username);

        String password = this.password.getText().toString();
        this.person.setPassword(password);

        String email = this.email.getText().toString();
        this.person.setEmail(email);

        String vehicle = this.vehicle;
        this.person.setVehicle(vehicle);
    }
}