package com.akshat.dsmnruandroidapp.faculty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.akshat.dsmnruandroidapp.LoginHome;
import com.akshat.dsmnruandroidapp.R;
import com.akshat.dsmnruandroidapp.faculty.classroom.AddClassroom;

public class FacultyHome extends AppCompatActivity implements View.OnClickListener {
    CardView acInfo,logout, addClassroom;
    String email, password, key, img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);


        Intent i = getIntent();

        email = i.getExtras().getString("email");
        password = i.getExtras().getString("password");
        key = i.getExtras().getString("key");
        img = i.getExtras().getString("image");

        FacultyData.setEMAIL(email);
        FacultyData.setPASSWORD(password);
        FacultyData.setKEY(key);
        FacultyData.setIMAGE(img);


        addClassroom = findViewById(R.id.addClassroom);
        acInfo = findViewById(R.id.updateAcInfo);
        logout = findViewById(R.id.logout);


        addClassroom.setOnClickListener(this);
        acInfo.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Backward Movement Restricted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            case R.id.addClassroom:
                intent = new Intent(this, AddClassroom.class);
                intent.putExtra("email", email);
                intent.putExtra("key", key);
                startActivity(intent);
                break;
            case R.id.updateAcInfo:
                intent = new Intent(this, UpdateFacultyAccountInfo.class);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                intent.putExtra("key",key);
                intent.putExtra("image",img);
                startActivity(intent);
                break;
            case R.id.logout:
                intent = new Intent(this, LoginHome.class);
                startActivity(intent);
                break;
        }

    }

}