package com.example.parkify;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    EditText username, password, email;
    String vehicle;
    TextView loglink, errorText;
    ImageView pickavatar1, pickavatar2;
    Button regbutton;
    Spinner dropdown;
    Person person;
    private boolean existingUser = false;
    public static final String USER_EXTRA = "com.example.progetto.Person";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //Imposta di default la modalità giorno
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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

        dropdown = findViewById(R.id.spinnerVehicle);
        //lista di elementi per lo spinner
        String[] items = new String[]{"SUV", "Utilitaria", "Furgone",
                "Berlina", "Station Wagon", "A due ruote"};
        //Adapter per decidere come sono mostrati i dati dello spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //settare lo spinner
        dropdown.setAdapter(adapter);


        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword);
        email = findViewById(R.id.inputEmail);
        //prendo lo spinner e porto a string il selected item per avere il veicolo
        vehicle = dropdown.getSelectedItem().toString();
        loglink = findViewById(R.id.log_link);
        regbutton = findViewById(R.id.regButton);
        pickavatar1 = findViewById(R.id.avatar1);
        pickavatar2 = findViewById(R.id.avatar2);
        errorText = findViewById(R.id.error);

        pickavatar1.setClickable(true);
        pickavatar2.setClickable(true);

        //link per tornare al login
        loglink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loglink.setClickable(false);
                finish();
            }
        });

        person = new Person();

        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput()){
                    regbutton.setClickable(false);
                    updatePerson();

                    Intent showResult = new Intent(SignInActivity.this, LoginActivity.class);

                    person.setUserId(UserList.dbUser.size()+1);
                    UserList.dbUser.add(person);

                    Toast.makeText(SignInActivity.this, "Registrazione effettuata", Toast.LENGTH_SHORT).show();

                    //showResult.putExtra(USER_EXTRA, person);
                    startActivity(showResult);
                    finish();
                }
            }
        });


        pickavatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person.setPicId(1);
                pickavatar1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.transparent_grey));
                pickavatar2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
            }
        });

        pickavatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person.setPicId(2);
                pickavatar2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.transparent_grey));
                pickavatar1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
            }
        });

    }


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

        //controllo se l'username esiste già nel DB
        for(Person u : UserList.dbUser){
            if(u.getUsername().equals(username.getText().toString())){
                existingUser = true;
            }
        }

        if(existingUser){
            errors++;
            username.setError("L'username scelto è già stato preso");
            existingUser = false;
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



    private void updatePerson(){
        String username = this.username.getText().toString();
        this.person.setUsername(username);
        String password = this.password.getText().toString();
        this.person.setPassword(password);
        String email = this.email.getText().toString();
        this.person.setEmail(email);
        String vehicle = this.vehicle;
        this.person.setVehicle(vehicle);
        this.person.setUserId(UserList.dbUser.size());
    }
}