package com.akshat.dsmnruandroidapp.admin.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.akshat.dsmnruandroidapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateStudents extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView cseDepartment, meDepartment, eeDepartment, ceDepartment, eceDepartment;
    private LinearLayout cseNoData, meNoData, eeNoData, ceNoData, eceNoData;
    private List<StudentsData> list1, list2, list3, list4, list5;
    private StudentAdapter adapter;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_students);


        eceDepartment = findViewById(R.id.eceDepartmentStd);
        ceDepartment = findViewById(R.id.ceDepartmentStd);
        eeDepartment = findViewById(R.id.eeDepartmentStd);
        meDepartment = findViewById(R.id.meDepartmentStd);
        cseDepartment = findViewById(R.id.cseDepartmentStd);

        eceNoData = findViewById(R.id.eceStdNoData);
        ceNoData = findViewById(R.id.ceStdNoData);
        eeNoData = findViewById(R.id.eeStdNoData);
        meNoData = findViewById(R.id.meStdNoData);
        cseNoData = findViewById(R.id.cseStdNoData);


        reference = FirebaseDatabase.getInstance().getReference().child("Students");

        cseDepartment();
        meDepartment();
        eeDepartment();
        ceDepartment();
        eceDepartment();



        fab = findViewById(R.id.fabStd);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddStudents.class));
            }
        });




    }

    private void eceDepartment() {
        final String key = reference.getKey();
        Query q = reference.orderByChild("category").equalTo("Electronics & Communication Engineering");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3 = new ArrayList<>();
                if(!snapshot.exists()) {
                    eceNoData.setVisibility(View.VISIBLE);
                    eceDepartment.setVisibility(View.GONE);
                }else{
                    eceNoData.setVisibility(View.GONE);
                    eceDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot sp: snapshot.getChildren()){
                        StudentsData data = sp.getValue(StudentsData.class);
                        list3.add(data);
                    }
                    eceDepartment.setHasFixedSize(true);
                    eceDepartment.setLayoutManager(new LinearLayoutManager(UpdateStudents.this));
                    adapter = new StudentAdapter(list3, UpdateStudents.this,"Electronics & Communication Engineering",key);
                    eceDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateStudents.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ceDepartment() {
        final String key = reference.getKey();
        Query q = reference.orderByChild("category").equalTo("Civil Engineering");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4 = new ArrayList<>();
                if(!snapshot.exists()) {
                    ceNoData.setVisibility(View.VISIBLE);
                    ceDepartment.setVisibility(View.GONE);
                }else{
                    ceNoData.setVisibility(View.GONE);
                    ceDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot sp: snapshot.getChildren()){
                        StudentsData data = sp.getValue(StudentsData.class);
                        list4.add(data);
                    }
                    ceDepartment.setHasFixedSize(true);
                    ceDepartment.setLayoutManager(new LinearLayoutManager(UpdateStudents.this));
                    adapter = new StudentAdapter(list4, UpdateStudents.this, "Civil Engineering",key);
                    ceDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateStudents.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eeDepartment() {
        final String key = reference.getKey();
        Query q = reference.orderByChild("category").equalTo("Electrical Engineering");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list5 = new ArrayList<>();
                if(!snapshot.exists()) {
                    eeNoData.setVisibility(View.VISIBLE);
                    eeDepartment.setVisibility(View.GONE);
                }else{
                    eeNoData.setVisibility(View.GONE);
                    eeDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot sp: snapshot.getChildren()){
                        StudentsData data = sp.getValue(StudentsData.class);
                        list5.add(data);
                    }
                    eeDepartment.setHasFixedSize(true);
                    eeDepartment.setLayoutManager(new LinearLayoutManager(UpdateStudents.this));
                    adapter = new StudentAdapter(list5, UpdateStudents.this, "Electrical Engineering", key);
                    eeDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateStudents.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void meDepartment() {
        final String key = reference.getKey();
        Query q = reference.orderByChild("category").equalTo("Mechanical Engineering");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2 = new ArrayList<>();
                if(!snapshot.exists()) {
                    meNoData.setVisibility(View.VISIBLE);
                    meDepartment.setVisibility(View.GONE);
                }else{
                    meNoData.setVisibility(View.GONE);
                    meDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot sp: snapshot.getChildren()){
                        StudentsData data = sp.getValue(StudentsData.class);
                        list2.add(data);
                    }
                    meDepartment.setHasFixedSize(true);
                    meDepartment.setLayoutManager(new LinearLayoutManager(UpdateStudents.this));
                    adapter = new StudentAdapter(list2, UpdateStudents.this, "Mechanical Engineering",key);
                    meDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateStudents.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cseDepartment() {
        final String key = reference.getKey();
        Query q = reference.orderByChild("category").equalTo("Computer Science & Engineering");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1 = new ArrayList<>();
                if(!snapshot.exists()) {
                    cseNoData.setVisibility(View.VISIBLE);
                    cseDepartment.setVisibility(View.GONE);
                }else{
                    cseNoData.setVisibility(View.GONE);
                    cseDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot sp: snapshot.getChildren()){
                        StudentsData data = sp.getValue(StudentsData.class);
                        list1.add(data);
                    }
                    cseDepartment.setHasFixedSize(true);
                    cseDepartment.setLayoutManager(new LinearLayoutManager(UpdateStudents.this));
                    adapter = new StudentAdapter(list1, UpdateStudents.this, "Computer Science & Engineering",key);
                    cseDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateStudents.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}