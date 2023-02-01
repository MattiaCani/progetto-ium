package com.example.progetto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.*;

public class Login extends AppCompatActivity {

    EditText editUsername, editPassword;
    TextView regLink, errorText;
    Button loginButton;
    public static final String USER_EXTRA =
            "com.example.progetto.Person";

    //credenziali dell'utente di root che non devono essere modificate
    private static final Person root = new Person("root", "pswroot", "", "");

    Person person = new Person();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        loginButton = findViewById(R.id.loginButton);
        regLink = findViewById(R.id.reg_link);
        errorText = findViewById(R.id.errorlogin);


        if (!(UserList.initialized)) {
            UserList.initializeDB();
        }


        //click listener per il tasto di login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    updatePerson();

                    if (UserList.dbUser.contains(person)) {
                        if (!(isAdmin(person.getUsername(), person.getPassword()))) {
                            person = UserList.dbUser.get(UserList.dbUser.indexOf(person));
                        }

                        Intent showResult = new Intent(Login.this,
                                Login.class); //da cambiare, deve portare a home

                        showResult.putExtra(USER_EXTRA, person);
                        startActivity(showResult);
                        finish();
                    } else {
                        if(!(isAdmin(person.getUsername(), person.getPassword()))){
                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText("Credenziali non valide");
                        }
                        else{
                            Intent showResult = new Intent(Login.this,
                                    Login.class); //da cambiare, deve portare a home

                            showResult.putExtra(USER_EXTRA, person);
                            startActivity(showResult);
                            finish();
                        }
                    }
                }
            }
        });


        //click listener per il link di registrazione
        regLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationPage = new Intent(Login.this,
                        SignIn.class); //da cambiare, deve portare a registrazione
                startActivity(registrationPage);
            }
        });

    }



    private boolean checkInput() {
        boolean check = false;
        int errors = 0;
        //editName ecc contengono i riferimenti all'edit text
        if (editUsername.getText().toString().length() == 0) {
            errors++;
            editUsername.setError("Inserire l'username");
        }

        if (editPassword.getText().toString().length() == 0) {
            errors++;
            editPassword.setError("Inserire una password");
        }

        if (!(UserList.dbUser.contains(person))) {
            check = true;
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Credenziali non valide");
        }
        else{
            if(!(person.getPassword().equals(editPassword.getText().toString())))
                editPassword.setError("Password non corretta");
                errors++;
        }

        switch(errors){
            case 0:
                //se l'utente c'è non viene mostrato il messaggio, se non c'è altrimenti.
                if(check){
                    errorText.setVisibility(View.GONE);
                }
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
        String username = this.editUsername.getText().toString();
        this.person.setUsername(username);
        String password = this.editPassword.getText().toString();
        this.person.setPassword(password);
    }

    public boolean isAdmin(String user, String password){
        if(user.equals(root.getUsername()) && password.equals(root.getPassword())){
            return true;
        }

        return false;
    }


}



