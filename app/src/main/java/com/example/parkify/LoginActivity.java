package com.example.parkify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skydoves.elasticviews.ElasticAnimation;
import com.skydoves.elasticviews.ElasticFinishListener;

import java.text.SimpleDateFormat;
import java.util.*;

import jp.wasabeef.blurry.Blurry;

public class LoginActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    EditText editUsername, editPassword;
    TextView regLink, errorText;
    Button loginButton;

    ProgressBar progressBar;

    TextInputLayout usernameError;
    TextInputLayout passwordError;

    //Per il passaggio ad un altra activity
    public static final String USER_EXTRA = "com.example.progetto.Person";

    Person person = new Person();

    //Riattiva il click del bottone quando si torna al login dalla registrazione
    @Override
    protected void onResume() {
        super.onResume();

        regLink.setClickable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Imposta di default la modalità giorno
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //Inizializza la connessione al database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Manteniamo la sessione dell'utente loggato
        SharedPreferences sharedPreferences = getSharedPreferences("Parkify", MODE_PRIVATE);

        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);
        if (isLoggedIn) {
            // The user is logged in.
            Intent showResult = new Intent(LoginActivity.this, HomepageActivity.class);
            startActivity(showResult);
            finish();
        }

        //Chiamata della actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        //Cambia il colore della statusbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_transparent));
        }

        //Cambia il colore del testo della statusbar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Richiede le autorizzazioni per usare la mappa
        String[] autorizzazioniMappa = new String[3];
        autorizzazioniMappa[0] = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        autorizzazioniMappa[1] = android.Manifest.permission.ACCESS_FINE_LOCATION;
        autorizzazioniMappa[2] = Manifest.permission.ACCESS_COARSE_LOCATION;
        requestPermissionsIfNecessary(autorizzazioniMappa);

        //Raccolta dei dati
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);

        loginButton = findViewById(R.id.loginButton);
        regLink = findViewById(R.id.reg_link);
        errorText = findViewById(R.id.errorlogin);

        progressBar = findViewById(R.id.progressBar);

        usernameError = (TextInputLayout) findViewById(R.id.usernameError);
        passwordError = (TextInputLayout) findViewById(R.id.passwordError);

        //click listener per il tasto di login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    usernameError.setError(null);
                    passwordError.setError(null);

                    //Finta barra di caricamento
                    progressBar.setVisibility(View.VISIBLE);

                    //Rende il bottone non cliccabile e aggiorna i dati dell'utente
                    loginButton.setClickable(false);
                    updatePerson();

                    //Controlla se l'utente è nel database e se la password è corretta
                    DocumentReference docRef = db.collection("utente").document(person.getUsername());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Person utente = documentSnapshot.toObject(Person.class);

                            if(documentSnapshot.exists()){
                                if(utente.getPassword().equals(person.getPassword())) {

                                    Intent showResult = new Intent(LoginActivity.this, HomepageActivity.class); //da cambiare, deve portare a home

                                    showResult.putExtra(USER_EXTRA, person);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("IS_LOGGED_IN", true);
                                    editor.putString("USER", utente.getUsername());
                                    editor.apply();

                                    startActivity(showResult);
                                    finish();

                                } else {
                                    errorText.setVisibility(View.VISIBLE);
                                    errorText.setText("Password errata");
                                    loginButton.setClickable(true);
                                    progressBar.setVisibility(View.GONE);
                                }

                            } else {
                                errorText.setVisibility(View.VISIBLE);
                                errorText.setText("Account non esistente");
                                loginButton.setClickable(true);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

        editUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This method is called before the text is changed.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editUsername.toString().length() != 0)
                    usernameError.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This method is called after the text is changed.
            }
        });

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This method is called before the text is changed.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editPassword.toString().length() != 0)
                    passwordError.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This method is called after the text is changed.
            }
        });


        //click listener per il link di registrazione
        regLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regLink.setClickable(false);

                Intent registrationPage = new Intent(LoginActivity.this,
                        SignInActivity.class);

                new ElasticAnimation(regLink).setScaleX(0.9f).setScaleY(0.9f).setDuration(150)
                        .setOnFinishListener(new ElasticFinishListener() {
                            @Override
                            public void onFinished() {
                                startActivity(registrationPage);
                            }
                        }).doAction();
            }
        });
    }


    //Controlla che gli input siano corretti
    private boolean checkInput() {
        //editName ecc contengono i riferimenti all'edit text
        if (editUsername.getText().toString().length() == 0) {
            usernameError.setError("Inserire l'username");

            if (editPassword.getText().toString().length() == 0) {
                passwordError.setError("Inserire una password");

                return false;
            }

            return false;
        }

        if (editPassword.getText().toString().length() == 0) {
            passwordError.setError("Inserire una password");

            if (editUsername.getText().toString().length() == 0) {
                usernameError.setError("Inserire l'username");

                return false;
            }

            return false;
        }

        return true;
    }

    //Aggiorna
    private void updatePerson(){
        String username = this.editUsername.getText().toString();
        this.person.setUsername(username);

        String password = this.editPassword.getText().toString();
        this.person.setPassword(password);
    }

    //Richiede i permessi per l'utilizzo del GPS
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


}



