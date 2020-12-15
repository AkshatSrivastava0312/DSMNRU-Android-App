package com.akshat.dsmnruandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akshat.dsmnruandroidapp.admin.AdminHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity {
    private EditText emailText, passwordText;
    private Button loginBtn;

    private DatabaseReference ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        loginBtn = findViewById(R.id.loginBtn);

        ref = FirebaseDatabase.getInstance().getReference().child("Admin");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });


    }

    private void validate() {
        String email, password;
        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        if(email.isEmpty()){
            emailText.setError("Empty");
            emailText.requestFocus();
        }else if(password.isEmpty()){
            passwordText.setError("Empty");
            passwordText.requestFocus();
        }else{
            login();
        }
    }

    private void login() {

        final String[] mailid = new String[1];
        final String[] pass = new String[1];

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mailid[0] = snapshot.child("email").getValue().toString();
                pass[0] = snapshot.child("password").getValue().toString();

                if(mailid[0].equals(emailText.getText().toString()) && pass[0].equals(passwordText.getText().toString())){
                    Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(AdminLogin.this, "Wrong Email Id or Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}