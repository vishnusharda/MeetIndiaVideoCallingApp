package com.vindevelopment.meetindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateAccount extends AppCompatActivity {

    Button createAccount, login;
    EditText fullName, emailAddress, Password;
    private FirebaseAuth mAuth;
    FirebaseFirestore database;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        createAccount = findViewById(R.id.createAccount);
        login = findViewById(R.id.login);
        fullName = findViewById(R.id.fullName);
        emailAddress = findViewById(R.id.EmailAddress);
        Password = findViewById(R.id.Password);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullName.getText().toString();
                String email = emailAddress.getText().toString();
                String password = Password.getText().toString();

                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setPassword(password);

                if (email.isEmpty()) {
                    Toast.makeText(CreateAccount.this, "email or password should not be empty", Toast.LENGTH_SHORT).show();
                }
                if (password.isEmpty()) {
                    Toast.makeText(CreateAccount.this, "email or password should not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        database.collection("User")
                                                .document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressBar.setVisibility(View.VISIBLE);
                                                createAccount.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(CreateAccount.this, "Account Created", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                        startActivity(new Intent(getApplicationContext(),DashBoard.class));
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(CreateAccount.this, task.getException().getLocalizedMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                        progressBar.setVisibility(View.GONE);
                                        createAccount.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        
    }
}