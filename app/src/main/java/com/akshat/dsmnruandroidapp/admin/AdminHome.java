package com.akshat.dsmnruandroidapp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.akshat.dsmnruandroidapp.LoginHome;
import com.akshat.dsmnruandroidapp.R;
import com.akshat.dsmnruandroidapp.admin.faculty.UpdateFaculty;
import com.akshat.dsmnruandroidapp.admin.notice.DeleteNotice;
import com.akshat.dsmnruandroidapp.admin.notice.UploadNotice;
import com.akshat.dsmnruandroidapp.admin.students.UpdateStudents;

public class AdminHome extends AppCompatActivity implements View.OnClickListener {

    CardView uploadNotice, faculty, deleteNotice, students, acInfo, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        uploadNotice = findViewById(R.id.addNotice);
        faculty = findViewById(R.id.updateFaculty);
        students = findViewById(R.id.updateStudents);
        acInfo = findViewById(R.id.updateAcInfo);
        logout = findViewById(R.id.logout);

        deleteNotice = findViewById(R.id.deleteNotice);


        faculty.setOnClickListener(this);
        students.setOnClickListener(this);
        acInfo.setOnClickListener(this);
        uploadNotice.setOnClickListener(this);
        deleteNotice.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Backward Movement Restricted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.addNotice:
                intent = new Intent(AdminHome.this, UploadNotice.class);
                startActivity(intent);
                break;
            case R.id.updateFaculty:
                intent = new Intent(AdminHome.this, UpdateFaculty.class);
                startActivity(intent);
                break;
            case R.id.deleteNotice:
                intent = new Intent(AdminHome.this, DeleteNotice.class);
                startActivity(intent);
                break;
            case R.id.updateStudents:
                intent = new Intent(AdminHome.this, UpdateStudents.class);
                startActivity(intent);
                break;
            case R.id.updateAcInfo:
                intent = new Intent(AdminHome.this, UpdateAccountInfo.class);
                startActivity(intent);
                break;
            case R.id.logout:
                intent = new Intent(AdminHome.this, LoginHome.class);
                startActivity(intent);
                break;
        }
    }
}
