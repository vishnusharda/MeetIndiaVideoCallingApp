package com.vindevelopment.meetindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button createAccount;
    Button login;
    EditText emailAddress, Password;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        emailAddress = findViewById(R.id.EmailAddress);
        Password = findViewById(R.id.Password);
        mAuth = FirebaseAuth.getInstance();
        createAccount = findViewById(R.id.createAccount);
        progressBar = findViewById(R.id.progressBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailAddress.getText().toString();
                String password = Password.getText().toString();
                if (email.isEmpty()){
                    Toast.makeText(MainActivity.this, "email or password should not be empty", Toast.LENGTH_SHORT).show();
                }
                if (password.isEmpty()){
                    Toast.makeText(MainActivity.this, "email or password should not be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                        progressBar.setVisibility(View.VISIBLE);
                                        login.setVisibility(View.INVISIBLE);
                                        startActivity(new Intent(getApplicationContext(),DashBoard.class));
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                        progressBar.setVisibility(View.GONE);
                                        login.setVisibility(View.VISIBLE);
                                    }

                                    // ...
                                }
                            });
                }
            }
        });



        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateAccount.class));
                finish();
            }
        });
    }

    private void updateUI(Object o) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            startActivity(new Intent(getApplicationContext(),DashBoard.class));
        }
    }
}