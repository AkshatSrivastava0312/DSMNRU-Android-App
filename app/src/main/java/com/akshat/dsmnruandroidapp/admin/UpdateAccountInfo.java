package com.akshat.dsmnruandroidapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akshat.dsmnruandroidapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateAccountInfo extends AppCompatActivity {
    EditText updateAdminEmail, updateAdminPassword;
    Button updateAdminBtn;
    DatabaseReference ref;
    
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account_info);

        updateAdminEmail = findViewById(R.id.updateAdminEmail);
        updateAdminPassword = findViewById(R.id.updateAdminPassword);
        updateAdminBtn = findViewById(R.id.updateAdminBtn);


        ref = FirebaseDatabase.getInstance().getReference().child("Admin");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("email").getValue().toString();
                password = snapshot.child("password").getValue().toString();

                updateAdminEmail.setText(email);
                updateAdminPassword.setText(password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        updateAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });



    }

    private void validate() {
        String e, p;
        e = updateAdminEmail.getText().toString();
        p = updateAdminPassword.getText().toString();

        if(e.isEmpty()){
            updateAdminEmail.setError("Empty");
            updateAdminEmail.requestFocus();
        }else if(p.isEmpty()){
            updateAdminPassword.setError("Empty");
            updateAdminPassword.requestFocus();
        }else{
            updateData();
        }
    }

    private void updateData() {

        HashMap hp = new HashMap();
        hp.put("email",updateAdminEmail.getText().toString());
        hp.put("password",updateAdminPassword.getText().toString());

        ref.updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateAccountInfo.this, "Account Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateAccountInfo.this, AdminHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateAccountInfo.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }


}