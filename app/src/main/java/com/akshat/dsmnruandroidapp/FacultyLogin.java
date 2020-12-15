package com.akshat.dsmnruandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akshat.dsmnruandroidapp.faculty.FacultyHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FacultyLogin extends AppCompatActivity {
    private EditText emailText, passwordText;
    private Button loginBtn;

    private DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_login);

        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        loginBtn = findViewById(R.id.loginBtn);

        reference = FirebaseDatabase.getInstance().getReference().child("Faculty");

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
        Query q = reference.orderByChild("email").equalTo(emailText.getText().toString());

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    Toast.makeText(FacultyLogin.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                }else {
                    int i = 0;
                    for(DataSnapshot sp: snapshot.getChildren()){
                        String k = sp.child("key").getValue().toString();
                        String img = sp.child("image").getValue().toString();
                        String pass = sp.child("password").getValue().toString();
                        if(pass.equals(passwordText.getText().toString())){
                            i = i+1;
                            Intent intent = new Intent(getApplicationContext(),FacultyHome.class);
                            intent.putExtra("key",k);
                            intent.putExtra("email",emailText.getText().toString());
                            intent.putExtra("password",passwordText.getText().toString());
                            intent.putExtra("image",img);
                            startActivity(intent);
                        }else{
                                if(i==0)
                                Toast.makeText(FacultyLogin.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

//    private void login() {
//
//        Query q = reference.orderByChild("email").equalTo(emailText.getText().toString());
//
//        q.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(!snapshot.exists()) {
//                    Toast.makeText(FacultyLogin.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
//                }else{
//                    if(snapshot.getValue().toString().equals(passwordText.getText().toString())){
//                        Intent intent = new Intent(getApplicationContext(), FacultyHome.class);
//                        startActivity(intent);
//                    }else {
//                        Toast.makeText(FacultyLogin.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
}